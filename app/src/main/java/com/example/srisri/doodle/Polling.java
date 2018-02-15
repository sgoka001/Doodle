package com.example.srisri.doodle;


import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.TextView;


import com.google.android.gms.maps.model.Dash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Map;


/**
 * Created by Bang Se Hoon on 2/7/2018.
 */

public class Polling extends AppCompatActivity {
    CheckBox boxChoice;
    TextView newDesc;
    Button btnLocation;
    ScrollView svPoll;
    LinearLayout boxContiner;
    ArrayList<String> choiceList;
    ArrayList<String> inviteKeys;
    ArrayList<Integer> choiceId;        //Will start at 100
    Integer submitId = 85;
    Integer eventid;
    Integer inviteid;
    FirebaseDatabase database;

    boolean temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_polling);

        newDesc = findViewById(R.id.text_pollingDesc);
        btnLocation = findViewById(R.id.btn_location);

        database = FirebaseDatabase.getInstance();

        //GetInviteId from dashboard
        inviteid = 7;
        //Get inviteKeys
        DatabaseReference myRefkeys = database.getReference("invites/"+ inviteid.toString() );
        myRefkeys.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //will hold name of choices
                inviteKeys = new ArrayList<>();

                //fill up the array
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    inviteKeys.add(dsp.getKey()); //add result into array list
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
//                // Failed to read value
                Toast.makeText(Polling.this,"Fail",Toast.LENGTH_LONG).show();
            }
        });

        //Get EventId
        DatabaseReference myRefEid = database.getReference("invites/"+ inviteid.toString() +"/eventid");
        myRefEid.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                eventid = dataSnapshot.getValue(Integer.class);

                //Event Name
                DatabaseReference myRef = database.getReference("Events/"+ eventid.toString() +"/name");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);
//                Toast.makeText(Polling.this,"Success" ,Toast.LENGTH_LONG).show();
                        newDesc.setText(value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Toast.makeText(Polling.this,"Fail",Toast.LENGTH_LONG).show();
                    }
                });

                //Location Button
                DatabaseReference myRefLoc = database.getReference("Events/"+ eventid.toString() +"/location");
                myRefLoc.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        String value = dataSnapshot.getValue(String.class);
//                Toast.makeText(Polling.this,"Success" ,Toast.LENGTH_LONG).show();
                        btnLocation.setText(value);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Toast.makeText(Polling.this,"Fail",Toast.LENGTH_LONG).show();
                    }
                });

                //Dynamically Create checkbox polling
                //Dynamically Create Button to Submit
                svPoll = findViewById(R.id.pollingScrollView);
                DatabaseReference myRefChoices = database.getReference("Events/"+ eventid.toString() ).child("choices");
                myRefChoices.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //will hold name of choices
                        choiceList = new ArrayList<>();

                        //fill up the array
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            choiceList.add(String.valueOf(dsp.getValue(String.class))); //add result into array list
                        }

                        //Here Create a Check box for each choice and implement the action of each checkbox
                        //Include into scroll view
                        String testing = "First....";

                        //Create linear layout to contain box choices
                        boxContiner = new LinearLayout(Polling.this);
                        boxContiner.setOrientation(LinearLayout.VERTICAL);

                        //Id starting point
                        Integer id = 100;
                        choiceId = new ArrayList<Integer>();
                        for(String choice : choiceList){
                            testing = testing + choice + " : ";

                            //Create check boxes
                            boxChoice = new CheckBox(Polling.this);
                            boxChoice.setText(choice);
                            boxChoice.setId(id);
                            choiceId.add(id);
                            if(inviteKeys.contains(choice))
                            {
                                DatabaseReference myRefInvKey = database.getReference("invites/"+ inviteid.toString() + "/" + choice);
                                myRefInvKey.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // This method is called once with the initial value and again
                                        // whenever data at this location is updated.
                                        temp = dataSnapshot.getValue(boolean.class);
                                        if(temp) {
                                            Toast.makeText(Polling.this, "FIRSTSTOAST", Toast.LENGTH_LONG).show();
                                            boxChoice.setChecked(true);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Failed to read value
                                        Toast.makeText(Polling.this, "Failed to create checkbox from database" ,Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            id = id + 1;
                            boxChoice.setOnClickListener(getOnClickChangeBool(boxChoice));
                            boxContiner.addView(boxChoice);

                        }

                        //Add Cannot Attend Check Box (if) checked make all others false
                        boxChoice = new CheckBox(Polling.this);
                        boxChoice.setText("Cannot Attend");
                        DatabaseReference myRefnoAttend = database.getReference("invites/"+ inviteid.toString() + "/declined");
                        myRefnoAttend.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                boolean value = dataSnapshot.getValue(boolean.class);
                                boxChoice.setChecked(value);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Toast.makeText(Polling.this, "Failed to create checkbox from database" ,Toast.LENGTH_LONG).show();
                            }
                        });
                        boxChoice.setId(id);
                        choiceId.add(id);
                        id = id + 1;

                        //set all checked checkbox to false if we click not attending
                        boxChoice.setOnClickListener(noattendChangeBool(boxChoice));

                        //Add cannot attend button to the check box container
                        boxContiner.addView(boxChoice);


                        //Submit Button
                        Button btnSubmit = new Button(Polling.this);
                        btnSubmit.setText("Submit");
                        btnSubmit.setId(submitId);
                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                submitPoll();
                            }
                        });
                        boxContiner.addView(btnSubmit);


                        //Add container into scroll view
                        svPoll.addView(boxContiner);
