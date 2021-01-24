package com.example.tourtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tourtracker.viewModels.LocationViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private LocationViewModel locationViewModel;
    private FusedLocationProviderClient providerClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        providerClient = LocationServices.getFusedLocationProviderClient(this);


        if (isPermissionGranted()) {
            getUserCurrentLocation();
        } else {
            requestLocationPermissionFromUser();
        }

    }

    public boolean isPermissionGranted() {
        Log.e("debugg", "onSuccess: isPermissionGranted ");

        return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    public void requestLocationPermissionFromUser() {
        final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissions(permissions, 111);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getUserCurrentLocation();
            Log.e("debugg", "onSuccess: onRequestPermissionsResult ");


        } else {
            Toast.makeText(this, "Denied by user", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserCurrentLocation() {
        Log.e("debugg", "onSuccess: getUserCurrentLocation ");

            providerClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location == null) {
                        Log.e("debugg", "onfail: location :"+location.getLatitude() +"      "+location.getLongitude());
                        return;
                    } else {
                        locationViewModel.setNewLocation(location);
                        Log.e("debugg", "onSuccess: location");
                    }
                }
            });



    }

}