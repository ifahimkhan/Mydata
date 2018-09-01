package com.fahim.smartsold;

import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.fahim.smartsold.model.ApManager;
import com.fahim.smartsold.sharedPrefrences.ModuleTest;
import com.fahim.smartsold.sharedPrefrences.Price;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Locale;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.License;
import de.psdev.licensesdialog.model.Notice;
import es.dmoral.toasty.Toasty;
import jp.shts.android.library.TriangleLabelView;

public class EvaluatorActivity extends AppCompatActivity {

    private static final int SPEECH_REQ_CODE = 100;
    Toolbar toolbar;

    RippleBackground RB1, RB2, RB3, RB4, RB5, RB6;
    ImageView wiFi, bluetooth, location, hotspot, mic, battery;
    Boolean flagBluetooth, flagWifi, flagLocation, flagHotspot, flagMic, flagBattery;
    BluetoothAdapter bluetoothAdapter;
    WifiManager wifiManager;
    LocationManager locationManager;
    TriangleLabelView checkmark1, checkmark2, checkmark3, checkmark4, checkmark5, checkmark6;
    Button checkout_button;

    int deduction_amount = 0;
    ModuleTest moduleTest;
    Price price;
    boolean checkoutClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluator);

        try {
            init();
            setListners();
            pref();
        } catch (Exception e) {

        }
    }

    private void pref() {
        price = new Price(this);
        moduleTest = new ModuleTest(this);
        flagWifi = moduleTest.getWifi();
        flagBluetooth = moduleTest.getBluetooth();
        flagLocation = moduleTest.getLocation();
        flagHotspot = moduleTest.getHotspot();
        flagMic = moduleTest.getMic();
        flagBattery = moduleTest.getBattery();


        if (flagWifi) {
            checkmark1.setVisibility(View.VISIBLE);
        }
        if (flagBluetooth) {
            checkmark2.setVisibility(View.VISIBLE);
        }

        if (flagLocation) {
            checkmark3.setVisibility(View.VISIBLE);
        }

        if (flagHotspot) {
            checkmark4.setVisibility(View.VISIBLE);
        }


        if (flagMic) {
            checkmark5.setVisibility(View.VISIBLE);
        }

        if (flagBattery) {
            checkmark6.setVisibility(View.VISIBLE);
        }
    }

    private void setListners() {

        //img1 Listener
        wiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wiFi();

            }
        });

        //img2 Listener

        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetooth();
            }
        });

        //img3 Listener
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location();
            }
        });

        hotspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hotspot();
            }
        });

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EvaluatorActivity.this);
                String test = getString(R.string.test);
                builder.setMessage("This is the Mic Testing you have to say the following line \n " + test)
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                promptSpeechInput();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                battery();
            }
        });

        checkout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkoutIsClicked();
            }
        });
    }

    private void checkoutIsClicked() {

        checkoutDialog();

    }

    private void doComplete() {

        if (!checkoutClicked) {
            checkoutDialog();

        }

        if (price.getDeductionAmount() > 0) {
            Toasty.info(getApplicationContext(), "Deduction Amount is : " + deduction_amount, 1000, true)
                    .show();

            int marketprice = price.getMarketPrice();
            Toasty.info(getApplicationContext(), "Market Amount is : " + marketprice, 1000, true)
                    .show();
            int deduction = price.getDeductionAmount();
            marketprice = marketprice - deduction;
            price.setKeyMarketDisplayPrice(marketprice);

            Toasty.info(getApplicationContext(), "After deduction Amount is : " + marketprice, 1000, true)
                    .show();
        } else {
            price.setKeyMarketDisplayPrice(price.getMarketPrice());
            Toasty.success(getApplicationContext(), "Final price is: " + price.getMarketPrice(), 1000, true)
                    .show();

        }


    }

    private void checkoutDialog() {

        String details = "";

        deduction_amount = 0;
        checkoutClicked = true;
        if (!flagWifi) {
            details += getString(R.string.wifi_not_working) + "\n";
            deduction_amount += 500;
        }
        if (!flagBluetooth) {
            details += getString(R.string.bluetooth_not_working) + "\n";
            deduction_amount += 500;
        }

        if (!flagLocation) {
            details += getString(R.string.location_not_working) + "\n";
            deduction_amount += 500;
        }

        if (!flagHotspot) {
            details += getString(R.string.hotspot_not_working) + "\n";
            deduction_amount += 500;
        }

        if (!flagMic) {
            details += getString(R.string.Mic_not_working) + "\n";
            deduction_amount += 500;
        }

        if (!flagBattery) {
            details += getString(R.string.charging_not_working) + "\n";
            deduction_amount += 500;

        }

        if (flagWifi && flagBluetooth && flagLocation && flagHotspot && flagMic && flagBattery) {
            details += getString(R.string.all_works_good);
        }

        final String name = "Details Dialog";
        final String finalDetails = details;
        License license = new License() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String readSummaryTextFromResources(Context context) {
                return finalDetails;
            }

            @Override
            public String readFullTextFromResources(Context context) {
                return finalDetails;
            }

            @Override
            public String getVersion() {
                return null;
            }

            @Override
            public String getUrl() {
                return null;
            }
        };
        Notice notice = new Notice(name, "", "", license);
        new LicensesDialog.Builder(EvaluatorActivity.this)
                .setNotices(notice)
                .build()
                .show();
        moduleTest.setKeyInfo(finalDetails);
        price.setDeductionAmount(deduction_amount);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, SPEECH_REQ_CODE);
        } catch (ActivityNotFoundException e) {

            Toasty.error(getApplicationContext(), "Speech not supported", 500, true).show();
        }
    }

    private void hotspot() {
        RB4.startRippleAnimation();


        if (ApManager.isApOn(EvaluatorActivity.this)) {
            RB4.stopRippleAnimation();
            Toasty.info(getApplicationContext(), "already Enabled", 500, true).show();
        } else {
            ApManager.configApState(EvaluatorActivity.this);
            IntentFilter mIntentFilter = new IntentFilter("android.net.wifi.WIFI_AP_STATE_CHANGED");
            registerReceiver(mHotspotBroadcastReceiver, mIntentFilter);
        }
    }

    private void battery() {
        RB6.startRippleAnimation();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("please First Connect the Charger")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        registerReceiver(mBatteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void location() {
        RB3.startRippleAnimation();
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGPS();
            Toasty.info(getApplicationContext(), "try to Enable Location", 500, true).show();
        } else {

            Toasty.success(getApplicationContext(), "Location is Enabled", 500, true).show();
            RB3.stopRippleAnimation();
        }
        getApplicationContext().registerReceiver(mLocationBroadcastReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

    }

    private void buildAlertMessageNoGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS Seems to be Disabled, do you want to enable it?")
                .setCancelable(false)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void bluetooth() {

        RB2.startRippleAnimation();

        if (bluetoothAdapter.isEnabled()) {
            RB2.stopRippleAnimation();
            Toasty.info(getApplicationContext(), "Bluetooth is already On", 500, true).show();
        } else {
            bluetoothAdapter.enable();
            getApplicationContext().registerReceiver(this.mBluetoothBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            Toasty.info(getApplicationContext(), "Enabling Bluetooth", 500, true).show();
        }

    }


    private void wiFi() {
        RB1.startRippleAnimation();
        if (wifiManager.isWifiEnabled()) {
            RB1.stopRippleAnimation();
            Toasty.info(getApplicationContext(), "Wifi is already enabled", 1000, true).show();
        } else {
            wifiManager.setWifiEnabled(true);
            Toasty.info(getApplicationContext(), "Enabling Wifi", 1000, true).show();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
            getApplicationContext().registerReceiver(mWifiBroadCastReceiver, intentFilter);

        }

    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);


        RB1 = (RippleBackground) findViewById(R.id.content1);
        RB2 = (RippleBackground) findViewById(R.id.content2);
        RB3 = (RippleBackground) findViewById(R.id.content3);
        RB4 = (RippleBackground) findViewById(R.id.content4);
        RB5 = (RippleBackground) findViewById(R.id.content5);
        RB6 = (RippleBackground) findViewById(R.id.content6);

        wiFi = (ImageView) findViewById(R.id.centerImage1);
        bluetooth = (ImageView) findViewById(R.id.centerImage2);
        location = (ImageView) findViewById(R.id.centerImage3);
        hotspot = (ImageView) findViewById(R.id.centerImage4);
        mic = (ImageView) findViewById(R.id.centerImage5);
        battery = (ImageView) findViewById(R.id.centerImage6);
        checkout_button = (Button) findViewById(R.id.checkout);

        flagBluetooth = false;
        flagLocation = false;
        flagWifi = false;
        flagHotspot = false;
        flagMic = false;
        flagBattery = false;

        checkmark1 = (TriangleLabelView) findViewById(R.id.checkmark1);
        checkmark2 = (TriangleLabelView) findViewById(R.id.checkmark2);
        checkmark3 = (TriangleLabelView) findViewById(R.id.checkmark3);
        checkmark4 = (TriangleLabelView) findViewById(R.id.checkmark4);
        checkmark5 = (TriangleLabelView) findViewById(R.id.checkmark5);
        checkmark6 = (TriangleLabelView) findViewById(R.id.checkmark6);
    }

    //Checking Bluetooth Background

    private BroadcastReceiver mBluetoothBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                        == BluetoothAdapter.STATE_ON) {
                    whenBluetoothEnabled();
                    context.unregisterReceiver(mBluetoothBroadcastReceiver);
                }
            }

        }
    };

    private void whenBluetoothEnabled() {
        Toasty.success(getApplicationContext(),
                " Bluetooth is Enabled",
                500,
                true)
                .show();
        RB2.stopRippleAnimation();
        flagBluetooth = true;
        moduleTest.setKeyBluetooth(flagBluetooth);
        checkmark2.setVisibility(View.VISIBLE);
    }

    //Checking  WIF Background
    private BroadcastReceiver mWifiBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int wifiExtraState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

            if (wifiExtraState == WifiManager.WIFI_STATE_ENABLED) {
                whenWifiEnabled();
                context.unregisterReceiver(mWifiBroadCastReceiver);

            }
        }
    };

    private void whenWifiEnabled() {
        Toasty.success(getApplicationContext(),
                " Wifi is Enabled",
                500,
                true)
                .show();
        RB1.stopRippleAnimation();
        flagWifi = true;
        checkmark1.setVisibility(View.VISIBLE);
        moduleTest.setKeyWifi(flagWifi);

    }

    //Checking Location in BCKGROUND

    private BroadcastReceiver mLocationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().matches(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                whenLocationEnabled();
            } else {
                Toasty.error(getApplicationContext(), "Error", 1000, true).show();
            }
            context.unregisterReceiver(mLocationBroadcastReceiver);

        }
    };

    private void whenLocationEnabled() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            Toasty.success(getApplicationContext(),
                    " Location is Enabled",
                    800,
                    true)
                    .show();
            RB3.stopRippleAnimation();
            flagLocation = true;
            moduleTest.setKeyLocation(flagLocation);
            checkmark3.setVisibility(View.VISIBLE);

        }
    }

    private final BroadcastReceiver mHotspotBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.net.wifi.WIFI_AP_STATE_CHANGED".equals(action)) {

                Toasty.success(context, "Enabled Hotspot", 500, true).show();
                context.unregisterReceiver(mHotspotBroadcastReceiver);

                RB4.stopRippleAnimation();

                flagHotspot = true;
                moduleTest.setKeyHotspot(flagHotspot);
                checkmark4.setVisibility(View.VISIBLE);

                // get Wi-Fi Hotspot state here
                int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);

                if (WifiManager.WIFI_STATE_ENABLED == state % 10) {
                    // Wifi is enabled
                    Toasty.error(context, "Not Working", 500, true).show();
                }
            }

        }
    };

    private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            if (isCharging) {
                flagBattery = true;
                moduleTest.setKeyBattery(flagBattery);
                checkmark6.setVisibility(View.VISIBLE);
                int level = intent.getIntExtra("level", 0);
                Toasty.success(getApplicationContext(), "Charging point is Working Battery is :" + level + "%", 1500, true).show();
            } else {
                Toasty.error(getApplicationContext(), "Charging point is not Connected", 1500, true).show();
            }
            context.unregisterReceiver(mBatteryInfoReceiver);
            RB6.stopRippleAnimation();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case SPEECH_REQ_CODE: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String yousaid = result.get(0);
                    if (!yousaid.isEmpty()) {
                        String test = getString(R.string.test);
                        test = test.toLowerCase();
                        if (yousaid.toLowerCase().equals(test)) {

                            Toasty.success(getApplicationContext(), "Mic is working", 500, true).show();
                            flagMic = true;
                            moduleTest.setKeyMic(flagMic);
                            Toasty.info(getApplicationContext(), "you said:- " + yousaid, 1500, true).show();

                            checkmark5.setVisibility(View.VISIBLE);

                        } else {
                            Toasty.error(getApplicationContext(), "Mic is not Working", 500, true).show();
                            Toasty.info(getApplicationContext(), "This is what you said " + yousaid, 1500, true).show();
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.evaluate_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.complete:
                doComplete();
                finish();
                return true;
            case R.id.retest:
                moduleTest.refresh();
                price.refresh();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        tutorialDialog();
    }

    public void tutorialDialog() {
        final String name = "Tutorial Dialog";
        final String finalDetails = getString(R.string.evaluation_tutorial);
        License license = new License() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public String readSummaryTextFromResources(Context context) {
                return finalDetails;
            }

            @Override
            public String readFullTextFromResources(Context context) {
                return finalDetails;
            }

            @Override
            public String getVersion() {
                return null;
            }

            @Override
            public String getUrl() {
                return null;
            }
        };
        Notice notice = new Notice(name, "", "", license);
        new LicensesDialog.Builder(EvaluatorActivity.this)
                .setNotices(notice)
                .build()
                .show();

    }
}