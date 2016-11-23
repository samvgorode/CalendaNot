package com.example.who.calendanot.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.who.calendanot.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by who on 23.11.2016.
 */

public class EventActivity extends AppCompatActivity {
    ListView mListView;
    Event mEvent;
    private static final String TAG = "EventActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_main);
        mListView = (ListView) findViewById(R.id.events);
        final List<Event> events = mEvent.getEventsForQuery(null, null, null, getContentResolver());
        final List<Integer> eventsAccountId = new ArrayList<>();
        for(Event e:events) {
            eventsAccountId.add(e.id);}
        final ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, eventsAccountId);

        final List<String> eventsTitle = new ArrayList<>();
        int idCalendar = getIntent().getExtras().getInt("CalendarId");
        for (Event e : events) {
            if (e.calendarId == idCalendar) {
                eventsTitle.add(e.title);
            }
        }
        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(this, R.layout.list_item, eventsTitle);
        mListView.setAdapter(arrayAdapter1);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventActivity.this, EventActivity.class);
                intent.putExtra("EventId", arrayAdapter.getItem(position));
                startActivity(intent);
            }
        });
    }
}
