package com.fahim.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fahim.myapplication.model.AlbumModel;
import com.fahim.myapplication.Common.Common;
import com.fahim.myapplication.DetailActivity;
import com.fahim.myapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HSBC on 09-10-2017.
 */


class ListSourceViewHolder extends RecyclerView.ViewHolder {

    TextView source_title;
    CircleImageView source_image;
    Button details_button;


    public ListSourceViewHolder(View itemView) {
        super(itemView);
        source_image = (CircleImageView) itemView.findViewById(R.id.source_image);
        source_title = (TextView) itemView.findViewById(R.id.source_name);
        details_button = (Button) itemView.findViewById(R.id.details);
    }
}

public class ListSourceAdapter extends RecyclerView.Adapter<ListSourceViewHolder> {
    Context context;
    List<AlbumModel> webSite;

    public ListSourceAdapter(Context context, List<AlbumModel> webSite) {
        this.context = context;
        this.webSite = webSite;
        // mService = Common.getIconsService();


    }

    @Override
    public ListSourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.source_layout, parent, false);
        return new ListSourceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListSourceViewHolder holder, final int position) {

        holder.source_title.setText(webSite.get(position).getId() + ". " +
                webSite.get(position).getTitle());
        Picasso.with(context).load(webSite.get(position).getThumbnailUrl())
                .into(holder.source_image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(webSite.get(position).getThumbnailUrl())
                                .error(R.drawable.ic_terrain_black_24dp)
                                .into(holder.source_image, new Callback() {
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

        holder.details_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Common.setAlbumModel1(webSite.get(position));
                context.startActivity(new Intent(context, DetailActivity.class));


            }
        });


    }

    @Override
    public int getItemCount() {
        return webSite.size();
    }

}