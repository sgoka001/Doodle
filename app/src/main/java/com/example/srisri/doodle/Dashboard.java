package com.example.srisri.doodle;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Dashboard extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout_menu:
                logout();
                return true;
            case R.id.btnDelAcc:
                deleteAccountPopup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        Intent loginscreen = new Intent(this, Login.class);
        loginscreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        GlobalVars.logout();
        startActivity(loginscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button delButton = (Button) findViewById(R.id.btnDelAcc);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccountPopup();
            }
        });


    }
    public void deleteAccountPopup(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Dashboard.this);
        View mView = getLayoutInflater().inflate(R.layout.activity_delete_acc, null);
        Button mConfirm = mView.findViewById(R.id.btnYes);

        mConfirm.setOnClickListener(new View.OnClickListener() {
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
                        GlobalVars.setUser(null,null, null, null);

                        finish();

                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
    public void deleteAcc(){

        DatabaseReference drUser = FirebaseDatabase.getInstance().getReference("Users").child(GlobalVars.getUserID());

        drUser.removeValue();
        Intent loginscreen = new Intent(this, Login.class);
        loginscreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        GlobalVars.logout();
        startActivity(loginscreen);
    }
    public void openEvent(View view) {
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);
    }
}


