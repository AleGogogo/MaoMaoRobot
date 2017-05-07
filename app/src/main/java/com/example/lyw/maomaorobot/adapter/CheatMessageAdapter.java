package com.example.lyw.maomaorobot.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lyw.maomaorobot.activity.SecondActivity;
import com.example.lyw.maomaorobot.Bean.BaseResponseMessage;
import com.example.lyw.maomaorobot.Bean.CaiPuResponseMessage;
import com.example.lyw.maomaorobot.Bean.LinkResponseMessage;
import com.example.lyw.maomaorobot.Bean.NewsResponseMessage;
import com.example.lyw.maomaorobot.Bean.SendMessage;
import com.example.lyw.maomaorobot.Bean.TextResponseMessage;
import com.example.lyw.maomaorobot.Bean.TulingMessage;
import com.example.lyw.maomaorobot.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by LYW on 2016/5/28.
 */
public class CheatMessageAdapter extends BaseAdapter {

    private static final String TAG = "CheatMessageAdapter";

    private LayoutInflater mInflater;
    private List<TulingMessage> mDates;
    private Context mContext;

    public CheatMessageAdapter(Context context, List<TulingMessage> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDates = list;
    }

    public List<TulingMessage> getDates() {
        return mDates;
    }

    public void setDates(List<TulingMessage> mDates) {
        this.mDates = mDates;
    }

    @Override
    public int getCount() {
        return mDates.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDates.get(position).getMessageType();
    }

    @Override
    public Object getItem(int i) {
        return mDates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View itemView = null;
        if (getItemViewType(position) == TulingMessage.TYPE_MESSAGE_SEND) {
            itemView = mInflater.inflate(R.layout.cheat_me_layout, null);
            new SendMessageViewHolder(itemView, (SendMessage) mDates.get(position));
        }
        if (getItemViewType(position) == TulingMessage.TYPE_MESSAGE_RECEIVE) {
            BaseResponseMessage response = (BaseResponseMessage) mDates.get(position);
            itemView = initViewHolder(response.getCode(), position);
        }
        return itemView;
    }

    private View initViewHolder(int responseMessageType, int position) {
        View itemView = null;
        switch (responseMessageType) {
            case BaseResponseMessage.RESPONSE_TYPE_TEXT:

                itemView = mInflater.inflate(R.layout
                        .cheat_robot_layout, null);
                new TextResponseViewHodler(itemView, (BaseResponseMessage) mDates
                        .get(position));
                break;

            case BaseResponseMessage.RESPONSE_TYPE_LINK:
                itemView = mInflater.inflate(R.layout
                        .layout_item_link_message, null);
                new LinkResponseViewHolder(itemView, (BaseResponseMessage) mDates
                        .get(position));
                break;
            case BaseResponseMessage.RESPONSE_TYPE_NEWS:
                itemView = mInflater.inflate(R.layout
                        .layout_item_news_message, null);
                new NewsResponseViewHolder(itemView, (BaseResponseMessage) mDates
                        .get(position));
                break;
            case BaseResponseMessage.RESPONSE_TYPE_CAIPU:
                itemView = mInflater.inflate(R.layout
                        .layout_item_caipu_message, null);
                new CaiPuResponseViewHolder(itemView, (BaseResponseMessage) mDates
                        .get(position));
                break;
            default:
                break;

        }
        if (null != itemView)
            itemView.setTag(mDates.get(position));

        return itemView;
    }


    class TextResponseViewHodler {

        View parent;
        public TextView mShowTime;
        public TextView mTextView;

        public TextResponseViewHodler(View parent, BaseResponseMessage response) {
            this.parent = parent;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TextResponseMessage textResponse = (TextResponseMessage) response;
            mTextView = (TextView) parent.findViewById(R.id
                    .id_tvt_cheat_robot);
            mTextView.setText(textResponse.getText());
            mShowTime = (TextView) parent.findViewById(R.id.id_tvt_timeshow);
            mShowTime.setText(sdf.format(new Date(System.currentTimeMillis())));
        }
    }

    class LinkResponseViewHolder extends TextResponseViewHodler {
        public TextView mUrl;

        public LinkResponseViewHolder(View parent, BaseResponseMessage response) {
            super(parent, response);
            LinkResponseMessage linkResponse = (LinkResponseMessage) response;
            mUrl = (TextView) parent.findViewById(R.id
                    .txv_list_view_message_url_link);

            mUrl.setText(linkResponse.getUrl());
            jumpTo(mUrl, response);
        }

    }


    class NewsResponseViewHolder extends TextResponseViewHodler {
        public List<NewsItemViewHolder> mItems;

        class NewsItemViewHolder {
            ImageView mIcon;
            TextView mTextViewTitle;

        }

