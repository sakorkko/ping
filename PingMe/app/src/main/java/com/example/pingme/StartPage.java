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

public class StartPage extends AppCompatActivity implements OnMapReadyCallback {

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
    }

    public void goTo(double lat, double longi, int zoom){       //positions the map based on coordinates and zoom level
        LatLng spot = new LatLng(lat, longi);
        CameraUpdate upper = CameraUpdateFactory.newLatLngZoom(spot, zoom);
        myMap.moveCamera(upper);
    }
}