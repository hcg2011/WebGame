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
import com.prize.webgame.adapter.PersonalWrapperAdapter;
import com.prize.webgame.bean.GameBean;
import com.prize.webgame.bean.HotGameResultBean;
import com.prize.webgame.db.DBMediaHelper;
import com.prize.webgame.http.HttpCallback;
import com.prize.webgame.http.HttpClient;
import com.prize.webgame.listener.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prize on 2018/11/15.
 */

public class PersonalPageFragment extends BaseFragment
{
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private LoadMoreWrapper loadMoreWrapper;

    private List<GameBean> dataList = new ArrayList<>();

    public static PersonalPageFragment newInstance(String webUrl)
    {
        Bundle args = new Bundle();
        args.putString("WEB_URL", webUrl);
        PersonalPageFragment fragment = new PersonalPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_rank_page, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        final Bundle bundle = getArguments();

        if (bundle != null)
        {
            //webUrl = bundle.get("WEB_URL").toString();
        }
        if(getUserVisibleHint())
        {

        }
    }

    private void getData()
    {
        getHotData();
    }
    private void getLocalData()
    {
        /*dataList.clear();
        List<GameBean> tempList = DBMediaHelper.getInstance().getAllRecordsByPage(100);
        if (tempList != null && tempList.size() > 0)
            dataList.addAll(tempList);
        if(loadMoreWrapper != null)
            loadMoreWrapper.notifyDataSetChanged();
        if(swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);*/


        dataList.clear();
        //更新界面数据，如果数据还在下载中，就显示加载框
        List<GameBean> tempList = DBMediaHelper.getInstance().getAllRecordsByPage(100);
        if(tempList != null && tempList.size() != dataList.size())
        {
            for(int i=0;i<tempList.size();i++)
            {
                GameBean tempBean = tempList.get(i);
                if(tempBean.isHot())
                    dataList.add(0,tempBean);
                else
                    dataList.add(tempBean);
            }
        }

        if(loadMoreWrapper != null)
            loadMoreWrapper.notifyDataSetChanged();
        if(swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);

    }

    private void getHotData()
    {
        HttpClient.getHotList(getContext(), new HttpCallback<HotGameResultBean>() {
            @Override
            public void onSuccess(HotGameResultBean hotGameResultBean) {

                if(hotGameResultBean.isSuccess())
                {
                    List<GameBean> tempList = hotGameResultBean.getData().getHotList();
                    if(tempList.size() > 0)
                    {
                        for(int i=0;i<tempList.size();i++)
                        {
                            GameBean bean = tempList.get(i);
                            bean.setHot(true);

                            DBMediaHelper.getInstance().addRecord(bean);
                            //dataList.add(i,bean);
                        }
                    }
                    getLocalData();

                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                }
                else
                {
                    getLocalData();
                }
            }

            @Override
            public void onFail(Exception e) {
                Toast.makeText(getContext(), "请求数据失败", Toast.LENGTH_SHORT).show();
                getLocalData();
            }
        });
    }

    private void initViews(View view)
    {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        // 设置刷新控件颜色
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#4DB6AC"));

        PersonalWrapperAdapter loadMoreWrapperAdapter = new PersonalWrapperAdapter(getContext(),dataList);
        loadMoreWrapper = new LoadMoreWrapper(loadMoreWrapperAdapter);
        loadMoreWrapperAdapter.setOnItemClickListener(new PersonalWrapperAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                int index = (int) view.getTag();
                GameBean mGameBean = dataList.get(index);
                Intent intent = new Intent();
                intent.setClass(getContext(), WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("WEB_URL",mGameBean.getH5_url());
                bundle.putString("TITLE",mGameBean.getName());
                bundle.putString("SCREEN_ORITATION",mGameBean.getScreen());
                intent.putExtras(bundle);
                startActivity(intent);

                //mGameBean.setLast_modify_time(System.currentTimeMillis());
                if(!mGameBean.isHot())
                    DBMediaHelper.getInstance().addRecord(mGameBean);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(loadMoreWrapper);

        // 设置下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 刷新数据
                if(dataList != null && dataList.size() > 0)
                    dataList.clear();
                getData();
            }
        });

        // 设置加载更多监听
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

                if (dataList.size() < 52)
                {

                }
                else
                {
                    // 显示加载到底的提示
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                }
            }
        });

        swipeRefreshLayout.setRefreshing(true);
        getData();


    }

    @Override
    public void loadWeb()
    {

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible)
    {
        if (isVisible)
        {
            getLocalData();
        }
        else
        {
            //关闭加载框

        }
    }

    @Override
    protected void onFragmentFirstVisible()
    {
        //去服务器下载数据

    }
}