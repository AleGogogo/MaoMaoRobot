package com.xiaomi.shop2.base;

/**
 * Created by bluerain on 17-1-14.
 */

public interface ICardAble {

    boolean isValid();

    String getViewType();

    ICardItem onCreateCardView();
}
