package com.example.pingme;


import com.google.android.gms.maps.model.LatLng;



public class Pings {
    private String id;
    private String title;
    private String info;
    private LatLng position;

    public Pings(String name, String additional, LatLng place, Pings[] list, String ident){
        id = ident;
        title = name;
        info = additional;
        position = place;

    }

    public String getId(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getInfo(){
        return info;
    }

    public LatLng getPosition(){
        return position;
    }


}
