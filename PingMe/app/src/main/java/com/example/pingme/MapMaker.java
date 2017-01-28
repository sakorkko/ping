package com.example.pingme;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.pingme.GpsTracker;

public class MapMaker extends Activity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    public GoogleMap myMap;
    private GpsTracker looker;
    private Context mCont;
    private Activity original;

    public MapMaker(Context cont){
        mCont = cont;
        looker = new GpsTracker(cont);
        original = (Activity) cont;
        MapFragment frag =  (MapFragment) original.getFragmentManager().findFragmentById(R.id.pingMap);
        frag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap mapster){
        myMap = mapster;

        if(myMap != null){
            myMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View looker = original.getLayoutInflater().inflate(R.layout.info_screen, null);

                    TextView window = (TextView) looker.findViewById(R.id.textView4);
                    window.setText(marker.getTitle());

                    return looker;
                }
            });
        }

        LatLng coordinates = new LatLng(looker.getLatitude(), looker.getLongitude());
        LatLng university = new LatLng(65.0593186, 25.4662925);
        goTo(university,15);         //sets the map to oulu university.
        Log.d("Latitude", String.valueOf(looker.getLatitude()));
        Log.d("Longitude", String.valueOf(looker.getLongitude()));
        String title1 = "Hello world";
        String title2 = "Life is a set of cross-roads";
        String title3 = "Dormammu, I've come to bargain";
        Marker marker1 = myMap.addMarker(new MarkerOptions().position(new LatLng(65.062766, 25.472340)).title(title1));
        Marker marker2 = myMap.addMarker(new MarkerOptions().position(new LatLng(65.055751, 25.472329)).title(title2));
        Marker marker3 = myMap.addMarker(new MarkerOptions().position(new LatLng(65.059235, 25.469904)).title(title3));

        marker1.setTag(title1);
        marker2.setTag(title2);
        marker3.setTag(title3);

        myMap.setOnInfoWindowClickListener(this);

    }

    public void onInfoWindowClick(Marker marker) {
        final String selected = (String) marker.getTag();
        Intent i = new Intent(mCont, PingInfo.class);
        i.putExtra("name", selected);
        mCont.startActivity (i);
    }


    public void goTo(LatLng spot, int zoom){       //positions the map based on coordinates and zoom level
        CameraUpdate upper = CameraUpdateFactory.newLatLngZoom(spot, zoom);
        myMap.moveCamera(upper);
    }

    public Marker setMark(String title){
        Marker marker = myMap.addMarker(new MarkerOptions().position(myMap.getCameraPosition().target).title(title));
        return marker;
    }


}
