package com.prize.webgame.bean;

import java.util.List;

/**
 * Created by prize on 2018/11/22.
 */

public class HomeDataRowsBean {

    private List<GameBean> rows;
    private String total;

    public List<GameBean> getRows() {
        return rows;
    }

    public void setRows(List<GameBean> rows) {
        this.rows = rows;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
