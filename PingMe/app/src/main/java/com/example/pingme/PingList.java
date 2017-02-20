package com.example.pingme;

import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

public class PingList extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping_list);
        ListView listView1 = (ListView) findViewById(android.R.id.list);
        String[] list = PingHandler.getInstance().getTitles();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView < ? > parent, View view, int position, long id){
                final String selected = (String) parent.getItemAtPosition(position);
                Log.d("PASSER", selected);
                Intent i = new Intent(getApplicationContext(), PingInfo.class);
                i.putExtra("name", selected);
                i.putExtra("info", PingHandler.getInstance().getInfos()[position]);
                i.putExtra("id", PingHandler.getInstance().getThisId(position));
                startActivity (i);
            }
        }
        );
    }

    public void openCreate(View v) { startActivity(new Intent(PingList.this, CreatePing.class));
        finish();
    }
    public void openStart(View v) {
        finish();
    }

    public void openProfile(View v){
        startActivity(new Intent(PingList.this, profile.class));
        finish();
    }
}
