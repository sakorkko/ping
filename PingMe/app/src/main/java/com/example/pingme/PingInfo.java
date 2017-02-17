package com.example.pingme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class PingInfo extends AppCompatActivity {

    private MapMaker mapMine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_info);
        Intent in = getIntent();

        String text = in.getStringExtra(("name"));      //text for info boxes
        String info = in.getStringExtra(("info"));

        TextView box1 = (TextView)findViewById(R.id.textView);      //write to info boxes
        TextView box2 = (TextView) findViewById(R.id.textView3);
        box1.setText(text);
        box2.setText(info);

        int id = R.id.infomap;
        String pingId = in.getStringExtra("id");
        LatLng ll = PingHandler.getInstance().getLocation(pingId);
        String title = PingHandler.getInstance().getHeader(pingId);
        mapMine = new MapMaker(PingInfo.this, ll, title, id);     //creates empty map
    }
}
