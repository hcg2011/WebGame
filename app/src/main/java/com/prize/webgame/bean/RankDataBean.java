package com.prize.webgame.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prize on 2018/11/22.
 */

public class RankDataBean
{

    private List<GameBean> popularityList;
    private List<GameBean> classicList;
    private List<GameBean> newProductList;

    private List<GameBean> dataList = new ArrayList<>();

    public List<GameBean> getDataList()
    {

        if(popularityList.size() > 0)
        {
            GameBean gbRenqi = new GameBean(1, GameBean.RENQI);
            dataList.add(gbRenqi);
            dataList.addAll(popularityList);
        }

        if(classicList.size() > 0)
        {
            GameBean gbJingdian = new GameBean(1, GameBean.JINGDIAN);
            dataList.add(gbJingdian);
            dataList.addAll(classicList);
        }

        if(newProductList.size() > 0)
        {
            GameBean gbXinPin = new GameBean(1, GameBean.XINPIN);
            dataList.add(gbXinPin);
            dataList.addAll(newProductList);
        }

        return dataList;

    }

    public List<GameBean> getPopularityList() {
        return popularityList;
    }

    public void setPopularityList(List<GameBean> popularityList) {
        this.popularityList = popularityList;
    }

    public List<GameBean> getClassicList() {
        return classicList;
    }

    public void setClassicList(List<GameBean> classicList) {
        this.classicList = classicList;
    }

    public List<GameBean> getNewProductList() {
        return newProductList;
    }

    public void setNewProductList(List<GameBean> newProductList) {
        this.newProductList = newProductList;
    }
}
