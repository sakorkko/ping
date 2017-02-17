package com.example.pingme;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.pingme.Pings;

import com.google.android.gms.maps.model.LatLng;

public class PingHandler{


    private static PingHandler Instance = null;
    private Pings[] list = {};
    private Pings newest;

    private PingHandler(){
    }


    public static PingHandler getInstance(){
        if (Instance == null){
            Instance = new PingHandler();
        }
        return Instance;
    }

    public String[] getTitles(){
        String[] titles = new String[list.length];
        for (int i = 0; i<list.length; i++){
            titles[i] = list[i].getTitle();
        }
        return titles;
    }

    public String[] getInfos(){
        String[] infos = new String[list.length];
        for (int i = 0; i<list.length; i++){
            infos[i] = list[i].getInfo();
        }
        return infos;
    }

    public LatLng[] getLocations(){
        LatLng[] locations = new LatLng[list.length];
        for (int i = 0; i<list.length; i++){
            locations[i] = list[i].getPosition();
        }
        return locations;
    }
    public String getThisId(int i){
        return list[i].getId();
    }

    public void addPing(String title, String info, LatLng position, String id){
        newest = new Pings(title, info, position, list, id);
        int size;
        size = list.length;
        Pings[] spare = new Pings[size+1];        //new list that has room for the new ping;
        for(int i = 0; i< size; i++) {
            spare[i] = list[i];
        }
        spare[size] = newest;
        list = spare;

    }

    public String getInfo(String id){
        for(int i = 0; i<list.length; i++){
            if(list[i].getId().equals(id)) {
                return list[i].getInfo();
            }
        }
        return null;
    }

    public LatLng getLocation(String id){
        for(int i = 0; i<list.length; i++){
            if(list[i].getId().equals(id)) {
                return list[i].getPosition();
            }
        }
        return null;
    }

    public String getHeader(String id){
        for(int i = 0; i<list.length; i++){
            if(list[i].getId().equals(id)) {
                return list[i].getTitle();
            }
        }
        return null;
    }

    public Pings getNewest(){
        return newest;
    }

    public String getOneId(int i){
        return list[i].getId();
    }

}
