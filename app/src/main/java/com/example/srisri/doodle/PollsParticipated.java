package com.example.srisri.doodle;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PollsParticipated extends AppCompatActivity {

    FirebaseAuth authu;
    String title;
    ArrayList<String> eventss = new ArrayList<String>();
    ArrayList<String> titles = new ArrayList<String>();
    ArrayList<String> inviteid = new ArrayList<String>();
    ListView mListView;
    ArrayAdapter<String> adapters;
    FirebaseDatabase database;
    ParticipatedAdapter partAdapter;


    //private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polls_participated);
        authu = FirebaseAuth.getInstance();
        String Eid;
        // adapters=new ArrayAdapter<String>(this,R.layout.create_help,eventss);
        //mListView.setAdapter(adapters);
        String inst = GlobalVars.getInstance().getUserEmail();

        //final String inst = "pgior001@ucr.edu";
        mListView = (ListView)findViewById(R.id.listlist2);
        //final FirebaseUser user = authu.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("invites");
        Query query = ref.orderByChild("email").equalTo(inst);
        Log.v("Query", query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null ) {
                    Iterator ret = dataSnapshot.getChildren().iterator();
                    //title=(ret.child("location").getValue().toString());//+ " \n" + ret.child("name").getValue().toString());
                    //String lala = ret.child("name").getValue().toString();
                    while(ret.hasNext()) {
                        DataSnapshot rets = (DataSnapshot)ret.next();
                        String id = rets.getKey();
                        String truth = rets.child("declined").getValue().toString();
                        Log.v("id", id);
                        String Eid = rets.child("eventId").getValue().toString();
                        Log.v("eventid", Eid);
                        //GlobalVars.getInstance().setEventID(ret.getKey()+1);
                        if(truth.equals("false")) {
                            getEventInfo(Eid, id);
                        }
                    }

                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            GlobalVars.getInstance().setInvite(inviteid.get(position));
                            Intent myIntent = new Intent(view.getContext(), Polling.class);
                            startActivityForResult(myIntent, 0);     }
                    });
                    //inviteid.add(id.toString());
                    //String count = (inviteid.size());
                    //Log.v("inviteId", );
                    //partAdapter = new ParticipatedAdapter(PollsParticipated.this,R.layout.create_help,eventss);
                    //mListView.setAdapter(partAdapter);
                    //partAdapter.notifyDataSetChanged();
                    //adapters = new ArrayAdapter<String>(PollsCreated.this,R.layout.create_help, R.id.textView5,titles);
                    //mListView.setAdapter(adapters);
                    //adapters.notifyDataSetChanged();
                    //Log.v("title2",eventss.get(0));
                    //Log.v("title", title);
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

    }

    public void getEventInfo(final String EventId, final String InviteId)
    {
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Events");
        Query query = ref.orderByKey().equalTo(EventId);
        Log.v("Query", query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null ) {
                    DataSnapshot ret = dataSnapshot.getChildren().iterator().next();
                    title=(ret.child("location").getValue().toString());
                    Log.v("title2",title);//+ " \n" + ret.child("name").getValue().toString());
                    String lala = ret.child("name").getValue().toString();
                    eventss.add(title.toString());
                    titles.add(lala.toString());
                    inviteid.add(InviteId.toString());
                    partAdapter = new ParticipatedAdapter(PollsParticipated.this,R.layout.create_help,eventss);
                    mListView.setAdapter(partAdapter);
                    partAdapter.notifyDataSetChanged();
                    Log.v("length", Integer.toString(inviteid.size()));
                    Log.v("lengtheventss", Integer.toString(eventss.size()));
                    //adapters = new ArrayAdapter<String>(PollsCreated.this,R.layout.create_help, R.id.textView5,titles);
                    //mListView.setAdapter(adapters);
                    //adapters.notifyDataSetChanged();
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
    }

    private void moreInfo(View view)
    {
        Intent intent = new Intent(this, ViewEventInfo.class);
        startActivity(intent);
    }


    private class ParticipatedAdapter extends ArrayAdapter<String> {
        private int layout;
        private Context mContext;
        private LayoutInflater mInflater;
        private List<String> mDataSource;
        public ParticipatedAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
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
            TextView id = (TextView)rowView.findViewById(R.id.textView6);
            textView.setText(eventss.get(position));
            loc.setText(titles.get(position));
            id.setText(inviteid.get(position));


            return rowView;
        }
    }

}

