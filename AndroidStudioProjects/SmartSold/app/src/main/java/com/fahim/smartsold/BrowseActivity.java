package com.fahim.smartsold;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.fahim.smartsold.Interface.ItemClickListener;
import com.fahim.smartsold.common.Common;
import com.fahim.smartsold.model.Item;
import com.fahim.smartsold.viewholder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class BrowseActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    FirebaseDatabase database;
    DatabaseReference referenceItems;

    FirebaseRecyclerOptions<Item> options;
    FirebaseRecyclerAdapter<Item, ItemViewHolder> adapter, adapter1;

    RecyclerView recyclerView;
    Query query;
    Toolbar toolbar;
    SearchView searchView;

    ArrayList<Item> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Products Feed");
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        referenceItems = database.getReference(Common.STR_FEED_REF);
        options = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(referenceItems, Item.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {


            @Override
            public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = getLayoutInflater().inflate(R.layout.layout_item, parent, false);
                return new ItemViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder holder, int position, @NonNull final Item model) {
                arrayList.add(model);
                Picasso.with(getApplicationContext())
                        .load(model.getItemImageCover())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.item_image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                Picasso.with(getApplicationContext()).load(model.getItemImageCover())
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

                holder.itemPrice.setAmount((float) model.getItemPrice());
                holder.ownerName.setText(model.getOwnerName());
                holder.itemName.setText(model.getItemName());
                holder.itemDescription.setText(model.getItemDescription());
                String date = "Date: " + DateFormat.format("dd:MM:yyyy", model.getTime());
                holder.date.setText(date);
                holder.item_location.setText("Location: " + model.getOwnerLocation());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Common.ITEM_ID_SELECTED = adapter.getRef(position).getKey();
                        Common.select_properties = model;

                        Toasty.info(BrowseActivity.this, Common.ITEM_ID_SELECTED + "", 1000, true).show();
                        startActivity(new Intent(getApplicationContext(), DetailActivity.class));

                    }
                });

            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_feed);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.m_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        query = referenceItems
                .orderByChild("ownerLocation").equalTo(newText);


        options = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        adapter1 = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {


            @Override
            public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = getLayoutInflater().inflate(R.layout.layout_item, parent, false);
                return new ItemViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder holder, int position, @NonNull final Item model) {
                arrayList.add(model);
                Picasso.with(getApplicationContext())
                        .load(model.getItemImageCover())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.item_image, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                                Picasso.with(getApplicationContext()).load(model.getItemImageCover())
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

                holder.itemPrice.setAmount((float) model.getItemPrice());
                holder.ownerName.setText(model.getOwnerName());
                holder.itemName.setText(model.getItemName());
                holder.itemDescription.setText(model.getItemDescription());
                String date = "Date: " + DateFormat.format("dd:MM:yyyy", model.getTime());
                holder.date.setText(date);
                holder.item_location.setText("Location: " + model.getOwnerLocation());

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Common.ITEM_ID_SELECTED = adapter.getRef(position).getKey();
                        Common.select_properties = model;

                        Toasty.info(BrowseActivity.this, Common.ITEM_ID_SELECTED + "", 1000, true).show();
                        startActivity(new Intent(getApplicationContext(), DetailActivity.class));

                    }
                });

            }
        };


        recyclerView.setAdapter(adapter1);
        adapter1.startListening();
        adapter1.notifyDataSetChanged();
        if (newText.equals("")){
            recyclerView.setAdapter(adapter);
            adapter.startListening();
            adapter.notifyDataSetChanged();
        }
        return true;

    }


}
