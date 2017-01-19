package com.example.pingme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
    }

    public void openCreate(View v) { startActivity(new Intent(ListView.this, CreatePing.class)); }
    public void openStart(View v) { startActivity(new Intent(ListView.this, StartPage.class)); }
}
