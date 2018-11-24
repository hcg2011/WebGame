package com.prize.webgame.http;

public class HttpApi {
    private static final String API = "http://open.szprize.cn";

    public static final String GET_TOP_LIST = API + "/mgame/h5/topList";
    public static final String GET_HOT_LIST = API + "/mgame/h5/hotList";
    public static final String GET_HOME_LIST = API + "/mgame/h5/home";

    public static final String CHECK_UPDATE = API + "/mgame/h5/upgrade";
    //public static final String GET_HOME_LIST = "http://192.168.0.154:10501/mgame/h5/home";
}
