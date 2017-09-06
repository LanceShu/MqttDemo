package com.example.xiyou3g.mqttdemo;

import android.support.v7.widget.RecyclerView;

import com.example.xiyou3g.mqttdemo.Adapter.CommuAdapter;
import com.example.xiyou3g.mqttdemo.Bean.ComBean;
import com.example.xiyou3g.mqttdemo.Mqtt.MyMqtt;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lance on 2017/8/6.
 */

public class Content {

    public static List<ComBean> comBeanList = new ArrayList<>();
    public static  CommuAdapter commuAdapter;;
    public static RecyclerView commRcyc;
    public static MyMqtt myMqtt;
    public static String puptopic;
}
