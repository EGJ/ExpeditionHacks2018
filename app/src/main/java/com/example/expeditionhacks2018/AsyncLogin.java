package com.example.expeditionhacks2018;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Greg on 3/3/18.
 */

public class AsyncLogin extends AsyncTask<String, Integer, Location> {
    DataRelay dataRelay;
    Context ctx;
    AsyncLoginDelegate delegate;


    public AsyncLogin(Context ctx)
    {
        this.ctx = ctx;
      dataRelay =  (DataRelay) ctx.getApplicationContext();
    }

    @Override
    protected void onPreExecute() {

    }


    @Override
    protected Location doInBackground(String... strings) {
        Location location = getLastKnownLocation();
        dataRelay.someLocation = location;
        return location;
    }

    @Override
    protected void onPostExecute(Location location) {
        delegate.processFinish(location);
    }
    public Location getLastKnownLocation() {

        Location bestLocation = null;
        if (ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager mLocationManager = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);

            mLocationManager = (LocationManager) ctx.getSystemService(LOCATION_SERVICE);

            List<String> providers = mLocationManager.getProviders(true);
            for (String provider : providers) {
                Location l = mLocationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    // Found best last known location: %s", l);
                    bestLocation = l;
                }
            }
        }
        return bestLocation;
    }



}
