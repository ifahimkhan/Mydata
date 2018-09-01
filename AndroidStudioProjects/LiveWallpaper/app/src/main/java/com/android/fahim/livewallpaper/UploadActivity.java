package com.android.fahim.livewallpaper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.fahim.livewallpaper.Models.CategoryItem;
import com.android.fahim.livewallpaper.Models.WallpaperItem;
import com.android.fahim.livewallpaper.common.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {

    Button btn_upload, btn_browse;
    MaterialSpinner spinner;
    Map<String, String> spinnerData = new HashMap<>();
    ImageView imageView_preview;

    private Uri filepath;
    String categoryIdSelect = "";

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        init();
        loadCategoryToSpinner();
        //Log.d("Spinner size", spinner.getItems().size() + "");
        //Toast.makeText(this, "" + spinner.getItems().size(), Toast.LENGTH_SHORT).show();

        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedIndex() == 0) {

                    Toast.makeText(getApplicationContext(), "Please select Category", Toast.LENGTH_LONG).show();
                } else {

                    upload();

                }
            }
        });
    }

    private void upload() {

        if (filepath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference myref = storageReference.child(new StringBuilder("images/").append(UUID.randomUUID().toString())
                    .toString());

            myref.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            saveUrlToCategory(categoryIdSelect, taskSnapshot.getDownloadUrl().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UploadActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            try {
                                double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Progress: " + (int) progress + "%");
                            }catch (Exception e)
                            {
                                Log.d("ERROR",e.getMessage());
                            }
                        }
                    });
        }
    }

    private void saveUrlToCategory(String categoryIdSelect, String imageLink) {

        FirebaseDatabase.getInstance()
                .getReference(Common.STR_WALLPAPERS)
                .push()
                .setValue(new WallpaperItem(imageLink, categoryIdSelect))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UploadActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Common.PICK_IMAGE_REQ);
    }

    private void loadCategoryToSpinner() {

        FirebaseDatabase.getInstance()
                .getReference(Common.STR_CATEGORY_BACKGROUND)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                            CategoryItem categoryItem = postSnapshot.getValue(CategoryItem.class);
                            String key = postSnapshot.getKey();

                            spinnerData.put(key, categoryItem.getName());
                        }
                        Object[] valueArray = spinnerData.values().toArray();
                        //Toast.makeText(this, "" + spinner.getItems().size(), Toast.LENGTH_SHORT).show();

                        List<Object> valueList = new ArrayList<>();
                        valueList.add(0, "Category");
                        valueList.addAll(Arrays.asList(valueArray));
                        spinner.setItems(valueList);
                        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                                Object[] keyArray = spinnerData.keySet().toArray();
                                List<Object> keyList = new ArrayList<>();
                                keyList.add(0, "Category_key");
                                keyList.addAll(Arrays.asList(keyArray));
                                categoryIdSelect = keyList.get(position).toString();


                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void init() {

        btn_browse = (Button) findViewById(R.id.btn_browse);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        imageView_preview = (ImageView) findViewById(R.id.image_preview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQ && resultCode == RESULT_OK && data.getData() != null) {

            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imageView_preview.setImageBitmap(bitmap);
                btn_upload.setEnabled(true);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
