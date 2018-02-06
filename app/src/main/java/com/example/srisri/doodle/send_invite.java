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
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Semaphore;

public class send_invite extends AppCompatActivity {

    private static String[] emails = new String [] {};
    private static
    ArrayAdapter<String> autocompleteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_invite);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, emails);
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.countries_list);
        textView.setAdapter(adapter);
        textView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                autocompleteEmail(textView.getText().toString());
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
                adapter.getItem(position).toString();
            }

        });
    }

    public static void autocompleteEmail(String email){
        final Semaphore semaphore = new Semaphore(0);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users");
        Query query = ref.orderByChild("email").startAt(email).endAt(email + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emails = new String[(int)dataSnapshot.getChildrenCount()];
                int i = 0;
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    Log.v("email", child.child("email").getValue().toString());
                    emails[i] = child.child("email").getValue().toString();
                    ++i;
                }
                semaphore.release();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
