package com.fahim.onlinequiz.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fahim.onlinequiz.Interface.ItemClickListener;
import com.fahim.onlinequiz.R;

/**
 * Created by HSBC on 18-02-2018.
 */

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textName, textScore;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private ItemClickListener itemClickListener;

    public RankingViewHolder(View itemView) {
        super(itemView);
        textName = (TextView) itemView.findViewById(R.id.txt_name);
        textScore = (TextView) itemView.findViewById(R.id.textScore);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view, getAdapterPosition(), false);

    }
}
