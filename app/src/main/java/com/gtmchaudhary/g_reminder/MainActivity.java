package com.gtmchaudhary.g_reminder;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public String TAG = "myReminderApp";

    RecyclerView myRecyclerView;
    LinearLayoutManager myLayoutManager;
    ArrayList<Reminder> reminders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Datasource for adapter

        // referencing the RecyclerView
        myRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        myRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        myLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(myLayoutManager);

        // set Adapter to RecyclerView
        //MyAdapter myAdapter = new MyAdapter(this, reminders);
        //myRecyclerView.setAdapter(myAdapter);

        /************** FAB onClickListener **************/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MainActivity.this, EditReminder.class);
                startActivity(intent);

                Log.d(TAG, "FAB clicked");
            }
        });

    }
}
