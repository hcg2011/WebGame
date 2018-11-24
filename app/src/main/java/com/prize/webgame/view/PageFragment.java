package com.prize.webgame.view;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class PageFragment extends BaseFragment {

    private View view = null;
    private AgentWeb mAgentWeb = null;
    private LinearLayout mLinearLayout;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String webUrl = "";

    public static PageFragment newInstance(String webUrl) {

        Bundle args = new Bundle();
        args.putString("WEB_URL", webUrl);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_page, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        //swipeRefreshLayout.setRefreshing(true);
        seeRefresh();

        mLinearLayout = (LinearLayout) view.findViewById(R.id.view_wv_home_page);

        final Bundle bundle = getArguments();

        if (bundle != null)
        {
            webUrl = bundle.get("WEB_URL").toString();
        }
        if(getUserVisibleHint())
        {
            loadWeb();
        }
    }

    //设置刷新
    public void seeRefresh() {
        // 设置下拉进度的背景颜色，默认就是白色的
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        // 设置下拉进度的主题颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        //监听刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWeb();
            }
        });
    }

    public AgentWeb getAgentWeb()
    {
        return mAgentWeb;
    }


    public void loadWeb()
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
        loadWeb();
    }



    /**
     * 获取title
     */
    ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            //Toast.makeText(WebViewActivity.this, "这个是标题哦 ==>   " + title, Toast.LENGTH_SHORT).show();
        }
    };

    //WebViewClient 用于监听界面的开始和结束
    private WebViewClient mWebViewClient=new WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //界面开始
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //界面结束
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    //WebChromeClient 监听界面的改变
    private WebChromeClient mWebChromeClient=new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //界面改变
        }
    };

}
