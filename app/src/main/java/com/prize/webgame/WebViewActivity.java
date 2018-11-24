package com.prize.webgame;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.just.library.AgentWeb;
import com.just.library.ChromeClientCallbackManager;
import com.prize.webgame.bean.AdvConstants;
import com.prize.webgame.utils.StatusBarUtil;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.MediaListener;
import com.qq.e.ads.nativ.MediaView;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.ads.nativ.NativeMediaAD;
import com.qq.e.ads.nativ.NativeMediaADData;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;

import java.util.List;

public class WebViewActivity extends AppCompatActivity implements NativeExpressAD.NativeExpressADListener {


    private String TAG = "WebViewActivity";

    private LinearLayout viewParent = null;
    private TextView tvTitle = null;
    private View viewClose = null;
    private View viewRefresh = null;
    private ImageView ivRefresh = null;

    private AgentWeb mAgentWeb = null;

    private NativeExpressAD mNativeADManager;
    private NativeExpressADView nativeExpressADView = null;
    public static final int AD_COUNT = 1;// 加载广告的条数，取值范围为[1, 10]

    private String title = "";
    private String webUrl = "";
    private String requestOritation = "";


    // 与广告无关的变量，本示例中的一些其他UI
    private AQuery mAQuery;                               // 用于加载图片AQuery开源组件，开发者可根据需求使用自己的图片加载框架
    private Button mDownloadButton;
    private RelativeLayout mADInfoContainer;
    private TextView textLoadResult;

    private NativeMediaADData mAD;                        // 加载的原生视频广告对象，本示例为简便只演示加载1条广告的示例
    private NativeMediaAD mMediaManager;                     // 原生广告manager，用于管理广告数据的加载，监听广告回调
    private MediaView mMediaView;

