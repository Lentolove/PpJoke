package com.tsp.libcommon.exetion;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author : shengping.tian
 * time   : 2021/10/27
 * desc   :一个能够添加HeaderView,FooterView的PagedListAdapter。解决了添加HeaderView和FooterView时 RecyclerView定位不准确的问题
 * version: 1.0
 */
public abstract class AbsPagedListAdapter<T, VH extends RecyclerView.ViewHolder> extends PagedListAdapter<T, VH> {

    private SparseArray<View> mHeaders = new SparseArray<>();

    private SparseArray<View> mFooters = new SparseArray<>();

    //作为 mHeaders 的 key ，与 mFooters 区分开来
    private int BASE_ITEM_TYPE_HEADER = 100000;

    private int BASE_ITEM_TYPE_FOOTER = 200000;

    protected AbsPagedListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    /**
     * 添加 Header
     */
    public void addHeaderView(View view) {
        //判断给View对象是否还没有处在mHeaders数组里面
        if (mHeaders.indexOfValue(view) < 0) {
            mHeaders.put(BASE_ITEM_TYPE_HEADER++, view);
            notifyDataSetChanged();
        }
    }

    /**
     * 添加 footer
     */
    public void addFooterView(View view) {
        //判断给View对象是否还没有处在mFooters数组里面
        if (mFooters.indexOfValue(view) < 0) {
            mFooters.put(BASE_ITEM_TYPE_FOOTER++, view);
            notifyDataSetChanged();
        }
    }

    /**
     * 移除头部
     */
    public void removeHeaderView(View view) {
        int index = mHeaders.indexOfValue(view);
        if (index < 0) return;
        mHeaders.removeAt(index);
        notifyDataSetChanged();
    }

    /**
     * 移除底部
     *
     * @param view
     */
    public void removeFooterView(View view) {
        int index = mFooters.indexOfValue(view);
        if (index < 0) return;
        mFooters.removeAt(index);
        notifyDataSetChanged();
    }

    public int getHeaderCount() {
        return mHeaders.size();
    }

    public int getFooterCount() {
        return mFooters.size();
    }


    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        return itemCount + getHeaderCount() + getFooterCount();
    }

    /**
     * 获取body部分数据
     */
    public int getOriginalItemCount() {
        return getItemCount() - mHeaders.size() - mFooters.size();
    }

    /**
     * 返回对应的 View 的type
     */
    @Override
    public int getItemViewType(int position) {
        if (isHeaderPosition(position)) {
            //返回该position对应的headerview的  viewType
            return mHeaders.keyAt(position);
        }
        if (isFooterPosition(position)) {
            //footer类型的，需要计算一下它的 position 实际大小
            position = position - getOriginalItemCount() - mHeaders.size();
            return mFooters.keyAt(position);
        }
        position = position - mHeaders.size();
        return getItemBodyViewType(position);
    }

    /**
     * 获取 body 部分类型，由子类去复写
     */
    protected int getItemBodyViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mHeaders.indexOfKey(viewType) >= 0) {
            //是添加的 headers
            View view = mHeaders.get(viewType);
            return (VH) new RecyclerView.ViewHolder(view) {
            };
        }
        if (mFooters.indexOfKey(viewType) > 0) {
            //是 footer 类型
            View view = mFooters.get(viewType);
            return (VH) new RecyclerView.ViewHolder(view) {
            };
        }
        //否则交给子类去绑定数据
        return onCreateViewHolder2(parent, viewType);
    }

    /**
     * 由子类来实现绑定数据
     */
    protected abstract VH onCreateViewHolder2(ViewGroup parent, int viewType);


    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        //如果是头部和footer就不需要绑定了
        if (isHeaderPosition(position) || isFooterPosition(position)) return;
        position = position - mHeaders.size();
        onBindViewHolder2(holder, position);
    }

    /**
     * 由子类来实现，绑定数据
     */
    protected abstract void onBindViewHolder2(VH holder, int position);


    /**
     * 当此适配器创建的视图已附加到窗口时调用
     */
    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        if (!isFooterPosition(holder.getAdapterPosition()) && !isFooterPosition(holder.getAdapterPosition())) {
            this.onViewAttachedToWindow2(holder);
        }
    }

    /**
     * 由子类根据需求自行复写
     */
    protected void onViewAttachedToWindow2(VH holder) {

    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        if (!isHeaderPosition(holder.getAdapterPosition()) && !isFooterPosition(holder.getAdapterPosition())) {
            this.onViewDetachedFromWindow2(holder);
        }
    }

    /**
     * 当此适配器创建的视图与其窗口分离时调用
     * 由子类根据需求自行复写
     */
    public void onViewDetachedFromWindow2(VH holder) {

    }

    /**
     * 如果我们先添加了headerView,而后网络数据回来了再更新到列表上
     * 由于 Paging 在计算列表上 item 的位置时 并不会顾及我们有没有添加 headerView，就会出现列表定位的问题
     * 实际上 RecyclerView#setAdapter方法，它会给Adapter注册了一个AdapterDataObserver
     * 可以通过代理 registerAdapterDataObserver()传递进来的observer。在各个方法的实现中，把headerView 的个数算上，再中转出去即可
     */
    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(new AdapterDataObserverProxy(observer));
    }

    /**
     * 判断当前位置是否是 header
     */
    private boolean isHeaderPosition(int position) {
        return position < mHeaders.size();
    }

    /**
     * 判断当前位置是否是 footer
     */
    private boolean isFooterPosition(int position) {
        return position >= getOriginalItemCount() + mHeaders.size();
    }

    /**
     * 我们对 RecyclerView.AdapterDataObserver 做了一层代理，从而确保因为添加了 header导致 item 定位不准的为题
     */
    private class AdapterDataObserverProxy extends RecyclerView.AdapterDataObserver{
        private RecyclerView.AdapterDataObserver mObserver;

        public AdapterDataObserverProxy(RecyclerView.AdapterDataObserver observer) {
            this.mObserver = observer;
        }

        @Override
        public void onChanged() {
            mObserver.onChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mObserver.onItemRangeChanged(positionStart + mHeaders.size(),itemCount);
        }

        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            mObserver.onItemRangeChanged(positionStart + mHeaders.size(), itemCount, payload);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            mObserver.onItemRangeInserted(positionStart + mHeaders.size(), itemCount);
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mObserver.onItemRangeRemoved(positionStart + mHeaders.size(), itemCount);
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mObserver.onItemRangeMoved(fromPosition + mHeaders.size(), toPosition + mHeaders.size(), itemCount);
        }
    }
}
