package com.example.pingme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
    }

    public void openList(View v){
        startActivity(new Intent(StartPage.this, ListView.class));
    }
    public void openMap(View v) { startActivity(new Intent(StartPage.this, MapsActivity.class)); }
}