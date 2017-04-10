package com.example.pingme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

public class pop_up_signing extends AppCompatActivity {

    EditText text;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_signing);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*0.6), (int) (height*0.2));

        text = (EditText) findViewById(R.id.nameField);

    }

    public void clickOk(View v){
        name = text.getText().toString();
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        i.putExtra("name", name);
        finish();


    }

    public void clickCancel(View v){
        Intent i = new Intent();
        setResult(RESULT_CANCELED, i);
        finish();
    }

}
