package com.example.srisri.doodle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Kristen on 2/21/2018.
 */

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

        TextView textView = (TextView)rowView.findViewById(R.id.textView3);
        //TextView loc = (TextView)rowView.findViewById(R.id.textView5);
        textView.setText(mDataSource.get(position));
        //loc.setText("Home");

        return rowView;
    }
}
