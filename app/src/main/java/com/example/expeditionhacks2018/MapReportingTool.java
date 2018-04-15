package com.example.expeditionhacks2018;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapReportingTool extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMapClickListener {

    private View mView;
    private FloatingActionsMenu reportMenu;
    private GoogleMap map;
    private com.google.android.gms.maps.MapView mapView;
    private Location location;
    private DataRelay dataRelay;
    private DatabaseReference dbRef;
    private int reportedEventType = 0;
    private String reportDescription;
    private ArrayList<Marker> markers = new ArrayList<>();

    private MapTrackYourself.OnFragmentInteractionListener mListener;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5;

    public MapReportingTool() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map_reporting_tool, container, false);

        dataRelay = (DataRelay) getActivity().getApplicationContext();
        location = dataRelay.someLocation;

        setupReportMenu();

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);

        mapView = mView.findViewById(R.id.map2);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
        @SuppressLint("ResourceType") View locationButton = ((View) mView.findViewById(1).getParent()).findViewById(2);

        // and next place it, for exemple, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);


        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.




    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapTrackYourself.OnFragmentInteractionListener) {
            mListener = (MapTrackYourself.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setOnMapClickListener(this);
        try {
            zoomToLocation();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dbRef = FirebaseDatabase.getInstance().getReference().child("Reported Events");

        // Attach a listener to read the data at our posts reference
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Any time this function is called *ALL* of the children of the database reference are returned.
                //(In our case, literally everything). Since This will duplicate all of the map markers, remove
                //all of them from the map first.
                for(Iterator<Marker> it = markers.iterator(); it.hasNext(); ){
                    Marker m = it.next();
                    //Remove the marker from the list
                    it.remove();
                    //Remove the marker from the map
                    m.remove();
                }

                Iterable<DataSnapshot> allEvents = dataSnapshot.getChildren();
                //For every reported event
                for(DataSnapshot newReportedEvent : allEvents) {
                    //The values associated with that event
                    HashMap<String, Object> hm = (HashMap<String, Object>) newReportedEvent.getValue();

                    if(hm == null){
                        return;
                    }

                    String description = (String) hm.get("Description");
                    int eventType = ((Long) hm.get("Event Type")).intValue();
                    double latitude = (double) hm.get("Latitude");
                    double longitude = (double) hm.get("Longitude");
                    LatLng latLng = new LatLng(latitude, longitude);

                    displayPointOnMap(description, eventType, latLng);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "The read failed", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void zoomToLocation() throws InterruptedException {
        if (ActivityCompat.checkSelfPermission(mView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(mView.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);

            if (location != null) {
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(location.getLatitude(), location.getLongitude())));


            }
            else {
                requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onMapClick(final LatLng point) {
        if(reportedEventType != 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Describe What You Are Reporting.");

            final EditText input = new EditText(getActivity());
            builder.setView(input);

            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    reportDescription = input.getText().toString();
                    pushReportToDatabase(point);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    reportedEventType = 0;
                }
            });
            builder.show();
        }
    }

    private void pushReportToDatabase(LatLng point){
        //Create a new reported event
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("Latitude", point.latitude);
        hm.put("Longitude", point.longitude);
        hm.put("Description", reportDescription);
        hm.put("Event Type", reportedEventType);
        //hm.put("TimeStamp", GregorianCalendar.getInstance().getTimeInMillis());
        //hm.put("Num Times Reported", "1");
        //Marker m;

        DatabaseReference newEvent = dbRef.push();
        newEvent.setValue(hm);
        reportedEventType = 0;
    }

    private double distanceBetweenPoints(LatLng ll1, LatLng ll2){
        // Approximate Equirectangular -- works if (lat1,lon1) ~ (lat2,lon2)
        int R = 6371; // km
        double x = (ll2.longitude - ll1.longitude) * Math.cos((ll1.latitude + ll2.latitude) / 2);
        double y = (ll2.latitude - ll1.latitude);
        double distance = Math.sqrt(x * x + y * y) * R;

        return distance;
    }

    private void displayPointOnMap(String description, int eventType, LatLng point){
        int iconID;
        if(eventType == 1){
            iconID = R.drawable.transit_32;
        }else if(eventType == 2){
            iconID = R.drawable.suspicious_man_32;
        }else if(eventType == 3){
            iconID = R.drawable.warning_32;
        }else{
            return;
        }

        Marker m = map.addMarker(new MarkerOptions()
                .position(point)
                .title(description)
                .icon(BitmapDescriptorFactory.fromResource(iconID)));

        markers.add(m);
    }

    private void setupReportMenu(){
        //Transit
        //Suspicious Individual
        //General

        com.getbase.floatingactionbutton.FloatingActionButton action1 = new com.getbase.floatingactionbutton.FloatingActionButton(getActivity().getBaseContext());
        action1.setTitle("Report Activity Near Transit");
        action1.setIcon(R.drawable.transit_32);
        action1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportedEventType = 1;
                reportMenu.collapse();
                Toast.makeText(getActivity(), "Click on the map to place the marker.", Toast.LENGTH_SHORT).show();
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton action2 = new com.getbase.floatingactionbutton.FloatingActionButton(getActivity().getBaseContext());
        action2.setTitle("Report Suspicious Individual");
        action2.setIcon(R.drawable.suspicious_man_32);
        action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportedEventType = 2;
                reportMenu.collapse();
                Toast.makeText(getActivity(), "Click on the map to place the marker.", Toast.LENGTH_SHORT).show();
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton action3 = new com.getbase.floatingactionbutton.FloatingActionButton(getActivity().getBaseContext());
        action3.setTitle("Report (General)");
        action3.setIcon(R.drawable.warning_32);
        action3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportedEventType = 3;
                reportMenu.collapse();
                Toast.makeText(getActivity(), "Click on the map to place the marker.", Toast.LENGTH_SHORT).show();
            }
        });

        reportMenu = mView.findViewById(R.id.reportMenu);
        reportMenu.addButton(action1);
        reportMenu.addButton(action2);
        reportMenu.addButton(action3);
    }

}
