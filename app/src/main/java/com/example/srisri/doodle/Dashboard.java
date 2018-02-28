package com.example.srisri.doodle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    FirebaseAuth authu;
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userEmail", "none");
        editor.putString("password", "none");
        editor.putString("name", "none");
        editor.putString("id", "none");
        editor.commit();

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

    AcceptEventAdapter acceptInvites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        final ListView invites = (ListView)findViewById(R.id.event_invite_pending);
        acceptInvites = new AcceptEventAdapter(this, R.layout.event_accept_listview, pendingInvites);
        invites.setAdapter(acceptInvites);

        DatabaseReference dbUserInvites = FirebaseDatabase.getInstance().getReference("invites");
        Query query = dbUserInvites.orderByChild("email").equalTo(GlobalVars.getUserEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator invitesItterator = dataSnapshot.getChildren().iterator();
                while(invitesItterator.hasNext()) {
                    DataSnapshot tmp = (DataSnapshot) invitesItterator.next();
                    Iterator childeren = tmp.getChildren().iterator();
                    DataSnapshot checker = (DataSnapshot)childeren.next();
                    Boolean valid = true;
                    int eventId = 0;
                    while (checker != null){
                        String Key = checker.getKey();
                        switch(Key){
                            case "accepted":
                                if(String.valueOf(checker.getValue()).equals("true"))
                                    valid = false;
                                break;
                            case "declined":
                                if(String.valueOf(checker.getValue()).equals("true"))
                                    valid = false;
                                break;
                            case "eventId":
                                eventId = Integer.parseInt(checker.getValue().toString());
                                break;
                            default:
                                break;
                        }
                        if(childeren.hasNext())
                            checker = (DataSnapshot)childeren.next();
                        else
                            break;
                    }
                    if(valid){
                        getEventDetails(eventId, tmp.getKey(), acceptInvites, invites);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    ArrayList<String> pendingInvites = new ArrayList<String>();
    ArrayList<String> pendingInviteIds = new ArrayList<String>();

    public void getEventDetails(int eventId, final String key, final AcceptEventAdapter acceptInvites, final ListView list){
        Log.v("testing", "looking for event");
        DatabaseReference dbUserInvites = FirebaseDatabase.getInstance().getReference("Events");
        Query query = dbUserInvites.orderByKey().equalTo(String.valueOf(eventId));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator event = dataSnapshot.getChildren().iterator();
                while(event.hasNext()){
                    DataSnapshot child = (DataSnapshot)event.next();
                    Iterator details = child.getChildren().iterator();
                    while(details.hasNext()) {
                        DataSnapshot childDetails = (DataSnapshot)details.next();
                        Log.v("testing", childDetails.getKey());
                        if (childDetails.getKey().equals("name")) {
                            Log.v("testingadd", "event found");
                            synchronized (pendingInvites){
                                pendingInvites.add(pendingInvites.size() + ") " + childDetails.getValue().toString());
                                pendingInviteIds.add(key);
                                acceptInvites.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                }
                Log.v("testing", pendingInvites.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
    /*
    public void openEvent(View view) {
        Intent intent = new Intent(this, NewEvent.class);
        startActivity(intent);
    }
    */
    public void openMap(View view) {
        Intent intent = new Intent(this, Location.class);
        startActivity(intent);
    }

    public void openEvent2(View view) {
        Intent intent = new Intent(this, PollsCreated.class);
        startActivity(intent);
    }
    public void openEvent3(View view) {
        Intent intent = new Intent(this, PollsParticipated.class);
        startActivity(intent);
    }
    private class AcceptEventAdapter extends ArrayAdapter<String> {
        private int layout;
        public AcceptEventAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            AcceptEventViewHolder mainHolder;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final AcceptEventViewHolder holder = new AcceptEventViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.event_name_invite);
                holder.name.setText(pendingInvites.get(position));
                holder.Accept = (Button) convertView.findViewById(R.id.accept_button_invite);
                holder.Accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference dbUserInvites = FirebaseDatabase.getInstance().getReference("invites");
                        int id = pendingInvites.indexOf(holder.name.getText());
                        String eventId = pendingInviteIds.get(id);
                        dbUserInvites.child(eventId).child("accepted").setValue(true);
                        pendingInviteIds.remove(id);
                        pendingInvites.remove(id);
                        acceptInvites.notifyDataSetChanged();
                    }
                });
                holder.reject = (Button) convertView.findViewById(R.id.decline_button_invite);
                holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference dbUserInvites = FirebaseDatabase.getInstance().getReference("invites");
                        int id = pendingInvites.indexOf(holder.name.getText());
                        String eventId = pendingInviteIds.get(id);
                        dbUserInvites.child(eventId).child("declined").setValue(true);
                        pendingInviteIds.remove(id);
                        pendingInvites.remove(id);
                        acceptInvites.notifyDataSetChanged();
                    }
                });
                convertView.setTag(holder);
            } else {
                mainHolder = (AcceptEventViewHolder) convertView.getTag();
                mainHolder.name.setText(getItem(position));
            }
            return convertView;
        }
    }

    public class AcceptEventViewHolder {
        TextView name;
        Button Accept;
        Button reject;
    }
}


