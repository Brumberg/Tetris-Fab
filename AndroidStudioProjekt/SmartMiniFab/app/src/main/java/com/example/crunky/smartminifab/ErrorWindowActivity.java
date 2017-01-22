package com.example.crunky.smartminifab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ErrorWindowActivity extends AppCompatActivity {

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_window);
        // the extra id "message" is already used in other activities
        message = getIntent().getStringExtra("message");
    }
}
