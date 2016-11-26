package com.example.who.calendanot.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.who.calendanot.R;
import com.example.who.calendanot.utilites.Event;
import com.example.who.calendanot.utilites.Reminder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;


/**
 * Created by who on 23.11.2016.
 */

public class ReminderActivity extends AppCompatActivity {
    ListView mListView;
    Reminder mReminder;
    Button mButtonBefore;
    EditText mEditTextBefore;
    Button mButtonAfterCam;
    EditText mEditTextAfterCam;
    Button mButtonAfterScan;
    EditText mEditTextAfterScan;
    Button mButtonFillForm;
    EditText mEditTextFillForm;
    Event mEvent;
    long mEventStart;


    private static final String TAG = "EventActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_main);
        mListView = (ListView) findViewById(R.id.reminders);
        mButtonBefore = (Button) findViewById(R.id.reminderBefore);
        mEditTextBefore = (EditText) findViewById(R.id.input_minutes_before);
        mButtonAfterCam = (Button) findViewById(R.id.reminderAfterCam);
        mEditTextAfterCam = (EditText) findViewById(R.id.input_minutes_after_camera);
        mButtonAfterScan = (Button) findViewById(R.id.reminderAfterScan);
        mEditTextAfterScan = (EditText) findViewById(R.id.input_minutes_after_scan);
        mButtonFillForm = (Button) findViewById(R.id.reminderAfterFillForm);
        mEditTextFillForm = (EditText) findViewById(R.id.input_minutes_after_FillForm);


        final List<Reminder> reminders = mReminder.getRemindersForQuery(null, null, null, getContentResolver());
        final List<String> remindersAccountId = new ArrayList<>();

        int idEvent = getIntent().getExtras().getInt("EventId");
        for (Reminder re : reminders) {
            if (re.eventId == idEvent) {
                remindersAccountId.add("Reminder id =  " + re.id.toString() + "\n" + "Time before - " + re.minutesBefore + " minutes\n" + "Method - " + re.method + "\n");
            }
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, remindersAccountId);

        mListView.setAdapter(arrayAdapter);

