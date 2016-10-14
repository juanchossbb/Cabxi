package com.picoders.cabxicliente;

import android.*;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import fr.quentinklein.slt.LocationTracker;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    Usuario USUARIO;
    Intent serviciogps;
    double CURRENT_LATITUD;
    double CURRENT_LONGITUD;
    MarkerOptions ADDRESS_MARKER;
    Geocoder geocoder;
    List<Address> addresses;
    String CURRENT_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        USUARIO = new Usuario(getIntent().getExtras());
        serviciogps = new Intent(this,ServicioGPS.class);
        ADDRESS_MARKER = new MarkerOptions();
        geocoder = new Geocoder(this, Locale.getDefault());
        LocalBroadcastManager.getInstance(this).registerReceiver(GPSReceiver,
                new IntentFilter("posicion"));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private BroadcastReceiver GPSReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            CURRENT_LATITUD = intent.getDoubleExtra("latitud",0);
            CURRENT_LONGITUD=intent.getDoubleExtra("longitud",0);
            stopService(serviciogps);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(CURRENT_LATITUD,CURRENT_LONGITUD)));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            ADDRESS_MARKER.position(new LatLng(CURRENT_LATITUD,CURRENT_LONGITUD));
            ADDRESS_MARKER.icon(BitmapDescriptorFactory.fromResource(R.drawable.llamador));
            ADDRESS_MARKER.anchor(1,7);
            ADDRESS_MARKER.draggable(true);

            mMap.addMarker(ADDRESS_MARKER);
            try {
                addresses = geocoder.getFromLocation(CURRENT_LATITUD, CURRENT_LONGITUD, 1);
                String address = addresses.get(0).getAddressLine(0);
                CURRENT_ADDRESS=address;
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    };


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerDragListener(this);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // You need to ask the user to enable the permissions

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

        } else {
           startService(serviciogps);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startService(serviciogps);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        Double poslat = marker.getPosition().latitude;
        Double poslon = marker.getPosition().longitude;
        try {
            addresses = geocoder.getFromLocation(poslat, poslon, 1);
            String address = addresses.get(0).getAddressLine(0);
            CURRENT_ADDRESS=address;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
