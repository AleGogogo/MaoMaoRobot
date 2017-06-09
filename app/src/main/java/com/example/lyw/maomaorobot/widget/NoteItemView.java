package com.example.lyw.maomaorobot.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.lyw.maomaorobot.Bean.NoteItemBean;
import com.example.lyw.maomaorobot.R;
import com.example.lyw.maomaorobot.base.BaseCardView;

/**
 * Created by bluerain on 17-6-9.
 */

public class NoteItemView extends BaseCardView<NoteItemBean> {
    private TextView mItemNoteTitle;
    private TextView mItemNoteDes;
    private TextView mItemNoteDate;


    @Override
    protected View onDraw(Context context, NoteItemBean data, LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.item_listview_note , null);
        mItemNoteTitle = (TextView) rootView.findViewById(R.id.item_note_title);
        mItemNoteDes = (TextView) rootView.findViewById(R.id.item_note_des);
        mItemNoteDate = (TextView) rootView.findViewById(R.id.item_note_date);
        return rootView;
    }

    @Override
    protected void onBind(View rootView, NoteItemBean data, LayoutInflater inflater) {
        mItemNoteTitle.setText(data.title);
        mItemNoteDes.setText(data.des);
        mItemNoteDate.setText(data.date);
    }
}
