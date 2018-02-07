package com.example.srisri.doodle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Semaphore;

public class send_invite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invite);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new String[] {});
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.email_autocomplete_sendInvite);
        textView.setAdapter(adapter);
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
                textView.setText("");
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
