package com.example.pingme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class profile extends AppCompatActivity {

    private User mProfile

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mProfile = getProfile();
        TextView header = (TextView) findViewById(R.id.profileName);
        header.setText(mProfile.name);
    }


    public User getProfile(){
        // This function should fetch the user profile.
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

    public void openMyPings(View v){
        //opens ping list and sends it a list of my pings
    }

    public void openFriends(View v){
        //opens an activity that shows your friens and lets you add new ones
    }
}
