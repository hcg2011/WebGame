package com.prize.webgame.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.prize.webgame.R;
import com.prize.webgame.bean.GameBean;

import java.util.List;

/**
 * 上拉加载更多
 * Created by yangle on 2017/10/12.
 */

public class PersonalWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext = null;

    private List<GameBean> dataList;

    public interface OnItemClickListener
    {
        void onItemClick(View view);
    }

    private OnItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public PersonalWrapperAdapter(Context mContext, List<GameBean> dataList)
    {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_personal_item, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

        GameBean mGameBean = dataList.get(position);

        if(mGameBean.isHot())
        {
            recyclerViewHolder.ivIconHot.setVisibility(View.VISIBLE);
            recyclerViewHolder.tvDesc.setText(mGameBean.getUse_count() + mContext.getResources().getString(R.string.play_count));
        }
        else
        {
            recyclerViewHolder.ivIconHot.setVisibility(View.GONE);
            recyclerViewHolder.tvDesc.setText(mGameBean.getLast_modify_time_by_format() + mContext.getResources().getString(R.string.play_time));
        }

        recyclerViewHolder.tvName.setText(mGameBean.getName());

        Glide.with(mContext).load(mGameBean.getIcon_url()).into(recyclerViewHolder.ivIcon);

        recyclerViewHolder.itemView.setTag(position);
        recyclerViewHolder.btnStart.setTag(position);

        recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(recyclerViewHolder.itemView);
                }
            }
        });
        recyclerViewHolder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(recyclerViewHolder.itemView);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {


        TextView tvName;
        TextView tvDesc;
        ImageView ivIcon;
        ImageView ivIconHot;
        Button btnStart;

        RecyclerViewHolder(View itemView)
        {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_rank_item_name);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_rank_item_desc);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_rank_item_icon);
            ivIconHot = (ImageView) itemView.findViewById(R.id.iv_rank_item_icon_hot);
            btnStart = (Button) itemView.findViewById(R.id.btn_rank_item_start);
        }
    }
}
