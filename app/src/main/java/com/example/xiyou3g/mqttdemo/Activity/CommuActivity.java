package com.example.xiyou3g.mqttdemo.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.xiyou3g.mqttdemo.Adapter.CommuAdapter;
import com.example.xiyou3g.mqttdemo.Bean.ComBean;
import com.example.xiyou3g.mqttdemo.Content;
import com.example.xiyou3g.mqttdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lance on 2017/8/6.
 */

public class CommuActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView back;
    private EditText editText;
    private Button send;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.communi_activity);
        initWigth();
    }

    private void initWigth() {

        back = (ImageView) findViewById(R.id.back);
        Content.commRcyc = (RecyclerView) findViewById(R.id.recycler);
        editText = (EditText) findViewById(R.id.message);
        send = (Button) findViewById(R.id.send);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        Content.commRcyc.setLayoutManager(linearLayoutManager);

        Content.commuAdapter = new CommuAdapter(Content.comBeanList);
        Content.commRcyc.setAdapter(Content.commuAdapter);

        back.setOnClickListener(this);
        send.setOnClickListener(this);
        editText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.send:
                if(editText.getText().toString().length()>0&& Content.puptopic.length()>0){
                    ComBean comBean = new ComBean();
                    comBean.setGuideMsg(editText.getText().toString());
                    comBean.setType(ComBean.SEND_TYPE);
                    comBean.setUser("Me");
                    Content.comBeanList.add(comBean);
                    Content.commuAdapter.notifyItemInserted(Content.comBeanList.size()-1);
                    Content.commRcyc.scrollToPosition(Content.comBeanList.size()-1);
                    Content.myMqtt.pubMsg(Content.puptopic,editText.getText().toString().getBytes(),1);//发送消息
                    editText.setText("");
                }
                break;
            case R.id.message:
                Content.commRcyc.scrollToPosition(Content.comBeanList.size()-1);
                break;
        }
    }
}
