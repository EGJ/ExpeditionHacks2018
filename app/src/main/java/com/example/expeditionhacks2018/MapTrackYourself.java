package com.example.expeditionhacks2018;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapView;
import com.ramotion.fluidslider.FluidSlider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

import static android.content.ContentValues.TAG;
import static android.content.Context.LOCATION_SERVICE;
import static android.view.View.GONE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapTrackYourself.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapTrackYourself#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapTrackYourself extends Fragment implements OnMapReadyCallback, PlaceSelectionListener, GoogleMap.OnMyLocationButtonClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapTrackYourself() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapTrackYourself.
     */
    // TODO: Rename and change types and number of parameters
    public static MapTrackYourself newInstance(String param1, String param2) {
        MapTrackYourself fragment = new MapTrackYourself();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {

                Snackbar.make(getActivity().findViewById(android.R.id.content),
                        "Please Grant Permissions to upload profile photo",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                            new String[]{Manifest.permission
                                                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                                            2);
                                }
                            }
                        }).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                            2);
                }
            }
        } else {
            // write your logic code if permission already granted
        }
    }


    private View mView;
    private Location location;
    private DataRelay dataRelay;
    private GoogleMap map;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 5;
    private com.google.android.gms.maps.MapView mapView;
    private boolean didSelectStart = false;
    private boolean didselectDestination = false;
    private boolean didSelectPrepareRouteFAB = false;
    private RelativeLayout destinationPicker;
    private FloatingActionButton startRouteFab;
    private EditText startET;
    private EditText destinationET;
    private TextInputLayout startIET;
    private TextInputLayout destIET;
    PlaceAutocompleteFragment autocompleteFragment;
    private ImageButton sendIcon;
    private String startingAddress;
    private String destinationAddress;
    private LatLng startLatLong;
    private LatLng destinationLatLong;
    private ImageButton locateMe;
    private ArrayList<LatLng> markers;
    private int currentRadius = 1;
    private ImageButton exitButton;
    private FluidSlider fluidSlider;
    private FluidSlider timeSlider;
    private RelativeLayout radiusContainer;
    private HashMap<pointGeoFenceData, Integer> markerTimeMap = new HashMap<>();
    private int timeDuration;
    private ImageButton checkMark;
    private GoogleApiClient googleApiClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.fragment_map_track_yourself, container, false);
        dataRelay = (DataRelay) getActivity().getApplicationContext();
        location = dataRelay.someLocation;
        startET = mView.findViewById(R.id.toEdit);
        destinationET = mView.findViewById(R.id.destinationEdit);
        startIET = mView.findViewById(R.id.toTextInputLayer);
        destIET = mView.findViewById(R.id.destinationTextInputLayer);
        sendIcon = mView.findViewById(R.id.sendIcon);
        startRouteFab = mView.findViewById(R.id.startFAB);
        locateMe = mView.findViewById(R.id.locateMe);
        markers = new ArrayList<>();
        exitButton = mView.findViewById(R.id.exitButton);
        fluidSlider = mView.findViewById(R.id.fluidSlider);
        radiusContainer = mView.findViewById(R.id.sliderContainer);
        timeSlider = mView.findViewById(R.id.timeSlider);
        int min = 1;
        int max = 5;
        final int total = max - min;
        checkMark = mView.findViewById(R.id.checkbox);

        int minTime = 10;
        int maxTime = 60;
        int timeTotal = maxTime - minTime;

        fluidSlider.setStartText(String.valueOf(min));
        fluidSlider.setEndText(String.valueOf(max));
        fluidSlider.setBubbleText("5");

        createGoogleApi();
        googleApiClient.connect();


        checkMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //iterate through the the the markers, set up geo fence, and timer events for the police API
                //and save this information to their map so we can show this route and pins on the map.

                for (pointGeoFenceData someLocation : markerTimeMap.keySet())
                {

                    String key = UUID.randomUUID().toString();
                    float theRadius = someLocation.radius;
                    if (theRadius == 0.0)
                    {
                        theRadius = 500;
                    }
                    Geofence geofence = createGeofence(someLocation.somePoint, theRadius, key , markerTimeMap.get(someLocation));
                    dataRelay.existingFences.put(key, false);
                    GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
                    addGeofence( geofenceRequest );




                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.MINUTE,markerTimeMap.get(someLocation));

                    Date a=new Date();
                    //add the time of the event to a calendar and then pass the calendar into the alarm manager
                    a.setTime(System.currentTimeMillis()+(markerTimeMap.get(someLocation)*60*1000));// 11:59 PM
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, a.getHours());
                    calendar.set(Calendar.MINUTE, a.getMinutes());
                    calendar.set(Calendar.SECOND, a.getSeconds());
                    Intent intent = new Intent(getActivity(), TimedEvents.class);
                    intent.putExtra("UUID", key);
                    PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi);



                }
                Toast.makeText(getActivity(), "Geo-fencing enabled.", Toast.LENGTH_SHORT).show();




                fadeOutView(radiusContainer);
                radiusContainer.animate();
                radiusContainer.setVisibility(View.GONE);

                fadeOutView(exitButton);
                exitButton.animate();
                exitButton.setVisibility(View.GONE);

                fadeInView((startRouteFab));
                startRouteFab.animate();
                startRouteFab.setVisibility(View.VISIBLE);

            }
        });


        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radiusContainer.getVisibility() == View.VISIBLE) {
                    fadeOutView(radiusContainer);
                    radiusContainer.animate();
                    radiusContainer.setVisibility(View.GONE);
                } else if (destinationPicker.getVisibility() == View.VISIBLE) {
                    fadeOutView(destinationPicker);
                    destinationPicker.animate();
                    destinationPicker.setVisibility(View.GONE);
                }
                map.setOnMapClickListener(null);


                fadeOutView(exitButton);
                exitButton.animate();
                exitButton.setVisibility(View.GONE);

                fadeInView((startRouteFab));
                startRouteFab.animate();
                startRouteFab.setVisibility(View.VISIBLE);
            }
        });


        timeSlider.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        // TODO Auto-generated method stub
                        markers.add(point);
                        //we get whatever the position is, and that percentage of 60.
                        currentRadius = (int) (fluidSlider.getPosition() * 5);
                        pointGeoFenceData pointGeoFenceData = new pointGeoFenceData();
                        pointGeoFenceData.radius = currentRadius;
                        pointGeoFenceData.somePoint = point;
                        markerTimeMap.put(pointGeoFenceData, (int) (timeSlider.getPosition() * 60));

                        map.addMarker(new MarkerOptions().position(point)
                                .title("" + (int) (timeSlider.getPosition() * 60)));
                        CircleOptions circle = new CircleOptions()
                                .center(new LatLng(point.latitude, point.longitude))
                                .radius(currentRadius * 800)
                                .strokeColor(Color.parseColor("#ce1a65"))
                                .fillColor(Color.parseColor("#FF42C6B6"));
                        map.addCircle(circle);
                    }
                });
                return Unit.INSTANCE;
            }
        });

        fluidSlider.setPositionListener(pos -> {
            final String value = String.valueOf((int) (min + total * pos));
            fluidSlider.setBubbleText(value);
            return Unit.INSTANCE;
        });


        timeSlider.setPositionListener(pos -> {
            final String value = String.valueOf((int) (minTime + timeTotal * pos));
            timeSlider.setBubbleText(value);
            return Unit.INSTANCE;
        });


        fluidSlider.setEndTrackingListener(new Function0<Unit>() {
            @Override
            public Unit invoke() {
                map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        // TODO Auto-generated method stub
                        markers.add(point);

                        currentRadius = (int) (fluidSlider.getPosition() * 5);
                        pointGeoFenceData pointGeoFenceData = new pointGeoFenceData();
                        pointGeoFenceData.radius = currentRadius;
                        pointGeoFenceData.somePoint = point;
                        markerTimeMap.put(pointGeoFenceData, (int) (timeSlider.getPosition() * 60));
                        map.addMarker(new MarkerOptions().position(point)
                                .title("" + (int) (timeSlider.getPosition() * 60)));
                        Circle circle = map.addCircle(new CircleOptions()
                                .center(new LatLng(point.latitude, point.longitude))
                                .radius(currentRadius * 800)
                                .visible(true)
                                .strokeColor(Color.parseColor("#ce1a65"))
                                .fillColor(Color.parseColor("#FF42C6B6")));

                    }
                });


                return Unit.INSTANCE;
            }
        });


        startET.setKeyListener(null);
        destinationET.setKeyListener(null);

        locateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location myLocation = getLastKnownLocation();
                startLatLong = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                startET.setText(myLocation.toString());

            }
        });


        autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        startRouteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                didSelectStart = true;
                //fade out the FAB
                Animation fadeOut = new AlphaAnimation(1.0F, 0.0F);
                fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                fadeOut.setStartOffset(100);
                fadeOut.setDuration(1000);
                startRouteFab.setVisibility(View.INVISIBLE);

                startRouteFab.setAnimation(fadeOut);
                startRouteFab.animate();

                Animation fadeIn = new AlphaAnimation(0.0F, 1.0F);
                fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
                fadeIn.setStartOffset(500);
                fadeIn.setDuration(1000);


                //fade in the fragent
                destinationPicker = mView.findViewById(R.id.destinationPicker);
                destinationPicker.setAnimation(fadeIn);
                destinationPicker.animate();
                destinationPicker.setVisibility(View.VISIBLE);
                fadeIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        destinationPicker.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startRouteFab.setVisibility(GONE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                fadeInView(exitButton);
                exitButton.animate();
                exitButton.setVisibility(View.VISIBLE);

            }
        });


        sendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we take both the start point and end destination and compute the distance
                if (startET.getText().toString().length() > 0) {
                    startingAddress = startET.getText().toString();
                    startET.setText("");
                } else {
                    Toast.makeText(getActivity(), "Please add a starting point", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (destinationET.getText().toString().length() > 0) {
                    destinationAddress = destinationET.getText().toString();
                    destinationET.setText("");
                } else {
                    Toast.makeText(getActivity(), "Please add a destination", Toast.LENGTH_SHORT).show();
                    return;
                }

                //call the api


                GoogleDirection.withServerKey("AIzaSyD8jBnE82B6dtbnyvmcv4iqYPYeJtf3Xs4")
                        .from(startLatLong)
                        .to(destinationLatLong)
                        .transportMode(TransportMode.DRIVING)

                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction, String rawBody) {
                                if (direction.isOK()) {
                                    //if its okay, show the preview and allow to select custom points for geofence or
                                    //our leg-by-leg geofence.
                                    Route route = direction.getRouteList().get(0);
                                    Leg leg = route.getLegList().get(0);
                                    ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                    PolylineOptions polylineOptions = DirectionConverter.createPolyline(getActivity(), directionPositionList, 5, Color.RED);
                                    map.addPolyline(polylineOptions);

                                    ArrayList<LatLng> pointList = new ArrayList<>();

                                    for (Leg someLeg : route.getLegList()) {
                                        ArrayList<LatLng> directionPoints = someLeg.getDirectionPoint();
                                        ArrayList<LatLng> sectionPoints = someLeg.getSectionPoint();
                                        pointList.addAll(sectionPoints);

                                    }

                                    zoomRoute(map, pointList);


                                    // Do something
                                } else {
                                    // Do something
                                }
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {
                                // Do something
                            }
                        });


                didSelectStart = false;
                didselectDestination = false;


                Animation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                fadeOut.setStartOffset(1000);
                fadeOut.setDuration(1000);


                //fade in the fragent
                destinationPicker = mView.findViewById(R.id.destinationPicker);
                destinationPicker.setAnimation(fadeOut);
                destinationPicker.animate();
                destinationPicker.setVisibility(GONE);

                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        destinationPicker.setVisibility(GONE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });


        startET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                didSelectStart = true;
                didselectDestination = false;

                final View root = autocompleteFragment.getView();
                root.post(new Runnable() {
                    @Override
                    public void run() {
                        root.findViewById(R.id.place_autocomplete_search_input)
                                .performClick();
                    }
                });


            }
        });


        destinationET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                didSelectStart = false;
                didselectDestination = true;

                final View root = autocompleteFragment.getView();
                root.post(new Runnable() {
                    @Override
                    public void run() {
                        root.findViewById(R.id.place_autocomplete_search_input)
                                .performClick();
                    }
                });


            }
        });


