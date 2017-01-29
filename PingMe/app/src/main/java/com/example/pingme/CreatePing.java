package com.example.pingme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.pingme.MapMaker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class CreatePing extends AppCompatActivity {

    private MapMaker mapMine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ping);
        int id = R.id.creatorMap;
        String[] empty = {};
        LatLng[] empty2 = {};
        mapMine = new MapMaker(CreatePing.this, id, empty, empty2);
    }

    public void openStart(View v){ startActivity(new Intent(CreatePing.this, StartPage.class)); }
    public void openList(View v){startActivity(new Intent(CreatePing.this, PingList.class));
    }

    public void mark(View v){
        String title1 = "MINE";
        mapMine.setMark(title1);
        startActivity(new Intent(CreatePing.this, pop_up.class));

    }
}
