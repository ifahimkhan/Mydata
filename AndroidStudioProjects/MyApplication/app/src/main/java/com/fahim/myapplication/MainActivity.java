package com.fahim.myapplication;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fahim.myapplication.Adapter.ListSourceAdapter;
import com.fahim.myapplication.model.AlbumModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private RequestQueue mRequestQueue;
    private String url = "https://jsonplaceholder.typicode.com/photos";
    private List modelList = new ArrayList();
    RecyclerView recyclerView;
    ListSourceAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    SpotsDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
        alertDialog = new SpotsDialog(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                sendAndRequestResponse();
                swipeRefreshLayout.setRefreshing(false);


            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        String cache = Paper.book().read("cache");
        if (cache != null && !cache.isEmpty() && !cache.equals("null")) {

            try {
                JSONArray response = new JSONArray(cache);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject obj = response.getJSONObject(i);
                    AlbumModel model = new AlbumModel();
                    model.setTitle(obj.getString("title"));
                    model.setAlbumId(obj.getInt("albumId"));
                    model.setId(obj.getInt("id"));
                    model.setUrl(obj.getString("url"));
                    model.setThumbnailUrl(obj.getString("thumbnailUrl"));

                    modelList.add(model);
                }

                adapter = new ListSourceAdapter(getApplicationContext(), modelList);
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else sendAndRequestResponse();
    }

    private void sendAndRequestResponse() {

        alertDialog.show();
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        //String Request initialized
        JsonArrayRequest requestarray = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d(TAG, response.toString());
                        Paper.book().write("cache", response.toString());


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                AlbumModel model = new AlbumModel();
                                model.setTitle(obj.getString("title"));
                                model.setAlbumId(obj.getInt("albumId"));
                                model.setId(obj.getInt("id"));
                                model.setUrl(obj.getString("url"));
                                model.setThumbnailUrl(obj.getString("thumbnailUrl"));

                                modelList.add(model);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter = new ListSourceAdapter(getApplicationContext(), modelList);
                        recyclerView.setAdapter(adapter);
                        alertDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());


            }
        });
        mRequestQueue.add(requestarray);

    }
}
