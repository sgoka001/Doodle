package com.example.srisri.doodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class NewEvent extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
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
        final Spinner dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"1:", "2:", "3:", "4:", "5:", "6:",
                "7:", "8:", "9:", "10:","11:", "12:"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        final Spinner dropdown2 = findViewById(R.id.spinner2);
        String[] items2 = new String[]{"00", "15", "30", "45"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown2.setAdapter(adapter2);

        final Spinner dropdown3 = findViewById(R.id.spinner3);
        String[] items3 = new String[]{"AM", "PM"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown3.setAdapter(adapter3);

        final List<String> selected_times = new ArrayList<String>();
        Button evAddOptionDT = findViewById(R.id.button5);

        evAddOptionDT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String spinnertext = dropdown.getSelectedItem().toString() +
                        dropdown2.getSelectedItem().toString() + dropdown3.getSelectedItem().toString();
                selected_times.add(spinnertext);
            }
        });

        final ToggleButton togglebutton1 = findViewById(R.id.toggleButton);
        final ToggleButton togglebutton2 = findViewById(R.id.toggleButton2);

        Button evFinish = findViewById(R.id.button3);
        evFinish.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                List<String> selected_dates;
                selected_dates = getIntent().getStringArrayListExtra("SelectedDates");
                int eventChoices = selected_dates.size();
                int i;
                for(i = 0; i < eventChoices; ++i) {
                    //DatabaseReference myRef = database.getReference("Events/" + eventID.toString() + "/choices/" + i);
                    //myRef.setValue(selected_dates.get(i));
                }
                for(int j = 0; j < selected_times.size(); ++j) {
                    //DatabaseReference myRef = database.getReference("Events/" + eventID.toString() + "/choices/" + i);
                    //myRef.setValue(selected_times.get(j));
                    i = i + 1;
                }
                EditText evTitle = findViewById(R.id.editText);
                String title = evTitle.getText().toString();
                //DatabaseReference myRef = database.getReference("Events/" + eventID.toString() + "/title/");
                //myRef.setValue(title);
                EditText evNote = findViewById(R.id.editText2);
                String note = evNote.getText().toString();
                //myRef = database.getReference("Events/" + eventID.toString() + "/note/");
                //myRef.setValue(note);
                boolean yesNoCheck = togglebutton1.isChecked();
                boolean limitCheck = togglebutton2.isChecked();
                String yesno = String.valueOf(yesNoCheck);
                String limit = String.valueOf(limitCheck);
                //myRef = database.getReference("Events/" + eventID.toString() + "/yesno/");
                //myRef.setValue(yesno);
                //myRef = database.getReference("Events/" + eventID.toString() + "/limit/");
                //myRef.setValue(limit);
            }
        });


    }
    public void openMap(View view) {
        Intent intent = new Intent(this, Location.class);
        startActivity(intent);
    }
}