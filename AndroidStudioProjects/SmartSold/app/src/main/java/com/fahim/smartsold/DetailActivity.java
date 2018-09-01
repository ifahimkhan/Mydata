package com.fahim.smartsold;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fahim.smartsold.common.Common;
import com.fahim.smartsold.viewholder.ViewPagerAdapter;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    ViewPager viewPager;
    Toolbar toolbar;
    ViewPagerAdapter adapter;
    ArrayList<String> images = new ArrayList<>();

    TextView pageTitle, product_price, owner_name, email, product_location, product_mobile_no,
            product_description, fault_feature, product_date;
    Button call_owner, mail_owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
        adapter = new ViewPagerAdapter(this, images);
        viewPager.setAdapter(adapter);
        try {
            bindData();
        } catch (Exception e) {

        }

    }

    private void bindData() {
        pageTitle.setText(Common.select_properties.getItemName());
        product_price.setText("Rs. " + Common.select_properties.getItemPrice());
        owner_name.setText(Common.select_properties.getOwnerName());
        email.setText(Common.select_properties.getOwnerEmail());
        product_location.setText(Common.select_properties.getOwnerLocation());
        product_mobile_no.setText(String.valueOf(Common.select_properties.getOwnerContact()));
        product_description.setText(Common.select_properties.getItemDescription());
        fault_feature.setText(Common.select_properties.getFaulty_features());
        String date = "Date: " + DateFormat.format("dd:MM:yyyy", Common.select_properties.getTime());
        product_date.setText(date);

        call_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call();
            }
        });
        mail_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to = Common.select_properties.getOwnerEmail();
                String subject = "Regarding your Ad post on SmartSold";
                String body = "I'm Intrested in buying... \n Your " + Common.select_properties.getItemName() + "\n" +
                        Common.select_properties.getItemDescription();
                String mailTo = "mailto:" + to +
                        "?&subject=" + Uri.encode(subject) +
                        "&body=" + Uri.encode(body);
                Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                emailIntent.setData(Uri.parse(mailTo));
                startActivity(emailIntent);
            }
        });

    }

    private void call() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", String.valueOf(Common.select_properties.getOwnerContact()), null)));    }

    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Description");
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewPager);


        if (Common.select_properties.getItemImageCover() != null &&
                Common.select_properties.getItemImageCover().length() > 10) {
            images.add(Common.select_properties.getItemImageCover());
        }
        if (Common.select_properties.getUrl2() != null &&
                Common.select_properties.getUrl2().length() > 10) {
            images.add(Common.select_properties.getUrl2());
        }
        if (Common.select_properties.getUrl3() != null &&
                Common.select_properties.getUrl3().length() > 10) {
            images.add(Common.select_properties.getUrl3());
        }
        if (Common.select_properties.getUrl4() != null &&
                Common.select_properties.getUrl4().length() > 10) {
            images.add(Common.select_properties.getUrl4());
        }

        pageTitle = (TextView) findViewById(R.id.product_page_title_main_title);
        product_price = (TextView) findViewById(R.id.product_price);
        owner_name = (TextView) findViewById(R.id.owner_name);
        email = (TextView) findViewById(R.id.email);
        product_location = (TextView) findViewById(R.id.product_location);
        product_mobile_no = (TextView) findViewById(R.id.product_mobile_no);
        product_description = (TextView) findViewById(R.id.product_description);
        fault_feature = (TextView) findViewById(R.id.fault_feature);
        product_date = (TextView) findViewById(R.id.product_date);
        call_owner = (Button) findViewById(R.id.call_owner);
        mail_owner = (Button) findViewById(R.id.mail_owner);

    }


}
