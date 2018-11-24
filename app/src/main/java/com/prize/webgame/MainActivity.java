package com.prize.webgame;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.just.library.AgentWeb;
import com.prize.webgame.bean.CheckUpdateResultBean;
import com.prize.webgame.http.HttpCallback;
import com.prize.webgame.http.HttpClient;
import com.prize.webgame.utils.DensityUtil;
import com.prize.webgame.utils.StatusBarUtil;
import com.prize.webgame.view.HomePageFragmentNew;
import com.prize.webgame.view.PersonalPageFragment;
import com.prize.webgame.view.RankPageFragment;
import com.prize.webgame.view.SlideViewPager;
import com.prize.webgame.view.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ViewPagerAdapter viewPagerAdapter;
    private SlideViewPager viewPager;
    private View viewRefresh = null;
    private TextView tvTitle = null;

    private ImageView ivHome = null;
    private ImageView ivRank = null;
    private ImageView ivCategory = null;
    private View viewSpaceHome = null;
    private View viewSpaceRank = null;
    private View viewSpaceCategory = null;

    private HomePageFragmentNew mFragmentOne = null;
    private RankPageFragment mFragmentTwo = null;
    private PersonalPageFragment mFragmentThree = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);

        viewRefresh = findViewById(R.id.view_title_refresh);
        tvTitle = (TextView) findViewById(R.id.tv_title_content);

        ivHome = (ImageView) findViewById(R.id.iv_bottom_nav_home);
        ivRank = (ImageView) findViewById(R.id.iv_bottom_nav_rank);
        ivCategory = (ImageView) findViewById(R.id.iv_bottom_nav_category);

        viewSpaceHome = findViewById(R.id.view_space_bttom_nav_home);
        viewSpaceRank = findViewById(R.id.view_space_bttom_nav_rank);
        viewSpaceCategory = findViewById(R.id.view_space_bttom_nav_category);

        ivHome.setOnClickListener(this);
        ivRank.setOnClickListener(this);
        ivCategory.setOnClickListener(this);
        viewRefresh.setOnClickListener(this);

        viewPager = (SlideViewPager) findViewById(R.id.view_pager_home);
        viewPager.setSlide(false);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        List<Fragment> list = new ArrayList<>();

        mFragmentOne = new HomePageFragmentNew();
        mFragmentTwo = RankPageFragment.newInstance("http://www.7724.com/");
        mFragmentThree = PersonalPageFragment.newInstance("http://gc.hgame.com/");

        list.add(mFragmentOne);
        list.add(mFragmentTwo);
        list.add(mFragmentThree);


        viewPagerAdapter.setList(list);

        //PermisionUtils.verifyStoragePermissions(this);
        applypermission();

        HttpClient.checkUpdate(getApplicationContext(), new HttpCallback<CheckUpdateResultBean>() {
            @Override
            public void onSuccess(CheckUpdateResultBean resultBean) {

            }

            @Override
            public void onFail(Exception e) {

            }
        });

    }

    /*private HomePageFragment_old getCurFragment()
    {
        int index = viewPager.getCurrentItem();
        if(index == 0)
            return mFragmentOne;
        if(index == 1)
            return mFragmentTwo;
        if(index == 2)
            return mFragmentThree;
        if(index == 3)
            return mFragmentOne;
        return null;
    }*/

    private AgentWeb getAgentWeb()
    {
        AgentWeb mAgentWeb = null;

        int index = viewPager.getCurrentItem();
        /*if(index == 0)
            mAgentWeb = mFragmentOne.getAgentWeb();*/
        /*if(index == 1)
            mAgentWeb = mFragmentTwo.getAgentWeb();
        if(index == 2)
            mAgentWeb = mFragmentThree.getAgentWeb();*/

        return mAgentWeb;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (getAgentWeb() != null && getAgentWeb().handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        if(getAgentWeb() != null)
            getAgentWeb().getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        if(getAgentWeb() != null)
            getAgentWeb().getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if((System.currentTimeMillis() - touchTime) < 2000)
        {
            closeApp();
            super.onBackPressed();
            // TODO Auto-generated method stub
        }
        else
        {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.close_hint),Toast.LENGTH_LONG).show();
            touchTime = System.currentTimeMillis();
        }
        //super.onBackPressed();
    }

    private long touchTime = 0;
    private void closeApp()
    {

        /*if(mFragmentOne.getAgentWeb() != null)
            mFragmentOne.getAgentWeb().destroyAndKill();
        if(mFragmentTwo.getAgentWeb() != null)
            mFragmentTwo.getAgentWeb().destroyAndKill();
        if(mFragmentThree.getAgentWeb() != null)
            mFragmentThree.getAgentWeb().destroyAndKill();*/


        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    private void resetIocns()
    {
        ivHome.setImageResource(R.mipmap.icon_menu_home_off);
        ivRank.setImageResource(R.mipmap.icon_menu_rank_off);
        ivCategory.setImageResource(R.mipmap.icon_menu_category_off);

        setViewSize(ivHome,36,36);
        setViewSize(ivRank,36,36);
        setViewSize(ivCategory,36,36);

        viewSpaceHome.setVisibility(View.GONE);
        viewSpaceRank.setVisibility(View.GONE);
        viewSpaceCategory.setVisibility(View.GONE);

    }
    private void setViewSize(View view, int width,int height)
    {
        // 获取要改变view的父布局的布局参数
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        // 设置宽为100dp
        params.width = DensityUtil.dip2px(width, MainActivity.this);
        // 设置高为100dp
        params.height = DensityUtil.dip2px(height, MainActivity.this);
        // 根据布局参数的设置，重新设置view（这里用了text view，当然其他的view也是通用的）的大小
        view.setLayoutParams(params);
    }

    @Override
    public void onClick(View view)
    {
        if(view == ivHome)
        {
            resetIocns();
            ivHome.setImageResource(R.mipmap.icon_menu_home_on);
            setViewSize(ivHome,45,45);
            viewSpaceHome.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(0);

            viewRefresh.setVisibility(View.VISIBLE);

            //tvTitle.setText(R.string.bottom_nav_home);
        }
        else if(view == ivRank)
        {
            resetIocns();
            ivRank.setImageResource(R.mipmap.icon_menu_rank_on);
            setViewSize(ivRank,45,45);
            viewSpaceRank.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(1);

            viewRefresh.setVisibility(View.GONE);

            //tvTitle.setText(R.string.bottom_nav_rank);
        }
        else if(view == ivCategory)
        {
            resetIocns();
            ivCategory.setImageResource(R.mipmap.icon_menu_category_on);
            setViewSize(ivCategory,45,45);
            viewSpaceCategory.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(2);

            viewRefresh.setVisibility(View.GONE);

            //tvTitle.setText(R.string.bottom_nav_category);
        }
        else if(view == viewRefresh)
        {
            //mFragmentOne.loadWeb();
            mFragmentOne.playLayoutAnimationRadom();

            startShakeByView(viewRefresh, 1.0f,1.0f,20f,600);
        }
    }

    private void startShakeByView(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        //由小变大
        Animation scaleAnim = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        //从左向右
        Animation rotateAnim = new RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim.setDuration(duration);
        rotateAnim.setDuration(duration / 10);
        rotateAnim.setRepeatMode(Animation.REVERSE);
        rotateAnim.setRepeatCount(10);

        AnimationSet smallAnimationSet = new AnimationSet(false);
        smallAnimationSet.addAnimation(scaleAnim);
        smallAnimationSet.addAnimation(rotateAnim);

        view.startAnimation(smallAnimationSet);
    }




    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }



    public void applypermission()
    {
        if(Build.VERSION.SDK_INT>=23)
        {
            //检查是否已经给了权限
            int checkpermission = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if(checkpermission!=PackageManager.PERMISSION_GRANTED)
            {
                //没有给权限
                Log.e("permission","动态申请");
                //参数分别是当前活动，权限字符串数组，requestcode
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            int checkpermissionReadPhone = ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_PHONE_STATE);
            if (checkpermissionReadPhone != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
            /*else
                loadPlugin();*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //grantResults数组与权限字符串数组对应，里面存放权限申请结果
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            //loadPlugin();
            Toast.makeText(MainActivity.this,"已授权",Toast.LENGTH_SHORT).show();
        }
        else
        {
            finish();
            Toast.makeText(MainActivity.this,"拒绝授权",Toast.LENGTH_SHORT).show();
        }
    }
}
