package com.example.who.calendanot.ui;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.who.calendanot.R;
import com.example.who.calendanot.utilites.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by who on 23.11.2016.
 */

public class EventActivity extends AppCompatActivity {
    ListView mListView;
    Event mEvent;
    Button mButton;
    EditText mEdittext;

    private static final String TAG = "EventActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_main);
        mListView = (ListView) findViewById(R.id.events);
        mButton = (Button) findViewById(R.id.delete_event_button);
        mEdittext = (EditText) findViewById(R.id.input_id_event_delete);


        final List<Event> events = mEvent.getEventsForQuery(null, null, null, getContentResolver());
        final List<Integer> eventsAccountId = new ArrayList<>();
        for (Event e : events) {
            if (e.calendarId == getIntent().getExtras().getInt("CalendarId")) {
                eventsAccountId.add(e.id);
            }
        }
        final ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, eventsAccountId);

        final List<String> eventsTitle = new ArrayList<>();
        for (Event e : events) {
            if (e.calendarId == getIntent().getExtras().getInt("CalendarId")) {

                String x = e.startDate;
                String y = e.endDate;
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat formatter1 = new SimpleDateFormat("h:mm:ss a");
                long milliSecondsStart = Long.parseLong(x);
                long milliSecondsFinish = Long.parseLong(y);
                java.util.Calendar calendarStart = java.util.Calendar.getInstance();
                calendarStart.setTimeInMillis(milliSecondsStart);
                java.util.Calendar calendarFinish = java.util.Calendar.getInstance();
                calendarFinish.setTimeInMillis(milliSecondsFinish);

                eventsTitle.add("ID= " + e.id + "\nTitle - " + e.title + "\n" + "Date - " + formatter.format(calendarStart.getTime()) + "\nTime Start - " + formatter1.format(calendarStart.getTime()) + "\nTime Finish - " + formatter1.format(calendarFinish.getTime()));
            }
        }
        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, R.layout.list_item, eventsTitle);
        mListView.setAdapter(arrayAdapter1);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventActivity.this, ReminderActivity.class);
                intent.putExtra("EventId", arrayAdapter.getItem(position));
                startActivity(intent);
            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myInput = mEdittext.getText().toString();
                final List<Event> events = mEvent.getEventsForQuery(null, null, null, getContentResolver());
                for (Event e : events) {
                    if (e.calendarId == getIntent().getExtras().getInt("CalendarId")) {
                        if (myInput.matches("")) {
                            Toast.makeText(getApplicationContext(), "EMPTY VALUE IS NOT ALLOWED HERE", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            long idd = Long.parseLong(myInput);
                            if (idd == e.id) {
                                ContentResolver cr = getContentResolver();
                                ContentValues values = new ContentValues();
                                Uri deleteUri = null;
                                deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, idd);
                                int rows = getContentResolver().delete(deleteUri, null, null);
                                Log.d(TAG, "Rows deleted: " + rows + "  " + idd);
                                Toast.makeText(getApplicationContext(), "DELETED EVENT ID= \n" + idd + "\nSYNC YOUR CALENDAR", Toast.LENGTH_LONG).show();
                            } else if (idd != e.id) {
                                Toast.makeText(getApplicationContext(), "THIS VALUE IS NOT EVENT ID", Toast.LENGTH_LONG).show();
                                return;
                            }

                        }

                    }
                }
            }
        });
    }


}
