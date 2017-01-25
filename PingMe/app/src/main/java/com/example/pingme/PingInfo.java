package com.example.pingme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

public class PingInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_info);
        Intent in = getIntent();
        String text = in.getStringExtra(("name"));
        TextView box1 = (TextView)findViewById(R.id.textView);
        box1.setText(text);
    }
}
