package com.example.lyw.maomaorobot.Bean;

/**
 * Created by LYW on 2016/6/7.
 */
public class TextResponse extends BaseResponse {

    public String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TextResponse{" + super.toString() +
                "text='" + text + '\'' +
                '}';
    }
}
