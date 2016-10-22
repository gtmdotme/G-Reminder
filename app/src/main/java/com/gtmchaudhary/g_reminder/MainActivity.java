package com.gtmchaudhary.g_reminder;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public String TAG = "myReminderApp";

    RecyclerView myRecyclerView;
    LinearLayoutManager myLinearManager;
    GridLayoutManager myGridManager;
    StaggeredGridLayoutManager myStaggeredLayoutManager;
    ArrayList<Reminder> reminders;
    TextView empty_textView;
    ImageView empty_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Datasource for adapter
        DatabaseHelper db = new DatabaseHelper(this);
        reminders = new ArrayList<Reminder>();
        reminders = db.readAllRemindersFromDB();

        // referencing the RecyclerView
        myRecyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        myRecyclerView.setHasFixedSize(true);

        // use a layout manager
        myLinearManager = new LinearLayoutManager(this);
        myGridManager = new GridLayoutManager(this,2);
        myStaggeredLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        myRecyclerView.setLayoutManager(myLinearManager);

        // set Adapter to RecyclerView
        MyAdapter myAdapter = new MyAdapter(this, reminders);
        myRecyclerView.setAdapter(myAdapter);

        /**********Toggling between textbox+imageview and recyclerView**********/
        empty_textView = (TextView)findViewById(R.id.empty_textView);
        empty_imageView = (ImageView)findViewById(R.id.empty_imageView);
        if((myAdapter.getItemCount()) == 0){
            empty_textView.setText("Click to add new");
            empty_textView.setVisibility(View.VISIBLE);
            empty_imageView.setVisibility(View.VISIBLE);
            myRecyclerView.setVisibility(View.INVISIBLE);
        }
        else{
            empty_textView.setVisibility(View.INVISIBLE);
            empty_imageView.setVisibility(View.INVISIBLE);
            myRecyclerView.setVisibility(View.VISIBLE);
        }

        /************** FAB onClickListener **************/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(MainActivity.this, EditReminder.class);
                Reminder reminder = new Reminder();
                intent.putExtra("object", reminder);
                startActivity(intent);

                Log.d(TAG, "FAB clicked");
            }
        });

    }
}
