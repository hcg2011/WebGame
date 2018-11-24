package com.prize.webgame;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.just.library.AgentWeb;
import com.prize.webgame.view.BottomNavigationViewHelper;
import com.prize.webgame.view.HomePageFragment;
import com.prize.webgame.view.SlideViewPager;
import com.prize.webgame.view.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_old extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private BottomNavigationView navigation = null;
    private SlideViewPager viewPager;

    private HomePageFragment mFragmentOne = null;
    private HomePageFragment mFragmentTwo = null;
    private HomePageFragment mFragmentThree = null;
    private HomePageFragment mFragmentFour = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_1:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_2:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_3:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_4:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewPager = (SlideViewPager) findViewById(R.id.view_pager_home);
        viewPager.setSlide(false);


        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

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

        HomePageFragment mHomePageFragment1 = new HomePageFragment();
        HomePageFragment mHomePageFragment2 = new HomePageFragment();
        HomePageFragment mHomePageFragment3 = new HomePageFragment();
        HomePageFragment mHomePageFragment4 = new HomePageFragment();

        list.add(mHomePageFragment1);
        list.add(mHomePageFragment2);
        list.add(mHomePageFragment3);
        list.add(mHomePageFragment4);

        /*mFragmentOne = HomePageFragment.newInstance("http://04.duegame.com/");
        mFragmentTwo = HomePageFragment.newInstance("http://www.7724.com/");
        mFragmentThree = HomePageFragment.newInstance("http://gc.hgame.com/");
        mFragmentFour = HomePageFragment.newInstance("http://www.4177.com/");*/

        /*list.add(mFragmentOne);
        list.add(mFragmentTwo);
        list.add(mFragmentThree);
        list.add(mFragmentFour);*/
        viewPagerAdapter.setList(list);

    }

    private AgentWeb getAgentWeb()
    {
        AgentWeb mAgentWeb = null;

        int index = viewPager.getCurrentItem();
        /*if(index == 0)
            mAgentWeb = mFragmentOne.getAgentWeb();
        if(index == 1)
            mAgentWeb = mFragmentTwo.getAgentWeb();
        if(index == 2)
            mAgentWeb = mFragmentThree.getAgentWeb();
        if(index == 3)
            mAgentWeb = mFragmentFour.getAgentWeb();*/
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
            Toast.makeText(getApplicationContext(),"再按一次退出小游戏",Toast.LENGTH_LONG).show();
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
            mFragmentThree.getAgentWeb().destroyAndKill();
        if(mFragmentFour.getAgentWeb() != null)
            mFragmentFour.getAgentWeb().destroyAndKill();*/

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