// Inflate the layout for this fragment
        return mView;


    }

    public void fadeInView(View v) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(1000);
        fadeIn.setStartOffset(500);


        v.setAnimation(fadeIn);
    }

    public void fadeOutView(View v) {
        //
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setDuration(1000);
        fadeOut.setDuration(200);


        v.setAnimation(fadeOut);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        mapView = mView.findViewById(R.id.map1);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        startLocationUpdates();

        @SuppressLint("ResourceType") View locationButton = ((View) mView.findViewById(1).getParent()).findViewById(2);
        // and next place it, for exemple, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);


        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public void onPlaceSelected(Place place) {

        if (didSelectStart || didselectDestination) {
            if (didSelectStart) {
                startET.setText(place.getAddress());
                startLatLong = place.getLatLng();
            } else {
                destinationET.setText(place.getAddress());
                destinationLatLong = place.getLatLng();
            }
        } else {
            location.setLatitude(place.getLatLng().latitude);
            location.setLongitude(place.getLatLng().longitude);
            try {
                zoomToLocation();
            } catch (InterruptedException e) {
                e.printStackTrace();

            }
        }


    }

    @Override
    public void onError(Status status) {

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
        map.getUiSettings().setCompassEnabled(false);
        try {
            zoomToLocation();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        CameraPosition somePosition = CameraPosition.builder().target(new LatLng(41.316324, -72.922343)).zoom(16).bearing(0).tilt(45).build();
//        map.moveCamera(CameraUpdateFactory.newCameraPosition(somePosition));

//        LatLng pp = new LatLng();

    }


    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
        markers.clear();

        fadeOutView(destinationPicker);
        destinationPicker.animate();
        destinationPicker.setVisibility(GONE);

        fadeInView(radiusContainer);
        radiusContainer.animate();
        radiusContainer.setVisibility(View.VISIBLE);


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                markers.add(point);
                map.addMarker(new MarkerOptions().position(point));
                Circle circle = map.addCircle(new CircleOptions()
                        .center(new LatLng(point.latitude, point.longitude))
                        .radius(10)
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE));
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


            } else {
                requestPermissions(
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    public Location getLastKnownLocation() {

        Location bestLocation = null;
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

            mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

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

    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius, String Geo_Id, long duration) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(Geo_Id)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius*1600)
                .setExpirationDuration(duration)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

//    private void startGeofence() {
//        Log.i(TAG, "startGeofence()");
//            Geofence geofence = createGeofence( geoFenceMarker.getPosition(), GEOFENCE_RADIUS );
//            GeofencingRequest geofenceRequest = createGeofenceRequest( geofence );
//            addGeofence( geofenceRequest );
//
//    }

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        Intent intent = new Intent(getActivity(), GeofenceTrasitionService.class);
        return PendingIntent.getService(
                getActivity(), GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(
                googleApiClient,
                request,
                createGeofencePendingIntent()
        );
    }







    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL =  1000;
    private final int FASTEST_INTERVAL = 900;

    // Start location Updates
    private void startLocationUpdates(){
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        LocationManager mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,100,this);



        checkPermission();



    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("location changed to: " + location.toString());

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

    private class pointGeoFenceData {
        LatLng somePoint;
        float radius;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
