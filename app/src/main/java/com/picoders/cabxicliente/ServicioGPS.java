package com.picoders.cabxicliente;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import fr.quentinklein.slt.LocationTracker;
import fr.quentinklein.slt.TrackerSettings;

/**
 * Created by juancho on 13/10/16.
 */

public class ServicioGPS extends Service {
    LocationManager locationManager;
    LocationTracker tracker;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        TrackerSettings settings =
                new TrackerSettings()
                        .setUseGPS(true)
                        .setUseNetwork(true)
                        .setUsePassive(true)
                        .setTimeBetweenUpdates(5 * 1000)
                        .setMetersBetweenUpdates(100);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
         tracker = new LocationTracker(this, settings) {
            @Override
            public void onLocationFound(@NonNull Location location) {
                Intent intent = new Intent("posicion");
                intent.putExtra("latitud",location.getLatitude());
                intent.putExtra("longitud",location.getLongitude());
                LocalBroadcastManager.getInstance(ServicioGPS.this).sendBroadcast(intent);

            }

            @Override
            public void onTimeout() {

            }
        };
        tracker.startListening();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        tracker.stopListening();
    }
}
