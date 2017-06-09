package com.xiaomi.shop2.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by bluerain on 17-1-14.
 */

public abstract class BaseCardView<T> implements ICardItem {


    protected Context mContext;

    protected T mData;

    protected View mRootView;

    protected int mRes;

    private LayoutInflater mLayoutInflater;

    public void setRes(int res) {
        mRes = res;
    }

    public void setData(T data) {
        mData = data;
    }


    public void setContext(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    protected abstract View onDraw(Context context, T data, LayoutInflater inflater);

    protected abstract void onBind(View rootView, T data, LayoutInflater inflater);


    @Override
    public View draw(Object data) {
        mData = (T) data;
        mRootView = onDraw(mContext, mData, mLayoutInflater);
        return mRootView;
    }

    @Override
    public void bind(View view, Object data) {
        mData = (T) data;
        onBind(mRootView, mData, mLayoutInflater);
    }
}
