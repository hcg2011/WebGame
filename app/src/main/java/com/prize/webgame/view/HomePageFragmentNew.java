package com.prize.webgame.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.LayoutAnimationController;

import com.prize.webgame.R;
import com.prize.webgame.adapter.StaggeredGridAdapter;
import com.prize.webgame.bean.GameBean;
import com.prize.webgame.bean.HomeGameResultBean;
import com.prize.webgame.http.HttpCallback;
import com.prize.webgame.http.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by prize on 2018/11/15.
 */

public class HomePageFragmentNew extends BaseFragment {


    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private StaggeredGridRecyclerView mRecyclerView;
    private StaggeredGridAdapter mStaggeredGridAdapter;

    private ShakeUtils mShakeUtils = null;

    private List<GameBean> dataList = new ArrayList<>();

    public HomePageFragmentNew() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        initView(rootView);
        return rootView;
    }

    public void initView(View rootView) {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.home_page_swipe_refresh_layout);
        mRecyclerView = (StaggeredGridRecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mStaggeredGridAdapter = new StaggeredGridAdapter(getContext());
        mStaggeredGridAdapter.setDataSet(dataList);
        mRecyclerView.setAdapter(mStaggeredGridAdapter);
        LayoutAnimationController controller = MyLayoutAnimationHelper.makeLayoutAnimationController();
        ViewGroup viewGroup = (ViewGroup) rootView.findViewById(R.id.homefragment);
        viewGroup.setLayoutAnimation(controller);
        viewGroup.scheduleLayoutAnimation();

        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));

        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGameData();
            }
        });

        mShakeUtils = new ShakeUtils(getContext());
        mShakeUtils.setOnShakeListener(new ShakeUtils.OnShakeListener() {
            @Override
            public void onShake() {
                playLayoutAnimationRadom();
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        getGameData();
    }

    private void getGameData() {

        HttpClient.getHomeGameList(getContext(), new HttpCallback<HomeGameResultBean>() {
            @Override
            public void onSuccess(HomeGameResultBean homeGameResultBean) {
                if (homeGameResultBean.isSuccess()) {
                    dataList.clear();

                    List<GameBean> tempList = homeGameResultBean.getData().getEasyuilist().getRows();
                    if (tempList != null && tempList.size() > 0) {
                        dataList.addAll(tempList);
                        playLayoutAnimationRadom();
                        //mStaggeredGridAdapter.notifyItemRangeChanged(0,dataList.size());
                        mStaggeredGridAdapter.notifyDataSetChanged();
                    }
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFail(Exception e) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void playLayoutAnimationRadom() {
        Random random = new Random();

        int index = random.nextInt(7);

        if (index == 0) {
            playLayoutAnimation(MyLayoutAnimationHelper.getAnimationSetFromLeft());
        } else if (index == 1) {
            playLayoutAnimation(MyLayoutAnimationHelper.getAnimationSetFromRight());
        } else if (index == 2) {
            playLayoutAnimation(MyLayoutAnimationHelper.getAnimationSetFromBottom());
        } else if (index == 3) {
            playLayoutAnimation(MyLayoutAnimationHelper.getAnimationSetFromTop(), true);
        } else if (index == 4) {
            playLayoutAnimation(MyLayoutAnimationHelper.getAnimationSetScaleBig());
        } else if (index == 5) {
            playLayoutAnimation(MyLayoutAnimationHelper.getAnimationSetScaleNarrow());
        } else if (index == 6) {
            playLayoutAnimation(MyLayoutAnimationHelper.getAnimationSetAlpha());
        } else if (index == 7) {
            playLayoutAnimation(MyLayoutAnimationHelper.getAnimationSetRotation());
        }
    }

    public void playLayoutAnimation(Animation animation) {
        playLayoutAnimation(animation, false);
    }

    /**
     * 播放动画
     *
     * @param animation
     * @param isReverse
     */
    public void playLayoutAnimation(Animation animation, boolean isReverse) {
        GridLayoutAnimationController controller = new GridLayoutAnimationController(animation);
        controller.setColumnDelay(0.2f);
        controller.setRowDelay(0.3f);
        controller.setOrder(isReverse ? LayoutAnimationController.ORDER_REVERSE : LayoutAnimationController.ORDER_NORMAL);

        mRecyclerView.setLayoutAnimation(controller);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        mRecyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void onResume() {
        super.onResume();
        mShakeUtils.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShakeUtils.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void loadWeb() {

    }
}