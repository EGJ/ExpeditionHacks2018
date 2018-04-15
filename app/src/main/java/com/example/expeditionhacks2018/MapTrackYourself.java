package com.example.expeditionhacks2018;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapTrackYourself.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapTrackYourself#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapTrackYourself extends Fragment implements OnMapReadyCallback, PlaceSelectionListener, GoogleMap.OnMyLocationButtonClickListener {
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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.fragment_map_track_yourself, container, false);
        dataRelay = (DataRelay) getActivity().getApplicationContext();
        location = dataRelay.someLocation;







// Inflate the layout for this fragment
        return mView;



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);

        mapView = mView.findViewById(R.id.map1);
        mapView.onCreate(null);
        mapView.onResume();
        mapView.getMapAsync(this);
        PlaceAutocompleteFragment autocompleteFragment  = (PlaceAutocompleteFragment)getActivity().getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
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
        try {
            zoomToLocation();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        CameraPosition somePosition = CameraPosition.builder().target(new LatLng(41.316324, -72.922343)).zoom(16).bearing(0).tilt(45).build();
//        map.moveCamera(CameraUpdateFactory.newCameraPosition(somePosition));

//        LatLng pp = new LatLng();

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
