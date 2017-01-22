package com.example.crunky.smartminifab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;


public class DevelopmentModeActivity extends AppCompatActivity {

    private Button connectButton;
    EditText inpPassword;
    public String Password;
    private int passEmpty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_development_mode);

        inpPassword = (EditText) findViewById(R.id.Username);
        //Password = inpPassword.getText().toString();
        //passEmpty = inpPassword.getText().toString().trim().length();
        connectButton = (Button) findViewById(R.id.Connect);

        inpPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Password = inpPassword.getText().toString();
                passEmpty = inpPassword.getText().toString().trim().length();
                if(passEmpty==0 || Password.equals("Username")){
                    //Log.d("myTag", "Empty");
                    //Log.d("myTag", Password);
                    connectButton.setEnabled(false);
                    //disconnectButton.setEnabled(false);

                    //connectButton.setVisibility(View.GONE);
                } else {
                    //Log.d("myTag", "NotEmpty");
                    //Log.d("myTag", Password);
                    connectButton.setEnabled(true);
                    //connectButton.setVisibility(View.VISIBLE);
                }
                }
            });
        }
    public void goToHelpWindowActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, HelpWindowActivity.class);
        startActivity(intent);
    }

    public void goToSeedBoxModeActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, SeedBoxModeActivity.class);
        startActivity(intent);
    }


}
