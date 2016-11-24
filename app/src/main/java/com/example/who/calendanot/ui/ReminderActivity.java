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
import android.widget.ListView;

import com.example.who.calendanot.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by who on 23.11.2016.
 */

public class ReminderActivity extends AppCompatActivity {
    ListView mListView;
    Reminder mReminder;
    Button mButton;
    Event mEvent;
    long mEventStart;



    private static final String TAG = "EventActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_main);
        mListView = (ListView) findViewById(R.id.reminders);
        mButton = (Button) findViewById(R.id.customReminder);
        final List<Reminder> reminders = mReminder.getRemindersForQuery(null, null, null, getContentResolver());
        final List<String> remindersAccountId = new ArrayList<>();

        int idEvent = getIntent().getExtras().getInt("EventId");
        for (Reminder re : reminders) {
            if (re.eventId == idEvent) {
                remindersAccountId.add("Reminder id =  " + re.id.toString()+"\n"+"Time before - "+re.minutesBefore+" minutes\n"+"Method - "+re.method+"\n");
            }
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, remindersAccountId);

        mListView.setAdapter(arrayAdapter);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "onClick: " + mCalendar.name);
                setReminder(getApplication().getContentResolver(), getIntent().getExtras().getInt("EventId"), 60 );
                setReminder(getApplication().getContentResolver(), getIntent().getExtras().getInt("EventId"), 5 );
                alarmManagerFifteenMinFivePhoto();

            }
        });
    }

    //reminder before event
    public void setReminder(ContentResolver cr, long eventID, int timeBefore) throws SecurityException {
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
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alarmManagerFifteenMinFivePhoto(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent("android.media.action.DISPLAY_NOTIFICATION");
        notificationIntent.addCategory("android.intent.category.DEFAULT");

        PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        List<Event> events = mEvent.getEventsForQuery(null, null, null, getContentResolver());
        int idEve = getIntent().getExtras().getInt("EventId");
        for(Event e:events){
           if(e.id==idEve){
               String x = e.startDate;
               long milliSeconds= Long.parseLong(x);
               mEventStart=milliSeconds;
               Log.d(TAG, "mEventStart: " + mEventStart);
           }
        }
        //15 minutes after event to do 5 photo
        long timeInMillis = mEventStart + 150000;
        Log.d(TAG, "timeInMillis: " + timeInMillis);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, broadcast);
    }
    }



