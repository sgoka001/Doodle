package com.example.srisri.doodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class NewEvent extends AppCompatActivity {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        // Setting event buttons and text boxes

        Button evAddOptions = findViewById(R.id.button2);
        evAddOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(NewEvent.this, AddOptionsCalendarActivity.class);
                startActivity(intent);
            }
        });
        List<String> selected_dates = new ArrayList<>();
        selected_dates = (ArrayList<String>)getIntent().getSerializableExtra("SelectedDates");

        Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"1:", "2:", "3:", "4:", "5:", "6:",
                "7:", "8:", "9:", "10:","11:", "12:"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        Spinner dropdown2 = findViewById(R.id.spinner2);
        String[] items2 = new String[]{"00", "15", "30", "45"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown2.setAdapter(adapter2);

        Spinner dropdown3 = findViewById(R.id.spinner3);
        String[] items3 = new String[]{"AM", "PM"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown2.setAdapter(adapter3);

        Button evSettings = findViewById(R.id.button4);
        evSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent for settings page
            }
        });

    }
    public void openMap(View view) {
        Intent intent = new Intent(this, Location.class);
        startActivity(intent);
    }
}