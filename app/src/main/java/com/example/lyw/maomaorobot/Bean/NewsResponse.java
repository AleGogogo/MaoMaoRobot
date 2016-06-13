package com.example.lyw.maomaorobot.Bean;

import java.util.List;

/**
 * Created by LYW on 2016/6/7.
 */
public class NewsResponse extends TextResponse {

    public List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        public String article;
        public String source;
        public String icon;
        public String detailurl;

        public ListBean() {
        }

        public ListBean(String article, String source, String icon, String
                detailurl) {
            this.article = article;
            this.source = source;
            this.icon = icon;
            this.detailurl = detailurl;
        }

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
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
                    "article='" + article + '\'' +
                    ", source='" + source + '\'' +
                    ", icon='" + icon + '\'' +
                    ", detailurl='" + detailurl + '\'' +
                    '}';
        }
    }
}
