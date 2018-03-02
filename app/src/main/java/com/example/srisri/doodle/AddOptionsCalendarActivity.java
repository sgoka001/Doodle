package com.example.srisri.doodle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AddOptionsCalendarActivity extends AppCompatActivity {
    private CalendarPickerView calendar;

    ArrayList<String> dates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_options_calendar);
        final CalendarPickerView calendar_view = (CalendarPickerView) findViewById(R.id.calendar_view);
        //getting current
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();

        //add one year to calendar from todays date
        calendar_view.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        Button btn_show_dates = (Button) findViewById(R.id.btn_show_dates);
        btn_show_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i< calendar_view.getSelectedDates().size();i++){


                    dates.add(calendar_view.getSelectedDates().get(i).toString());

                }
                Intent selecteddates = new Intent();
                selecteddates.putExtra("SelectedDates", dates);
                setResult(RESULT_OK, selecteddates);
                finish();
            }
        });
    }

}
