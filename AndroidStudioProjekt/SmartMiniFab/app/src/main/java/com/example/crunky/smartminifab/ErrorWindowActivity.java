package com.example.crunky.smartminifab;

import android.content.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ErrorWindowActivity extends AppCompatActivity {

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_window);
        // the extra id "message" is already used in other activities
        message = getIntent().getStringExtra("message");
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
