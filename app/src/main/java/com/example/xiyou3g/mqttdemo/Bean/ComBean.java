package com.example.xiyou3g.mqttdemo.Bean;

/**
 * Created by Lance on 2017/8/2.
 */

public class ComBean {

    static public int SEND_TYPE = 1;
    static public int RECIVE_TYPE = 2;

    private String guideMsg;
    private String user;
    private int type;

    public void setGuideMsg(String guideMsg) {
        this.guideMsg = guideMsg;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGuideMsg() {
        return guideMsg;
    }

    public int getType() {
        return type;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
}

