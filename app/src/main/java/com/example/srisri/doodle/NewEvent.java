package com.example.srisri.doodle;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.EditText;

import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.List;

public class NewEvent extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    List<String> selected_times = new ArrayList<>();
    List<String> selected_dates = new ArrayList<>();
    Spinner dropdown;
    Spinner dropdown2;
    Spinner dropdown3;

    ListView userList;
    CreatedAdapter createAdapter;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        userList = findViewById(R.id.listdates);

        TextView displayName = findViewById(R.id.LocName);
        displayName.setText(Location.address);

        Button evAddOptions = findViewById(R.id.button2);
        evAddOptions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(NewEvent.this, AddOptionsCalendarActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        dropdown = findViewById(R.id.spinner);
        String[] items = new String[]{"01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10","11", "12"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown2 = findViewById(R.id.spinner2);
        String[] items2 = new String[]{"00", "15", "30", "45"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        dropdown2.setAdapter(adapter2);

        dropdown3 = findViewById(R.id.spinner3);
        String[] items3 = new String[]{"AM", "PM"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items3);
        dropdown3.setAdapter(adapter3);

        Button evAddOptionDT = findViewById(R.id.button5);

        evAddOptionDT.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                String spinnertext = dropdown.getSelectedItem().toString() + ":" +
                        dropdown2.getSelectedItem().toString() + ":00" + " " + dropdown3.getSelectedItem().toString();
                selected_times.add(spinnertext);
                createAdapter = new CreatedAdapter(NewEvent.this, R.layout.event_info_help, selected_times);
                userList.setAdapter(createAdapter);
                createAdapter.notifyDataSetChanged();
                Toast.makeText(NewEvent.this, "Added: " + spinnertext + "to your event!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void finishEvent(View v) {
        DatabaseReference ref = database.getReference("Events");
        Query query = ref.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int eventID = Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey()) + 1;
                if(selected_dates == null || selected_dates.isEmpty()) {
                    Toast.makeText(NewEvent.this, "You never selected any dates!", Toast.LENGTH_LONG).show();
                    return;
                }
                if(selected_times == null || selected_dates.isEmpty()) {
                    Toast.makeText(NewEvent.this, "You never selected any times!", Toast.LENGTH_LONG).show();
                    return;
                }
                int k = 0;
                for(int i = 0; i < selected_dates.size(); ++i) {
                    String [] tmp = selected_dates.get(i).split(" ");
                    for(int j = 0; j < selected_times.size(); ++j) {
                        DatabaseReference myRef = database.getReference("Events/" + Integer.toString(eventID) + "/choices/" + k);
                        String time = selected_times.get(j);
                        int hour = 0;
                        int min = 0;
                        hour = Integer.parseInt(String.valueOf(time.charAt(0)) + time.charAt(1));
                        if(time.charAt(9) == 'P' && hour != 12)
                            hour += 12;
                        else if(hour == 12 && time.charAt(9) == 'A')
                            hour = 0;
                        min = Integer.parseInt(String.valueOf(time.charAt(3)) + time.charAt(4));
                        String minute = String.valueOf(min);
                        if(min ==0)
                            minute = "00";
                        String h = String.valueOf(hour);
                        if(hour==0)
                            h = "00";
                        myRef.setValue(tmp[0] + ' ' + tmp[1] + ' ' +  tmp[2]  + ' ' + hour + ':' + minute + ":00" + " " + ' ' + tmp[4] + ' ' + tmp [5]);
                        k = k + 1;
                    }
                }
                EditText evTitle = findViewById(R.id.editText);
                String title = evTitle.getText().toString();
                if (evTitle.length() == 0) {
                    Toast.makeText(NewEvent.this, "You need to input a title!", Toast.LENGTH_LONG).show();
                    return;
                }
                DatabaseReference myRef = database.getReference("Events/" + Integer.toString(eventID) + "/name");
                myRef.setValue(title);
                String location = getIntent().getStringExtra("location");
                myRef = database.getReference("Events/" + Integer.toString(eventID) + "/location");
                myRef.setValue(location);
                myRef = database.getReference("Events/" + Integer.toString(eventID) + "/owner");
                myRef.setValue(GlobalVars.getInstance().getUserEmail());
                Intent send_invites = new Intent(NewEvent.this, send_invite.class);
                send_invites.putExtra("eventID", Integer.toString(eventID));
                startActivity(send_invites);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Failed to push event info to database
                Toast.makeText(NewEvent.this, "Failed to push data to Firebase", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                selected_dates = data.getStringArrayListExtra("SelectedDates");
            }
        }
    }

    public void show_dates(View v) {
        if(selected_dates == null || selected_dates.isEmpty()) {
            Toast.makeText(NewEvent.this, "You never selected any dates!", Toast.LENGTH_LONG).show();
            return;
        }
        String[] newArr = new String[selected_dates.size()];
        newArr = selected_dates.toArray(newArr);
        final String[] otherArr = newArr;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Selected dates");
        builder.setItems(otherArr, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(), otherArr[item], Toast.LENGTH_SHORT).show();
            }

        });

        AlertDialog alert = builder.create();

        alert.show();
    }

    private class CreatedAdapter extends ArrayAdapter<String> {
        private int layout;
        private Context mContext;
        private LayoutInflater mInflater;
        private List<String> mDataSource;
        public CreatedAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
            mDataSource = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get view for row item
            mInflater = LayoutInflater.from(getContext());
            View rowView = mInflater.inflate(R.layout.event_info_help, parent, false);
            TextView loc = (TextView)rowView.findViewById(R.id.textView);
            loc.setText(mDataSource.get(position));

            return rowView;
        }
    }
}