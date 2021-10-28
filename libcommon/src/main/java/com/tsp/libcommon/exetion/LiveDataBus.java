package com.tsp.libcommon.exetion;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   : LiveBus 事件总线
 * version: 1.0
 */
public class LiveDataBus {

    private LiveDataBus() {
    }

    private static class Holder {

        static LiveDataBus instance = new LiveDataBus();
    }

    public static LiveDataBus get() {
        return Holder.instance;
    }

    private ConcurrentHashMap<String, StickLiveData> mHashMap = new ConcurrentHashMap<>();

    public StickLiveData with(String eventName) {
        StickLiveData liveData = mHashMap.get(eventName);
        if (liveData == null) {
            liveData = new StickLiveData(eventName);
            mHashMap.put(eventName, liveData);
        }
        return liveData;
    }

    /**
     * 支持设置粘性事件的 LiveDate
     */
    public class StickLiveData<T> extends LiveData<T> {

        //事件名称，以String 作为唯一表示
        private String mEventName;

        private T mStickData;

        //是否发送给观察者的数据版本控制
        private int mVersion = 0;

        public StickLiveData(String eventName) {
            mEventName = eventName;
        }

        /**
         * LiveData 支持发送数据，每发送一次数据会将版本号++,因为我们要接管事件分发的流程，所以也需要有相对应的版本号 mVersion 来控制是否发送此次事件
         *
         * @param value
         */
        @Override
        protected void setValue(T value) {
            mVersion++;
            super.setValue(value);
        }

        /**
         * LiveData 内部也是通过 Handler 将 postValue 方法抛到主线程中再去调用 setValue 方法，所以这里我们同样需要将 mVersion++；
         *
         * @param value
         */
        @Override
        protected void postValue(T value) {
            mVersion++;
            super.postValue(value);
        }

        /**
         * 暴露给使用者在主线程发送数据
         *
         * @param data
         */
        public void setStickData(T data) {
            this.mStickData = data;
            setValue(data);
        }

        /**
         * 提供给使用者在子线程发送数据
         *
         * @param data
         */
        public void postStickData(T data) {
            this.mStickData = data;
            postValue(mStickData);
        }

        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
            super.observe(owner, observer);
        }

        public void observerSticky(LifecycleOwner owner, Observer<? super T> observer, boolean sticky) {
            super.observe(owner, new WrapperObserve(this, observer, sticky));
            owner.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    mHashMap.remove(mEventName);
                }
            });
        }

        /**
         * 在做数据分发的时候，对Observer进一步包装来控制是否分发粘性事件
         */
        private class WrapperObserve<T> implements Observer<T> {

            private StickLiveData<T> mLiveData;

            private Observer<T> mObserver;

            //是否是粘性事件
            private boolean mStick;

            //标记该liveData已经发射几次数据了，用以过滤老数据重复接收
            private int mLastVersion = 0;

            public WrapperObserve(StickLiveData<T> data, Observer<T> observer, boolean isStick) {
                this.mLiveData = data;
                this.mObserver = observer;
                this.mStick = isStick;
                //在LiveData中， 比如先使用StickyLiveData发送了一条数据。StickyLiveData#version = 1
                // 当我们创建WrapperObserver注册进去的时候，就至少需要把它的 version 和 StickyLiveData 的 version 保持一致用以过滤老数据，否则是会收到老的数据
                mLastVersion = mLiveData.mVersion;
            }

            @Override
            public void onChanged(T t) {
                /**
                 * observer.mLastVersion >= mLiveData.mVersion
                 * 这种情况 只会出现在，我们先行创建一个liveData发射了一条数据。此时liveData的 mVersion = 1.
                 * 而后注册一个observer进去。由于我们代理了传递进来的observer,进而包装成wrapperObserver，此时wrapperObserver的lastVersion 就会跟liveData的 mVersion 对齐。保持一样。把wrapperObserver注册到liveData中。
                 * 根据liveData的原理，一旦一个新的observer 注册进去,也是会尝试把数据派发给他的。这就是黏性事件(先发送,后接收)。
                 * 但此时wrapperObserver的lastVersion 已经和 liveData的version 一样了。由此来控制黏性事件的分发与否
                 */
                if (mLastVersion >= mLiveData.mVersion) {
                    //如果是粘性事件才发送
                    if (mStick && mLiveData.mStickData != null) {
                        mObserver.onChanged(mLiveData.mStickData);
                    }
                    return;
                }
                mLastVersion = mLiveData.mVersion;
                mObserver.onChanged(t);
            }
        }
    }

}
