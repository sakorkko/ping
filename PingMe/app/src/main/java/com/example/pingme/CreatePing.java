package com.example.pingme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CreatePing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ping);
    }

    public void openStart(View v){ startActivity(new Intent(CreatePing.this, StartPage.class)); }
    public void openList(View v){startActivity(new Intent(CreatePing.this, PingList.class));
    }
}
