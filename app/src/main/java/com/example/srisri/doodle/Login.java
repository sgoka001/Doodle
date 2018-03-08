package com.example.srisri.doodle;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

public class Login extends AppCompatActivity{

    SignInButton googlebutton;
    FirebaseAuth authu;

    private final static int GOOGLESN=2;
    GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toast.makeText(Login.this,"OnCreate",Toast.LENGTH_SHORT).show();
        Button loginBtn=(Button)findViewById(R.id.enter_button);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                login();
            }
        });
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googlebutton=(SignInButton)findViewById(R.id.sign_in_button);
        mGoogleSignInClient=GoogleSignIn.getClient(this,gso);
        googlebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                signIn();
            }
        });
        authu=FirebaseAuth.getInstance();
        final SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String id=sharedPref.getString("id","none");
        Log.v("userID", String.valueOf(!id.equals("none")));
        if(!id.equals("none")){
            Log.v("userID", "accepted");
            final FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference ref=database.getReference("Users");
            Query query=ref.orderByKey().equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        Iterator childeren = dataSnapshot.getChildren().iterator().next().getChildren().iterator();
                        //Iterator childeren = dataSnapshot.getChildren().iterator();
                        while (childeren.hasNext()) {
                            DataSnapshot child = (DataSnapshot) childeren.next();
                            if (child.getKey().equals("password")) {
                                if (child.getValue().toString().equals(sharedPref.getString("password", "none"))) {
                                    GlobalVars.getInstance().setUser(sharedPref.getString("name", "none"),
                                            sharedPref.getString("userEmail", "none"), sharedPref.getString("password", "none"),
                                            sharedPref.getString("id", "none"));
                                    Intent dashboard = new Intent(Login.this, Dashboard.class);
                                    startActivity(dashboard);
                                }
                            }
                            else
                            {
                                if(child.getValue().equals(sharedPref.getString("name","none"))) {
                                    GlobalVars.getInstance().setUser(sharedPref.getString("name", "none"),
                                            sharedPref.getString("userEmail", "none"),
                                            sharedPref.getString("id", "none"));
                                    Intent dashboard = new Intent(Login.this, Dashboard.class);
                                    startActivity(dashboard);
                                }
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError){

                }
            });
        }


    }

    @Override
    protected void onStart(){
        super.onStart();
        Toast.makeText(Login.this,"OnStart",Toast.LENGTH_SHORT).show();
        final FirebaseUser user=authu.getCurrentUser();
        //Log.v("user2", user.getEmail());
        final GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);

        if(account!=null)
        {
            final FirebaseDatabase database=FirebaseDatabase.getInstance();

            DatabaseReference ref=database.getReference("Users");
            Query query=ref.orderByChild("email").equalTo(user.getEmail());
            query.addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                    if(dataSnapshot.getValue()!=null){
                        DataSnapshot ret=dataSnapshot.getChildren().iterator().next();
                        GlobalVars.getInstance().setUser(user.getDisplayName(),user.getEmail(),ret.getKey().toString());
                        Log.v("key",ret.getKey().toString());
                        Log.v("user3", user.getEmail());
                                /*Intent dashboard=new Intent(Login.this,Dashboard.class);
                                startActivity(dashboard);*/
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError){

                }
            });


            Intent dashboard=new Intent(Login.this,Dashboard.class);
            startActivity(dashboard);
        }


    }

    public void registerPage(View view){
        Intent intent=new Intent(this,CustomRegistration.class);
        startActivity(intent);
    }

    private void login(){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("Users");
        Query query=ref.orderByChild("email").equalTo(((EditText)findViewById(R.id.email_login)).getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Log.v("testtest",dataSnapshot.toString());
                if(dataSnapshot.getValue()!=null&&dataSnapshot.getChildren().iterator().next().child("password").getValue()!=null&&
                        dataSnapshot.getChildren().iterator().next().child("password").getValue().toString().equals(((EditText)findViewById(R.id.password_login)).getText().toString())){
                    DataSnapshot ret=dataSnapshot.getChildren().iterator().next();
                    Intent dashboard=new Intent(Login.this,Dashboard.class);
                    GlobalVars.getInstance().setUser(ret.child("name").getValue().toString(),
                            ret.child("email").getValue().toString(),ret.child("password").getValue().toString(),
                            ret.getKey().toString());
                    SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor=sharedPref.edit();
                    editor.putString("userEmail",ret.child("email").getValue().toString());
                    editor.putString("password",ret.child("password").getValue().toString());
                    editor.putString("name",ret.child("name").getValue().toString());
                    editor.putString("id",ret.getKey().toString());
                    editor.commit();
                    startActivity(dashboard);
                    ((EditText)findViewById(R.id.email_login)).setText("");
                    ((EditText)findViewById(R.id.password_login)).setText("");
                    findViewById(R.id.Invalid_Login).setVisibility(View.INVISIBLE);
                }else{
                    findViewById(R.id.Invalid_Login).setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }
        });
    }
    /*/GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();*/

    private void signIn(){
        Intent signInIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,GOOGLESN);
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        final FirebaseUser user=authu.getCurrentUser();
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if(requestCode==GOOGLESN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                    final FirebaseDatabase database=FirebaseDatabase.getInstance();
                    final GoogleSignInAccount accounts=GoogleSignIn.getLastSignedInAccount(this);
                    DatabaseReference ref=database.getReference("Users");
                    Query query=ref.orderByChild("email").equalTo(user.getEmail());
                    query.addListenerForSingleValueEvent(new ValueEventListener(){
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot){
                            if(dataSnapshot.getValue()!=null){
                                DataSnapshot ret=dataSnapshot.getChildren().iterator().next();

                                if(!accounts.getEmail().equals(user.getEmail())) {
                                    GlobalVars.getInstance().setUser(user.getDisplayName(), user.getEmail(), ret.getKey().toString());
                                    Log.v("key", ret.getKey().toString());
                                    Log.v("user1", user.getEmail());
                                /*Intent dashboard=new Intent(Login.this,Dashboard.class);
                                startActivity(dashboard);*/
                                }
                            }

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError){

                        }
                    });
                /*GlobalVars.getInstance().setUser(user.getDisplayName(),user.getEmail(),user.get);
                Toast.makeText(Login.this, "set globe vars2", Toast.LENGTH_SHORT).show();
                Intent dashboard = new Intent(Login.this, Dashboard.class);
                startActivity(dashboard);*/
            }catch(ApiException e){
                // Google Sign In failed, update UI appropriately
                Toast.makeText(Login.this,"Error2",Toast.LENGTH_SHORT).show();
                //Log.w(TAG, "Google sign in failed", e);
                // ...
            }

        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        Log.d("TAG","firebaseAuthWithGoogle:"+account.getId());

        AuthCredential credential=GoogleAuthProvider.getCredential(account.getIdToken(),null);
        authu.signInWithCredential(credential)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG","signInWithCredential:success");
                            final FirebaseUser user=authu.getCurrentUser();
                            final FirebaseDatabase database=FirebaseDatabase.getInstance();
                            final DatabaseReference ref=database.getReference("Users");
                            Query query=ref.orderByChild("email").equalTo(user.getEmail());
                            query.addListenerForSingleValueEvent(new ValueEventListener(){
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot){
                                    if(dataSnapshot.getChildrenCount()!=0){
                                        //throw null because user found with email
                                        Log.v("register","error email already in use");
                                    }else{
                                        Query id=ref.orderByKey().limitToLast(1);
                                        id.addListenerForSingleValueEvent(new ValueEventListener(){
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot){
                                                Log.v("register",dataSnapshot.getChildren().iterator().next().getKey());
                                                Log.v("register",String.valueOf(Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey())+1));
                                                HashMap<String, Object> users=new HashMap<>();
                                                users.put("name",(user.getDisplayName()));
                                                users.put("email",((user.getEmail())));
                                                //users.put("password", ((EditText)findViewById(R.id.registration_password)).getText().toString());
                                                ref.child(String.valueOf(Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey())+1)).setValue(users);
                                                Intent dashboard=new Intent(Login.this,Dashboard.class);
                                                Log.v("userid",dataSnapshot.getChildren().iterator().next().getKey());
                                                Query query=ref.orderByChild("email").equalTo(user.getEmail());
                                                query.addListenerForSingleValueEvent(new ValueEventListener(){
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot){
                                                        if(dataSnapshot.getValue()!=null){
                                                            DataSnapshot ret=dataSnapshot.getChildren().iterator().next();
                                                            GlobalVars.getInstance().setUser(user.getDisplayName(),user.getEmail(),ret.getKey().toString());
                                                            /*Intent dashboard=new Intent(Login.this,Dashboard.class);
                                                            startActivity(dashboard);*/
                                                        }

                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError){

                                                    }
                                                });

                                                /*GlobalVars.getInstance().setUser(user.getDisplayName(),user.getEmail());
                                                Toast.makeText(Login.this, "set globe vars", Toast.LENGTH_SHORT).show();
                                                startActivity(dashboard);*/
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError){

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError){

                                }
                            });

                            //User newU = new User(user.getEmail(), user.getDisplayName());

                            //user.getDisplayName();
                            mDatabase=FirebaseDatabase.getInstance().getReference();
                            Intent dashboard=new Intent(Login.this,Dashboard.class);
                            startActivity(dashboard);
                            //mDatabase.child("Users").setValue(newU);


                            //updateUI(user);
                        }else{
                            // If sign in fails, display a message to the user.
                            Log.w("TAG","signInWithCredential:failure",task.getException());
                            Toast.makeText(Login.this,"Error1",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public class User {
        public String name;
        public String email;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String email, String name) {
            this.name = name;
            this.email = email;
        }

    }
}
