package com.example.pingme;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.Manifest;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.maps.model.LatLng;

import com.example.pingme.MapMaker;
import com.example.pingme.Pings;
import com.example.pingme.PingHandler;


public class StartPage extends AppCompatActivity{

    private MapMaker mapMine;
    PingHandler pingHandler = PingHandler.getInstance();
    private static final String TAG = "StartPage";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = R.id.pingMap;
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("pingReceiver"));

        FirebaseMessaging.getInstance().subscribeToTopic("pings");
        if (googleServicesWork()){
            setContentView(R.layout.activity_start_page);
            if(!hasPermissions()){              //checks gps permissions
                cansIHasPermissons();
            }

            mapMine = new MapMaker(StartPage.this, true, id);     //sets up map
        }
        else{
            // no map
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String pingTitle = intent.getStringExtra("title");
            String pingBody = intent.getStringExtra("body");
            String pingLocation = intent.getStringExtra("location");
            Log.d(TAG, "ping received: \n" + "title: " + pingTitle + " body: " + pingBody);
            String[] latlong =  pingLocation.split(",");
            double pingLatitude = Double.parseDouble(latlong[0]);
            double pingLongitude = Double.parseDouble(latlong[1]);

            LatLng pingPosition = new LatLng(pingLatitude, pingLongitude);

            PingHandler.getInstance().addPing(pingTitle, pingBody, pingPosition);
            mapMine.setMarkThere(pingTitle, PingHandler.getInstance().getNewest().getId(), pingPosition);
        }
    };


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





    private boolean hasPermissions(){       //gps permissions
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

    private void cansIHasPermissons(){      //asks user for gps permissions
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions, 123);
        }
    }

    @Override       //leads from canIhasPermissions. handels the results of the permission request
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

    public void center(View v){
        mapMine.goTo(mapMine.getGps(), 15);
    }
}