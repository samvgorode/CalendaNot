package com.example.who.calendanot.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
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
    private static final String TAG = "EventActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_main);
        mListView = (ListView) findViewById(R.id.reminders);
        final List<Reminder> reminders = mReminder.getRemindersForQuery(null, null, null, getContentResolver());
        final List<String> remindersAccountId = new ArrayList<>();

        int idEvent = getIntent().getExtras().getInt("EventId");
        for (Reminder re : reminders) {
            //if (re.eventId == idEvent) {
                remindersAccountId.add("Reminder id =  " + re.id.toString());
            //}
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, remindersAccountId);

        mListView.setAdapter(arrayAdapter);
}}