//                Toast.makeText(Polling.this,testing, Toast.LENGTH_LONG ).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Polling.this,"FAILED", Toast.LENGTH_LONG ).show();
                    }
                });

                Toast.makeText(Polling.this,eventid.toString() ,Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(DatabaseError error) {
//                // Failed to read value
                Toast.makeText(Polling.this,"Fail",Toast.LENGTH_LONG).show();
            }
        });

    }

    //OnClickListener for all choices but last: sets last choice as false if any clicked
    View.OnClickListener getOnClickChangeBool(final CheckBox box){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(box.isChecked()){
                    Integer true_id = getResources().getIdentifier(choiceId.get(choiceId.size() - 1).toString(), "id",getPackageName());
                    CheckBox box = findViewById(true_id);
                    box.setChecked(false);

                }
            }
        };
    }

    //OnClickListener for notAttending: Sets all check boxes except this to false
    View.OnClickListener noattendChangeBool(final CheckBox box){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(box.isChecked()){
                    //How to get all checked Boxes on page
                   for(Integer id = choiceId.get(0); id < choiceId.get(choiceId.size() - 1); id++){
                       Integer true_id = getResources().getIdentifier(id.toString(), "id",getPackageName());
                       CheckBox box = findViewById(true_id);
                       box.setChecked(false);
                   }
                }
            }
        };
    }

    //OnClickListener for Poll Submission
    public void submitPoll(){

        //Need to submit data
        //Cycle through invites to find the one we pulled from
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Go through all choices and save data
        boolean didNothing = true;
        for(Integer i = 0; i < choiceId.size(); i++){
            DatabaseReference myRef;
            //Going through all choices in choice list
            if(i < choiceList.size()){
                myRef = database.getReference("invites/"+ inviteid.toString() +
                        "/"+choiceList.get(i));

                Integer true_id = getResources().getIdentifier(choiceId.get(i).toString(), "id",getPackageName());
                CheckBox box = findViewById(true_id);
                if(box.isChecked())
                {
                    didNothing = false;
                }
                myRef.setValue(box.isChecked());
            }
            //this should be accessed only once for last check box (cannot attend)
            else{
                myRef = database.getReference("invites/"+ inviteid.toString() + "/declined");

                Integer true_id = getResources().getIdentifier(choiceId.get(i).toString(), "id",getPackageName());
                CheckBox box = findViewById(true_id);
                myRef.setValue(box.isChecked());
                if(!box.isChecked() && didNothing){
                    myRef = database.getReference("invites/"+ inviteid.toString() + "/accepted");
                    myRef.setValue(false);
                }
                else{
                    myRef = database.getReference("invites/"+ inviteid.toString() + "/accepted");
                    myRef.setValue(!box.isChecked());
                }
            }
        }


        Toast.makeText(Polling.this,"Submitted", Toast.LENGTH_LONG ).show();
        //Back to Dashboard
        Intent dashboard = new Intent(this, Dashboard.class);
        dashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(dashboard);
    }
}



