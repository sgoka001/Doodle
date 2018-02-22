package com.example.srisri.doodle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ViewEventInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_info);

        TextView text = (TextView)findViewById(R.id.textView4);
        String words = GlobalVars.getInstance().getEventID().toString();
        Log.v("eventId", words);
        text.setText(words);
    }
}
