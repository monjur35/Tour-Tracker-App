package com.example.tourtracker.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tourtracker.GeofenceBroadcastReceiver;
import com.example.tourtracker.R;
import com.example.tourtracker.viewModels.LocationViewModel;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements GoogleMap.OnMapLongClickListener {

    private GoogleMap map;
    private LocationViewModel locationViewModel;
    private GeofencingClient geofencingClient;
    private List<Geofence> geofenceList=new ArrayList<>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {

            map = googleMap;

            map.setMyLocationEnabled(true);
            map.setOnMapLongClickListener(MapsFragment.this);

            locationViewModel.locationMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Location>() {
                @Override
                public void onChanged(Location location) {
                    Log.e("debugg","location initialized");
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions marker=new MarkerOptions();
                    //marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));


                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 18f));
                }
            });





        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        geofencingClient = LocationServices.getGeofencingClient(getActivity());
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        createGeofence(latLng);
    }

    private void createGeofence(LatLng latLng) {
        map.addMarker(new MarkerOptions().position(latLng).title("Destination"));
        geofenceList.add(new Geofence.Builder()
                .setRequestId("Location")
                .setCircularRegion(latLng.latitude, latLng.longitude, 100)
                .setExpirationDuration(12 * 60 * 60 * 1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
           
            return;
        }
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(), "Destination Area Selected", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private GeofencingRequest getGeofencingRequest(){
        GeofencingRequest.Builder builder=new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();

    }
    private PendingIntent getGeofencePendingIntent(){
        PendingIntent geofencingPendingIntent=null;
        if (geofencingPendingIntent!=null){
            return geofencingPendingIntent;
        }
        Intent intent=new Intent(getActivity(), GeofenceBroadcastReceiver.class);
        geofencingPendingIntent=PendingIntent.getBroadcast(getActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencingPendingIntent;
    }
}