package com.example.pingme;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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


public class StartPage extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesWork()){
            setContentView(R.layout.activity_start_page);
            Toast.makeText(this, "YAY", Toast.LENGTH_LONG).show();
            makeMap();
        }
        else{
            // no map
        }


    }

    private void makeMap(){
        MapFragment frag =  (MapFragment) getFragmentManager().findFragmentById(R.id.pingMap);
        frag.getMapAsync(this);
    }

    public void openList(View v){
        startActivity(new Intent(StartPage.this, PingList.class));
    }
    public void openCreate(View v) { startActivity(new Intent(StartPage.this, CreatePing.class)); }

    public boolean googleServicesWork(){        // checks that google services are installed
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int working = api.isGooglePlayServicesAvailable(this);
        if (working == ConnectionResult.SUCCESS){
            return true;
        }
        else if (api.isUserResolvableError(working)){
            Dialog dial = api.getErrorDialog(this, working, 0);
            dial.show();
        }
        else{
            Toast.makeText(this, "I can't find play services. SAD!", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public void onMapReady(GoogleMap mapster){
        myMap = mapster;
        goTo(65.0591924, 25.466295,15);         //sets the map to oulu university.
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
        Intent i = new Intent(getApplicationContext(), PingInfo.class);
        i.putExtra("name", selected);
        startActivity (i);
    }


    public void goTo(double lat, double longi, int zoom){       //positions the map based on coordinates and zoom level
        LatLng spot = new LatLng(lat, longi);
        CameraUpdate upper = CameraUpdateFactory.newLatLngZoom(spot, zoom);
        myMap.moveCamera(upper);
    }
}