package com.android.fahim.livewallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.fahim.livewallpaper.Database.Recents;
import com.android.fahim.livewallpaper.Interface.ItemClickListener;
import com.android.fahim.livewallpaper.Models.WallpaperItem;
import com.android.fahim.livewallpaper.MyWallpaper;
import com.android.fahim.livewallpaper.R;
import com.android.fahim.livewallpaper.ViewHolder.ListWallpaperViewHolder;
import com.android.fahim.livewallpaper.common.Common;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by root on 7/3/18.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<ListWallpaperViewHolder> {

    private Context context;
    private List<Recents> recents;

    public MyRecyclerAdapter(Context context, List<Recents> recents) {
        this.context = context;
        this.recents = recents;
    }

    @NonNull
    @Override
    public ListWallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_wallpaper_item, parent, false);
        int height = parent.getMeasuredHeight() / 2;
        view.setMinimumHeight(height);

        return new ListWallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, final int position) {

        Picasso.with(context)
                .load(recents.get(position).getImageLink())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.wallpaper, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(recents.get(position).getImageLink())
                                .error(R.drawable.ic_terrain_black_24dp)
                                .into(holder.wallpaper, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {

                                        Log.e("ERROR ", "Could not fetch Image");
                                    }
                                });
                    }
                });

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                WallpaperItem wallpaperItem = new WallpaperItem();
                wallpaperItem.setCategoryId(recents.get(position).getCategoryId());
                wallpaperItem.setImageUrl(recents.get(position).getImageLink());

                Common.select_background = wallpaperItem;
                Common.select_background_key =recents.get(position).getKey();
                Intent intent = new Intent(context, MyWallpaper.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recents.size();
    }
}
