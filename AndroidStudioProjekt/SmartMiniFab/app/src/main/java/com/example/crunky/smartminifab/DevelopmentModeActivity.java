package com.example.crunky.smartminifab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.EllipsizeCallback;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.util.Log;
import java.lang.String;
import java.lang.Object;
import java.util.concurrent.locks.Condition;


public class DevelopmentModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_development_mode);
    }

    public void goToHelpWindowActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, HelpWindowActivity.class);
        startActivity(intent);
    }

    public void goToSeedBoxModeActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, SeedBoxModeActivity.class);
        startActivity(intent);
    }

    /* Hide ConnectButton until some kind of Passwort is tiped in*/

    final Button connectButton = (Button) findViewById(R.id.Connect);
    EditText inpPassword = (EditText) findViewById(R.id.Username);
    public String Password = inpPassword.getText().toString();
    int Test = inpPassword.getText().toString().trim().length();

    /*if(Test==0)
        connectButton.setVisibility(View.GONE);
    } else {
        connectButton.setVisibility(View.VISIBLE);
    }*/
}