    private ImageView mImagePoster;                       // 广告大图，没有加载好视频素材前，先显示广告的大图
    private final Handler tikTokHandler = new Handler();  // 倒计时读取Handler，开发者可以按照自己的设计实现，此处仅供参考
    private TextView mTextCountDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }

        viewClose = findViewById(R.id.view_title_close);
        viewRefresh = findViewById(R.id.view_title_refresh);
        ivRefresh = (ImageView) findViewById(R.id.iv_title_refresh);

        tvTitle = (TextView) findViewById(R.id.tv_title_content);
        viewParent = (LinearLayout) findViewById(R.id.view_web_view_parent);

        viewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        viewRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAgentWeb.getLoader().reload();
            }
        });

        webUrl = getIntent().getStringExtra("WEB_URL");
        title = getIntent().getStringExtra("TITLE");
        requestOritation = getIntent().getStringExtra("SCREEN_ORITATION");

        setScreenOritation(requestOritation);
        //
        //        ADSize adSize = new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT);
        //        mADManager = new NativeExpressAD(this, adSize, AdvConstants.APPID, AdvConstants.NativeExpressSupportVideoPosID, this);
        initView();
        initNativeVideoAD();

        tvTitle.setText(title);

        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(viewParent, new LinearLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams
                //                .closeProgressBar()//关闭进度条
                .useDefaultIndicator()// 使用默认进度条
                //                .setIndicatorColor(R.color.colorAccent)//设置进度条颜色
                .setIndicatorColorWithHeight(ContextCompat.getColor(this, R.color.colorAccent), 5)//设置进度条颜色和宽度
                //                .defaultProgressBarColor() // 使用默认进度条颜色
                .setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .createAgentWeb()
                .ready()
                .go(webUrl);

    }

    private void initView() {
        mMediaView = (MediaView) findViewById(R.id.gdt_media_view);
        mImagePoster = (ImageView) findViewById(R.id.img_poster);
        mADInfoContainer = (RelativeLayout) this.findViewById(R.id.ad_info_container);
        mDownloadButton = (Button) mADInfoContainer.findViewById(R.id.btn_download);
        mTextCountDown = (TextView) findViewById(R.id.text_count_down);
        textLoadResult = (TextView) findViewById(R.id.text_load_result);
        mAQuery = new AQuery((RelativeLayout) findViewById(R.id.root));
    }

    private void initNativeVideoAD() {
        NativeMediaAD.NativeMediaADListener listener = new NativeMediaAD.NativeMediaADListener() {

            /**
             * 广告加载成功
             *
             * @param adList  广告对象列表
             */
            @Override
            public void onADLoaded(List<NativeMediaADData> adList) {
                if (adList.size() > 0) {
                    mAD = adList.get(0);
                    /**
                     * 加载广告成功，开始渲染。
                     */
                    initADUI();

                    if (mAD.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                        /**
                         * 如果该条原生广告是一条带有视频素材的广告，还需要先调用preLoadVideo接口来加载视频素材：
                         *    - 加载成功：回调NativeMediaADListener.onADVideoLoaded(NativeMediaADData adData)方法
                         *    - 加载失败：回调NativeMediaADListener.onADError(NativeMediaADData adData, int errorCode)方法，错误码为700
                         */
                        mAD.preLoadVideo();
                        //textLoadResult.setText("getAdPatternType() == AdPatternType.NATIVE_VIDEO：" + "这是一条视频广告");
                    } else if (mAD.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT) {
                        /**
                         * 如果该条原生广告只是一个普通图文的广告，不带视频素材，那么渲染普通的UI即可。
                         */
                        //textLoadResult.setText("getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT：" + "这是一条两图两文广告");
                    } else if (mAD.getAdPatternType() == AdPatternType.NATIVE_3IMAGE) {
                        //textLoadResult.setText("getAdPatternType() == AdPatternType.NATIVE_3IMAGE：" + "这是一条三小图广告");
                    }
                }
            }

            /**
             * 广告加载失败
             *
             * @param adError   广告加载失败的错误内容，错误含义请参考开发者文档第4章。
             */
            @Override
            public void onNoAD(AdError adError) {
                Log.d(TAG, String.format("广告加载失败，错误码：%d，错误信息：%s", adError.getErrorCode(),
                        adError.getErrorMsg()));
            }

            /**
             * 广告状态发生变化，对于App类广告而言，下载/安装的状态和下载进度可以变化。
             *
             * @param ad    状态发生变化的广告对象
             */
            @Override
            public void onADStatusChanged(NativeMediaADData ad) {
                if (ad != null && ad.equals(mAD)) {
                    updateADUI();   // App类广告在下载过程中，下载进度会发生变化，如果开发者需要让用户了解下载进度，可以更新UI。
                }
            }

            /**
             * 广告处理发生错误，当调用一个广告对象的onExposured、onClicked、preLoadVideo接口时，如果发生了错误会回调此接口，具体的错误码含义请参考开发者文档。
             *
             * @param adData    广告对象
             * @param adError   错误内容，错误含义请参考开发者文档第4章。
             */
            @Override
            public void onADError(NativeMediaADData adData, AdError adError) {
                Log.i(TAG, adData.getTitle() + " onADError, error code: " + adError.getErrorCode()
                        + ", error msg: " + adError.getErrorMsg());
            }

            /**
             * 当调用一个广告对象的preLoadVideo接口时，视频素材加载完成后，会回调此接口，在此回调中可以给广告对象绑定MediaView组件播放视频。
             *
             * @param adData  视频素材加载完成的广告对象，很显然这个广告一定是一个带有视频素材的广告，需要给它bindView并播放它
             */
            @Override
            public void onADVideoLoaded(NativeMediaADData adData) {
                Log.i(TAG, adData.getTitle() + " ---> 视频素材加载完成"); // 仅仅是加载视频文件完成，如果没有绑定MediaView视频仍然不可以播放
                bindMediaView();
            }

            /**
             * 广告曝光时的回调
             *
             * 注意：带有视频素材的原生广告可以多次曝光 按照曝光计费
             * 没带有视频素材的广告只能曝光一次 按照点击计费
             *
             * @param adData  曝光的广告对象
             */
            @Override
            public void onADExposure(NativeMediaADData adData) {
                Log.i(TAG, adData.getTitle() + " onADExposure");
            }

            /**
             * 广告被点击时的回调
             *
             * @param adData  被点击的广告对象
             */
            @Override
            public void onADClicked(NativeMediaADData adData) {
                Log.i(TAG, adData.getTitle() + " onADClicked");
            }
        };

        mMediaManager = new NativeMediaAD(getApplicationContext(), AdvConstants.APPID, AdvConstants.NativeExpressSupportVideoPosID, listener);
        mMediaManager.loadAD(AD_COUNT);
    }

    /**
     * 如果选择支持视频的模版样式，请使用{@link AdvConstants#NativeExpressSupportVideoPosID}
     */
    private void initNativeExpressAD() {
        // 注意：如果您在联盟平台上新建原生模板广告位时，选择了“是”支持视频，那么可以进行个性化设置（可选）
        mNativeADManager.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()); //
        mNativeADManager.loadAD(1);
    }

    /**
     * 将广告实例和MediaView绑定，播放视频。
     *
     * 注意：播放视频前需要将广告的大图隐藏，将MediaView显示出来，否则视频将无法播放，也无法上报视频曝光，无法产生计费。
     */
    private void bindMediaView() {
        if (mAD.isVideoAD()) {
            mImagePoster.setVisibility(View.GONE);
            mMediaView.setVisibility(View.VISIBLE);

            /**
             * bindView(MediaView view, boolean useDefaultController):
             *    - useDefaultController: false，不会调用广点通的默认视频控制条
             *    - useDefaultController: true，使用SDK内置的播放器控制条，此时开发者需要把demo下的res文件夹里面的图片拷贝到自己项目的res文件夹去
             *
             * 在这里绑定MediaView后，SDK会根据视频素材的宽高比例，重新给MediaView设置新的宽高
             */
            mAD.bindView(mMediaView, true);
            mAD.play();

            /** 设置视频播放过程中的监听器 */
            mAD.setMediaListener(new MediaListener() {

                /**
                 * 视频播放器初始化完成，准备好可以播放了
                 *
                 * @param videoDuration 视频素材的总时长
                 */
                @Override
                public void onVideoReady(long videoDuration) {
                    Log.i(TAG, "onVideoReady, videoDuration = " + videoDuration);
                    duration = videoDuration;
                }

                /** 视频开始播放 */
                @Override
                public void onVideoStart() {
                    Log.i(TAG, "onVideoStart");
                    tikTokHandler.post(countDown);
                    mTextCountDown.setVisibility(View.VISIBLE);
                }

                /** 视频暂停 */
                @Override
                public void onVideoPause() {
                    Log.i(TAG, "onVideoPause");
                    mTextCountDown.setVisibility(View.GONE);
                }

                /** 视频自动播放结束，到达最后一帧 */
                @Override
                public void onVideoComplete() {
                    Log.i(TAG, "onVideoComplete");
                    releaseCountDown();
                    mTextCountDown.setVisibility(View.GONE);
                }

                /** 视频播放时出现错误 */
                @Override
                public void onVideoError(AdError adError) {
                    Log.i(TAG, String.format("onVideoError, errorCode: %d, errorMsg: %s",
                            adError.getErrorCode(), adError.getErrorMsg()));
                }

                /** SDK内置的播放器控制条中的重播按钮被点击 */
                @Override
                public void onReplayButtonClicked() {
                    Log.i(TAG, "onReplayButtonClicked");
                }

                /**
                 * SDK内置的播放器控制条中的下载/查看详情按钮被点击
                 * 注意: 这里是指UI中的按钮被点击了，而广告的点击事件回调是在onADClicked中，开发者如需统计点击只需要在onADClicked回调中进行一次统计即可。
                 */
                @Override
                public void onADButtonClicked() {
                    Log.i(TAG, "onADButtonClicked");
                }

                /** SDK内置的全屏和非全屏切换回调，进入全屏时inFullScreen为true，退出全屏时inFullScreen为false */
                @Override
                public void onFullScreenChanged(boolean inFullScreen) {
                    Log.i(TAG, "onFullScreenChanged, inFullScreen = " + inFullScreen);

                    // 原生视频广告默认静音播放，进入到全屏后建议开发者可以设置为有声播放
                    if (inFullScreen) {
                        mAD.setVolumeOn(true);
                    } else {
                        mAD.setVolumeOn(false);
                    }
                }
            });
        }
    }

    private void initADUI() {
        int patternType = mAD.getAdPatternType();
        if (patternType == AdPatternType.NATIVE_2IMAGE_2TEXT || patternType == AdPatternType.NATIVE_VIDEO) {
            mAQuery.id(R.id.img_logo).image(mAD.getIconUrl(), false, true);
            mAQuery.id(R.id.img_poster).image(mAD.getImgUrl(), false, true, 0, 0, new BitmapAjaxCallback() {
                @Override
                protected void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                    // AQuery框架有一个问题，就是即使在图片加载完成之前将ImageView设置为了View.GONE，在图片加载完成后，这个ImageView会被重新设置为VIEW.VISIBLE。
                    // 所以在这里需要判断一下，如果已经把ImageView设置为隐藏，开始播放视频了，就不要再显示广告的大图。否则将影响到sdk的曝光上报，无法产生收益。
                    // 开发者在用其他的图片加载框架时，也应该注意检查下是否有这个问题。
                    if (iv.getVisibility() == View.VISIBLE) {
                        iv.setImageBitmap(bm);
                    }
                }
            });
            mAQuery.id(R.id.text_title).text(mAD.getTitle());
            mAQuery.id(R.id.text_desc).text(mAD.getDesc());
        } else if (patternType == AdPatternType.NATIVE_3IMAGE) {
            mAQuery.id(R.id.img_1).image(mAD.getImgList().get(0), false, true);
            mAQuery.id(R.id.img_2).image(mAD.getImgList().get(1), false, true);
            mAQuery.id(R.id.img_3).image(mAD.getImgList().get(2), false, true);
            mAQuery.id(R.id.native_3img_title).text((String) mAD.getTitle());
            mAQuery.id(R.id.native_3img_desc).text((String) mAD.getDesc());
        }
        updateADUI();
        /**
         * 注意：在渲染时，必须先调用onExposured接口曝光广告，否则点击接口onClicked将无效
         */
        mAD.onExposured(mADInfoContainer);
        mDownloadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mAD.onClicked(view);
            }
        });
    }

    private void releaseCountDown() {
        if (countDown != null) {
            tikTokHandler.removeCallbacks(countDown);
        }
    }

    /**
     * 刷新广告倒计时，本示例提供的思路仅开发者供参考，开发者完全可以根据自己的需求设计不同的样式。
     */
    private static final String TEXT_COUNTDOWN = "广告倒计时：%s ";
    private long currentPosition, oldPosition, duration;
    private Runnable countDown = new Runnable() {
        public void run() {
            if (mAD != null) {
                currentPosition = mAD.getCurrentPosition();
                long position = currentPosition;
                if (oldPosition == position && mAD.isPlaying()) {
                    Log.d(TAG, "玩命加载中...");
                    mTextCountDown.setTextColor(Color.WHITE);
                    mTextCountDown.setText("玩命加载中...");
                } else {
                    Log.d(TAG, String.format(TEXT_COUNTDOWN, Math.round((duration - position) / 1000.0) + ""));
                    mTextCountDown.setText(String.format(TEXT_COUNTDOWN, Math.round((duration - position) / 1000.0) + ""));
                }
                oldPosition = position;
                if (mAD.isPlaying()) {
                    tikTokHandler.postDelayed(countDown, 500); // 500ms刷新一次进度即可
                }
            }
        }

    };


    private void updateADUI() {
        if (!mAD.isAPP()) {
            mDownloadButton.setText("浏览");
            return;
        }
        switch (mAD.getAPPStatus()) {
            case 0:
                mDownloadButton.setText("下载");
                break;
            case 1:
                mDownloadButton.setText("启动");
                break;
            case 2:
                mDownloadButton.setText("更新");
                break;
            case 4:
                mDownloadButton.setText(mAD.getProgress() + "%");
                break;
            case 8:
                mDownloadButton.setText("安装");
                break;
            case 16:
                mDownloadButton.setText("下载失败，重新下载");
                break;
            default:
                mDownloadButton.setText("浏览");
                break;
        }
    }

    private void setScreenOritation(String requestOritation) {
        if (requestOritation.equals("0"))//橫屏要求
        {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else if (requestOritation.equals("1"))//豎屏要求
        {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    /**
     * 获取title
     */
    ChromeClientCallbackManager.ReceivedTitleCallback mCallback = new ChromeClientCallbackManager.ReceivedTitleCallback() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            //Toast.makeText(WebViewActivity.this, "这个是标题哦 ==>   " + title, Toast.LENGTH_SHORT).show();
            tvTitle.setText(title);
        }
    };

    //WebViewClient 用于监听界面的开始和结束
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //界面开始
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //界面结束
        }

    };

    //WebChromeClient 监听界面的改变
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //界面改变


        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);

            //ivRefresh.setImageBitmap(icon);

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 跟随生命周期释放资源更省电
     */
    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        if (mAD != null)
            mAD.stop();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        if (mAD != null)
            mAD.resume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();

        // 4.使用完了每一个NativeExpressADView之后都要释放掉资源
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }
        if (mAD != null)
            mAD.destroy();

        if (requestOritation.equals("0"))
            requestOritation = "1";
        else
            requestOritation = "0";
        setScreenOritation(requestOritation);
        super.onDestroy();
    }

    @Override
    public void onNoAD(AdError adError) {
        Log.d("", "");
    }

    @Override
    public void onADLoaded(List<NativeExpressADView> list) {
        //Log.i(TAG, "onADLoaded: " + adList.size());
        // 释放前一个NativeExpressADView的资源
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }
        // 3.返回数据后，SDK会返回可以用于展示NativeExpressADView列表
        nativeExpressADView = list.get(0);
        if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            nativeExpressADView.setMediaListener(mediaListener);
        }
        nativeExpressADView.render();
        /*if (container.getChildCount() > 0) {
            container.removeAllViews();
        }

        // 需要保证View被绘制的时候是可见的，否则将无法产生曝光和收益。
        container.addView(nativeExpressADView);*/

    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {
        Log.d("", "");
    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
        Log.d("", "");
    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {
        Log.d("", "");
    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {
        Log.d("", "");
    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {
        Log.d("", "");
    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
        Log.d("", "");
    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {
        Log.d("", "");
    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {
        Log.d("", "");
    }

    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {
            /*Log.i(TAG, "onVideoInit: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));*/
        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoLoading");
        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
            Log.i(TAG, "onVideoReady");
        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {
            //Log.i(TAG, "onVideoStart: "+ getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {
            //Log.i(TAG, "onVideoPause: " + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {
            //Log.i(TAG, "onVideoComplete: " + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
            Log.i(TAG, "onVideoError");
        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageOpen");
        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageClose");
        }
    };
}
