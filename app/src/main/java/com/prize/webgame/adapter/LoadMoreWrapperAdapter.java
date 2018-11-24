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

public class LoadMoreWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    public LoadMoreWrapperAdapter(Context mContext,List<GameBean> dataList)
    {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        if(viewType == 0)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rank_item, parent, false);
            return new RecyclerViewHolder(view);
        }
        else if(viewType == 1)
        {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rank_item_type, parent, false);
            return new TypeViewHolder(view);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {

        if(holder instanceof RecyclerViewHolder)
        {
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;

            GameBean mGameBean = dataList.get(position);

            recyclerViewHolder.tvName.setText(mGameBean.getName());
            recyclerViewHolder.tvDesc.setText(mGameBean.getUse_count() + mContext.getResources().getString(R.string.play_count));
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
        else if(holder instanceof TypeViewHolder)
        {
            TypeViewHolder recyclerViewHolder = (TypeViewHolder) holder;
            GameBean mGameBean = dataList.get(position);
            if(mGameBean.getTypeCategory().equals(GameBean.RENQI))
                recyclerViewHolder.ivType.setImageResource(R.mipmap.type_rank_renqi);
            else if(mGameBean.getTypeCategory().equals(GameBean.JINGDIAN))
                recyclerViewHolder.ivType.setImageResource(R.mipmap.type_rank_jingdian);
            else if(mGameBean.getTypeCategory().equals(GameBean.XINPIN))
                recyclerViewHolder.ivType.setImageResource(R.mipmap.type_rank_xinpin);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDesc;
        ImageView ivIcon;
        Button btnStart;

        RecyclerViewHolder(View itemView)
        {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_rank_item_name);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_rank_item_desc);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_rank_item_icon);
            btnStart = (Button) itemView.findViewById(R.id.btn_rank_item_start);
        }
    }

    private class TypeViewHolder extends RecyclerView.ViewHolder {

        ImageView ivType;

        TypeViewHolder(View itemView)
        {
            super(itemView);
            ivType = (ImageView) itemView.findViewById(R.id.iv_rank_item_type);
        }
    }
}
