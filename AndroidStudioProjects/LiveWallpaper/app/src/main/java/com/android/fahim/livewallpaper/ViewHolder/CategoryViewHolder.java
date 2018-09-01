package com.android.fahim.livewallpaper.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fahim.livewallpaper.Interface.ItemClickListener;
import com.android.fahim.livewallpaper.R;
import com.flaviofaria.kenburnsview.KenBurnsView;


/**
 * Created by HSBC on 21-02-2018.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView category_name;
    public KenBurnsView backround_image;
    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public CategoryViewHolder(View itemView) {
        super(itemView);
        backround_image = (KenBurnsView) itemView.findViewById(R.id.image);
        category_name = (TextView) itemView.findViewById(R.id.name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());

    }
}
