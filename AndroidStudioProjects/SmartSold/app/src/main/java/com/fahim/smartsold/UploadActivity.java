package com.fahim.smartsold;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fahim.smartsold.common.Common;
import com.fahim.smartsold.model.Item;
import com.fahim.smartsold.sharedPrefrences.ModuleTest;
import com.fahim.smartsold.sharedPrefrences.Price;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import es.dmoral.toasty.Toasty;

public class UploadActivity extends AppCompatActivity {

    Button browse, upload, browse2, browse3, browse4;
    MaterialEditText itemName, itemDesc, itemLocation, itemMobile, itemPrice;
    ImageView imageView, imageView2, imageView3, imageView4;
    Toolbar toolbar;

    private Uri filepath, filepath2, filepath3, filepath4;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    Price sharedPrice;
    ModuleTest moduleTest;

    Item sendItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        init();
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(Common.PICK_IMAGE_REQ);
            }
        });
        browse2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(Common.PICK_IMAGE_REQ2);
            }
        });
        browse3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(Common.PICK_IMAGE_REQ3);
            }
        });
        browse4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(Common.PICK_IMAGE_REQ4);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPost();
            }
        });

    }

    private void uploadPost() {
        if (verification()) {
            uploadingPost();
        } else {
            Toasty.error(getApplicationContext(), "Evaluation is not done properly", 1900, true).show();
        }
    }

    private void uploadingPost() {
        if (filepath != null) {
            sendItem = new Item();
            sendItem.setOwnerName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            sendItem.setOwnerLocation(itemLocation.getText().toString().toLowerCase());
            sendItem.setOwnerEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            sendItem.setOwnerContact(Long.parseLong(itemMobile.getText().toString()));
            sendItem.setItemPrice(sharedPrice.getMarketDisplayPrice());
            sendItem.setItemName(itemName.getText().toString());
            sendItem.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
            sendItem.setItemDescription(itemDesc.getText().toString());
            sendItem.setFaulty_features(moduleTest.getInfo());

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();


            if (filepath4 != null) {
                StorageReference myref = storageReference.child(new StringBuilder("images/").append(UUID.randomUUID().toString())
                        .toString());
                myref.putFile(filepath4)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                sendItem.setUrl4(taskSnapshot.getDownloadUrl().toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Progress: " + (int) progress + "%");
                                } catch (Exception e) {
                                    Log.d("ERROR", e.getMessage());
                                }
                            }
                        });
            } else {
                sendItem.setUrl4(null);
            }
            if (filepath3 != null) {
                StorageReference myref = storageReference.child(new StringBuilder("images/").append(UUID.randomUUID().toString())
                        .toString());
                myref.putFile(filepath3)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                sendItem.setUrl3(taskSnapshot.getDownloadUrl().toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Progress: " + (int) progress + "%");
                                } catch (Exception e) {
                                    Log.d("ERROR", e.getMessage());
                                }
                            }
                        });
            } else {
                sendItem.setUrl3(null);
            }
            if (filepath2 != null) {
                StorageReference myref = storageReference.child(new StringBuilder("images/").append(UUID.randomUUID().toString())
                        .toString());
                myref.putFile(filepath2)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                sendItem.setUrl2(taskSnapshot.getDownloadUrl().toString());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UploadActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                try {
                                    double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Progress: " + (int) progress + "%");
                                } catch (Exception e) {
                                    Log.d("ERROR", e.getMessage());
                                }
                            }
                        });
            } else {
                sendItem.setUrl2(null);
            }
            StorageReference myrefof = storageReference.child(new StringBuilder("images/").append(UUID.randomUUID().toString())
                    .toString());
            myrefof.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            sendItem.setItemImageCover(taskSnapshot.getDownloadUrl().toString());
                            saveUrlToItems(sendItem);
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
                            } catch (Exception e) {
                                Log.d("ERROR", e.getMessage());
                            }
                        }
                    });
        }

    }

    private void saveUrlToItems(Item sendItem) {
        FirebaseDatabase.getInstance()
                .getReference(Common.STR_FEED_REF)
                .push()
                .setValue(sendItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UploadActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private boolean verification() {
        if (itemMobile.getText().toString().length() > 0 && itemMobile.getText().toString().length() == 10) {

        } else {
            itemMobile.setHelperText("10 digit numbers only");
            Toasty.error(getApplicationContext(), "10 digit numbers only", 1000, true).show();
            return false;
        }
        if (itemLocation.getText().length() == 0) {
            itemLocation.setHelperText("Please Enter Your Location");
            Toasty.error(getApplicationContext(), "Location is required", 1000, true).show();

            return false;
        }

        if (sharedPrice.getMarketDisplayPrice() > 0 && itemName.getText().toString().length() > 0 &&
                itemDesc.getText().toString().length() > 0 &&
                itemPrice.getText().toString().length() > 0 &&
                itemMobile.getText().toString().length() > 0 &&
                itemLocation.getText().toString().length() > 0
                ) {
            return true;
        } else return false;
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Post New Ad");
        setSupportActionBar(toolbar);

        sharedPrice = new Price(UploadActivity.this);
        moduleTest = new ModuleTest(UploadActivity.this);
        browse = (Button) findViewById(R.id.browse);
        browse2 = (Button) findViewById(R.id.browse2);
        browse3 = (Button) findViewById(R.id.browse3);
        browse4 = (Button) findViewById(R.id.browse4);

        upload = (Button) findViewById(R.id.upload);

        itemName = (MaterialEditText) findViewById(R.id.upload_item_name);
        itemDesc = (MaterialEditText) findViewById(R.id.upload_item_desc);
        itemLocation = (MaterialEditText) findViewById(R.id.upload_location);
        itemMobile = (MaterialEditText) findViewById(R.id.upload_mobile);
        itemPrice = (MaterialEditText) findViewById(R.id.upload_price);

        imageView = (ImageView) findViewById(R.id.upload_image);
        imageView2 = (ImageView) findViewById(R.id.upload_image2);
        imageView3 = (ImageView) findViewById(R.id.upload_image3);
        imageView4 = (ImageView) findViewById(R.id.upload_image4);

        if (Common.sellThisItem) {
            Common.sellThisItem = false;
            if (sharedPrice.getMarketDisplayPrice() > 0) {
                if (sharedPrice.getPrefName().toString() != null)
                    itemName.setText(sharedPrice.getPrefName().toString());
                if (sharedPrice.getDescription().toString() != null)
                    itemDesc.setText(sharedPrice.getDescription().toString());
                itemPrice.setText(sharedPrice.getMarketDisplayPrice() + "");
                itemPrice.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return true;
                    }
                });
            } else {
                itemDesc.setText("");
                itemName.setText("");
            }
        }

    }

    private void chooseImage(int REQUESTCODE) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQ && resultCode == RESULT_OK && data.getData() != null) {

            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imageView.setImageBitmap(bitmap);
                upload.setEnabled(true);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == Common.PICK_IMAGE_REQ2 && resultCode == RESULT_OK && data.getData() != null) {

            filepath2 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath2);
                imageView2.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == Common.PICK_IMAGE_REQ3 && resultCode == RESULT_OK && data.getData() != null) {

            filepath3 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath3);
                imageView3.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == Common.PICK_IMAGE_REQ4 && resultCode == RESULT_OK && data.getData() != null) {

            filepath4 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath4);
                imageView4.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
