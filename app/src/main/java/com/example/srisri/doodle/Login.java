package com.example.srisri.doodle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = (Button) findViewById(R.id.enter_button);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }
    public void registerPage(View view) {

        Intent intent = new Intent(this, CustomRegistration.class);
        startActivity(intent);
    }

    private void login(){
        Log.v("Login",((EditText)findViewById(R.id.email_login)).getText().toString());
        Log.v("Login", ((EditText)findViewById(R.id.password_login)).getText().toString());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users");
        Query query = ref.orderByChild("email").equalTo(((EditText)findViewById(R.id.email_login)).getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("login", dataSnapshot.getValue().toString());
                DataSnapshot ret = dataSnapshot.getChildren().iterator().next();
                if(dataSnapshot.getValue() != null &&
                        ret.child("password").getValue().toString().equals(((EditText)findViewById(R.id.password_login)).getText().toString())) {
                    Intent homepage = new Intent(Login.this, Dashboard.class);
                    homepage.putExtra("userID", dataSnapshot.getChildren().iterator().next().getKey());
                    Log.v("userid", dataSnapshot.getChildren().iterator().next().getKey());
                    GlobalVars.getInstance().setUser(ret.child("name").getValue().toString(),
                            ret.child("email").getValue().toString(), ret.child("password").getValue().toString());
                    startActivity(homepage);
                    ((EditText)findViewById(R.id.email_login)).setText("");
                    ((EditText)findViewById(R.id.password_login)).setText("");
//                    findViewById(R.id.Invalid_Login).setVisibility(View.INVISIBLE);
                } else {
//                    findViewById(R.id.Invalid_Login).setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
