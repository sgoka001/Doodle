package com.example.srisri.doodle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * Created by Bang Se Hoon on 2/26/2018.
 */

public class EventOwnerView extends AppCompatActivity{
    CheckBox boxChoice;
    TextView newDesc;
    ScrollView svPollRes;
    LinearLayout boxContiner;
    HashMap<String, Integer> choiceList;    //choiceList <choiceName,count> used to count how many people voted per choice
    HashMap<String, String> eventChoices;   //eventChoice will store <choiceName, id>
    HashMap<String, Boolean> tempInviteList;
    ArrayList<Integer> choiceId;        //Will start at 100
    String eventid;
    String finalChoice;
    FirebaseDatabase database;
    Semaphore MakeLinear;
    boolean addToChoiceList;
    //used to make sure the app doesn't crash after deleting from database
    boolean DoOnce = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_owner);

        newDesc = findViewById(R.id.text_pollingDesc);
        Button btnSubmit = findViewById(R.id.btnFinalize);

        database = FirebaseDatabase.getInstance();
        MakeLinear = new Semaphore(1);


        //Get EventID from dashboard
        eventid= GlobalVars.getEventID();

        //will hold name and count of choices
        choiceList = new HashMap<>();
        eventChoices = new HashMap<>();
        //Pull all choices from event
        //save all choices into choiceList

        DatabaseReference myRefEventTitle = database.getReference("Events/" + eventid + "/name");
        myRefEventTitle.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView EventTitle = findViewById(R.id.txtTitle);
                EventTitle.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventOwnerView.this,"FAILED", Toast.LENGTH_LONG ).show();
            }
        });

        DatabaseReference myRefEventLoc = database.getReference("Events/" + eventid + "/location");
        myRefEventLoc.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView EventLoc = findViewById(R.id.txtDesc);
                EventLoc.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventOwnerView.this,"FAILED", Toast.LENGTH_LONG ).show();
            }
        });

        //Get event choices, results of polling, dynamically created objects
        DatabaseReference myRefChoices = database.getReference("Events/"+ eventid ).child("choices");
        myRefChoices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //fill up the choiceList with different choices
                try {
                    MakeLinear.acquire();
                } catch(InterruptedException e){
                    Log.v("error", e.toString());
                }
                choiceList.put("accepted", 0);
                choiceList.put("declined", 0);

//                final String checkKey = dataSnapshot.getKey();
//                Toast.makeText(EventOwnerView.this,checkKey, Toast.LENGTH_LONG ).show();

                //Pull choices from database and save onto eventChoices and choiceList
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    choiceList.put(dsp.getValue(String.class),0);   //add values
                    eventChoices.put(dsp.getValue(String.class), dsp.getKey());   //add to list of event choices
                }

                choiceList.put("noAttend", 0);

                MakeLinear.release();

                //Go through all invites and see if their_eventid == eventID
                DatabaseReference inviteRef = database.getReference().child("invites");
                inviteRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tempInviteList = new HashMap<>();

                        //wait for top to release the semaphore
                        try {
                            MakeLinear.acquire();
                        } catch(InterruptedException e){
                            Log.v("error", e.toString());
                        }

                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            addToChoiceList = false;
                            for (DataSnapshot data : dsp.getChildren()) {
                                final String key = data.getKey();
                                //if key is "email" we dont care
                                if (!key.equals("email")) {
                                    //only one that is a string
                                    //if key is equal to eventid
                                    if (key.equals("eventId") ) {
                                        //and the two are the same then addToChoiceList is true
                                        if(data.getValue(Integer.class).toString().equals(eventid)) {
                                            addToChoiceList = true;
                                        }
                                    }
                                    //all else should be true or false boolean
                                    else {
//                                Toast.makeText(EventOwnerView.this,key, Toast.LENGTH_LONG ).show();
//                                Toast.makeText(EventOwnerView.this,String.valueOf(data.getValue()), Toast.LENGTH_LONG ).show();
                                        if (((Boolean)data.getValue()) != null && ((Boolean)data.getValue())) {
                                            tempInviteList.put(key, true);
                                        }
                                    }
                                }
                            }
                            //if addToChoiceList then check values of tempInviteList and increment values of choiceList appropriately
                            if (addToChoiceList) {
                                //cycle through all keys of the tempInviteList Map
                                for (String key : tempInviteList.keySet()) {
                                    //if and only if the value is true  AND the key is in choiceList increment the same key value of choiceList by 1
                                    if (tempInviteList.get(key) && choiceList.containsKey(key)) {
                                        Integer tempHolder = choiceList.get(key) + 1;
                                        choiceList.remove(key);
                                        choiceList.put(key, tempHolder);
                                        //can use following line instead but above lines help support sdk 15
                                        //choiceList.replace(key, choiceList.get(key)+1);
                                    }
                                }
                            }
                        }

