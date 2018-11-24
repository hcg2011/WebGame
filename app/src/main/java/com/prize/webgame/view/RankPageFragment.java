package com.prize.webgame.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.prize.webgame.R;
import com.prize.webgame.WebViewActivity;
import com.prize.webgame.adapter.LoadMoreWrapperAdapter;
import com.prize.webgame.bean.GameBean;
import com.prize.webgame.bean.TopGameResultBean;
import com.prize.webgame.db.DBMediaHelper;
import com.prize.webgame.http.HttpCallback;
import com.prize.webgame.http.HttpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prize on 2018/11/15.
 */

public class RankPageFragment extends BaseFragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LoadMoreWrapper loadMoreWrapper;

    private List<GameBean> dataList = new ArrayList<>();

    public static RankPageFragment newInstance(String webUrl) {
        Bundle args = new Bundle();
        args.putString("WEB_URL", webUrl);
        RankPageFragment fragment = new RankPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        final Bundle bundle = getArguments();

        if (bundle != null) {
            //webUrl = bundle.get("WEB_URL").toString();
        }
        if (getUserVisibleHint()) {

        }
    }

    private void getData() {
        // 刷新数据
        dataList.clear();

        HttpClient.getTopList(getContext(), new HttpCallback<TopGameResultBean>() {
            @Override
            public void onSuccess(TopGameResultBean topGameResultBean) {
                if (topGameResultBean.isSuccess()) {
                    List<GameBean> tempList = topGameResultBean.getData().getDataList();
                    if (tempList.size() > 0)
                        dataList.addAll(tempList);
                    loadMoreWrapper.notifyDataSetChanged();

                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(getContext(), "请求数据失败", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initViews(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));

        LoadMoreWrapperAdapter loadMoreWrapperAdapter = new LoadMoreWrapperAdapter(getContext(), dataList);
        loadMoreWrapper = new LoadMoreWrapper(loadMoreWrapperAdapter);

        loadMoreWrapperAdapter.setOnItemClickListener(new LoadMoreWrapperAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int index = (int) view.getTag();
                GameBean mGameBean = dataList.get(index);
                Intent intent = new Intent();
                intent.setClass(getContext(), WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("WEB_URL", mGameBean.getH5_url());
                bundle.putString("TITLE", mGameBean.getName());
                bundle.putString("SCREEN_ORITATION", mGameBean.getScreen());
                intent.putExtras(bundle);
                startActivity(intent);

                //mGameBean.setLast_modify_time(System.currentTimeMillis());
                DBMediaHelper.getInstance().addRecord(mGameBean);

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(loadMoreWrapper);

        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        getData();

    }

    @Override
    public void loadWeb() {

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if (isVisible) {
            //更新界面数据，如果数据还在下载中，就显示加载框

        } else {
            //关闭加载框

        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        //去服务器下载数据

    }
}