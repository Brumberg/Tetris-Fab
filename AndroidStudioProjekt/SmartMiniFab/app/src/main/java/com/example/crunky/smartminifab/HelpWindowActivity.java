package com.example.crunky.smartminifab;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class HelpWindowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_window);
    }

    /**
     * Goes Back to last Activity
     */
    public void goBack(View v) { //is called by onClick function of Back Button in activity_help_window.xml
        super.onBackPressed();
    }

}
