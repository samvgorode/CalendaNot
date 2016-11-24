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

import com.example.who.calendanot.R;

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
        for(Event e:events) {
            eventsAccountId.add(e.id);}
        final ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, eventsAccountId);

        final List<String> eventsTitle = new ArrayList<>();
        int idCalendar = getIntent().getExtras().getInt("CalendarId");
        for (Event e : events) {
            if (e.calendarId == idCalendar) {

                String x = e.startDate;
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                long milliSeconds= Long.parseLong(x);
                java.util.Calendar calendar1 = java.util.Calendar.getInstance();
                calendar1.setTimeInMillis(milliSeconds);
                eventsTitle.add("ID= "+e.id+"\nTitle - "+e.title+"\n"+"Date - "+formatter.format(calendar1.getTime())+"\n");
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
            public void onClick(View v)  {

                long idd = Long.parseLong(mEdittext.getText().toString());
                Log.d(TAG, "onClick: " + idd);
                ContentResolver cr = getContentResolver();
                ContentValues values = new ContentValues();
                Uri deleteUri = null;
                deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, idd);
                int rows = getContentResolver().delete(deleteUri, null, null);
                Log.d(TAG, "Rows deleted: " + rows +"  "+ idd);

            }
        });
    }





}
