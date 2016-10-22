package com.gtmchaudhary.g_reminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by gtmchaudhary on 10/22/2016.
 */
public class ViewReminder extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_view_reminider);

        // Swipe Button - "Mark As Done"
        SwipeButton mSwipeButton = (SwipeButton) findViewById(R.id.mSwipeButton);
        SwipeButtonCustomItems swipeButtonSettings = new SwipeButtonCustomItems() {
            @Override
            public void onSwipeConfirm() {
                Log.d("NEW_STUFF", "New swipe confirm callback");
                //Perform action on click

            }
        };
        swipeButtonSettings
                .setButtonPressText(">> Swipe to Mark-As-Done >>")
                .setGradientColor1(0xFF888888)
                .setGradientColor2(0xFF666666)
                .setGradientColor2Width(60)
                .setGradientColor3(0xFF333333)
                .setPostConfirmationColor(0xFF888888)
                .setActionConfirmDistanceFraction(0.7)
                .setActionConfirmText("Done !..");
        if (mSwipeButton != null) {
            mSwipeButton.setSwipeButtonCustomItems(swipeButtonSettings);
        }



        // Reschedule Button
        Button rescheduleReminder = (Button)findViewById(R.id.reschedule_button);
        rescheduleReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewReminder.this, EditReminder.class));
            }
        });



        // Textviews
        TextView reminder_view_label = (TextView)findViewById(R.id.label_reminder_view);
        TextView reminder_view_id = (TextView)findViewById(R.id.id_reminder_view);
    }
}
