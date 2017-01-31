package com.example.pingme;


import com.google.android.gms.maps.model.LatLng;



public class Pings {
    private int id;
    private String title;
    private String info;
    private LatLng position;

    public Pings(String name, String additional, LatLng place, Pings[] list){
        id = generateId(list);
        title = name;
        info = additional;
        position = place;

    }

    public int getId(){
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

    private int generateId(Pings[] list){
        int length;
        if (list == null){
            return 0;
        }
        else {
            length = list.length - 1;
        }
        Pings onePing = list[length];
        int id = onePing.getId();
        id = id + 1;
        return id;
    }

}
