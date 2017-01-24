package com.example.pingme;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class ListView extends  ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        String[] list = {"Wash my dog - John", "Walk my car - Dinglerberg"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(),
                android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        getListView().setAdapter(adapter);
    }

    public void openMap(View v) { startActivity(new Intent(ListView.this, MapsActivity.class)); }
    public void openStart(View v) { startActivity(new Intent(ListView.this, StartPage.class)); }
}
