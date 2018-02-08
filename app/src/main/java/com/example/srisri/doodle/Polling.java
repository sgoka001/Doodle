package com.example.srisri.doodle;


import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by Bang Se Hoon on 2/7/2018.
 */

public class Polling extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_polling);

        TextView newDesc = findViewById(R.id.text_pollingDesc);
        Button location_button = findViewById(R.id.btnLoc);


        //How are we getting the polling id?
        //How are we pulling descriptions
        newDesc.setText("Add Name of Event Here");
        location_button.setText("Add location here");

        //Set up onclick listener for this button --> will link to Location page
        //...

        //Got to go through array of time choices
        //pull array of time choices from firebase Database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //GetEventID
        String idEvent = "";
        DatabaseReference ref = database.getReference("doodler-4461c/Event/"+idEvent+"/Polling");


        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // Post post = dataSnapshot.getValue(Post.class);
                // System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

}



