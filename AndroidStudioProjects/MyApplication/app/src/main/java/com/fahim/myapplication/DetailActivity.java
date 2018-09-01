package com.fahim.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fahim.myapplication.Common.Common;
import com.fahim.myapplication.model.AlbumModel;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import static java.lang.String.valueOf;

public class DetailActivity extends AppCompatActivity {

    KenBurnsView kenBurnsView;
    TextView title, albumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AlbumModel albumModel = Common.getAlbumModel1();

        kenBurnsView = (KenBurnsView) findViewById(R.id.top_image);
        title = (TextView) findViewById(R.id.top_title);
        albumId = (TextView) findViewById(R.id.album_id);

        Picasso.with(getApplicationContext())
                .load(albumModel.getUrl())
                .into(kenBurnsView);
        albumId.setText(valueOf(albumModel.getAlbumId()));
        title.setText(albumModel.getId()+": "+albumModel.getTitle());


    }
}
