package com.example.lyw.maomaorobot.Bean;

import com.example.lyw.maomaorobot.base.ICardAble;
import com.example.lyw.maomaorobot.base.ICardItem;
import com.example.lyw.maomaorobot.widget.NoteItemView;

import org.litepal.crud.DataSupport;

/**
 * Created by bluerain on 17-6-9.
 */

public class NoteItemBean extends DataSupport implements ICardAble {

   public String title;
    public String des;
    public long date;


    public NoteItemBean(String title, String des, long date) {
        this.title = title;
        this.des = des;
        this.date = date;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String getViewType() {
        return "item";
    }

    @Override
    public ICardItem onCreateCardView() {
        return new NoteItemView();
    }
}
