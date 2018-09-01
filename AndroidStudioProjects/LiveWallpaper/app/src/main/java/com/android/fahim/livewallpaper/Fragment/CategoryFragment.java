package com.android.fahim.livewallpaper.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.fahim.livewallpaper.Interface.ItemClickListener;
import com.android.fahim.livewallpaper.ListActivity;
import com.android.fahim.livewallpaper.Models.CategoryItem;
import com.android.fahim.livewallpaper.R;
import com.android.fahim.livewallpaper.ViewHolder.CategoryViewHolder;
import com.android.fahim.livewallpaper.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


public class CategoryFragment extends Fragment {

    private static CategoryFragment instance = null;

    FirebaseDatabase database;
    DatabaseReference categoryBackground;

    FirebaseRecyclerOptions<CategoryItem> options;
    FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder> adapter;

    RecyclerView recyclerView;

    public CategoryFragment() {
        // Required empty public constructor
        database = FirebaseDatabase.getInstance();
        categoryBackground = database.getReference(Common.STR_CATEGORY_BACKGROUND);
        options = new FirebaseRecyclerOptions.Builder<CategoryItem>()
                .setQuery(categoryBackground, CategoryItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position, @NonNull final CategoryItem model) {

                Picasso.with(getActivity())
                        .load(model.getImageLink())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.backround_image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                Picasso.with(getActivity()).load(model.getImageLink())
                                        .error(R.drawable.ic_terrain_black_24dp)
                                        .into(holder.backround_image, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {


                                            }
                                        });
                            }
                        });
                holder.category_name.setText(model.getName());
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        Common.CATEGORY_ID_SELECTED=adapter.getRef(position).getKey();
                        Common.CATEGORY_SELECTED=model.getName();

                        startActivity(new Intent(getActivity(),ListActivity.class));
                    }
                });
            }

            @Override
            public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = getLayoutInflater().inflate(R.layout.layout_category_item, parent, false);
                return new CategoryViewHolder(itemView);
            }
        };
    }


    public static CategoryFragment getInstance() {
        if (instance == null)
            instance = new CategoryFragment();
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

        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        setCategory();
        return view;
    }

    private void setCategory() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    public void onStop() {
        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
        }
    }

}
