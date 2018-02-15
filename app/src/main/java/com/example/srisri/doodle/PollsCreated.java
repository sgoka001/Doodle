package com.example.srisri.doodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PollsCreated extends AppCompatActivity {
   FirebaseAuth authu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polls_created);
        authu = FirebaseAuth.getInstance();
        TextView txt = (TextView) findViewById(R.id.textbox);
        txt.setText("Hello");
        //String inst = GlobalVars.getInstance().getUserEmail();
        final String inst = "pgior001@ucr.edu";

        //final FirebaseUser user = authu.getCurrentUser();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Events");
        Query query = ref.orderByChild("owner").equalTo(inst);
        Log.v("Query", query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null ) {
                    DataSnapshot ret = dataSnapshot.getChildren().iterator().next();
                    String title=(ret.child("location").getValue().toString());
                    Log.v("title", title);
                    //GlobalVars.getInstance().setUser(user.getDisplayName(),user.getEmail(),ret.getKey().toString());
                    //Log.v("key",ret.getKey().toString());
                    Toast.makeText(PollsCreated.this, "Query Database", Toast.LENGTH_SHORT).show();
                    Button button = (Button) findViewById(R.id.textView3);
                    button.setText(title);
                                                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

         //findViewById(R.id.textView3);

        /*GlobalVars globVar = ((GlobalVars)getApplicationContext());
        String emaillglobe = globVar.getUserEmail();
        final FirebaseUser user = authu.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Events");
        Query query = ref.orderByChild("owner").equalTo(emaillglobe);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() != 0){
                    txt.setText(dataSnapshot.getValue(String.class));
                    //throw null because user found with email
                    //Log.v("register", "error email already in use");

                } else {
                    Query id = ref.orderByKey().limitToLast(1);
                    id.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.v("register", dataSnapshot.getChildren().iterator().next().getKey());
                            Log.v("register", String.valueOf(Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey()) + 1));
                            HashMap<String, Object> users = new HashMap<>();
                            users.put("name", (user.getDisplayName()));
                            users.put("email", ((user.getEmail())));
                            //users.put("password", ((EditText)findViewById(R.id.registration_password)).getText().toString());
                            ref.child(String.valueOf(Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey()) + 1)).setValue(users);
                            //Intent dashboard = new Intent(Login.this, Dashboard.class);
                            Log.v("userid", dataSnapshot.getChildren().iterator().next().getKey());
                            GlobalVars.getInstance().setUser(user.getDisplayName(),user.getEmail());
                            //startActivity(dashboard);
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //txt.setText(text);*/

    }
}
