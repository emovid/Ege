package com.emovid.ege;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

import java.util.List;
import java.util.Locale;

public class QuickCallActivity extends AppCompatActivity {
    LatLng userLocation;
    String cityName;

    SpotegeFilterableList spoteges = new SpotegeFilterableList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_call);

        // Input data
        //                      (latitude   , longitude   , TYPE read Spotege.java, name          , phone       )
        spoteges.add(new Spotege(-7.768260, 110.373915, SpotegeType.AMBULANCE, "RSUP Sardjito", "0274557111"));

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(myToolbar);
        getUserLocation();

        myToolbar.setTitle("Ege on " + cityName);
    }

    public void getUserLocation() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = service.getLastKnownLocation(provider);
        userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        Geocoder gcd = new Geocoder(this.getApplicationContext(), Locale.getDefault());
        List<Address> adrs;

        try {
            adrs = gcd.getFromLocation(userLocation.latitude, userLocation.longitude, 1);
            cityName = adrs.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("[Location latitude]", "" + userLocation.latitude);
        Log.d("[Location longitude]", "" + userLocation.longitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quick_call, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_gps) {
            getUserLocation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callPhoneNumber(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }
}
