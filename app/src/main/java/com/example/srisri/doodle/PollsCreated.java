package com.example.srisri.doodle;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PollsCreated extends AppCompatActivity {
   FirebaseAuth authu;
   String title;
   ArrayList<String> eventss = new ArrayList<String>();
   ArrayList<String> titles = new ArrayList<String>();
   ArrayList<String> eventid = new ArrayList<String>();
   ListView mListView;
    ArrayAdapter<String> adapters;
    FirebaseDatabase database;
    CreatedAdapter createAdapter;


    //private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polls_created);
        authu = FirebaseAuth.getInstance();
       // adapters=new ArrayAdapter<String>(this,R.layout.create_help,eventss);
        //mListView.setAdapter(adapters);
        String inst = GlobalVars.getInstance().getUserEmail();
        //final String inst = "pgior001@ucr.edu";
        mListView = (ListView)findViewById(R.id.listlist);
        //final FirebaseUser user = authu.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Events");
        Query query = ref.orderByChild("owner").equalTo(inst);
        Log.v("Query", query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null ) {
                    Iterator ret = dataSnapshot.getChildren().iterator();
                    while(ret.hasNext()) {
                        DataSnapshot rets = (DataSnapshot)ret.next();
                        title = (rets.child("location").getValue().toString());//+ " \n" + ret.child("name").getValue().toString());
                        String lala = rets.child("name").getValue().toString();
                        String id = rets.getKey();
                        GlobalVars.getInstance().setEventID(rets.getKey() + 1);
                        eventss.add(title.toString());
                        titles.add(lala.toString());
                        eventid.add(id);
                        createAdapter = new CreatedAdapter(PollsCreated.this, R.layout.create_help, eventss);
                        mListView.setAdapter(createAdapter);
                        createAdapter.notifyDataSetChanged();
                    }
                    //adapters = new ArrayAdapter<String>(PollsCreated.this,R.layout.create_help, R.id.textView5,titles);
                    //mListView.setAdapter(adapters);
                    //adapters.notifyDataSetChanged();
                    Log.v("title2",eventss.get(0));
                    Log.v("title", Integer.toString(eventid.size()));
                    //GlobalVars.getInstance().setUser(user.getDisplayName(),user.getEmail(),ret.getKey().toString());
                    //Log.v("key",ret.getKey().toString());
                    //Button button = (Button) findViewById(R.id.textView3);
                    //button.setText(title);
                                                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
            ;

            //adapters.notifyDataSetChanged();
            mListView = (ListView) findViewById(R.id.listlist);
            //eventss.add(title);
        createAdapter = new CreatedAdapter(PollsCreated.this,R.layout.create_help,eventss);
        //adapters=new ArrayAdapter<String>(PollsCreated.this,R.layout.create_help,R.id.textView3,eventss);
        mListView.setAdapter(createAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                    GlobalVars.getInstance().setEvent(eventid.get(position));
                    Intent myIntent = new Intent(view.getContext(), ViewEventInfo.class);
                    startActivityForResult(myIntent, 0);


            }
        });
    }

    private void moreInfo(View view)
    {
        Intent intent = new Intent(this, ViewEventInfo.class);
        startActivity(intent);
    }

    private class CreatedAdapter extends ArrayAdapter<String> {
        private int layout;
        private Context mContext;
        private LayoutInflater mInflater;
        private List<String> mDataSource;
        public CreatedAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
            mDataSource = objects;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get view for row item
            mInflater = LayoutInflater.from(getContext());
            View rowView = mInflater.inflate(R.layout.create_help, parent, false);
            TextView textView = (TextView)rowView.findViewById(R.id.textView3);
            TextView loc = (TextView)rowView.findViewById(R.id.textView5);
            textView.setText(titles.get(position));
            loc.setText(mDataSource.get(position));

            return rowView;
        }
    }

}


