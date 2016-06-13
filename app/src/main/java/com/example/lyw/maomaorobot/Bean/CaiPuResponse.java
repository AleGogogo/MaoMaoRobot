package com.example.lyw.maomaorobot.Bean;

import java.util.List;

/**
 * Created by LYW on 2016/6/7.
 */
public class CaiPuResponse extends TextResponse {

    public List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        public String name;
        public String icon;
        public String info;
        public String detailurl;

        public ListBean(String name, String icon, String info, String
                detailurl) {
            this.name = name;
            this.icon = icon;
            this.info = info;
            this.detailurl = detailurl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getDetailurl() {
            return detailurl;
        }

        public void setDetailurl(String detailurl) {
            this.detailurl = detailurl;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "name='" + name + '\'' +
                    ", info='" + info + '\'' +
                    ", icon='" + icon + '\'' +
                    ", detailurl='" + detailurl + '\'' +
                    '}';
        }
        }
    }

