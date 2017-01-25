package com.example.pingme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.app.ListActivity;

public class ListView extends  ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        String[] list = {"Wash my dog - John", "Walk my car - Dinglerberg"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        getListView().setAdapter(adapter);

    }

    public void openCreate(View v) { startActivity(new Intent(ListView.this, CreatePing.class)); }
    public void openStart(View v) { startActivity(new Intent(ListView.this, StartPage.class)); }
}