        public NewsResponseViewHolder(View parent, BaseResponseMessage response) {
            super(parent, response);
            NewsResponseMessage newsResponse = (NewsResponseMessage) response;
            LinearLayout container = (LinearLayout) parent.findViewById(R.id
                    .layout_news_container);
            List<NewsResponseMessage.ListBean> list = newsResponse.getList();
            for (final NewsResponseMessage.ListBean bean : list) {
                View item = mInflater.inflate(R.layout
                        .layout_item_news_item, null);
                ImageView igv = (ImageView) item.findViewById(R.id
                        .igv_news_item_icon);
                TextView txv = (TextView) item.findViewById(R.id
                        .txv_news_item_title);
                // TODO: 2016/6/7  设定image
                igv.setImageResource(R.mipmap.ic_launcher);
                txv.setText(bean.getArticle());
                // TODO: 2016/6/7 设定跳转
                jumpTo(txv, bean);

                LinearLayout.LayoutParams params = new LinearLayout
                        .LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                container.addView(item, params);
            }
        }
    }

    private void jumpTo(TextView textview, final Object o) {

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String jumpUrl;
                if (o instanceof LinkResponseMessage) {
                    jumpUrl = ((LinkResponseMessage) o).getUrl();
                } else if (o instanceof CaiPuResponseMessage.ListBean) {
                    jumpUrl = ((CaiPuResponseMessage.ListBean) o)
                            .getDetailurl();
                } else {
                    jumpUrl = ((NewsResponseMessage.ListBean) o)
                            .getDetailurl();
                }
                Uri url = Uri.parse(jumpUrl);
                Intent i = new Intent(mContext, SecondActivity.class);
                i.setData(url);
                mContext.startActivity(i);
            }
        });
    }

    class CaiPuResponseViewHolder extends TextResponseViewHodler {

        public List<CaiPuItemViewHolder> mItems;

        class CaiPuItemViewHolder {
            ImageView mIcon;
            TextView mTextViewTitle;
            TextView mTextViewInfo;
        }

        public CaiPuResponseViewHolder(View parent, BaseResponseMessage response) {
            super(parent, response);
            // TODO: 2016/6/7 可以参考上面
            CaiPuResponseMessage caiPuResponse = (CaiPuResponseMessage) response;
            LinearLayout contanner = (LinearLayout) parent.findViewById(R.id.layout_caipu_container);
            List<CaiPuResponseMessage.ListBean> list = caiPuResponse.getList();


            //你仔细看看我上面是怎么写的。 你这句话不就相当于new了一个新的view
            // 你给这个view赋值有什么用？他又不会添加到listview中去，真正添加进去的是parent吧！
            // 另外，我上面是动态添加item的吧，因为你并不知道服务器会给你返回多少的item，so，还是仿照上面写吧

            // TODO: 2016/6/14 设置图标
            for (CaiPuResponseMessage.ListBean bean : list
                    ) {
                View item = mInflater.inflate(R.layout.layout_item_caipi_item, null);
                final ImageView mIcon = (ImageView) item.findViewById(R.id.igv_caipu_icon);
                TextView mTitle = (TextView) item.findViewById(R.id.txv_caipu_item_title);
                TextView mInfo = (TextView) item.findViewById(R.id.txv_caipu_item_info);
                String imageUrl = bean.getIcon();
                setImageIcon(imageUrl, mIcon);
                CaiPuResponseMessage.ListBean data = list.get(0);
                //你仔细看看我上面是怎么写的。 你这句话不就相当于new了一个新的view
                // 你给这个view赋值有什么用？他又不会添加到listview中去，真正添加进去的是parent吧！
                // 另外，我上面是动态添加item的吧，因为你并不知道服务器会给你返回多少的item，so，还是仿照上面写吧
                CaiPuItemViewHolder caiPuItemViewHolder = new CaiPuItemViewHolder();
                caiPuItemViewHolder.mIcon = (ImageView) parent.findViewById(R.id.igv_caipu_icon);
                caiPuItemViewHolder.mTextViewTitle = (TextView) parent.findViewById(R.id.txv_caipu_item_title);
                caiPuItemViewHolder.mTextViewInfo = (TextView) parent.findViewById(R.id.txv_caipu_item_info);
                mIcon.setImageResource(R.mipmap.ic_launcher);
                mTitle.setText(bean.getName());
                mInfo.setText(bean.getInfo());

                LinearLayout.LayoutParams params = new LinearLayout
                        .LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                contanner.addView(item, params);
            }

        }
    }


    class SendMessageViewHolder {

        View parent;
        ProgressBar mProgressBarSendIng;
        public TextView mShowTime;
        public TextView mTextView;

        public SendMessageViewHolder(View parent, SendMessage message) {
            this.parent = parent;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            mTextView = (TextView) parent.findViewById(R.id.txv_send_message);
            mTextView.setText(message.getInfo());
            mShowTime = (TextView) parent.findViewById(R.id.id_tvt_timeshow);
            mShowTime.setText(sdf.format(message.getMessageData()));
            mProgressBarSendIng = (ProgressBar) parent.findViewById(R.id.progressBar_send_ing);
            // TODO: 2016/7/2 目前最弱智的一种做法，时间复杂度很高
            mProgressBarSendIng.setVisibility(message.isHadResponse() ? View.GONE : View.VISIBLE);

        }
    }

    private void setImageIcon(String url, final ImageView image) {
        ImageSize size = new ImageSize(70, 70);
        ImageLoader.getInstance().loadImage(url, size, new SimpleImageLoadingListener() {

            @Override
            public void onLoadingComplete(String imageUri, View
                    view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                image.setImageBitmap(loadedImage);
            }
        });

    }
}
