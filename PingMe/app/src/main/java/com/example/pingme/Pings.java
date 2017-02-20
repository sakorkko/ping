package com.example.pingme;


import com.google.android.gms.maps.model.LatLng;



public class Pings {
    private String id;
    private String title;
    private String info;
    private LatLng position;
    private String sender;
    private Long timestamp;

    public Pings(String name, String additional, LatLng place, Pings[] list, String ident, String senderid, Long timesent){
        id = ident;
        title = name;
        info = additional;
        position = place;
        sender = senderid;
        timestamp = timesent;
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

    public String getSender(){
        return sender;
    }

    public Long getTimestamp() {
        return timestamp;
    }

}
