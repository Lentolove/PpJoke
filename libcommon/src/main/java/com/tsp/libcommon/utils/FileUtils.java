package com.tsp.libcommon.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 文件操作工具类
 * version: 1.0
 */
public class FileUtils {

    /**
     * 截取视频文件的封面图
     */
    @SuppressLint("RestrictedApi")
    public static LiveData<String> generateVideoCover(final String filePath) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        ArchTaskExecutor.getIOThreadExecutor().execute(() -> {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(filePath);
            // 获取默认的第一个关键帧
            Bitmap frame = retriever.getFrameAtTime();
            FileOutputStream fos = null;
            if (frame != null) {
                //压缩到200k以下，再存储到本地文件中
                byte[] bytes = compressBitmap(frame, 200);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), System.currentTimeMillis() + ".jpeg");
                try {
                    file.createNewFile();
                    fos = new FileOutputStream(file);
                    fos.write(bytes);
                    liveData.postValue(file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.flush();
                            fos.close();
                            fos = null;
                        } catch (IOException ignore) {
                            ignore.printStackTrace();
                        }
                    }
                }

            } else {
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    //循环压缩
    private static byte[] compressBitmap(Bitmap frame, int limit) {
        if (frame != null && limit > 0) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = 100;
            frame.compress(Bitmap.CompressFormat.JPEG, options, baos);
            while (baos.toByteArray().length > limit * 1024) {
                baos.reset();
                options -= 5;
                frame.compress(Bitmap.CompressFormat.JPEG, options, baos);
            }

            byte[] bytes = baos.toByteArray();
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                baos = null;
            }
            return bytes;
        }
        return null;
    }
}
