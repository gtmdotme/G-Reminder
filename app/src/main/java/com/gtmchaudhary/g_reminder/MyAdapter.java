package com.gtmchaudhary.g_reminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by gtmchaudhary on 10/19/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public final String TAG = "myReminderApp";

    public ArrayList<Reminder> reminders = new ArrayList<Reminder>();
    public Reminder reminder = new Reminder();

    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView label_reminder;
        TextView id_reminder;
        public ViewHolder(View itemView) {
            super(itemView);
            label_reminder = (TextView)itemView.findViewById(R.id.label_reminder);
            id_reminder = (TextView)itemView.findViewById(R.id.id_reminder);
            // Define click listener for the ViewHolder's View.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Reminder-> " + label_reminder.getText().toString());
                    //Perform action on click


                }
            });

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditReminder.class);
                    context.startActivity(intent);
                    /* read from id and pass reminder through intent */

                    Log.d(TAG, "Card Clicked");
                }

            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Show alert dialog box for delete
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Delete this reminder ?");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //You clicked "YES"
                                /* delete reminder from database and arrayList*/
                                /* update arrayList */

                        }
                    });
                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //You clicked "NO"
                                /* no code, since nothing happens */
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    Log.d(TAG, "Card LongClicked");
                    return true;
                }
            });
        }

    }

    public MyAdapter(Context context, ArrayList<Reminder> reminders) {
        this.context = context;
        this.reminders = reminders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.label_reminder.setText(reminders.get(position).getLabel());
        holder.id_reminder.setText(reminders.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }
}
