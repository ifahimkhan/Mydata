package com.fahim.smartsold;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fahim.smartsold.common.Common;
import com.fahim.smartsold.sharedPrefrences.Price;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.android.device.DeviceName;
import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable;
import com.squareup.picasso.Picasso;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.ArrayList;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import ru.katso.livebutton.LiveButton;

import static java.lang.String.valueOf;

public class HomeActivity extends AppCompatActivity {

    private TextView tv_myphone, text;
    private ImageView iv_device;
    boolean isChargingGood = false;
    private Toolbar toolbar;
    String deviceName = "";
    String modelnumber = "";
    int totalprice = 0;
    LiveButton evaluation, sellItem;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    CardView moneyView;
    MoneyTextView moneyTextView;
    Price sharedPrefPrice;
    ProgressBar progressBar;

    BottomNavigationView bottomNavigationView;

    ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
    private int priceposition = 0;

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPrefPrice.getMarketPrice() > 0) {
            moneyView.setVisibility(View.VISIBLE);
            moneyTextView.setAmount(sharedPrefPrice.getMarketDisplayPrice());
        } else {
            moneyView.setVisibility(View.INVISIBLE);
            // getPrice();
        }
        if (sharedPrefPrice.getMarketDisplayPrice() <= 0 && sharedPrefPrice.getMarketPrice() > 0) {
            Toasty.error(getApplicationContext(), "Evaluate First Couldn't offer you the assured selling price right now",
                    2000, true).show();
            moneyView.setVisibility(View.INVISIBLE);

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        detection();
        init();
        evaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEvaluation();
            }
        });
        sellItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.sellThisItem=true;
                startActivity(new Intent(getApplicationContext(), UploadActivity.class));
            }
        });

        final int getmarketPrice = sharedPrefPrice.getMarketPrice();
        //Toasty.error(this,getmarketPrice+" ",2000,true).show();
        if (getmarketPrice == 0) {

            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            getPrice();

        }
        moneyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrice();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.action_feed) {

                    startActivity(new Intent(getApplicationContext(), BrowseActivity.class));

                } else if (item.getItemId() == R.id.action_upload) {

                    startActivity(new Intent(getApplicationContext(), UploadActivity.class));

                } else {
                    startActivity(new Intent(getApplicationContext(), MyAdsActivity.class));
                }
                return false;
            }
        });
        try {
            if (deviceName.length() > 0 && tv_myphone.getText().length() > 0) {
                sharedPrefPrice.setPrefName(deviceName);
                sharedPrefPrice.setDescriptionPref(tv_myphone.getText().toString());
            } else {
                Toasty.error(getApplicationContext(), "Error" + deviceName, 1000, true).show();
            }
        } catch (Exception e) {

        }
    }

    private void init() {
        checkManufacturerURl();

        sharedPrefPrice = new Price(this);

        moneyView = (CardView) findViewById(R.id.moneyView);
        moneyTextView = (MoneyTextView) findViewById(R.id.price);
        evaluation = (LiveButton) findViewById(R.id.sell_item);
        sellItem = (LiveButton) findViewById(R.id.sell_this_item);
        progressBar = (ProgressBar) findViewById(R.id.google_progress);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

    }

    private void checkManufacturerURl() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        String manufacturer = Build.MANUFACTURER;
        manufacturer.toLowerCase();
        if (manufacturer.equals("samsung")) {

            databaseReference = firebaseDatabase.getReference("smartphones").child("samsung");
        }
    }

    private void detection() {
        text = (TextView) findViewById(R.id.textView);


        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        int availableMegs = (int) (mi.availMem / 0x100000L);

        //Percentage can be calculated for API 16+
        double percentAvail = mi.availMem / (double) mi.totalMem * 100;
        tv_myphone = (TextView) findViewById(R.id.myphone);
        iv_device = (ImageView) findViewById(R.id.deviceimage);
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.contains("-")) {
            String[] modelArray = model.split("-");
            modelnumber = modelArray[1];
        }
        deviceName = DeviceName.getDeviceName();

        TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String Telephony_service = mngr.getDeviceId();

        tv_myphone.setText("" + manufacturer.toUpperCase() + " " + model.toUpperCase() + " \n");
        tv_myphone.append(deviceName.toUpperCase() + "\n");
        tv_myphone.append("IMEI: " + Telephony_service + "\n");

        tv_myphone.append("RAM: " + getRamSize() + " GB " + "\nSTORAGE: " + TotalInternalMemory());


        Picasso.with(HomeActivity.this).load("https://cdn1.airdroid.com/V3331707211712/theme/stock/images/findmyphone/default-l.jpg").into(iv_device);

    }

    public int getRamSize() {
        int ramsize;
        int tm = (int) (mi.totalMem / 1048576L);
        if (tm < 1024) {
            ramsize = 1;
        } else if (tm < 2048 && tm > 1024) {
            ramsize = 2;
        } else if (tm < 3072 && tm > 2048) {
            ramsize = 3;
        } else {
            ramsize = 4;
        }
        return ramsize;
    }

    public static String TotalInternalMemory() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        // long   Total  = ( (long) statFs.getBlockCountLong() * (long) statFs.getBlockSizeLong());
        long blocksize = statFs.getBlockSizeLong();
        long blockcount = statFs.getBlockCountLong();
        // long busy = BusyMemory();
        long total = (blockcount * blocksize);

        long totalSize = total / (1000 * 1000 * 1000);

        return valueOf(totalSize) + "GB";

    }

    public static long BusyMemory() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        long Total = ((long) statFs.getBlockCountLong() * (long) statFs.getBlockSizeLong());
        long Free = ((long) statFs.getAvailableBlocksLong() * (long) statFs.getBlockSizeLong());
        long Busy = Total - Free;
        return Busy;
    }

    public void setEvaluation() {
        if (sharedPrefPrice.getMarketPrice() > 0) {
            startActivity(new Intent(this, EvaluatorActivity.class));
        } else {
            getPrice();
            Toasty.warning(getApplicationContext(), "Loading"
                    , 2000, true).show();
        }
    }

    public void getPrice() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminateDrawable(new FoldingCirclesDrawable.Builder(HomeActivity.this)
                .build());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> pricelist = new ArrayList<>();
                pricelist = collectAllPrice((Map<String, Object>) dataSnapshot.getValue());
                Log.d("Tag: ", pricelist.toString());
                ArrayList<String> modellist = new ArrayList<>();
                modellist = collectAllModel((Map<String, Object>) dataSnapshot.getValue());
                Log.d("Tag: ", modellist.toString());

                for (int c = 0; c < modellist.size(); c++) {

                    if (modellist.get(c).toLowerCase().equals(modelnumber.toLowerCase())) {
                        priceposition = c;
                    }
                }

                totalprice = Integer.parseInt(pricelist.get(priceposition));
                sharedPrefPrice.setMarketPrice(totalprice);
                Toasty.success(getApplicationContext(), "Market price is fetched start Evaluation", 2000, true)
                        .show();

                Log.d("TAG", totalprice + " " + priceposition + "");

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private ArrayList<String> collectAllModel(Map<String, Object> value) {
        ArrayList<String> model = new ArrayList<>();
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            model.add(String.valueOf(singleUser.get("model")));
        }
        return model;
    }

    private ArrayList<String> collectAllPrice(Map<String, Object> value) {
        ArrayList<String> price = new ArrayList<>();
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            Map singleUser = (Map) entry.getValue();
            price.add(String.valueOf((Long) singleUser.get("price")));
        }
        return price;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                AuthUI.getInstance().signOut(HomeActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(HomeActivity.this, "you have been sign out", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }
}













