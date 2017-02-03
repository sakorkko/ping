package com.example.pingme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pingme.MapMaker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class CreatePing extends AppCompatActivity {

    private MapMaker mapMine;

    private static final String TAG = "CreatePing";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ping);
        int id = R.id.creatorMap;

        LocalBroadcastManager.getInstance(this).registerReceiver(
                mMessageReceiver, new IntentFilter("pingReceiver"));

        mapMine = new MapMaker(CreatePing.this, false, id);     //creates empty map
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String pingTitle = intent.getStringExtra("title");
            String pingBody = intent.getStringExtra("body");
            String pingLocation = intent.getStringExtra("location");
            Log.d(TAG, "ping received: \n" + "title: " + pingTitle + " body: " + pingBody);
            String[] latlong =  pingLocation.split(",");
            double pingLatitude = Double.parseDouble(latlong[0]);
            double pingLongitude = Double.parseDouble(latlong[1]);

            LatLng pingPosition = new LatLng(pingLatitude, pingLongitude);

            PingHandler.getInstance().addPing(pingTitle, pingBody, pingPosition);
        }
    };

    public void openStart(View v){
        finish();
    }
    public void openList(View v){startActivity(new Intent(CreatePing.this, PingList.class));
        finish();
    }


    public void mark(View v){           //starts a pop_up
        Intent i = new Intent(this, pop_up.class);
        final int result = 1;
        startActivityForResult(i, result);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     //result from the pop_up
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            EditText title = (EditText) findViewById(R.id.editText);
            String head = String.valueOf(title.getText());
            EditText info = (EditText) findViewById(R.id.editText3);
            String additionalInfo = String.valueOf(info.getText());
            LatLng position = mapMine.getPosition();

            String positionString = String.valueOf(position.latitude) + "," + String.valueOf(position.longitude);
            String[] pingParams = { head, additionalInfo, positionString };
            AsyncT asyncT = new AsyncT();
            asyncT.execute(pingParams);



            // PingHandler.getInstance().addPing(head, additionalInfo, position);

            Intent intent = new Intent(CreatePing.this, StartPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}
