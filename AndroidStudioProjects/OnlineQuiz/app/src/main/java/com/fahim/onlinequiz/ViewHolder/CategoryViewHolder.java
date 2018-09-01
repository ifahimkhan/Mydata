package com.fahim.onlinequiz.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fahim.onlinequiz.Interface.ItemClickListener;
import com.fahim.onlinequiz.R;

/**
 * Created by HSBC on 07-12-2017.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView category_name;
    public ImageView category_image;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        category_image = (ImageView) itemView.findViewById(R.id.category_image);
        category_name = (TextView) itemView.findViewById(R.id.category_name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
