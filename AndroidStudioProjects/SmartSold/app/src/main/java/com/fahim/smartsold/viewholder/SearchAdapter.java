package com.fahim.smartsold.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fahim.smartsold.DetailActivity;
import com.fahim.smartsold.Interface.ItemClickListener;
import com.fahim.smartsold.R;
import com.fahim.smartsold.common.Common;
import com.fahim.smartsold.model.Item;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.ArrayList;

/**
 * Created by root on 29/4/18.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>implements View.OnClickListener {


    Context context;
    ArrayList<Item> arrayList;

    public SearchAdapter(ArrayList<Item> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchAdapter.MyViewHolder holder, final int position) {

        Picasso.with(context)
                .load(arrayList.get(position).getItemImageCover())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.item_image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                        Picasso.with(context).load(arrayList.get(position).getItemImageCover())
                                .error(R.drawable.ic_terrain_black_24dp)
                                .into(holder.item_image, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {


                                    }
                                });
                    }
                });

        holder.itemPrice.setAmount((float) arrayList.get(position).getItemPrice());
        holder.ownerName.setText(arrayList.get(position).getOwnerName());
        holder.itemName.setText(arrayList.get(position).getItemName());
        holder.itemDescription.setText(arrayList.get(position).getItemDescription());
        String date = "Date: " + DateFormat.format("dd:MM:yyyy", arrayList.get(position).getTime());
        holder.date.setText(date);
        holder.item_location.setText("Location: " + arrayList.get(position).getOwnerLocation());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Common.select_properties = arrayList.get(position);

                context.startActivity(new Intent(context, DetailActivity.class));

            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public void onClick(View view) {

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ownerName, itemName, itemDescription, item_location, date;
        public KenBurnsView item_image;
        public MoneyTextView itemPrice;
        ItemClickListener itemClickListener;


        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }


        public MyViewHolder(View itemView) {
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
}
