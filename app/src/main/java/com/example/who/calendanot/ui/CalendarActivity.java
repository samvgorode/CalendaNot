package com.example.who.calendanot.ui;


import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.who.calendanot.R;
import com.example.who.calendanot.utilites.Calendar;

import java.util.ArrayList;
import java.util.List;


public class CalendarActivity extends AppCompatActivity {
    ListView mListView;
    ArrayAdapter mAdapter;
    private static final String TAG = "CalendarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.calendars);
        final String ordering = CalendarContract.Calendars.NAME + " ASC";
        final List<Calendar> calendars = Calendar.getCalendarsForQuery(null, null, ordering, getContentResolver());
        //final List<Calendar> calendarsWritable = Calendar.getWritableCalendars(getContentResolver());
        final List<Integer> calendarsAccountId = new ArrayList<>();
        for (Calendar c : calendars) {
            calendarsAccountId.add(c.id);
        }
        //adapter to send Calendar Id to Event activity
        final ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, calendarsAccountId);

        final List<String> calendarsAccountInfo = new ArrayList<>();

        for (Calendar c : calendars) {
            calendarsAccountInfo.add("Calendar name - " + c.name + "\nOwner of account\n" + c.ownerAccount.toString() + "\n");

            mAdapter = new ArrayAdapter<>(this, R.layout.list_item, calendarsAccountInfo);
            mListView.setAdapter(mAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(CalendarActivity.this, EventActivity.class);
                    intent.putExtra("CalendarId", arrayAdapter.getItem(position));
                    startActivity(intent);
                }
            });

        }


    }
}



