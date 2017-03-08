package com.example.crunky.smartminifab;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class ErrorWindowActivity extends AppCompatActivity {

    private String message;
    // get actual instance of the Server
    IFabCommunication fab = CBlockFactory.getInstance().getFabCommunication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_window);
        // the extra id "message" is already used in other activities
        message = getIntent().getStringExtra("message");
        if (message!=null) {
            TextView txtView = (TextView) findViewById(R.id.ID_ErrorWindow_Message_TextView);
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
