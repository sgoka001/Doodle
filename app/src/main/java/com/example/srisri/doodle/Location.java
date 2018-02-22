package com.example.srisri.doodle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class Location extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    public static CharSequence name;
    public CharSequence address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            final Intent intent = intentBuilder.build(this);
            startActivityForResult(intentBuilder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            //chose place
            Place place = PlacePicker.getPlace(data, this);

            //store restaurant info
            name = place.getName();
            address = place.getAddress();

            String location = name + ": " + address;
            Intent event_location = new Intent(Location.this, NewEvent.class);
            event_location.putExtra("location", location);
            startActivity(event_location);

            /*
            //notification saying restaurant address
            String toastMsg = String.format("Place: %s", address);
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            */
        }
    }
}
