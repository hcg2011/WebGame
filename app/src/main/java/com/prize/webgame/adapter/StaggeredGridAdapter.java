package com.prize.webgame.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.prize.webgame.R;
import com.prize.webgame.bean.GameBean;

import java.util.ArrayList;
import java.util.List;


public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.ViewHolder> {

    private Context mContext = null;

    private List<GameBean> mDataSet = new ArrayList<>();

    public StaggeredGridAdapter(Context mContext)
    {
        this.mContext = mContext;
    }


    public void setDataSet(List<GameBean> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        GameBean mGameBean = mDataSet.get(position);
        holder.tvName.setText(mGameBean.getName());
        Glide.with(mContext).load(mGameBean.getIcon_url()).into(holder.imageView);

        Glide.with(mContext).load(mGameBean.getIcon_url()).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                holder.imageView.setImageBitmap(resource);

//                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
//                    public void onGenerated(Palette palette) {
//                        // Use generated instance
//                        Palette.Swatch vibrant = palette.getVibrantSwatch();
//                        if (vibrant != null) {
//                            holder.ivBg.setBackgroundColor(vibrant.getRgb());
//                            /*titleTv.setTextColor(vibrant.getTitleTextColor());
//                            mToolbar.setBackgroundColor(vibrant.getRgb());*/
//                        }
//                    }
//                });

            }
        }); //方法中设置asBitmap可以设置回调类型



    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView tvName;
        public TextView tvCount;
        public View ivBg = null;

        public ViewHolder(View itemView)
        {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            tvName = (TextView) itemView.findViewById(R.id.tv_home_item_name);
            tvCount = (TextView) itemView.findViewById(R.id.tv_home_item_count);
            ivBg = itemView.findViewById(R.id.view_home_item_bg);
        }
    }

    /*public static class Items implements Serializable {
        public String name;
        public int resId;

        public Items(String name, int resId) {
            this.name = name;
            this.resId = resId;
        }
    }*/

}