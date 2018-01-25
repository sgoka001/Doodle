package com.example.srisri.doodle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

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
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", "Preston Giorgianni");
        user.put("email", "pgior001@ucr.edu");
        user.put("password", 12345);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users");
        ref.child("1").setValue(user);
        Query query = ref.orderByChild("email");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    Log.v("user", dataSnapshot.getValue().toString());
                } else {
                    Log.v("user", "none");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void createAccount() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users");
        Query query = ref.orderByChild("email").equalTo(((EditText)findViewById(R.id.editText)).getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() != 0){
                    //throw null because user found with email
                    Log.v("register", dataSnapshot.getChildren().iterator().next().getKey());
                } else {
                    Query id = ref.orderByKey().limitToLast(1);
                    id.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.v("register", dataSnapshot.getChildren().iterator().next().getKey());
                            Log.v("register", String.valueOf(Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey()) + 1));
//                                User input = new User(((EditText)findViewById(R.id.Password_Register_Input)).getText().toString(),
//                                        ((EditText)findViewById(R.id.Name_Register_input)).getText().toString(),
//                                        ((EditText)findViewById(R.id.Email_Register_Input)).getText().toString());
                            HashMap<String, Object> user = new HashMap<>();
//                            user.put("name", ((EditText)findViewById(R.id.Name_Register_input)).getText().toString());
//                            user.put("email", ((EditText)findViewById(R.id.Email_Register_Input)).getText().toString());
//                            user.put("password", ((EditText)findViewById(R.id.Password_Register_Input)).getText().toString());
                            ref.child(String.valueOf(Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey()) + 1)).setValue(user);
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
}
