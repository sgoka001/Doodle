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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PollsCreated extends AppCompatActivity {
   FirebaseAuth authu;
   String title;
   ArrayList<String> eventss = new ArrayList<String>();
   ArrayList<String> titles = new ArrayList<String>();
   ListView mListView;
    ArrayAdapter<String> adapters;
    FirebaseDatabase database;

    //private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polls_created);
        authu = FirebaseAuth.getInstance();
        TextView txt = (TextView) findViewById(R.id.textbox);
        txt.setText("Hello");
       // adapters=new ArrayAdapter<String>(this,R.layout.create_help,eventss);
        //mListView.setAdapter(adapters);
        //String inst = GlobalVars.getInstance().getUserEmail();
        final String inst = "pgior001@ucr.edu";
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
                    DataSnapshot ret = dataSnapshot.getChildren().iterator().next();
                    title=(ret.child("location").getValue().toString());//+ " \n" + ret.child("name").getValue().toString());
                    String lala = ret.child("name").getValue().toString();
                    GlobalVars.getInstance().setEventID(ret.getKey());
                    eventss.add(title.toString());
                    titles.add(lala.toString());
                    adapters=new ArrayAdapter<String>(PollsCreated.this,R.layout.create_help,R.id.textView3,eventss);
                    mListView.setAdapter(adapters);
                    adapters.notifyDataSetChanged();
                    adapters = new ArrayAdapter<String>(PollsCreated.this,R.layout.create_help, R.id.textView5,titles);
                    mListView.setAdapter(adapters);
                    adapters.notifyDataSetChanged();
                    Log.v("title2",eventss.get(0));
                    Log.v("title", title);
                    //GlobalVars.getInstance().setUser(user.getDisplayName(),user.getEmail(),ret.getKey().toString());
                    //Log.v("key",ret.getKey().toString());
                    Toast.makeText(PollsCreated.this, "Query Database", Toast.LENGTH_SHORT).show();
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
        adapters=new ArrayAdapter<String>(PollsCreated.this,R.layout.create_help,R.id.textView3,eventss);
        mListView.setAdapter(adapters);
    }

    private void moreInfo(View view)
    {
        Intent intent = new Intent(this, ViewEventInfo.class);
        startActivity(intent);
    }

    public class CreateAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private ArrayList<String> mDataSource;

        public CreateAdapter(Context context, ArrayList<String> events) {
            mContext = context;
            mDataSource = events;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public Object getItem(int position) {
            return mDataSource.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public int getCount() {
            return mDataSource.size();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get view for row item
            View rowView = mInflater.inflate(R.layout.create_help, parent, false);
            Toast.makeText(PollsCreated.this, "View",Toast.LENGTH_SHORT).show();
            TextView textView = (TextView)rowView.findViewById(R.id.textView3);
            TextView loc = (TextView)rowView.findViewById(R.id.textView5);
            textView.setText(mDataSource.get(position));
            loc.setText("Home");

            return rowView;
        }
    }




    public void GetPollInfo()
    {
        final String inst = "pgior001@ucr.edu";
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Events");
        Query query = ref.orderByChild("owner").equalTo(inst);
        Log.v("Query", query.toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null ) {
                    DataSnapshot ret = dataSnapshot.getChildren().iterator().next();
                    title=(ret.child("location").getValue().toString()); //+ " \n" + ret.child("name").getValue().toString());
                    GlobalVars.getInstance().setEventID(ret.getKey());

                        eventss.add(title.toString());
                        //adapters.notifyDataSetChanged();
                    Log.v("title2",eventss.get(0));
                    Log.v("title", title);
                    //GlobalVars.getInstance().setUser(user.getDisplayName(),user.getEmail(),ret.getKey().toString());
                    //Log.v("key",ret.getKey().toString());
                    Toast.makeText(PollsCreated.this, "Query Database", Toast.LENGTH_SHORT).show();
                    //Button button = (Button) findViewById(R.id.textView3);
                    //button.setText(title);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

   /* public class CreatedAdapter extends BaseAdapter
    {
        private Context mContext;
        private LayoutInflater mInflater;
        private ArrayList<String> mDataSource;

        public CreatedAdapter(Context context, ArrayList<String> events) {
            mContext = context;
            mDataSource = events;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public Object getItem(int position) {
            return mDataSource.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public int getCount() {
            return mDataSource.size();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get view for row item
            View rowView = mInflater.inflate(R.layout.create_help, parent, false);

            TextView textView = (TextView)rowView.findViewById(R.id.textView3);

            textView.setText(title);

            return rowView;
        }
    }

   /* private class CreatedAdapter extends ArrayAdapter<String>
    {
        private int layout;
        public CreatedAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            layout = resource;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            CreatedAdapter mainHolder;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final CreatedAdapter holder = new CreatedAdapter();

                holder.name = (TextView) convertView.findViewById(R.id.event_name_invite);
                holder.name.setText(pendingInvites.get(position));
                holder.Accept = (Button) convertView.findViewById(R.id.accept_button_invite);
                holder.Accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //add on click code
                        Log.v("click", "accept:" + position);
                        Log.v("click", holder.name.getText().toString());
                    }
                });
                holder.reject = (Button) convertView.findViewById(R.id.decline_button_invite);
                holder.reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //add on click code
                        Log.v("click", "decline:" + position);
                    }
                });
                convertView.setTag(holder);
            } else {
                mainHolder = (Dashboard.AcceptEventViewHolder) convertView.getTag();
                mainHolder.name.setText(getItem(position));
            }
            return convertView;
        }

    }*/
}


