package com.example.xiyou3g.mqttdemo.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiyou3g.mqttdemo.Adapter.PupAdapter;
import com.example.xiyou3g.mqttdemo.Adapter.UserAdapter;
import com.example.xiyou3g.mqttdemo.Bean.ComBean;
import com.example.xiyou3g.mqttdemo.Content;
import com.example.xiyou3g.mqttdemo.Mqtt.ComponentSetting;
import com.example.xiyou3g.mqttdemo.Mqtt.MyMqtt;
import com.example.xiyou3g.mqttdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static com.example.xiyou3g.mqttdemo.Content.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

//    private TextView hosturl;
//    private Button hostconnect;
    private EditText pupedit;
    private Button pupadd;
    private EditText submsg;
    private Button subsadd;
    private RecyclerView puplist;
    private RecyclerView userlist;
    private FloatingActionButton flb;

    private Dialog dialog;

    private List<String> pupList = new ArrayList<>();
    private PupAdapter pupAdapter;
    private LinearLayoutManager pupllm;
    private List<String> userList = new ArrayList<>();
    private UserAdapter userAdapter;
    private LinearLayoutManager userllm;

    /**MQTT相关参数**/
    private static String host = "123.207.145.251";//主机地址
    private static String port = "1883";//MQTT端口(一般为1883)
    private static String userID = "";//用户ID(无可以不填)
    private static String passWord = "";//用户密码(无可以不填)
    private static String clientID = UUID.randomUUID().toString();//随机生成字符串，放置clientID冲突
    private static String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Content.comBeanList.clear();
        initWight();
        initMqtt();
    }

    private void initWight(){

//        hosturl = (TextView) findViewById(R.id.hosturl);
//        hostconnect = (Button) findViewById(R.id.serverconnect);
        pupedit = (EditText) findViewById(R.id.topic);
        pupadd = (Button) findViewById(R.id.pupadd);
        submsg = (EditText) findViewById(R.id.subtopic);
        subsadd = (Button) findViewById(R.id.addsub);
        puplist = (RecyclerView) findViewById(R.id.puplist);
        userlist = (RecyclerView) findViewById(R.id.userlist);
        flb = (FloatingActionButton) findViewById(R.id.flb);

        flb.setOnClickListener(this);
        userllm = new LinearLayoutManager(this);
        userllm.setOrientation(LinearLayoutManager.VERTICAL);
        userlist.setLayoutManager(userllm);
        userAdapter = new UserAdapter(userList);
        userlist.setAdapter(userAdapter);

        pupllm = new LinearLayoutManager(this);
        pupllm.setOrientation(LinearLayoutManager.VERTICAL);
        puplist.setLayoutManager(pupllm);
        pupAdapter = new PupAdapter(pupList);
        puplist.setAdapter(pupAdapter);

//        hostconnect.setOnClickListener(this);
        pupadd.setOnClickListener(this);
        subsadd.setOnClickListener(this);

//        btn_connect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myMqtt.setMqttSetting(edt_host.getText().toString(),port,userID,passWord,clientID);//开启Mqtt连接
//                myMqtt.connectMqtt();
//            }
//        });
//        btn_disConnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myMqtt.disConnectMqtt();//断开Mqtt连接
//            }
//        });
//        btn_pub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myMqtt.pubMsg(edt_pubTopic.getText().toString(),edt_pubMsg.getText().toString().getBytes(),1);//发送消息
//            }
//        });-
//        btn_sub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myMqtt.subTopic(edt_subTopic.getText().toString(),2);//收听指定Topic
//            }
//        });
    }

    private void initMqtt(){
        myMqtt = new MyMqtt(new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what== ComponentSetting.MQTT_STATE_CONNECTED){
                    //当Mqtt连接成功时
                    Toast.makeText(MainActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                    hosturl.setText(host);
                    Log.d(TAG,"连接成功");
                }else if(msg.what==ComponentSetting.MQTT_STATE_LOST){
                    //当丢失连接后
                    Toast.makeText(MainActivity.this,"连接丢失，进行重连",Toast.LENGTH_SHORT).show();
                }else if(msg.what==ComponentSetting.MQTT_STATE_FAIL){
                    //当连接失败时
                    Toast.makeText(MainActivity.this,"连接失败",Toast.LENGTH_SHORT).show();
                }else if(msg.what==ComponentSetting.MQTT_STATE_RECEIVE){
                    //当主线程收到数据后做什么
                    Bundle bundle = msg.getData();
                    ComBean comBean = new ComBean();
                    comBean.setGuideMsg(bundle.getString(ComponentSetting.MESSAGE));
                    comBean.setType(ComBean.RECIVE_TYPE);
                    comBean.setUser(bundle.getString(ComponentSetting.TOPIC));
                    Content.comBeanList.add(comBean);
                    Content.commuAdapter.notifyItemInserted(Content.comBeanList.size()-1);
                    Content.commRcyc.scrollToPosition(Content.comBeanList.size()-1);

//                    message.add(bundle.getString(ComponentSetting.TOPIC)+":"+bundle.getString(ComponentSetting.MESSAGE));//向List添加数据
//                    adapter.notifyDataSetChanged();//调用Adapter的notifyDataSetChanged，更改列表的显示
                }
                super.handleMessage(msg);
            }
        });
        myMqtt.setMqttSetting(host,port,userID,passWord,clientID);//设置当前Mqtt连接信息
        myMqtt.connectMqtt();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.serverconnect:
//                dialog = new Dialog(this);
//                dialog.setContentView(R.layout.login_dialog);
//                final TextInputLayout textInputLayout = (TextInputLayout) dialog.findViewById(R.id.serverlayout);
//                Button connect = (Button) dialog.findViewById(R.id.connect);
//                connect.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        host = textInputLayout.getEditText().getText().toString();
//                        myMqtt.setMqttSetting(host,port,userID,passWord,clientID);//开启Mqtt连接
//                        myMqtt.connectMqtt();
//                    }
//                });
//                dialog.show();
//                break;
            case R.id.pupadd:
                if(pupedit.getText().toString().length()>0){
                    puptopic = pupedit.getText().toString();
                    pupList.add(pupedit.getText().toString());
                    pupAdapter.notifyItemInserted(pupList.size()-1);
                    puplist.scrollToPosition(pupList.size()-1);
                    pupedit.setText("");
                }
                break;
            case R.id.addsub:
                if(submsg.getText().toString().length()>0){
                    myMqtt.subTopic(submsg.getText().toString(),2);//收听指定Topic
                    userList.add(submsg.getText().toString());
                    userAdapter.notifyItemInserted(userList.size()-1);
                    userlist.scrollToPosition(userList.size()-1);
                    submsg.setText("");
                }
                break;
            case R.id.flb:
                if(pupList.size()>0 && userList.size()>0){
                    Intent intent = new Intent(this,CommuActivity.class);
                    startActivity(intent);
                }else if(pupList.size()==0){
                    Snackbar.make(flb,"请添加您的ID",Snackbar.LENGTH_SHORT).setAction("好的",null);
                }else if(userList.size()==0){
                    Snackbar.make(flb,"请添加订阅ID",Snackbar.LENGTH_SHORT).setAction("好的",null);
                }
                break;
        }
    }
}
