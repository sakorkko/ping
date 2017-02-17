package com.example.pingme;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.pingme.GpsTracker;

public class MapMaker extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap myMap;
    private GpsTracker looker;
    private Context mCont;
    private Activity original;
    private LatLng[] lats;
    private String[] titles;
    private LatLng coordinates;
    private boolean clickable = true;
    private String infoTitle;
    private String myId;

    public MapMaker(Context cont, boolean draw, int id){
        mCont = cont;
        looker = new GpsTracker(cont);
        original = (Activity) cont;
        if(draw) {
            lats = PingHandler.getInstance().getLocations();
            titles = PingHandler.getInstance().getTitles();
        }
        else {
            lats = new LatLng[0];
            titles = new String[0];
        }
        MapFragment frag =  (MapFragment) original.getFragmentManager().findFragmentById(id);
        frag.getMapAsync(this);

    }

    public MapMaker(Context cont, LatLng coordinate, String title, int id, String pingId){           //constructor for ping info activity
        mCont = cont;
        myId = pingId;
        looker = new GpsTracker(cont);
        original = (Activity) cont;
        lats = new LatLng[0];
        titles = new String[0];
        infoTitle = title;
        coordinates = coordinate;
        clickable = false;
        MapFragment frag =  (MapFragment) original.getFragmentManager().findFragmentById(id);
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
        looker.getLocation();
        if (coordinates == null) {
            coordinates = new LatLng(looker.getLatitude(), looker.getLongitude());
        }
        else{
            setMarkThere(infoTitle, myId, coordinates);
        }
        LatLng university = new LatLng(65.0593186, 25.4662925);     // university coordinates
        goTo(coordinates,15);         //sets the map current position


        Log.d("Latitude", String.valueOf(looker.getLatitude()));
        Log.d("Longitude", String.valueOf(looker.getLongitude()));

        for (int i = 0; i < titles.length; i++ ){
            setMarkThere(titles[i], String.valueOf(i), lats[i]);
        }

        if(clickable) {         //if map is on ping info activity, info click shouldn't work
            myMap.setOnInfoWindowClickListener(this);
        }
        Log.d("MapStatus", "ready");
    }


    public void onInfoWindowClick(Marker marker) {      //when clicking on marker info box
        final String selected = (String) marker.getTag();
        Intent i = new Intent(mCont, PingInfo.class);
        i.putExtra("name", selected);
        String snippet = marker.getSnippet();
        i.putExtra("id", snippet);
        snippet = PingHandler.getInstance().getInfo(snippet);
        i.putExtra("info", snippet);
        mCont.startActivity (i);
    }


    public void goTo(LatLng spot, int zoom){       //positions the map based on coordinates and zoom level
        CameraUpdate upper = CameraUpdateFactory.newLatLngZoom(spot, zoom);
        myMap.moveCamera(upper);
    }

    public void setMark(String title){      //set marker on the camerea position
        Marker marker = myMap.addMarker(new MarkerOptions().position(myMap.getCameraPosition().target).title(title));
        marker.setTag(title);
    }

    public void setMarkThere(String title, String id,LatLng ll){      //set marker on specific coordinates
        Marker marker =  myMap.addMarker(new MarkerOptions().position(ll).title(title).snippet(id));
        marker.setTag(title);
    }

    public LatLng getPosition(){
        return myMap.getCameraPosition().target;
    }

    public LatLng getGps(){
        return new LatLng(looker.getLatitude(), looker.getLongitude());
    }



}
