package com.victorchen.mycurrency.ui.dataBinding;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.victorchen.mycurrency.R;

public abstract class AbstractRecyclerViewAdapter<T extends AbstractRecyclerViewAdapter.ViewHolder> extends RecyclerView.Adapter<T> {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected OnItemClickListener mListener;
    protected Comparable mSelectedItemId = -1;


    public AbstractRecyclerViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View holderView;
        public ViewDataBinding binding;

        public ViewHolder(ViewDataBinding viewDataBinding) {
            this(viewDataBinding.getRoot());
            binding = viewDataBinding;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            holderView = itemView;
            if (mListener != null) {
                holderView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public int getSelectedItemBgColor() {
        return R.color.colorStable;
    }

    /**
     * Save selected item object so object's equals method is used to compare
     */
    public void setSelectedItemId(Comparable selectedItemId) {
        mSelectedItemId = selectedItemId;
        notifyDataSetChanged();
    }

    public Comparable getSelectedItemId() {
        return mSelectedItemId;
    }

    public boolean isSelectedItem(Comparable itemId) {
        return mSelectedItemId != null && itemId != null && itemId.getClass() == mSelectedItemId.getClass() && mSelectedItemId.compareTo(itemId) == 0;
    }
}

