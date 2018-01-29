package com.example.srisri.doodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewEvent extends AppCompatActivity {

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        // Setting event buttons and text boxes

        Button evAddOptions = findViewById(R.id.button2);
        evAddOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //New intent will be made here (options page)
            }
        });

        Button evLocation = findViewById(R.id.button3);
        evLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent for location page
            }
        });

        Button evSettings = findViewById(R.id.button4);
        evSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent for settings page
            }
        });

    }
}