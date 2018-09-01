package com.android.fahim.livewallpaper.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.fahim.livewallpaper.Interface.ItemClickListener;
import com.android.fahim.livewallpaper.Models.WallpaperItem;
import com.android.fahim.livewallpaper.MyWallpaper;
import com.android.fahim.livewallpaper.R;
import com.android.fahim.livewallpaper.ViewHolder.ListWallpaperViewHolder;
import com.android.fahim.livewallpaper.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class TrendingFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseRecyclerOptions<WallpaperItem> options;
    FirebaseRecyclerAdapter<WallpaperItem, ListWallpaperViewHolder> adapter;


    private static TrendingFragment instance = null;

    public TrendingFragment() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(Common.STR_WALLPAPERS);

        // Required empty public constructor
        Query query = databaseReference.orderByChild("viewCount").limitToLast(10);
        options = new FirebaseRecyclerOptions.Builder<WallpaperItem>()
                .setQuery(query, WallpaperItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<WallpaperItem, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, int position, @NonNull final WallpaperItem model) {
                Picasso.with(getActivity())
                        .load(model.getImageUrl())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.wallpaper, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity())
                                        .load(model.getImageUrl())
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

                        Common.select_background = model;
                        Common.select_background_key = adapter.getRef(position).getKey();
                        Intent intent = new Intent(getActivity(), MyWallpaper.class);
                        startActivity(intent);
                    }
                });

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
        };
    }


    public static TrendingFragment getInstance() {
        if (instance == null)
            instance = new TrendingFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_popular, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_trending);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loadTreandingList();


        return view;
    }

    private void loadTreandingList() {

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStop() {
        adapter.stopListening();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null) {
            adapter.startListening();
        }
    }
}
