package com.example.pingme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class profile extends AppCompatActivity {

    private MyProfile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mProfile = getProfile();
        if (mProfile == null){
            createProfile();
            mProfile = getProfile();
        }
            TextView header = (TextView) findViewById(R.id.profileName);
            header.setText(mProfile.getName());
    }

    public void createProfile(){

    }

    public MyProfile getProfile(){

    }

    public void openCreate(View v) { startActivity(new Intent(profile.this, CreatePing.class));
        finish();
    }
    public void openList(View v) {
        startActivity(new Intent(profile.this, PingList.class));
        finish();
    }
    public void openMap(View v) {
        finish();
    }
}
