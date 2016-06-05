package com.example.lyw.maomaorobot;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lyw.maomaorobot.Bean.CheatMessage;

import com.example.lyw.maomaorobot.Bean.LinkMsg;
import com.example.lyw.maomaorobot.Bean.NewsMsg;
import com.example.lyw.maomaorobot.Bean.ReturnMessage;
import com.example.lyw.maomaorobot.Bean.TextMsg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by LYW on 2016/5/28.
 */
public class CheatMessageAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    private ArrayList mDates;
    private static final String TAG = "CheatMessageAdapter";


    public CheatMessageAdapter(Context context,ArrayList list){
        mInflater = LayoutInflater.from(context);
        mDates = list;
    }
    @Override
    public int getCount() {return mDates.size();

    }

    @Override
    public java.lang.Object getItem(int i) {
        return mDates.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = mDates.get(position);
        int i=0;
        if (object instanceof ReturnMessage){
            i++;
            Log.d("TAG","maomao 第 "+i +"次出现");
            return 0;
        }else
            return 1;


    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Object message = mDates.get(position);
        ViewHolder mHolder = null;
       if (convertView ==null) {
           if (getItemViewType(position) == 0) {
               convertView = mInflater.inflate(R.layout.cheat_robot_layout, null);
               mHolder = new ViewHolder();
               mHolder.msgText = (TextView) convertView.findViewById(R.id.id_tvt_cheat_robot);
               mHolder.showTimeText = (TextView) convertView.findViewById(R.id.id_tvt_timeshow);

           } else {
               convertView = mInflater.inflate(R.layout.cheat_me_layout, null);
               mHolder = new ViewHolder();
               mHolder.msgText = (TextView) convertView.findViewById(R.id.id_tvt_cheat_me);
               mHolder.showTimeText = (TextView) convertView.findViewById(R.id.id_tvt_timeshow);

           }
           convertView.setTag(mHolder);
       }else{
           mHolder = (ViewHolder) convertView.getTag();
       }
           if (message instanceof ReturnMessage) {
               setDate(mDates.get(position),mHolder);
           }else if (message instanceof CheatMessage){
              setDate(mDates.get(position),mHolder);
           }
        return convertView;
    }

   private final class ViewHolder{
      TextView showTimeText;
       TextView msgText;
    }

    private void setDate(Object object,ViewHolder holder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (object instanceof CheatMessage) {
            holder.showTimeText.setText(sdf.format(new Date()));

            holder.msgText.setText(((CheatMessage) object).getMsg());
        } else {
            if (object instanceof ReturnMessage) {
                switch (typeConvertNumber((ReturnMessage)object)) {
                    case 0:
                        holder.showTimeText.setText((sdf.format(new Date())));

                        holder.msgText.setText("出错了，我没有找到匹配的东西");
                        break;
                    case 1:  holder.showTimeText.setText((sdf.format(new Date())));

                            holder.msgText.setText(((TextMsg) object).getmMsg());
                        break;
                    case 2:holder.showTimeText.setText(sdf.format(new Date()));

                        holder.msgText.setText(((LinkMsg) object).getmMsg());
                        break;
                    case 3:holder.showTimeText.setText(sdf.format(new Date()));

                        holder.msgText.setText(((NewsMsg) object).getmMsg());
                }


            }
        }
    }
    private int typeConvertNumber(ReturnMessage message){
        int n = 0;
        if (message instanceof TextMsg){
            n=1;
            return n;
        }else if (message instanceof LinkMsg){
            n=2;
            return n ;
        }else if (message instanceof NewsMsg){
            n =3;
            return n;
        }
        return 0;
    }
}
