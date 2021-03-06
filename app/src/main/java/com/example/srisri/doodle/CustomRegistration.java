package com.example.srisri.doodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CustomRegistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_registration);
        Button register = (Button) findViewById(R.id.registration_complete_button);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    public void createAccount() {
        Boolean valid = true;
        if(!isEmailValid(((EditText)findViewById(R.id.registration_email)).getText().toString())) {
            ((TextView)findViewById(R.id.email_error_registration)).setVisibility(View.VISIBLE);
            valid = false;
        } else
            ((TextView)findViewById(R.id.email_error_registration)).setVisibility(View.INVISIBLE);
        if(!isPasswordValid(((EditText)findViewById(R.id.registration_password)).getText().toString())){
            ((TextView)findViewById(R.id.invalid_password_registration)).setVisibility(View.VISIBLE);
            valid = false;
        } else
            ((TextView)findViewById(R.id.invalid_password_registration)).setVisibility(View.INVISIBLE);
        if(((EditText)findViewById(R.id.registration_name)).getText().toString().length() == 0){
            ((TextView)findViewById(R.id.empty_name_registration)).setVisibility(View.VISIBLE);
            valid = false;
        } else
        ((TextView)findViewById(R.id.empty_name_registration)).setVisibility(View.INVISIBLE);
        if(!valid)
            return;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users");
        Query query = ref.orderByChild("email").equalTo(((EditText)findViewById(R.id.registration_email)).getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() != 0){
                    //throw null because user found with email
                    Log.v("register", "error email already in use");
                    ((TextView)findViewById(R.id.email_already_used_registration)).setVisibility(View.VISIBLE);
                } else {
                    Query id = ref.orderByKey().limitToLast(1);
                    id.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.v("register", dataSnapshot.getChildren().iterator().next().getKey());
                            Log.v("register", String.valueOf(Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey()) + 1));
                            HashMap<String, Object> user = new HashMap<>();
                            user.put("name", ((EditText)findViewById(R.id.registration_name)).getText().toString());
                            user.put("email", ((EditText)findViewById(R.id.registration_email)).getText().toString());
                            user.put("password", ((EditText)findViewById(R.id.registration_password)).getText().toString());

                            //GlobalVars.setUserId(String.valueOf(Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey()) + 1));

                            ref.child(String.valueOf(Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey()) + 1)).setValue(user);
                            Intent dashboard = new Intent(CustomRegistration.this, Dashboard.class);
                            Log.v("userid", dataSnapshot.getChildren().iterator().next().getKey());
                            GlobalVars.getInstance().setUser(((EditText)findViewById(R.id.registration_name)).getText().toString(),
                                    ((EditText)findViewById(R.id.registration_email)).getText().toString(),
                                    ((EditText)findViewById(R.id.registration_password)).getText().toString(),
                                    Integer.toString(Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey()) + 1));
                            startActivity(dashboard);
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
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 7;
    }
}