        //calendar reminder set time
        mButtonBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idd = Integer.parseInt(mEditTextBefore.getText().toString());
                setReminder(getApplication().getContentResolver(), getIntent().getExtras().getInt("EventId"), idd);
                Log.d(TAG, "mButtonBefore " + getIntent().getExtras().getInt("EventId"));
            }
        });
        //camera notification set time
        mButtonAfterCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long minutess = Long.parseLong(mEditTextAfterCam.getText().toString());
                alarmManagerMinAfterEventPhoto(minutess);
            }
        });
        //Business Card Reader CRM Pro notification set time
        mButtonAfterScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long minutes = Long.parseLong(mEditTextAfterScan.getText().toString());
                alarmManagerMinAfterEventScan(minutes);
            }
        });
        mButtonFillForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long minutes = Long.parseLong(mEditTextFillForm.getText().toString());
                alarmManagerMinAfterEventFillForm(minutes);
            }
        });
    }


    //calendar reminder before event works
    private void setReminder(ContentResolver cr, long eventID, int timeBefore) throws SecurityException {
        try {
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Reminders.MINUTES, timeBefore);
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
            Cursor c = CalendarContract.Reminders.query(cr, eventID,
                    new String[]{CalendarContract.Reminders.MINUTES});
            if (c.moveToFirst()) {
                System.out.println("calendar"
                        + c.getInt(c.getColumnIndex(CalendarContract.Reminders.MINUTES)));
            }
            values.notifyAll();
            c.notifyAll();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method for making photo with standart camera app works
    private void alarmManagerMinAfterEventPhoto(long minutess) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent("com.example.who.calendanot.OPEN_CAMERA");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 101, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //find our event start time
        List<Event> events = mEvent.getEventsForQuery(null, null, null, getContentResolver());
        for (Event e : events) {
            if (e.id == getIntent().getExtras().getInt("EventId")) {
                String x = e.startDate;
                long miliSeconds = Long.parseLong(x);
                mEventStart = miliSeconds;
            }
        }

        //after event to do photo
        if (currentTimeMillis() <= mEventStart) {
            long timeInMillis = (mEventStart + TimeUnit.MINUTES.toMillis(minutess));
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, broadcast);
            Log.d(TAG, "mEventStart: " + timeInMillis);

            //ifo toast
            String timeLeft = String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(timeInMillis - System.currentTimeMillis()),
                    TimeUnit.MILLISECONDS.toSeconds(timeInMillis - System.currentTimeMillis()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillis - System.currentTimeMillis())));
            Toast.makeText(getApplicationContext(), "ADDED NOTIFICATION CAMERA AFTER " +
                            (timeLeft) + " FROM NOW",
                    Toast.LENGTH_LONG).show();
        } else if (currentTimeMillis() > mEventStart) {
            Toast.makeText(getApplicationContext(), "EVENT HAS ALREADY BEGUN",
                    Toast.LENGTH_LONG).show();
        }
    }

    //method for scan with Business Card Reader CRM Pro
    private void alarmManagerMinAfterEventScan(long minutess) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent("com.example.who.calendanot.OPEN_SCAN");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 102, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //find our event start time
        List<Event> events = mEvent.getEventsForQuery(null, null, null, getContentResolver());
        for (Event e : events) {
            if (e.id == getIntent().getExtras().getInt("EventId")) {
                String x = e.startDate;
                long milliSeconds = Long.parseLong(x);
                mEventStart = milliSeconds;
            }
        }

        if (currentTimeMillis() <= mEventStart) {
            long timeInMillis = (mEventStart + TimeUnit.MINUTES.toMillis(minutess));
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, broadcast);
            Log.d(TAG, "mEventStart: " + timeInMillis);

            //ifo toast
            String timeLeft = String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(timeInMillis - System.currentTimeMillis()),
                    TimeUnit.MILLISECONDS.toSeconds(timeInMillis - System.currentTimeMillis()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillis - System.currentTimeMillis())));
            Toast.makeText(getApplicationContext(), "ADDED NOTIFICATION SCANNING AFTER " +
                            (timeLeft) + " FROM NOW",
                    Toast.LENGTH_LONG).show();
        } else if (currentTimeMillis() > mEventStart) {
            Toast.makeText(getApplicationContext(), "EVENT HAS ALREADY BEGUN",
                    Toast.LENGTH_LONG).show();
        }
    }

    //method for starting SheetForm activity to fill form and send it to GooSheets
    private void alarmManagerMinAfterEventFillForm(long minutes){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent notificationIntent = new Intent("com.example.who.calendanot.OPEN_FIL_FORM");
        notificationIntent.addCategory("android.intent.category.DEFAULT");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 103, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //find our event start time
        List<Event> events = mEvent.getEventsForQuery(null, null, null, getContentResolver());
        for (Event e : events) {
            if (e.id == getIntent().getExtras().getInt("EventId")) {
                String x = e.startDate;
                long milliSeconds = Long.parseLong(x);
                mEventStart = milliSeconds;
            }
        }

        if (currentTimeMillis() <= mEventStart) {
            long timeInMillis = (mEventStart + TimeUnit.MINUTES.toMillis(minutes));
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, broadcast);
            Log.d(TAG, "mEventStart: " + timeInMillis);

            //ifo toast
            String timeLeft = String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(timeInMillis - System.currentTimeMillis()),
                    TimeUnit.MILLISECONDS.toSeconds(timeInMillis - System.currentTimeMillis()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMillis - System.currentTimeMillis())));
            Toast.makeText(getApplicationContext(), "ADDED NOTIFICATION TO FILL FORM AFTER " +
                            (timeLeft) + " FROM NOW",
                    Toast.LENGTH_LONG).show();
        } else if (currentTimeMillis() > mEventStart) {
            Toast.makeText(getApplicationContext(), "EVENT HAS ALREADY BEGUN",
                    Toast.LENGTH_LONG).show();
        }


    }


}




