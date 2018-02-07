package com.example.srisri.doodle;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button mDelAccPopup = findViewById(R.id.btnDelAcc);
        mDelAccPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(Dashboard.this);
                View mView = getLayoutInflater().inflate(R.layout.activity_delete_acc, null);
                Button mConfirm = mView.findViewById(R.id.btnYes);

                mConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                    public void onClick(View view) {
                        Toast.makeText(Dashboard.this, "Account Deleted", Toast.LENGTH_LONG).show();
                        //delete account in database
                        deleteAcc();
                        //set the data of the username, email, pass to NULL
                        GlobalVars.setUser(null,null,null);
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });
    }
    public void deleteAcc(){
        DatabaseReference drUser = FirebaseDatabase.getInstance().getReference("username").child(GlobalVars.getUserName());
        drUser.removeValue();
    }
    public void openEvent(View view) {
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);
    }
}


