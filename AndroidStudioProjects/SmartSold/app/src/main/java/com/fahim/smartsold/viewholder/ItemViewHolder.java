package com.fahim.smartsold.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fahim.smartsold.Interface.ItemClickListener;
import com.fahim.smartsold.R;
import com.flaviofaria.kenburnsview.KenBurnsView;

import org.fabiomsr.moneytextview.MoneyTextView;

/**
 * Created by root on 11/4/18.
 */

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView ownerName, itemName, itemDescription, item_location, date;
    public KenBurnsView item_image;
    public MoneyTextView itemPrice;
    ItemClickListener itemClickListener;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    public ItemViewHolder(View itemView) {
        super(itemView);

        ownerName = (TextView) itemView.findViewById(R.id.name);
        itemName = (TextView) itemView.findViewById(R.id.item_name);
        itemDescription = (TextView) itemView.findViewById(R.id.description);
        itemPrice = (MoneyTextView) itemView.findViewById(R.id.item_price);
        item_image = (KenBurnsView) itemView.findViewById(R.id.item_image);
        date = (TextView) itemView.findViewById(R.id.item_date);
        item_location = (TextView) itemView.findViewById(R.id.item_location);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());

    }
}
