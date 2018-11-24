package com.prize.webgame.http;

import android.content.Context;

import com.google.gson.Gson;
import com.prize.webgame.BuildConfig;
import com.prize.webgame.bean.CheckUpdateResultBean;
import com.prize.webgame.bean.HomeGameResultBean;
import com.prize.webgame.bean.HotGameResultBean;
import com.prize.webgame.bean.TopGameResultBean;
import com.prize.webgame.utils.Verification;
import com.prize.webgame.utils.XXTEAUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;


public class HttpClient extends HttpApi {


    //1: 请求上报，2：曝光上报，3：点击下载上报 4：下载完场上报
    public static final int REPORT_TYPE_REQUEST = 1;
    public static final int REPORT_TYPE_EXPORT = 2;
    public static final int REPORT_TYPE_DOWNLOAD_START = 3;
    public static final int REPORT_TYPE_DOWNLOAD_SUCCESS = 4;

    static {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new HttpInterceptor())
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public static void checkUpdate(Context context, final HttpCallback<CheckUpdateResultBean> callback) {

        ClientInfo mClientInfo = ClientInfo.getInstance(context);
        mClientInfo.setClientStartTime(System.currentTimeMillis());
        mClientInfo.setNetStatus(ClientInfo.networkType);

        String headParams = new Gson().toJson(mClientInfo);

        headParams = XXTEAUtil.getParamsEncypt(headParams);

        TreeMap<String, String> params = new TreeMap<>();
        params.put("appVersion", BuildConfig.VERSION_NAME);
        String sign = Verification.getInstance().getSign(params);

        OkHttpUtils.post().url(CHECK_UPDATE)
                .addHeader("params", headParams)
                .addParams("appVersion", BuildConfig.VERSION_NAME)
                .addParams("sign", sign)
                .build()
                .execute(new CommonCallback<CheckUpdateResultBean>(CheckUpdateResultBean.class) {
                    @Override
                    public void onResponse(CheckUpdateResultBean response, int id) {
                        if (response == null)
                            callback.onFail(null);
                        else
                            callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onAfter(int id) {
                        callback.onFinish();
                    }
                });

    }

    public static void getHomeGameList(Context context, final HttpCallback<HomeGameResultBean> callback) {

        ClientInfo mClientInfo = ClientInfo.getInstance(context);
        mClientInfo.setClientStartTime(System.currentTimeMillis());
        mClientInfo.setNetStatus(ClientInfo.networkType);

        String headParams = new Gson().toJson(mClientInfo);

        headParams = XXTEAUtil.getParamsEncypt(headParams);

        TreeMap<String, String> params = new TreeMap<>();
        params.put("source", "");
        params.put("name", "");
        params.put("pageIndex", "1");
        params.put("pageSize", "20");

        String sign = Verification.getInstance().getSign(params);

        OkHttpUtils.get().url(GET_HOME_LIST)
                // .addHeader("App-Key", appkey)
                .addHeader("params", headParams)
                .addParams("pageIndex", "1")
                .addParams("pageSize", "20")
                .addParams("source", "")
                .addParams("name", "")
                .addParams("sign", sign)
                .build()
                .execute(new CommonCallback<HomeGameResultBean>(HomeGameResultBean.class) {
                    @Override
                    public void onResponse(HomeGameResultBean response, int id) {
                        if (response == null)
                            callback.onFail(null);
                        else
                            callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onAfter(int id) {
                        callback.onFinish();
                    }
                });

    }

    public static void getTopList(Context context, final HttpCallback<TopGameResultBean> callback) {

        ClientInfo mClientInfo = ClientInfo.getInstance(context);
        mClientInfo.setClientStartTime(System.currentTimeMillis());
        mClientInfo.setNetStatus(ClientInfo.networkType);

        String headParams = new Gson().toJson(mClientInfo);

        headParams = XXTEAUtil.getParamsEncypt(headParams);

        OkHttpUtils.get().url(GET_TOP_LIST)
                .addHeader("params", headParams)
                .addParams("sign", Verification.getInstance().getSign(new TreeMap<>()))
                .build()
                .execute(new CommonCallback<TopGameResultBean>(TopGameResultBean.class) {
                    @Override
                    public void onResponse(TopGameResultBean response, int id) {
                        if (response == null)
                            callback.onFail(null);
                        else
                            callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onAfter(int id) {
                        callback.onFinish();
                    }
                });

    }

    public static void getHotList(Context context, final HttpCallback<HotGameResultBean> callback) {

        ClientInfo mClientInfo = ClientInfo.getInstance(context);
        mClientInfo.setClientStartTime(System.currentTimeMillis());
        mClientInfo.setNetStatus(ClientInfo.networkType);

        String headParams = new Gson().toJson(mClientInfo);

        headParams = XXTEAUtil.getParamsEncypt(headParams);

        OkHttpUtils.get().url(GET_HOT_LIST)
                // .addHeader("App-Key", appkey)
                .addHeader("params", headParams)
                .addParams("sign", Verification.getInstance().getSign(new TreeMap<>()))
                .build()
                .execute(new CommonCallback<HotGameResultBean>(HotGameResultBean.class) {
                    @Override
                    public void onResponse(HotGameResultBean response, int id) {
                        if (response == null)
                            callback.onFail(null);
                        else
                            callback.onSuccess(response);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onAfter(int id) {
                        callback.onFinish();
                    }
                });

    }


    /*@TargetApi(Build.VERSION_CODES.KITKAT)
    public static void getAdList(Context context, String appkey, final HttpCallback<AdInfo[]> callback)
    {

        ClientInfo mClientInfo = ClientInfo.getInstance(context);
        mClientInfo.setClientStartTime(System.currentTimeMillis());
        mClientInfo.setNetStatus(ClientInfo.networkType);

        String headParams = new Gson().toJson(mClientInfo);

        headParams = XXTEAUtil.getParamsEncypt(headParams);

        *//*RequestParams entity = new RequestParams();
        entity.addHeader("params", headParams);

        entity.addBodyParameter("pageIndex", "1");
        entity.addBodyParameter("pageSize", "20");*//*
        TreeMap<String, String> params = new TreeMap<>();
        params.put("pageIndex", "1");
        params.put("pageSize", "20");


        String sign = Verification.getInstance().getSign(params);
        //entity.addBodyParameter("sign", sign);

        OkHttpUtils.post().url(GET_TOP_LIST)
                .addHeader("App-Key", appkey)
                .addHeader("params", headParams)
                .addParams("sign", sign)
                .addParams("pageIndex", "1")
                .addParams("pageSize", "20")
                .build()
                .execute(new CommonCallback<TopGameResultBean[]>(TopGameResultBean[].class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(TopGameResultBean[] response, int id) {
                        if(response == null)
                            callback.onFail(null);
                        else
                            callback.onSuccess(response);
                    }

                    @Override
                    public void onAfter(int id) {
                        callback.onFinish();
                    }

                });
    }*/

    //1: 请求上报，2：曝光上报，3：点击下载上报 4：下载完场上报
    public static void adReport(Context context, String appkey, String appPackageName, int reportType) {

        /*ClientInfo mClientInfo = ClientInfo.getInstance(context);
        mClientInfo.setClientStartTime(System.currentTimeMillis());
        mClientInfo.setNetStatus(ClientInfo.networkType);

        String headParams = new Gson().toJson(mClientInfo);

        headParams = XXTEAUtil.getParamsEncypt(headParams);

        RequestParams entity = new RequestParams();
        entity.addHeader("params", headParams);

        entity.addBodyParameter("pageIndex", "1");
        entity.addBodyParameter("pageSize", "20");


        String sign = Verification.getInstance().getSign(entity.getBodyParams());
        entity.addBodyParameter("sign", sign);

        OkHttpUtils.post().url(AD_REPORT)
                .addHeader("App-Key", appkey)
                .addHeader("params", headParams)
                .addParams("sign", sign)
                .addParams("appPackageName", appPackageName)
                //.addParams("imei", "12345678")
                .addParams("imei", mClientInfo.getImei(context))
                .addParams("ip", IpGetUtil.getIPAddress(context))
                .addParams("reportType", reportType + "")

                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("","");
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("","");
            }
        });*/

    }

}
