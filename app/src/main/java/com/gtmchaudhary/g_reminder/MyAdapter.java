package com.gtmchaudhary.g_reminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
        public ViewHolder(final View itemView) {
            super(itemView);
            label_reminder = (TextView)itemView.findViewById(R.id.label_reminder);
            id_reminder = (TextView)itemView.findViewById(R.id.id_reminder);


            // Define click listener for the ViewHolder's View.
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    reminder = reminders.get(getAdapterPosition());
                    Intent intent = new Intent(context, EditReminder.class);
                    intent.putExtra("object",reminder);
                    context.startActivity(intent);

                    Log.d(TAG, "Card Clicked");
                    Log.d(TAG, "Reminder-> " + reminder.getLabel());
                }

            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    //Show alert dialog box for delete
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage("Delete this reminder ?");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //You clicked "YES"
                            //  delete from database
                            DatabaseHelper db = new DatabaseHelper(context);
                            reminder = reminders.get(getAdapterPosition());
                            Log.d(TAG, "Reminder sent for deletion");
                            db.deleteReminderFromDB(reminder.getId());
                            //  delete from array list
                            reminders.remove(getAdapterPosition());
                            //  notify the adapter
                            notifyDataSetChanged();

                            Snackbar.make(itemView, "Deleted", Snackbar.LENGTH_LONG)
                                    //.setAction("Undo", mOnClickListener)
                                    .setActionTextColor(Color.RED)
                                    .show();
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
        holder.id_reminder.setText(reminders.get(position).getId()+"");
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }
}
