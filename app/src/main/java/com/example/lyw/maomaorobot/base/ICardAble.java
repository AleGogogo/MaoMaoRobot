package com.example.lyw.maomaorobot.base;

/**
 * Created by bluerain on 17-1-14.
 */

public interface ICardAble {

    boolean isValid();

    String getViewType();

    ICardItem onCreateCardView();
}
