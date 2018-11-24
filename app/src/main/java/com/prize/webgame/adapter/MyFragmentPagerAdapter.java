package com.prize.webgame.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.prize.webgame.view.PageFragment;


/**
 * Created by LaravelChen on 2017/6/8.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] titles = new String[]{"推荐","热点","娱乐","科技","体育","军事","图片"};
    public int COUNT = titles.length;
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }



    @Override
    public Fragment getItem(int position)
    {
        return PageFragment.newInstance("http://04.duegame.com");
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
