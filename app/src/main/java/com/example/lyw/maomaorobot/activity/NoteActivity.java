package com.example.lyw.maomaorobot.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.lyw.maomaorobot.Bean.NoteItemBean;
import com.example.lyw.maomaorobot.R;
import com.example.lyw.maomaorobot.base.CardViewListAdapter;
import com.example.lyw.maomaorobot.base.ICardAble;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bluerain on 17-6-9.
 */

public class NoteActivity extends Activity {
    private ListView mLvTips;
    private ImageView mIgvPlus;

    private ArrayList<ICardAble> mData = new ArrayList<>();
    private CardViewListAdapter mCardViewListAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);
        customTitle();
        initViews();
        loadData();
    }

    private void loadData() {
        queryData();
    }

    private void customTitle() {
//        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43b05c")));
        getActionBar().setTitle("备忘录");
    }

    private void queryData() {
        mCardViewListAdapter.clear();
        final List<NoteItemBean> all = DataSupport.findAll(NoteItemBean.class);
        mCardViewListAdapter.addAll(all);
        mCardViewListAdapter.notifyDataSetChanged();
    }

    private void initViews() {
        mLvTips = (ListView) findViewById(R.id.lv_tips);
        mIgvPlus = (ImageView) findViewById(R.id.igv_plus);

        mCardViewListAdapter = new CardViewListAdapter(this, mData, mLvTips);
        mLvTips.setAdapter(mCardViewListAdapter);

        mIgvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPagerActivity();
            }
        });
    }

    private void launchPagerActivity() {
        Intent intent = new Intent(this, NotePageActivity.class);
        startActivityForResult(intent , 11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 11:
               queryData();
                break;
        }
    }
}
