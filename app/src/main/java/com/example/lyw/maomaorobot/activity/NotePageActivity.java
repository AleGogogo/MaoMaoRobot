package com.example.lyw.maomaorobot.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lyw.maomaorobot.Bean.NoteItemBean;
import com.example.lyw.maomaorobot.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotePageActivity extends Activity {

    private EditText mNotePagerTitle;
    private EditText mNotePagerContent;
    private String mDate;

    private boolean isSaved;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-DD-hh-mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);
        customTitle();
        mNotePagerTitle = (EditText) findViewById(R.id.note_pager_title);
        mNotePagerTitle.setHint("标题");
        mNotePagerContent = (EditText) findViewById(R.id.note_pager_content);
        mNotePagerContent.setHint("内容");
        mNotePagerContent.requestFocus();
    }

    private void customTitle() {
        final ActionBar actionBar = getActionBar();
//        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#43b05c")));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("编辑备忘录");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!isSaved)
                    onSaveClick();
                break;
            case R.id.action_close:
                onCloseClick();
                break;
            case R.id.action_save:
                if (!isSaved)
                onSaveClick();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }


    private void onSaveClick() {
        final String inputTitle = mNotePagerTitle.getText().toString();
        final String inputContent = mNotePagerContent.getText().toString();
        if (TextUtils.isEmpty(inputContent)){
            Toast.makeText(this, "没有输入任何内容,不保存!", Toast.LENGTH_SHORT).show();
            closePage();
            return;
        }
        final String date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        NoteItemBean itemBean = new NoteItemBean(
                TextUtils.isEmpty(inputTitle) ? "默认标题" : inputTitle,
                inputContent , date);
        itemBean.save();
        isSaved = true;
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        closePage();
    }

    private void onCloseClick() {
        closePage();
    }

    private void closePage() {
        NotePageActivity.this.setResult(RESULT_OK);
        NotePageActivity.this.finish();
    }
}
