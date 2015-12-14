package com.emovid.ege;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
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

import com.emovid.ege.view.CallButton;
import com.emovid.ege.ConditionalLocationManager.LocationResult;

public class QuickCallActivity extends AppCompatActivity implements LocationListener {
    public static String PACKAGE_NAME;

    // View
    Toolbar myToolbar;

    CallButton policeButton;
    CallButton ambulanceButton;
    CallButton sarButton;
    CallButton fireButton;

    // Utility class
    ConditionalLocationManager locatio;
    LocationResult locationResult;

    // Location Store
    LatLng userLocation;
    String cityName;

    SpotEgeFilterableList spoteges = new SpotEgeFilterableList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PACKAGE_NAME = getApplicationContext().getPackageName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_call);

        // Input data
        //                      (latitude   , longitude   , TYPE read SpotEge.java, name          , phone       )
        spoteges.add(new SpotEge(-7.770476,  110.384954,  SpotEgeType.POLICE,    "Polisi Sektor Bulak Sumur ",   "0274557111"));
        spoteges.add(new SpotEge(-7.669801,  110.417959,  SpotEgeType.POLICE,    "Polisi Sektor Pakem",          "0274895110"));
        spoteges.add(new SpotEge(-7.699376,  110.352692,  SpotEgeType.POLICE,    "Polisi Sektor Turi",           "0274896705"));
        spoteges.add(new SpotEge(-7.652887, 110.326593, SpotEgeType.POLICE, "Polisi Sektor Tempel", "0274868110"));
        spoteges.add(new SpotEge(-7.697050,  110.346959,  SpotEgeType.POLICE,    "Polisi Sektor Sleman",         "0274868424"));
        spoteges.add(new SpotEge(-7.753918,  110.383550,  SpotEgeType.POLICE,    "Polisi Sektor Ngagklik",       "0274882810"));
        spoteges.add(new SpotEge(-7.771325,  110.467315,  SpotEgeType.POLICE,    "Polisi Sektor Kalasan",        "0274496110"));
        spoteges.add(new SpotEge(-7.734591,  110.328510,  SpotEgeType.POLICE,    "Polisi Sektor Mlati",          "0274869410"));
        spoteges.add(new SpotEge(-7.772230,  110.308501,  SpotEgeType.POLICE,    "Polisi Sektor Godean",         "0274869110"));
        spoteges.add(new SpotEge(-7.802857,  110.314030,  SpotEgeType.POLICE,    "Polisi Sektor Gamping",        "0274797110"));
        spoteges.add(new SpotEge(-7.7801722, 110.346321,  SpotEgeType.FIRE,      "Pemadam Kebakaran Yogyakarta", "0274587101"));
        spoteges.add(new SpotEge(-7.768260,  110.373915,  SpotEgeType.AMBULANCE, "RSUP Sardjito",                "0274587333"));
        spoteges.add(new SpotEge(-7.687639,  110.342202,  SpotEgeType.AMBULANCE, "RSUD Sleman",                  "0274868437"));
        spoteges.add(new SpotEge(-7.755183,  110.242355,  SpotEgeType.AMBULANCE, "RSU Baktiningsih",             "0274798281"));
        spoteges.add(new SpotEge(-7.772111,  110.466753,  SpotEgeType.AMBULANCE, "RSU Panti Rini",               "0274496022"));
        spoteges.add(new SpotEge(-7.669054,  110.417138,  SpotEgeType.AMBULANCE, "RSU Panti Nugroho ",           "0274897231"));
        spoteges.add(new SpotEge(-7.758593,  110.402989,  SpotEgeType.AMBULANCE, "Jogja International Hospital", "02744463535"));
        spoteges.add(new SpotEge(-7.662427,  110.421739,  SpotEgeType.AMBULANCE, "RSJ Grhasia Pakem",            "0274895297"));
        spoteges.add(new SpotEge(-7.8132229, 110.2593121, SpotEgeType.SAR,       "BASARNAS Yogyakarta",          "0274587559"));

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);

        locationResult = new LocationResult(){
            @Override
            public void gotLocation(Location location) {
                assignPreciseLocation(location);
                Log.d(PACKAGE_NAME, "gotLocation().city-name :: " + cityName);
                if (cityName == "" || cityName == null) {
                    myToolbar.setTitle("Ege");
                } else {
                    myToolbar.setTitle("Ege on " + cityName);
                }
                assignPhoneNumbers();
            }
        };
        locatio = new ConditionalLocationManager();

        setSupportActionBar(myToolbar);
        locatio.getLocation(this, locationResult);
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
            locatio.getLocation(this, locationResult);
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
            Log.d(PACKAGE_NAME, "assignPreciseLocation().address :: " + adrs.get(0).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void assignPhoneNumbers() {
        // For testing purpose
        // double fakeLatitude = -7.7801722;
        // double fakeLongitude = 110.384954;
        // SpotEge nearestPolice = spoteges.nearestSpotEge(fakeLatitude, fakeLongitude, SpotEgeType.POLICE);

        SpotEge nearestPolice = spoteges.nearestSpotEge(userLocation.latitude, userLocation.longitude, SpotEgeType.POLICE);
        policeButton.setPhoneNumber(nearestPolice.getPhone());
        Log.d(PACKAGE_NAME, "assignPhoneNumbers().police-phone :: " + nearestPolice.getPhone());

        SpotEge nearestAmbulance = spoteges.nearestSpotEge(userLocation.latitude, userLocation.longitude, SpotEgeType.AMBULANCE);
        ambulanceButton.setPhoneNumber(nearestAmbulance.getPhone());
        Log.d(PACKAGE_NAME, "assignPhoneNumbers().ambulance-phone :: " + nearestAmbulance.getPhone());

        SpotEge nearestSar = spoteges.nearestSpotEge(userLocation.latitude, userLocation.longitude, SpotEgeType.SAR);
        sarButton.setPhoneNumber(nearestSar.getPhone());
        Log.d(PACKAGE_NAME, "assignPhoneNumbers().sar-phone :: " + nearestSar.getPhone());

        SpotEge nearestFire = spoteges.nearestSpotEge(userLocation.latitude, userLocation.longitude, SpotEgeType.FIRE);
        fireButton.setPhoneNumber(nearestFire.getPhone());
        Log.d(PACKAGE_NAME, "assignPhoneNumbers().fire-phone :: " + nearestFire.getPhone());
    }

    @Override
    public void onLocationChanged(Location location) {
        locationResult.gotLocation(location);
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
