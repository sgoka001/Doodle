package com.example.srisri.doodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class send_invite extends AppCompatActivity {

    final ArrayList<String> setEmails = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invite);

        Button sendInvites = (Button)findViewById(R.id.send_invites);
        sendInvites.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                sendInvites();
            }
        });

        initAutocomplete();

    }

    public void initAutocomplete(){
        final ArrayAdapter<String> setEmailsAdapter = new ArrayAdapter<String>(this, R.layout.send_invite_row,
                R.id.inviteEmail, setEmails);
        ListView emailsDisplayed = (ListView)findViewById(R.id.displayEmails) ;
        emailsDisplayed.setAdapter(setEmailsAdapter);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new String[] {});
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.email_autocomplete_sendInvite);
        textView.setAdapter(adapter);

        emailsDisplayed.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                //do your job here, position is the item position in ListView
                Log.v("testing", Integer.toString(position));
                setEmails.remove(position);
                setEmailsAdapter.notifyDataSetChanged();
            }
        });

        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                autocompleteEmail(textView.getText().toString(), adapter);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.v("emailGrabbed", adapter.getItem(position).toString());
                //add code to add email to invite list here
                Boolean add = true;
                for(int i = 0; i < setEmails.size(); ++i){
                    if(setEmails.get(i).equals(textView.getText().toString())){
                        add = false;
                        break;
                    }
                }
                if(add) {
                    setEmails.add(textView.getText().toString());
                    setEmailsAdapter.notifyDataSetChanged();
                }
                textView.setText("");
            }

        });
    }

    public void sendInvites(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("invites");
        Query start = ref.orderByKey().limitToLast(1);
        start.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int first = Integer.parseInt(dataSnapshot.getChildren().iterator().next().getKey()) + 1;
                for(int i = 0; i < setEmails.size(); ++i){
                    HashMap<String, Object> invite = new HashMap<>();
                    invite.put("email", setEmails.get(i));
                    invite.put("eventId", 1);
                    invite.put("accepted", false);
                    invite.put("declined", false);
                    ref.child(Integer.toString(first  + i)).setValue(invite);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    Semaphore adapterClear = new Semaphore(1);
    public void autocompleteEmail(String email, final ArrayAdapter<String> adapter){
        Log.v("autocomplete", "searching");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users");
        Query query = ref.orderByChild("email").startAt(email).endAt(email + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    adapterClear.acquire();
                } catch(InterruptedException e){
                    Log.v("error", e.toString());
                }
                adapter.clear();
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    Log.v("email", child.child("email").getValue().toString());
                    adapter.add(child.child("email").getValue().toString());
                }
                adapterClear.release();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
