package com.tsp.libnetword;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;

import com.tsp.libnetword.cache.CacheManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : 构建组装用于请求 Request
 * version: 1.0
 */
public abstract class Request<T, R extends Request> implements Cloneable {
    /**
     * 请求地址
     */
    protected String mUrl;
    /**
     * 请求头
     */
    protected HashMap<String, String> headers = new HashMap<>();

    /**
     * 请求参数
     */
    protected HashMap<String, Object> params = new HashMap<>();

    /**
     * 仅仅只访问本地缓存，即便本地缓存不存在，也不会发起网络请求
     */
    public static final int CACHE_ONLY = 1;

    /**
     * 先访问缓存，同时发起网络的请求，成功后缓存到本地
     */
    public static final int CACHE_FIRST = 2;

    /**
     * 仅仅只访问服务器，不存任何存储
     */
    public static final int NET_ONLY = 3;

    /**
     * 先访问网络，成功后缓存到本地
     */
    public static final int NET_CACHE = 4;

    /**
     * 缓存的 key
     */
    private String cacheKey;


    private Type mType;

    /**
     * 缓存策略
     */
    private int mCacheStrategy = NET_ONLY;

    @IntDef({CACHE_ONLY, CACHE_FIRST, NET_CACHE, NET_ONLY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CacheStrategy {
    }

    public Request(String url) {
        this.mUrl = url;
    }

    /**
     * 添加请求头
     */
    public R addHeader(String key, String value) {
        headers.put(key, value);
        return (R) this;
    }

    /**
     * 添加请求参数
     */
    public R addParam(String key, Object value) {
        if (value == null) {
            return (R) this;
        }
        //int byte char short long double float boolean 和他们的包装类型，但是除了 String.class 所以要额外判断
        try {
            if (value.getClass() == String.class) {
                params.put(key, value);
            } else {
                Field field = value.getClass().getField("TYPE");
                Class claz = (Class) field.get(null);
                //判断是否是基本类型
                if (claz.isPrimitive()) {
                    params.put(key, value);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (R) this;
    }

    /**
     * 设置缓存策略
     */
    public R cacheStrategy(@CacheStrategy int cacheStrategy) {
        mCacheStrategy = cacheStrategy;
        return (R) this;
    }

    /**
     * 设置缓存的 key
     */
    public R cacheKey(String key) {
        this.cacheKey = key;
        return (R) this;
    }

    /**
     * 结果返回类型
     */
    public R responseType(Type type) {
        mType = type;
        return (R) this;
    }

    public R responseType(Class claz) {
        mType = claz;
        return (R) this;
    }

    /**
     * 执行同步方法
     */
    public ApiResponse<T> execute() {
        if (mType == null) {
            throw new RuntimeException("同步方法,response 返回值类型必须设置");
        }
        if (mCacheStrategy == CACHE_ONLY) {
            return readCache();
        }
        ApiResponse<T> result = null;
        try {
            Response response = getCall().execute();
            result = parseResponse(response, null);
        } catch (IOException e) {
            e.printStackTrace();
            if (result == null) {
                result = new ApiResponse<>();
                result.message = e.getMessage();
            }
        }
        return result;
    }

    /**
     * 异步请求
     */
    @SuppressLint("RestrictedApi")
    public void execute(final JsonCallback callback) {
        if (mCacheStrategy != NET_ONLY) {
            ArchTaskExecutor.getIOThreadExecutor().execute(() -> {
                ApiResponse<T> response = readCache();
                if (callback != null && response.body != null) {
                    callback.onCacheSuccess(response);
                }
            });
        }
        if (mCacheStrategy != CACHE_ONLY) {
            getCall().enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    ApiResponse<T> result = new ApiResponse<>();
                    result.message = e.getMessage();
                    callback.onError(result);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    ApiResponse<T> result = parseResponse(response, callback);
                    if (!result.success) {
                        callback.onError(result);
                    } else {
                        callback.onSuccess(result);
                    }
                }
            });
        }
    }

    /**
     * 解析返回结果
     */
    private ApiResponse<T> parseResponse(Response response, JsonCallback<T> callback) {
        String message = null;
        int status = response.code();
        boolean success = response.isSuccessful();
        ApiResponse<T> result = new ApiResponse<>();
        Convert convert = ApiService.sConvert;
        try {
            String content = response.body().string();
            if (success) {
                if (callback != null) {
                    ParameterizedType type = (ParameterizedType) callback.getClass().getGenericSuperclass();
                    Type argument = type.getActualTypeArguments()[0];
                    result.body = (T) convert.convert(content, argument);
                } else if (mType != null) {
                    result.body = (T) convert.convert(content, mType);
                }
//                } else if (mClaz != null) {
//                    result.body = (T) convert.convert(content, mClaz);
//                }
                else {
                    Log.e("request", "parseResponse: 无法解析 ");
                }
            } else {
                message = content;
            }
        } catch (Exception e) {
            message = e.getMessage();
            success = false;
            status = 0;
        }
        result.success = success;
        result.status = status;
        result.message = message;
        if (mCacheStrategy != NET_ONLY && result.success && result.body instanceof Serializable) {
            saveCache(result.body);
        }
        return result;
    }

    /**
     * 从缓存中获取请求结果
     */
    protected ApiResponse<T> readCache() {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        Object cache = CacheManager.getCache(key);
        ApiResponse<T> result = new ApiResponse<>();
        result.status = 304;
        result.message = "缓存获取成功";
        result.body = (T) cache;
        result.success = true;
        return result;
    }

    /**
     * 将请求地址和参数封装成缓存的唯一key标识
     */
    private String generateCacheKey() {
        cacheKey = UrlCreator.createUrlFromParams(mUrl, params);
        return cacheKey;
    }

    private void saveCache(T body) {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        CacheManager.save(key, body);
    }


    /**
     * 创建用于OkHttp call 对象
     */
    private Call getCall() {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        addHeaders(builder);
        //构建请求头
        okhttp3.Request request = generateRequest(builder);
        Call call = ApiService.okHttpClient.newCall(request);
        return call;
    }

    protected abstract okhttp3.Request generateRequest(okhttp3.Request.Builder builder);

    private void addHeaders(okhttp3.Request.Builder builder) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
    }

    @NonNull
    @Override
    protected Request clone() throws CloneNotSupportedException {
        return (Request<T, R>) super.clone();
    }
}
