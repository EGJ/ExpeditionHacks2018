package com.example.expeditionhacks2018;

import android.app.Application;
import android.location.Location;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;

import java.util.ArrayList;
import java.util.HashMap;



public class DataRelay extends Application {
    public Location someLocation;
    public String pin = "12455";
    HashMap<LatLng, AnalysisResults> analysisResults = new HashMap<>();
    HashMap<String, Boolean> existingFences = new HashMap<>();
    //public HashMap<LatLng, ?> theMap = new HashMap<>();
}
