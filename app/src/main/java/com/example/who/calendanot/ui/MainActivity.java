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

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    ListView mListView;
    ArrayAdapter mAdapter;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.calendars);
        final String ordering = CalendarContract.Calendars.NAME + " ASC";
        final List<Calendar> calendars = Calendar.getCalendarsForQuery(null, null, ordering, getContentResolver());
        final List<Integer> calendarsAccountId = new ArrayList<>();
        for(Calendar c:calendars) {
        calendarsAccountId.add(c.id);}
        final ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_item, calendarsAccountId);

        final List<String> calendarsAccountName = new ArrayList<>();
        for(Calendar c:calendars) {
            calendarsAccountName.add("Owner of account" + "\n" + c.ownerAccount.toString());
        mAdapter = new ArrayAdapter<>(this, R.layout.list_item, calendarsAccountName);
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                intent.putExtra("CalendarId", arrayAdapter.getItem(position));

                startActivity(intent);
            }
        });

    }


}}



