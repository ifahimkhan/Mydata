package com.fahim.smartsold;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;

import com.fahim.smartsold.Interface.ItemClickListener;
import com.fahim.smartsold.common.Common;
import com.fahim.smartsold.model.Item;
import com.fahim.smartsold.viewholder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MyAdsActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference referenceItems;
    Query query;

    FirebaseRecyclerOptions<Item> options;
    FirebaseRecyclerAdapter<Item, ItemViewHolder> adapter;

    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Ads");
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        referenceItems = database.getReference(Common.STR_FEED_REF);
        query = referenceItems
                .orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        options = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {


            @Override
            public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = getLayoutInflater().inflate(R.layout.layout_item, parent, false);
                return new ItemViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder holder, int position, @NonNull final Item model) {
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


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyAdsActivity.this);
                        alertDialog.setTitle("Warning..!");

                        alertDialog
                                .setMessage("You sure you want to delete this Ad")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        database.getReference(Common.STR_FEED_REF).child(Common.ITEM_ID_SELECTED).removeValue();

                                        dialogInterface.cancel();
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog dialog = alertDialog.create();
                        dialog.show();
                    }
                });

            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.my_ads_recyclerview);
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


}
