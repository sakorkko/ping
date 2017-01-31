package com.example.pingme;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.maps.model.LatLng;



import com.example.pingme.GpsTracker;
import com.example.pingme.MapMaker;


public class StartPage extends AppCompatActivity{

    private MapMaker mapMine;
    String[] titles = {"Hello world","Life is a set of cross-roads","Dormammu, I've come to bargain"};
    LatLng[] ll = {new LatLng(65.062766, 25.472340),new LatLng(65.055751, 25.472329),new LatLng(65.059235, 25.469904)};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesWork()){
            setContentView(R.layout.activity_start_page);
            if(!hasPermissions()){
                cansIHasPermissons();
            }
            int id = R.id.pingMap;
            mapMine = new MapMaker(StartPage.this, id, titles, ll);
        }
        else{
            // no map
        }
        Intent intent = getIntent();
        if(intent.hasExtra("Title")) {
            Bundle extras = intent.getExtras();
            String title = extras.getString("Title");
            String info = extras.getString("Info");
            String location = extras.getString("Position");
            String[] parts = location.split(":");
            String Latitude = parts[0];
            String Longitude = parts[1];
            Toast.makeText(this, "Latitude:" + Latitude + "\nLongitude:" + Longitude, Toast.LENGTH_LONG).show();
            double lat = Double.parseDouble(Latitude);
            double lon = Double.parseDouble(Longitude);
        }
    }


    public void openList(View v){
        startActivity(new Intent(StartPage.this, PingList.class));
    }
    public void openCreate(View v) { startActivity(new Intent(StartPage.this, CreatePing.class));
    }



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





    private boolean hasPermissions(){
        int result = 0;
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        for (String perms : permissions){
            result = checkCallingOrSelfPermission(perms);
            if (!(result == PackageManager.PERMISSION_GRANTED)){
                return false;
            }
        }
        return true;
    }

    private void cansIHasPermissons(){
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions, 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean granted = true;

        switch (requestCode){
            case 123:
                for (int results : grantResults){
                    granted = granted && (results == PackageManager.PERMISSION_GRANTED);
                }
            default:
                granted = false;
        }
        if (granted){
            //user gave permission
        }
        else{
            Toast.makeText(this, "I can't find you if you don't let me", Toast.LENGTH_LONG).show();
        }
    }
}