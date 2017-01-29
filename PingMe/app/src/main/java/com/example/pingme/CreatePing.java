package com.example.pingme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.pingme.MapMaker;

public class CreatePing extends AppCompatActivity {

    private MapMaker mapMine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ping);
        int id = R.id.creatorMap;
        mapMine = new MapMaker(CreatePing.this, id);
    }

    public void openStart(View v){ startActivity(new Intent(CreatePing.this, StartPage.class)); }
    public void openList(View v){startActivity(new Intent(CreatePing.this, PingList.class));
    }
}
