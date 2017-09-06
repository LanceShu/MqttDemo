package com.example.xiyou3g.mqttdemo.Mqtt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.UUID;

/**
 * Created by Lance on 2017/8/4.
 */

public class MyMqtt {

    /*gDeBug用信息*/
    private String Tag = "MyMqtt";
    private boolean isDebug = false;

    /*MQTT配置信息*/
    private static String host = "123.207.145.251";
    private static String port = "1883";
    private static String userID = "";
    private static String passWord = "";
    private static String clientID = UUID.randomUUID().toString();

    /*MQTT状态信息*/
    public boolean isConnect = false;

    /*系统变量*/
    private Handler handler;

    /*MQTT支持类*/
    private MqttAsyncClient mqttAsyncClient = null;

    /*构造函数，默认的Mqtt信息内容，直接在Log中输出相关信息*/
    public MyMqtt(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case ComponentSetting.MQTT_STATE_CONNECTED:
                        Log.e(Tag,"链接成功");
                        break;
                    case ComponentSetting.MQTT_STATE_LOST:
                        Log.e(Tag,"链接丢失，进行重连");
                        break;
                    case ComponentSetting.MQTT_STATE_FAIL:
                        Log.e(Tag,"链接失败");
                        break;
                    case ComponentSetting.MQTT_STATE_RECEIVE:
                        Log.e(Tag, (String) msg.obj);
                        break;
                }
            }
        };
    }

    /*构造函数，如果需要在其他类中调用Mqtt信息的话，需要传入一个Handle*/
    public MyMqtt(Handler handler){
        this.handler = handler;
    }

    public void setMqttSetting(String host,String port,String userID,String passWord,String clientID){
        this.host = host;
        this.port = port;
        this.userID = userID;
        this.passWord = passWord;
        this.clientID = clientID;
    }

    /*进行Mqtt链接*/
    public void connectMqtt(){
        try {
            mqttAsyncClient = new MqttAsyncClient("tcp://"+host,"ClientID"+clientID,new MemoryPersistence());
            mqttAsyncClient.connect(getOptions(),null,mqttActionListener);
            mqttAsyncClient.setCallback(callback);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /*重新链接*/
    public void reStartMqtt(){
        disConnectMqtt();
        connectMqtt();
    }

    /*断开链接*/
    public void disConnectMqtt(){
        try {
            mqttAsyncClient.disconnect();
            mqttAsyncClient = null;
            isConnect = false;
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /*向Mqtt服务器发送数据*/
    public void pubMsg(String Topic,String Msg,int Qos){
        if(!isConnect){
            Log.e(Tag,"Mqtt链接未打开");
            return;
        }
        try {
            mqttAsyncClient.publish(Topic,Msg.getBytes(),Qos,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /*向Mqtt服务器发送数据*/
    public void pubMsg(String topic,byte[] Msg,int qos){
        if(!isConnect){
            Log.e(Tag,"Mqtt链接为打开");
            return;
        }
        try {
            mqttAsyncClient.publish(topic,Msg,qos,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /*向Mqtt服务器订阅一个Topic*/
    public void subTopic(String topic,int qos){
        if(!isConnect){
            Log.e(Tag,"链接未打开");
            return;
        }
        try {
            mqttAsyncClient.subscribe(topic, qos);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private MqttConnectOptions getOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        if(userID!=null&&userID.length()>0&&passWord!=null&&passWord.length()>0){
            options.setUserName(userID);
            options.setPassword(passWord.toCharArray());
        }
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(30);
        return options;
    }

    /*自带监听类，判断Mqtt活动变化*/
    private IMqttActionListener mqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Message msg = Message.obtain();
            msg.what = ComponentSetting.MQTT_STATE_CONNECTED;
            isConnect = true;
            handler.sendMessage(msg);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            exception.printStackTrace();
            Message msg = Message.obtain();
            isConnect = false;
            handler.sendMessage(msg);
            new Thread(){
                @Override
                public void run() {
                    try {
                        sleep(300);
                        connectMqtt();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    };

    /*自带监听回传类,向handle发送Message*/
    private MqttCallback callback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
                /*链接断开*/
            Message msg = Message.obtain();
            msg.what = ComponentSetting.MQTT_STATE_LOST;
            isConnect = false;
            handler.sendMessage(msg);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
                /*消息到达*/
            Message msg = Message.obtain();
            Bundle obj = new Bundle();
            obj.putString(ComponentSetting.TOPIC,topic);
            obj.putString(ComponentSetting.MESSAGE,new String(message.getPayload()));
            msg.what = ComponentSetting.MQTT_STATE_RECEIVE;
            msg.obj = new String(message.getPayload());
            msg.setData(obj);
            handler.sendMessage(msg);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
                /*消息发送完成*/
        }
    };
}
