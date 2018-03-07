package com.example.srisri.doodle;

import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.firebase.auth.AuthCredential;

import java.io.IOException;
import java.util.Arrays;

public class googleCalendar {
    GoogleAccountCredential mCredential;

    public googleCalendar(GoogleAccountCredential mCredential) {
        this.mCredential = mCredential;
    }

    public void createEvent() {

        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mCredential.setSelectedAccountName("preston.giorgianni@gmail.com");
        com.google.api.services.calendar.Calendar service = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName("com.example.srisri.doodle")
                .build();


        Event event = new Event()
                .setSummary("Event- April 2016")
                .setLocation("Dhaka")
                .setDescription("New Event 1");

        DateTime startDateTime = new DateTime("2018-04-17T18:10:00+06:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Pacific");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2018-04-17T18:40:00+06:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Pacific");
        event.setEnd(end);

        String[] recurrence = new String[]{"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[]{
                new EventAttendee().setEmail("preston.giorgianni@gmail.com"),
                new EventAttendee().setEmail("asdasd@andlk.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[]{
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "primary";
        Log.v("calendar", mCredential.getSelectedAccountName() + ": empty?");
        Log.v("calendar", service.getApplicationName());
        Log.v("calendar", event.toString());
        try {
            event = service.events().insert(calendarId, event).execute();
            Log.v("calendarTest", "event");
        } catch (UserRecoverableAuthIOException e) {
            Log.v("calendar", e.getIntent().toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("calendarTest", e.toString());
        }
        Log.v("calendarTest", "tried to set event");

    }
}
