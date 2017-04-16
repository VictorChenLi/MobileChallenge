package com.victorchen.mycurrency.ui.component;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.victorchen.mycurrency.BR;

import java.util.List;

/**
 * Simple data bound recycler view adapter.
 *
 * NOTE: The bounded data name MUST be named "data" when defined
 * in item's layout.
 * @param <T> type of data for this adapter, must implement both Indexable and Identifiable
 */
public class DataBoundAdapter<T> extends AbstractRecyclerViewAdapter<DataBoundAdapter<T>.ViewHolder> {
    //layout of list item
    protected int mItemLayout;
    protected List<T> mDataList;
    protected LayoutInflater mLayoutInflater;

    public DataBoundAdapter(List<T> source, int itemLayout, Context context) {
        this(context, itemLayout);
        setSource(source);
    }

    public DataBoundAdapter(Context context, int itemLayout) {
        super(context);
        mLayoutInflater = LayoutInflater.from(context);
        mItemLayout = itemLayout;
    }

    public void setSource(List<T> source) {
        mDataList = source;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding = DataBindingUtil.inflate(mLayoutInflater, mItemLayout, parent, false);
        return new DataBoundAdapter<T>.ViewHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mDataList.get(position));
        bindData(holder, position);
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public T getItem(int position) {
        return mDataList.get(position);
    }

    /**
     * Override this method to provide additional data binding,
     * for example complex binding logic or not directly bindable fields
     * @param viewHolder viewHolder
     * @param position position of data item
     */
    protected void bindData(DataBoundAdapter<T>.ViewHolder viewHolder, int position) {
        //additional binding goes here
    }

    public class ViewHolder extends AbstractRecyclerViewAdapter<ViewHolder>.ViewHolder {
        public T data;

        public ViewHolder(ViewDataBinding viewDataBinding) {
            super(viewDataBinding);
        }

        public void setData(T data) {
            this.data = data;
            binding.setVariable(BR.data, data);
            binding.executePendingBindings();
        }
    }
}
