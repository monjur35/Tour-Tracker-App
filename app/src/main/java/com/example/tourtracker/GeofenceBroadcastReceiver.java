package com.example.tourtracker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        GeofencingEvent geofencingEvent=GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()){
            String broadcastError= GeofenceStatusCodes.getStatusCodeString(geofencingEvent.getErrorCode());
            Toast.makeText(context, ""+broadcastError, Toast.LENGTH_SHORT).show();
            return;
        }

        int geofenceTransition=geofencingEvent.getGeofenceTransition();
        String transitionType="";
        if (geofenceTransition== Geofence.GEOFENCE_TRANSITION_ENTER){
            transitionType="Entered";

        }else if (geofenceTransition==Geofence.GEOFENCE_TRANSITION_EXIT){
            transitionType="Exit";
            GoogleMap map;

        }
        List<String> names=new ArrayList<>();
        List<Geofence>triggeringGeofences=geofencingEvent.getTriggeringGeofences();

        for (Geofence g :triggeringGeofences){
            names.add(g.getRequestId());
        }
        String nameString= TextUtils.join(",",names);
        sendNotificationToUser( context,transitionType,nameString);




    }

    private void sendNotificationToUser(Context context ,String transitionType, String nameString) {
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"Channel01");
        builder.setSmallIcon(R.drawable.ic_baseline_notifications_active_24);
        builder.setContentTitle("Tour Tracker");
        builder.setContentText(transitionType+" "+nameString);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);


        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name="Geofence Channel";
            String description="This channel sends geofence notification";
            int importace=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel geofenceChannel=new NotificationChannel("Channel01",name,importace);
            geofenceChannel.setDescription(description);
            notificationManager.createNotificationChannel(geofenceChannel);

        }
        notificationManager.notify(111,builder.build());
    }
}