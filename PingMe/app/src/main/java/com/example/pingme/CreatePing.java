package com.example.pingme;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pingme.MapMaker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class CreatePing extends AppCompatActivity {

    private MapMaker mapMine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ping);
        int id = R.id.creatorMap;

        String[] empty = {};
        LatLng[] empty2 = {};
        mapMine = new MapMaker(CreatePing.this, id, empty, empty2);     //creates empty map
    }

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
            String location = String.valueOf(position.latitude) + ":" + String.valueOf(position.longitude);
            Bundle extras = new Bundle();
            extras.putString("Title",head);
            extras.putString("Info",additionalInfo);
            extras.putString("Position",location);
            Intent intent = new Intent(CreatePing.this, StartPage.class);
            intent.putExtras(extras);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

}