//                        Toast.makeText(EventOwnerView.this,"sizes Final: " + choiceList.size(), Toast.LENGTH_LONG ).show();
                        //choiceList = map of keys + # of responses per key
                        //need to create buttons for each response
                        svPollRes = findViewById(R.id.svEventOwner);
                        //Create linear layout to contain box choices
                        boxContiner = new LinearLayout(EventOwnerView.this);
                        boxContiner.setOrientation(LinearLayout.VERTICAL);
                        //Id starting point
                        Integer id = 100;
                        choiceId = new ArrayList<>();

                        //keys already used before checkbox
                        ArrayList<String> keyBeforeCB = new ArrayList<>();

                        // * hard code #accepted and # declined  #not attending first as just TEXT VIEW
                        TextView temp = new TextView(EventOwnerView.this);
                        keyBeforeCB.add("accepted");
                        String choiceName = "Number of accepted invites: " + choiceList.get("accepted");
                        temp.setText(choiceName);
                        temp.setId(id);
                        choiceId.add(id);
                        id = id + 1;
                        boxContiner.addView(temp);

                        temp = new TextView(EventOwnerView.this);
                        keyBeforeCB.add("declined");
                        choiceName = "Number of declined invites: " + choiceList.get("declined");
                        temp.setText(choiceName);
                        temp.setId(id);
                        choiceId.add(id);
                        id = id + 1;
                        boxContiner.addView(temp);

                        temp = new TextView(EventOwnerView.this);
                        keyBeforeCB.add("noAttend");
                        choiceName = "Number of not attending: " + choiceList.get("noAttend");
                        temp.setText(choiceName);
                        temp.setId(id);
                        choiceId.add(id);
                        id = id + 1;
                        boxContiner.addView(temp);
                        // Create box for choices
                        //Dynamically Create checkbox polling
                        //Dynamically Create Button to Submit

                        for(String key : choiceList.keySet()){
                            if(!keyBeforeCB.contains(key)){
                                //Create check boxes
                                boxChoice = new CheckBox(EventOwnerView.this);
                                choiceName = key + ": " + choiceList.get(key).toString();
                                boxChoice.setText( choiceName);
                                boxChoice.setId(id);
                                choiceId.add(id);
                                id = id + 1;
                                boxChoice.setOnClickListener(getOnClickChangeBool(boxChoice));
                                boxContiner.addView(boxChoice);
                            }
                        }

                        //we only want to add to SV
                        //if no if statement it will do this again when we submit and delete object from firebase
                        if(DoOnce) {
                            //Add container into scroll view
                            svPollRes.addView(boxContiner);
                            DoOnce = false;
                        }

                        MakeLinear.release();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(EventOwnerView.this,"FAILED", Toast.LENGTH_LONG ).show();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventOwnerView.this,"FAILED", Toast.LENGTH_LONG ).show();
            }
        });

        //On click Listener for Submission button
        //will check if the owner chose one choice for the event then move to finalizeEvent()
        //else toast error msg
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean Chosen = false;
                //check if there is a box selected
                for(Integer id = 0; id < choiceId.size(); id++){
                    Integer true_id = getResources().getIdentifier(choiceId.get(id).toString(), "id",getPackageName());
                    if(findViewById(true_id) instanceof CheckBox) {
                       CheckBox boxes = findViewById(true_id);
                       if(boxes.isChecked()) {
                           Chosen = true;
                       }
                    }
                }

               if(Chosen) {
                   finalizeEvent();
               }
               else
               {
                   Toast.makeText(EventOwnerView.this,"You must select one finalized event time", Toast.LENGTH_LONG ).show();
               }
            }
        });

    }

    //OnClickListener for notAttending: Sets all check boxes except this to false
    View.OnClickListener getOnClickChangeBool(final CheckBox box){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(box.isChecked()){
                    //How to get all checked Boxes on page
                    finalChoice = box.getText().toString().split(":")[0];
//                    Toast.makeText(EventOwnerView.this, "choiceId size: " + choiceId.size(), Toast.LENGTH_LONG ).show();
                    for(Integer id = 0; id < choiceId.size(); id++){
                        Integer true_id = getResources().getIdentifier(choiceId.get(id).toString(), "id",getPackageName());
                        if(findViewById(true_id) instanceof CheckBox && true_id != box.getId()) {
//                            Toast.makeText(EventOwnerView.this, "choiceId In: " + choiceId.get(id).toString(), Toast.LENGTH_LONG ).show();
                            CheckBox boxes = findViewById(true_id);
                            boxes.setChecked(false);
                        }
                    }
                }
            }
        };
    }

    //OnClickListener for finalizeEvent
    //delete all other choices except the selected one
    //return to dashboard
    public void finalizeEvent(){

        //Get rid of all choices except selected choice from the event
        for(String key : choiceList.keySet()){
            if(!key.equals(finalChoice) && eventChoices.keySet().contains(key) ) {

                DatabaseReference myRefChoices = database.getReference("Events/" + eventid + "/choices/" + eventChoices.get(key));
                for(Integer id = 0; id < choiceId.size(); id++){
                    Integer true_id = getResources().getIdentifier(choiceId.get(id).toString(), "id",getPackageName());
                    if(findViewById(true_id) instanceof CheckBox) {
                        CheckBox boxes = findViewById(true_id);
                        if(boxes.getText().toString().split(":")[0].equals(key) ) {
                            Toast.makeText(EventOwnerView.this,"Deleted Key " + key, Toast.LENGTH_LONG ).show();
                            svPollRes.removeView(boxes);
                        }
                    }
                }
                myRefChoices.removeValue();
            }
        }

        Toast.makeText(EventOwnerView.this,"Submitted", Toast.LENGTH_LONG ).show();
        //Back to Dashboard
        Intent dashboard = new Intent(this, Dashboard.class);
        dashboard.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(dashboard);
    }
}
