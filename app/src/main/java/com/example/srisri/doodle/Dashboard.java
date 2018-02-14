package com.example.srisri.doodle;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Dashboard extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authList;
    GoogleSignInClient mGoogleSignInClient;
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
            case R.id.delete_account_menu:
                deleteAccountPopup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){

        Toast.makeText(Dashboard.this, "Logout", Toast.LENGTH_SHORT).show();

        /*if(auth.getCurrentUser()!= null)
        {
        auth = FirebaseAuth.getInstance();
        auth.signOut();
        }*/
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

           // FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
       mGoogleSignInClient.signOut();
             //   .addOnCompleteListener(this, new OnCompleteListener<Void>() {
               //     @Override
                 //   public void onComplete(@NonNull Task<Void> task) {
                   //     // ...
                    //}
                //});

        Intent loginscreen = new Intent(this, Login.class);
        loginscreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        GlobalVars.logout();
        //if(auth.getCurrentUser()!= null)
        //{
           // auth = FirebaseAuth.getInstance();
            //auth.signOut();
        //}
        startActivity(loginscreen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        Button delButton = (Button) findViewById(R.id.btnDelAcc);
//        delButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deleteAccountPopup();
//            }
//        });


    }
    public void deleteAccountPopup(){

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
            }
        });

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
    public void deleteAcc(){


        DatabaseReference drUser = FirebaseDatabase.getInstance().getReference("Users").child(GlobalVars.getUserID());

        drUser.removeValue();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        mGoogleSignInClient.revokeAccess();
        Intent loginscreen = new Intent(this, Login.class);
        loginscreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        GlobalVars.logout();
        startActivity(loginscreen);
    }
    public void openEvent(View view) {
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);
    }

    public void goToPollsCreated(View view) {
        Intent intent = new Intent(this, PollsCreated.class);
        startActivity(intent);
    }
}


