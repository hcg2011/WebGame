package com.prize.webgame.bean;

import java.io.Serializable;

/**
 * Created by prize on 2018/10/31.
 */

public class HotGameResultBean implements Serializable
{
    private String code;
    private String msg;

    private HotDataBean data;


    public boolean isSuccess()
    {
        return code != null && code.equals("00000");
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HotDataBean getData() {
        return data;
    }

    public void setData(HotDataBean data) {
        this.data = data;
    }
}
