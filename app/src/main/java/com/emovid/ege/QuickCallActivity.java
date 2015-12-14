package com.emovid.ege;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
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

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class QuickCallActivity extends AppCompatActivity implements LocationListener {
    public static String PACKAGE_NAME;

    Toolbar myToolbar;

    LatLng userLocation;
    String cityName;

    SpotegeFilterableList spoteges = new SpotegeFilterableList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PACKAGE_NAME = getApplicationContext().getPackageName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_call);

        // Input data
        //                      (latitude   , longitude   , TYPE read Spotege.java, name          , phone       )
        spoteges.add(new Spotege(-7.770476,  110.384954,  SpotegeType.POLICE,    "Polisi Sektor Bulak Sumur ",   "0274557111"));
        spoteges.add(new Spotege(-7.669801,  110.417959,  SpotegeType.POLICE,    "Polisi Sektor Pakem",          "0274895110"));
        spoteges.add(new Spotege(-7.699376,  110.352692,  SpotegeType.POLICE,    "Polisi Sektor Turi",           "0274896705"));
        spoteges.add(new Spotege(-7.652887,  110.326593,  SpotegeType.POLICE,    "Polisi Sektor Tempel",         "0274868110"));
        spoteges.add(new Spotege(-7.697050,  110.346959,  SpotegeType.POLICE,    "Polisi Sektor Sleman",         "0274868424"));
        spoteges.add(new Spotege(-7.753918,  110.383550,  SpotegeType.POLICE,    "Polisi Sektor Ngagklik",       "0274882810"));
        spoteges.add(new Spotege(-7.771325,  110.467315,  SpotegeType.POLICE,    "Polisi Sektor Kalasan",        "0274496110"));
        spoteges.add(new Spotege(-7.734591,  110.328510,  SpotegeType.POLICE,    "Polisi Sektor Mlati",          "0274869410"));
        spoteges.add(new Spotege(-7.772230,  110.308501,  SpotegeType.POLICE,    "Polisi Sektor Godean",         "0274869110"));
        spoteges.add(new Spotege(-7.802857,  110.314030,  SpotegeType.POLICE,    "Polisi Sektor Gamping",        "0274797110"));
        spoteges.add(new Spotege(-7.7801722, 110.346321,  SpotegeType.FIRE,      "Pemadam Kebakaran Yogyakarta", "0274587101"));
        spoteges.add(new Spotege(-7.768260,  110.373915,  SpotegeType.AMBULANCE, "RSUP Sardjito",                "0274587333"));
        spoteges.add(new Spotege(-7.687639,  110.342202,  SpotegeType.AMBULANCE, "RSUD Sleman",                  "0274868437"));
        spoteges.add(new Spotege(-7.755183,  110.242355,  SpotegeType.AMBULANCE, "RSU Baktiningsih",             "0274798281"));
        spoteges.add(new Spotege(-7.772111,  110.466753,  SpotegeType.AMBULANCE, "RSU Panti Rini",               "0274496022"));
        spoteges.add(new Spotege(-7.669054,  110.417138,  SpotegeType.AMBULANCE, "RSU Panti Nugroho ",           "0274897231"));
        spoteges.add(new Spotege(-7.758593,  110.402989,  SpotegeType.AMBULANCE, "Jogja International Hospital", "02744463535"));
        spoteges.add(new Spotege(-7.662427,  110.421739,  SpotegeType.AMBULANCE, "RSJ Grhasia Pakem",            "0274895297"));
        spoteges.add(new Spotege(-7.8132229, 110.2593121, SpotegeType.SAR,       "BASARNAS Yogyakarta",          "0274587559"));

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(myToolbar);
        getUserLocation();
    }

    public void getUserLocation() {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
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
        Location location = service.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location != null && location.getTime() > Calendar.getInstance().getTimeInMillis() - 2 * 60 * 1000) {
            assignPreciseLocation(location);

            Log.d(PACKAGE_NAME, "getUserLocation().latitude :: " + userLocation.latitude);
            Log.d(PACKAGE_NAME, "getUserLocation().longitude :: " + userLocation.longitude);
        } else {
            Log.d(PACKAGE_NAME, "getUserLocation() :: " + "Location not found");
            service.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
        }
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

    private void assignPreciseLocation(Location location) {
        userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        // Get location user-friendly name
        Geocoder gcd = new Geocoder(this.getApplicationContext(), Locale.getDefault());
        List<Address> adrs;

        try {
            adrs = gcd.getFromLocation(userLocation.latitude, userLocation.longitude, 1);
            cityName = adrs.get(0).getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        myToolbar.setTitle("Ege on " + cityName);
        assignPreciseLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
