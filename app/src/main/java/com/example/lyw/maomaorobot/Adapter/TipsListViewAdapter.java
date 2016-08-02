package com.example.lyw.maomaorobot.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.lyw.maomaorobot.Bean.TipsBean;
import com.example.lyw.maomaorobot.R;

import java.util.ArrayList;

/**
 * Created by rain on 2016/7/22.
 */
public class TipsListViewAdapter extends BaseAdapter {
    private ArrayList<TipsBean> mDatas;
    private LayoutInflater mLayoutInflater;
    private String tipsMessage;
    public TipsListViewAdapter(Context context, ArrayList<TipsBean> mDatas) {
        this.mDatas = mDatas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TipsBean itemBean = mDatas.get(position);

        convertView = mLayoutInflater.inflate(R.layout.item_tips_layout, null);
          new TipsViewHolder(convertView,itemBean);

        return convertView;
    }

     class TipsViewHolder{
         View parent;
        ImageButton mImageButton;
        EditText mEditTips;
         Button mHandButton;

         public TipsViewHolder(View parent, TipsBean bean){
            this.parent = parent;
             mImageButton = (ImageButton) parent.findViewById(R.id.id_img_createtip);

             mEditTips = (EditText) parent.findViewById(R.id.id_edit_createtip);

             mHandButton = (Button) parent.findViewById(R.id.id_but_handtips);

             mEditTips.setText(bean.getmTipsInfo());
             Log.d("TAG", "getView: editor info is" + bean.getmTipsInfo());
             mImageButton.setImageResource(bean.getmImageId());
             initListener();
         }

         private void initListener() {
             mImageButton.setOnClickListener(new View.OnClickListener() {

                 @Override
                 public void onClick(View v) {
                     mEditTips.setCursorVisible(true);
                     tipsMessage = mEditTips.getText().toString();
                 }
             });
             /*设置输入况状态*/
             mEditTips.addTextChangedListener(new TextWatcher() {
                 @Override
                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                 }

                 @Override
                 public void onTextChanged(CharSequence s, int start, int before, int count) {
                     if (isEditEmpty()) {
                         mHandButton.setVisibility(View.GONE);
                     } else {
                         mHandButton.setVisibility(View.VISIBLE);
                     }
                 }
                 @Override
                 public void afterTextChanged(Editable s) {

                 }
             });

             mHandButton.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                 }
             });

         }

         private boolean isEditEmpty() {
             return tipsMessage == null;
         }
     }

}
