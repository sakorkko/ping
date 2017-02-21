package com.example.pingme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PingInfo extends AppCompatActivity {

    private MapMaker mapMine;
    private String pingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_info);
        Intent in = getIntent();

        String text = in.getStringExtra(("name"));      //text for info boxes
        String info = in.getStringExtra(("info"));
//        Log.d("Pinginfo", info);

        TextView box1 = (TextView)findViewById(R.id.textView);      //write to info boxes
        TextView box2 = (TextView) findViewById(R.id.textView3);
        box1.setText(text);
        box2.setText(info);

        int id = R.id.infomap;
        pingId = in.getStringExtra("id");
        LatLng ll = PingHandler.getInstance().getLocation(pingId);
        String title = PingHandler.getInstance().getHeader(pingId);
        mapMine = new MapMaker(PingInfo.this, ll, title, id, pingId);     //creates empty map
    }


    public void clickDelete(){

        //Root reference
        DatabaseReference myRef1 = FirebaseDatabase.getInstance().getReference("pings");

        //Child reference
        DatabaseReference myRef2 = myRef1.child(pingId).child("timestamp");

        ValueEventListener valuelistener = myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.getValue();
                value = value - 604800;
                myRef2.setValue(value);
            }

        });





                //Set timestamp to a week earlier, to effectively remove the ping
        myRef2.setValue("Hello, World!");
    }
}
