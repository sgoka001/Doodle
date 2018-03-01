package com.example.srisri.doodle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Bang Se Hoon on 2/26/2018.
 */

public class EventOwnerView extends AppCompatActivity{
    CheckBox boxChoice;
    TextView newDesc;
    Button btnLocation;
    ScrollView svPoll;
    LinearLayout boxContiner;
    HashMap<String, Integer> choiceList;
    HashMap<String, Boolean> tempInviteList;
    ArrayList<Integer> choiceId;        //Will start at 100
    Integer submitId = 85;
    String eventid;
    String inviteid;
    FirebaseDatabase database;

    boolean addToChoiceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_polling);

        newDesc = findViewById(R.id.text_pollingDesc);
        btnLocation = findViewById(R.id.btn_location);

        database = FirebaseDatabase.getInstance();
        /*
            TODO:
                1) Pull all event invite data from database
                2) Cycle through them and if eventid = CURRENT EVENT ID
                        1) pull data from CURRENT EVENT ID --> EVENT
                            - get choices and store them in an arrayLIST (EventChoices
                        Pull data from the event
                            Go through the nodes and search keys
                                if EventChoices.contains(KEY)
                                    then create ArrayList of Pair  with First = Key, Second = # of True
                                else: nothing

                        with the data we got if we have more than 1 choice + not attending + attending
                        we will create buttons for all choices in which the owner can choose one
                                - each button will have choice name + # of participants that chose it

                        if none of the buttons are chosen do nothing
                            if one button is chosen rest = false

        */


        //Get EventID from dashboard
//        eventid= GlobalVars.getEventID();       //TODO: set inviteid to a string and remove toString from every time it is called
        eventid = "2";
        //will hold name and count of choices
        choiceList = new HashMap<>();
        //Pull all choices from event
        //save all choices into choiceList
        DatabaseReference myRefChoices = database.getReference("Events/"+ eventid ).child("choices");
        myRefChoices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //fill up the hashmap
                final String checkKey = dataSnapshot.getKey();
                Toast.makeText(EventOwnerView.this,checkKey, Toast.LENGTH_LONG ).show();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    choiceList.put(dsp.getValue(String.class),0);   //add values
                     }
                Toast.makeText(EventOwnerView.this,"size: " + choiceList.size(), Toast.LENGTH_LONG ).show();

                //Go through all invites and see if their_eventid == eventID
                DatabaseReference inviteRef = database.getReference().child("invites");
                inviteRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        tempInviteList = new HashMap<>();

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
                                        if (data.getValue(boolean.class)) {
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

                        Toast.makeText(EventOwnerView.this,"sizes Final: " + choiceList.size(), Toast.LENGTH_LONG ).show();
                        for(String keys : choiceList.keySet()){
                            Toast.makeText(EventOwnerView.this,keys + ":" + choiceList.get(keys).toString(), Toast.LENGTH_LONG ).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EventOwnerView.this,"FAILED", Toast.LENGTH_LONG ).show();
            }
        });



    }
}
