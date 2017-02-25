package com.example.crunky.smartminifab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HelpWindowActivity extends Activity {

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_window);

        // the extra id "message" is already used in other activities
        message = getIntent().getStringExtra("message");
        if (message!=null) {
            TextView txtView = (TextView) findViewById(R.id.ID_HelpWindow_Message_TextView);
            txtView.setText(message);
        } else {
            //do nothing
        }
    }

    /**
     * Goes Back to last Activity
     */
    public void goBack(View v) { //is called by onClick function of Back Button in activity_help_window.xml
        super.onBackPressed();
    }

    /**
     * Shows ErrorWindowActivity
     */
    public static void show(Context from, String message) {
        Intent intent = new Intent(from, ErrorWindowActivity.class);
        intent.putExtra("message", message);
        from.startActivity(intent);
    }

}
