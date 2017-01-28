package com.example.pingme;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import android.location.LocationListener;



public class GpsTracker extends Activity implements LocationListener {

    private Context mCon;
    boolean GpsEnabled = false;
    boolean NetworkEnabled = false;
    boolean findMySelf = false;
    Location Loc;
    double latitude = 0;
    double longitude = 0;
    private static final long MIN_TIME = 0;
    private static final long MIN_DISTANCE = 0;

    protected LocationManager whereAmI;

    public GpsTracker(Context cont){
        this.mCon = cont;
        getLocation();
    }

    public Location getLocation(){
        try{        //First check status
            whereAmI = (LocationManager) mCon.getSystemService(LOCATION_SERVICE);
            GpsEnabled = whereAmI.isProviderEnabled(whereAmI.GPS_PROVIDER);
            NetworkEnabled = whereAmI.isProviderEnabled(whereAmI.NETWORK_PROVIDER);

            if (!GpsEnabled && !NetworkEnabled){
                Log.d("Network", "Disabled");
                this.findMySelf = false;
            }
            else{
                this.findMySelf = true;
                if (NetworkEnabled){
                    whereAmI.requestLocationUpdates(whereAmI.NETWORK_PROVIDER, MIN_TIME,
                            MIN_DISTANCE, this);
                    Log.d("Network", "Active");
                    if (whereAmI != null){
                        Loc = whereAmI.getLastKnownLocation(whereAmI.NETWORK_PROVIDER);
                        if (Loc !=null){
                            latitude = Loc.getLatitude();
                            longitude = Loc.getLongitude();
                        }
                    }
                }
                if(GpsEnabled){
                    if (Loc == null){
                        whereAmI.requestLocationUpdates(whereAmI.GPS_PROVIDER, MIN_TIME,
                                MIN_DISTANCE, this);
                        Log.d("GPS", "Active");
                        if (whereAmI != null){
                            Loc = whereAmI.getLastKnownLocation(whereAmI.GPS_PROVIDER);
                            if (Loc != null){
                                latitude = Loc.getLatitude();
                                longitude = Loc.getLongitude();
                            }
                        }
                    }
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("Error", "fatal error");
        }
        return Loc;
    }
    public void stopUsingGPS(){
        if(whereAmI != null){
            whereAmI.removeUpdates(GpsTracker.this);
        }
    }

    public double getLatitude(){
        if(Loc != null){
            latitude = Loc.getLatitude();
        }
        else if (latitude == 0){
            latitude = 65.0591924;
        }
        else{
            // return last latitude
        }
        return latitude;
    }

    public double getLongitude(){
        if(Loc != null){
            longitude = Loc.getLongitude();
        }
        else if (longitude == 0){
            longitude = 25.4662925;
        }
        else{
            // return last longitude
        }
        return longitude;
    }

    public boolean netWorkWorks(){
        return this.findMySelf;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "No way to find you", Toast.LENGTH_LONG).show();

    }


    @Override
    public void onProviderEnabled(String provider) {
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }


}
