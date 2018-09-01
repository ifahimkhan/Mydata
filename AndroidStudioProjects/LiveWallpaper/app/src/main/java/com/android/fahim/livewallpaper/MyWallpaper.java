package com.android.fahim.livewallpaper;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.fahim.livewallpaper.Database.DataSource.RecentRepository;
import com.android.fahim.livewallpaper.Database.LocalDatabase.LocalDatabase;
import com.android.fahim.livewallpaper.Database.LocalDatabase.RecentDataSource;
import com.android.fahim.livewallpaper.Database.Recents;
import com.android.fahim.livewallpaper.Helper.SaveImageHelper;
import com.android.fahim.livewallpaper.Models.WallpaperItem;
import com.android.fahim.livewallpaper.common.Common;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import at.markushi.ui.CircleButton;
import dmax.dialog.SpotsDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyWallpaper extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout rootLayout;

    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;


    CircleButton circleButton, download, shareButton;
    KenBurnsView imageView;
    SpotsDialog spotsDialog;

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

            try {
                wallpaperManager.setBitmap(bitmap);
                Snackbar.make(rootLayout, "Wallpaper is set", Snackbar.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {


        }
    };

    String fileName;
    private static String dirPath;
    int downloadId;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case Common.PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {
                    Toast.makeText(this, "Permission Required to Download Image", Toast.LENGTH_SHORT).show();

                }
                break;
            }
        }

    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallpaper);

        dirPath = SaveImageHelper.getRootDirPath(getApplicationContext());
        fileName = UUID.randomUUID().toString() + ".png";

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_wallpaper_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        compositeDisposable = new CompositeDisposable();
        LocalDatabase localDatabase = LocalDatabase.getInstance(this);
        recentRepository = RecentRepository.getInstance(RecentDataSource.getInstance(localDatabase.recentsDAO()));


        rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setTitle(Common.CATEGORY_SELECTED);
        imageView = (KenBurnsView) findViewById(R.id.imagethumb);
        Picasso.with(this)
                .load(Common.select_background.getImageUrl())
                .into(imageView);


        addToRecents();

        circleButton = (CircleButton) findViewById(R.id.setWallpaper);
        circleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.with(getBaseContext())
                        .load(Common.select_background.getImageUrl())
                        .into(target);


            }
        });

        download = (CircleButton) findViewById(R.id.downloadWallpaper);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(MyWallpaper.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Common.PERMISSION_REQUEST_CODE);
                } else {

                    final SpotsDialog alertDialog = new SpotsDialog(MyWallpaper.this);

                    downloadId = PRDownloader.download(Common.select_background.getImageUrl(), dirPath, fileName)
                            .build()
                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                @Override
                                public void onStartOrResume() {

                                    alertDialog.show();
                                    alertDialog.setMessage("please Wait..!");
                                }
                            })
                            .setOnPauseListener(new OnPauseListener() {
                                @Override
                                public void onPause() {
                                }
                            })
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel() {
                                    alertDialog.dismiss();
                                    Toast.makeText(MyWallpaper.this, "Download Canceled", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setOnProgressListener(new OnProgressListener() {
                                @Override
                                public void onProgress(Progress progress) {
                                    long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                                    alertDialog.setMessage("Downloading: " + String.valueOf(progressPercent));
                                }
                            })
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {

                                    alertDialog.setMessage("Download Compeleted");
                                    alertDialog.dismiss();
                                    Snackbar.make(rootLayout, "Download Completed Successfully..", Snackbar.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onError(Error error) {
                                    alertDialog.setMessage("Error in Downloading");
                                    downloadId = 0;
                                }

                            });


                }
            }
        });

        shareButton = (CircleButton) findViewById(R.id.shareWallpaper);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Picasso.with(getApplicationContext()).load(Common.select_background.getImageUrl()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("image/*");
                        i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap, MyWallpaper.this));
                        MyWallpaper.this.startActivity(Intent.createChooser(i, "Share Image"));

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

            }
        });

        increaseViewCount();
    }

    private void increaseViewCount() {

        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPERS)
                .child(Common.select_background_key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("viewCount")) {
                            WallpaperItem wallpaperItem = dataSnapshot.getValue(WallpaperItem.class);
                            long count = wallpaperItem.getViewCount() + 1;

                            Map<String, Object> update_view = new HashMap<>();
                            update_view.put("viewCount", Long.valueOf(count));

                            FirebaseDatabase.getInstance().getReference(Common.STR_WALLPAPERS)
                                    .child(Common.select_background_key)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Cannot update view Count", Toast.LENGTH_LONG).show();
                                        }
                                    });

                        } else {

                            Map<String, Object> update_view = new HashMap<>();
                            update_view.put("viewCount", Long.valueOf(1));

                            FirebaseDatabase.getInstance().getReference(Common.STR_WALLPAPERS)
                                    .child(Common.select_background_key)
                                    .updateChildren(update_view)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Cannot update view Count", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    private void addToRecents() {
        Disposable disposable = Observable.create(
                new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                        Recents recents = new Recents(
                                Common.select_background.getImageUrl(),
                                Common.select_background.getCategoryId(),
                                String.valueOf(System.currentTimeMillis()),
                                Common.select_background_key
                        );
                        recentRepository.insertRecents(recents);
                        e.onComplete();
                    }
                }
        ).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
        compositeDisposable.add(disposable);
    }

    private void delete(String uri) {
        File file = new File(Uri.parse(uri).getPath());
        file.delete();
    }

    private Uri getLocalBitmapUri(Bitmap bitmap, Context context) {
        Uri bmpUri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    protected void onDestroy() {
        Picasso.with(this).cancelRequest(target);
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
