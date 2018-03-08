package com.example.srisri.doodle;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

public class ViewEventInfo extends AppCompatActivity {

    ArrayList<String> users = new ArrayList<String>();
    ListView userList;
    String username;
    FirebaseDatabase database;
    CreatedAdapter createAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_info);
        String eid = GlobalVars.getInstance().getEventID();
        Log.v("Eid", eid);
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("invites");
        Query query = ref.orderByChild("eventId").equalTo(Integer.valueOf(eid));
        Log.v("Query", query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null ) {
                    Log.v("datasnap", dataSnapshot.getValue().toString());
                    Iterator ret = dataSnapshot.getChildren().iterator();
                    //title=(ret.child("location").getValue().toString());//+ " \n" + ret.child("name").getValue().toString());
                    //String lala = ret.child("name").getValue().toString();
                    while(ret.hasNext()) {
                        DataSnapshot rets = (DataSnapshot)ret.next();
                        String user = rets.child("email").getValue().toString();
                        Log.v("id", user);
                        //GlobalVars.getInstance().setEventID(ret.getKey()+1);
                        users.add(user);
                        createAdapter = new CreatedAdapter(ViewEventInfo.this, R.layout.event_info_help, users);
                        userList.setAdapter(createAdapter);
                        createAdapter.notifyDataSetChanged();
                    }
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
        DatabaseReference dbEventInfo = FirebaseDatabase.getInstance().getReference("Events");
        query = dbEventInfo.orderByKey().equalTo(GlobalVars.getEventID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!= null)
                {
                    DataSnapshot val = dataSnapshot.getChildren().iterator().next();
                    String title = val.child("name").getValue().toString();
                    String location = val.child("location").getValue().toString();
                    TextView text = (TextView)findViewById(R.id.textView4);
                    TextView textloc = (TextView)findViewById(R.id.textView7);
                    text.setText(title);
                    textloc.setText(location);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ;

        //adapters.notifyDataSetChanged();
        createAdapter = new CreatedAdapter(ViewEventInfo.this, R.layout.event_info_help, users);
        userList.setAdapter(createAdapter);
        createAdapter.notifyDataSetChanged();

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
            View rowView = mInflater.inflate(R.layout.event_info_help, parent, false);
            TextView loc = (TextView)rowView.findViewById(R.id.textView);
            loc.setText(mDataSource.get(position));

            return rowView;
        }
    }
}
