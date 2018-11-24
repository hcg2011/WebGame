package com.prize.webgame.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.prize.webgame.R;
import com.prize.webgame.bean.GameBean;
import com.prize.webgame.db.DBMediaHelper;
import com.prize.webgame.utils.BitMapUtils;

/**
 * Created by prize on 2018/11/15.
 */

public class HomePageFragment_old extends BaseFragment  {

    private AgentWeb mAgentWeb = null;
    private LinearLayout mLinearLayout;

    private String webUrl = "";
    private String curLoadUrl = "";

    private GameBean mGameBean = null;

    public static HomePageFragment_old newInstance(String webUrl) {

        Bundle args = new Bundle();
        args.putString("WEB_URL", webUrl);
        HomePageFragment_old fragment = new HomePageFragment_old();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page_old, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mLinearLayout = (LinearLayout) view.findViewById(R.id.view_wv_home_page);

        final Bundle bundle = getArguments();

        if (bundle != null)
        {
            webUrl = bundle.get("WEB_URL").toString();
        }
        if(getUserVisibleHint())
        {
            initWebView();
        }
    }

    public AgentWeb getAgentWeb()
    {
        return mAgentWeb;
    }

    private void initWebView()
    {
        if(mLinearLayout == null)
            return;
        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
//                .closeProgressBar()//关闭进度条
                .useDefaultIndicator()// 使用默认进度条
//                .setIndicatorColor(R.color.colorAccent)//设置进度条颜色
                // .setIndicatorColorWithHeight(ContextCompat.getColor(getActivity().getApplicationContext(),R.color.colorAccent),5)//设置进度条颜色和宽度
//                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .createAgentWeb()
                .ready()
                .go(webUrl);
    }

    @Override
    public void loadWeb()
    {
        if(mAgentWeb != null)
            mAgentWeb.getLoader().reload();
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible)
        {
            //更新界面数据，如果数据还在下载中，就显示加载框

        }
        else
        {
            //关闭加载框

        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        //去服务器下载数据
        initWebView();
    }
    /**
     * 获取title
     */
    ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            //Toast.makeText(WebViewActivity.this, "这个是标题哦 ==>   " + title, Toast.LENGTH_SHORT).show();
            if(mGameBean != null)
            {
                mGameBean.setName(title);
                //DBMediaHelper.getInstance().addRecord(mGameBean);
            }
        }
    };

    //WebViewClient 用于监听界面的开始和结束
    private WebViewClient mWebViewClient=new WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            if(url.equals(webUrl))
            {
                Log.d("","is home page");
                mGameBean = null;
                return;
            }
            curLoadUrl = url;
            mGameBean = new GameBean();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //界面结束
        }
    };

    //WebChromeClient 监听界面的改变
    private WebChromeClient mWebChromeClient=new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //界面改变
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);

            if(mGameBean != null)
            {
                String iconUrl = BitMapUtils.saveBitmap(icon,curLoadUrl);
                mGameBean.setH5_url(curLoadUrl);
                mGameBean.setIcon_url(iconUrl);
                DBMediaHelper.getInstance().addRecord(mGameBean);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };
}