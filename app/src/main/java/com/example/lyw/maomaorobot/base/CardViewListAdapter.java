package com.example.lyw.maomaorobot.base;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bluerain on 17-1-14.
 */

public class CardViewListAdapter extends BaseAdapter {


    private Context mContext;

    private List<ICardAble> mCardAbles;

    private int mTypeCount = 1;

    private AbsListView mListView;

    private HashMap<String, Integer> mTypes = new HashMap<>();

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();


    public CardViewListAdapter(Context context) {
        mContext = context;
        initVariables();
    }

    public CardViewListAdapter(Context context, List data, AbsListView listView) {
        this(context);
        mListView = listView;
        addAll(data);
    }

    public void setTypeCount(int typeCount) {
        this.mTypeCount = typeCount;
    }

    private void initVariables() {
        mCardAbles = new ArrayList<>();
    }

    public void add(Object data) {
        if (null != data && data instanceof ICardAble) {
            ICardAble cur = (ICardAble) data;
            if (!mCardAbles.contains(cur)) {
                mCardAbles.add(cur);
            }
        }

    }

    public void addAll(List dataList) {
        if (null != dataList) {
            for (Object next : dataList) {
                add(next);
            }
        }
    }

    public void clear() {
        mCardAbles.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mCardAbles.size();
    }

    @Override
    public ICardAble getItem(int position) {
        return mCardAbles.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        int index = 0;
        mTypes.clear();
        for (int i = 0; i < mCardAbles.size(); i++) {
            final String type = mCardAbles.get(i).getViewType();
            if (!mTypes.containsKey(type))
                mTypes.put(type, index++);
        }
        return mTypes.size() == 0 ? 1 : mTypes.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0 && position <= mCardAbles.size()) {
            final String type = mCardAbles.get(position).getViewType();
            return mTypes.get(type);
        }
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        resetTypeCount();
    }

    private void resetTypeCount() {
        try {
            final Class<AbsListView> absListViewClass = AbsListView.class;
            final Class<?>[] absListViewClassDeclaredClasses = absListViewClass.getDeclaredClasses();
            final Field mRecycler = absListViewClass.getDeclaredField("mRecycler");
            mRecycler.setAccessible(true);
            for (Class c : absListViewClassDeclaredClasses) {
                if (TextUtils.equals(c.getName(), "android.widget.AbsListView$RecycleBin")) {
                    final Method setViewTypeCount = c.getDeclaredMethod("setViewTypeCount", int.class);
                    final Object o = mRecycler.get(mListView);
                    setViewTypeCount.invoke(o, getViewTypeCount());
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        if (v == null) {
            ICardItem ICardItem = mCardAbles.get(position).onCreateCardView();
            if (ICardItem != null) {
                if (ICardItem instanceof BaseCardView)
                    ((BaseCardView) ICardItem).setContext(mContext);

                v = ICardItem.draw(getItem(position));
                if (v != null) {
                    v.setTag(ICardItem);
                }
            }
        }
        if (v != null) {
            Object tagValue = v.getTag();
            if (tagValue != null && tagValue instanceof ICardItem) {
                ICardItem adapter = (ICardItem) tagValue;
                adapter.bind(v, getItem(position));
            }
        }
        return v;
    }

}
