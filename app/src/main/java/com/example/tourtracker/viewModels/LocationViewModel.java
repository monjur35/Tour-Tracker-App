package com.example.tourtracker.viewModels;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LocationViewModel extends ViewModel {
    public MutableLiveData<Location> locationMutableLiveData=new MutableLiveData<>();

    public void setNewLocation(Location location){
        locationMutableLiveData.postValue(location);
    }
}
