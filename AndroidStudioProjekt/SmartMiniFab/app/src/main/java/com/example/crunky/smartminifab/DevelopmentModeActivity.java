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
import android.widget.ProgressBar;
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

    // Initialize Variables

    private Button connectButton;
    private Button scanNetworkButton;
    private Button disconnectButton;
    private Button helpButton;
    private Button loadTestStringButton;
    private Button seedTrayButton;
    private Button sendOrderButton;
    private EditText inpPassword;
    private EditText inpUserTestString;
    private TextView orderSuccess;
    public String Password;
    public String userTestString;
    public String preDefTestString = "xxxStringxxx";
    private int passEmpty = 0;
    private int stringEmpty = 0;
    private Spinner wifiDropDown;
    private ProgressBar connectStatus;
    private ProgressBar orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_development_mode);

        //Connect GUI elements to variables
        connectButton = (Button) findViewById(R.id.Connect);
        scanNetworkButton = (Button) findViewById(R.id.ID_Dev_Mode_Scan_Button);
        disconnectButton = (Button) findViewById(R.id.ID_Dev_Mode__Button);
        helpButton = (Button)findViewById(R.id.ID_Dev_Mode_Help_Button);
        loadTestStringButton = (Button)findViewById(R.id.ID_Dev_Mode_LoadTestString_Button);
        seedTrayButton = (Button)findViewById(R.id.ID_Dev_Mode_SeedTray_Button);
        sendOrderButton= (Button)findViewById(R.id.ID_Dev_Mode_Order_Button);

        inpPassword = (EditText) findViewById(R.id.Username);
        inpUserTestString = (EditText) findViewById(R.id.TestString);
        orderSuccess = (TextView) findViewById(R.id.ID_Dev_Mode_OrderSucces_TextView);
        wifiDropDown = (Spinner) findViewById(R.id.ID_Dev_Mode_DropDownWifi_Spinner);
        connectStatus = (ProgressBar) findViewById(R.id.ID_Dev_Mode_Connecting_ProcessBar);
        orderStatus = (ProgressBar) findViewById(R.id.ID_Dev_Mode_InOperation_ProcessBar);
        //Password = inpPassword.getText().toString();
        //passEmpty = inpPassword.getText().toString().trim().length();

        // Empty Textfields Username or Teststring on User clicking into field

        inpPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus==true)
                {
                    Log.d("myTag", "Focus");
                    if (inpPassword.getText().toString().compareTo("Username")==0)
                    {
                        inpPassword.setText("");
                        //Log.d("myTag", "setEmpty Pass");
                    }
                }
            }
        });

        inpUserTestString.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus==true)
                {
                    Log.d("myTag", "Focus");
                    if (inpUserTestString.getText().toString().compareTo("TestString")==0)
                    {
                        inpUserTestString.setText("");
                        //Log.d("myTag", "setEmpty String");
                    }
                }
            }
        });


        // Track changes in textfield Username, if some input is there enable connectButton

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
                if(passEmpty==0){
                    //Log.d("myTag", "Empty Pass");
                    //Log.d("myTag", Password);
                    connectButton.setEnabled(false);
                    //disconnectButton.setEnabled(false);

                    //connectButton.setVisibility(View.GONE);
                } else {
                    //Log.d("myTag", "NotEmpty Pass");
                    //Log.d("myTag", Password);
                    connectButton.setEnabled(true);
                    //connectButton.setVisibility(View.VISIBLE);
                }

                }
            });

        // Track changes in textfield TestString, if some input is there enable sendOrderButton

        inpUserTestString.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // if(Password.equals("Username")) {
                //     inpPassword.setText("");
                // }
            }
            @Override
            public void afterTextChanged(Editable s) {
                userTestString = inpUserTestString.getText().toString();
                stringEmpty = inpUserTestString.getText().toString().trim().length();
                if(stringEmpty==0){
                    //Log.d("myTag", "Empty String");
                    //Log.d("myTag", Password);
                    sendOrderButton.setEnabled(false);
                    //disconnectButton.setEnabled(false);

                    //connectButton.setVisibility(View.GONE);
                } else {
                    //Log.d("myTag", "NotEmpty String");
                    //Log.d("myTag", Password);
                    sendOrderButton.setEnabled(true);
                    //connectButton.setVisibility(View.VISIBLE);
                }


            }
        });

        // Write Testring on String field on clicking button

        loadTestStringButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inpUserTestString.setText(preDefTestString, TextView.BufferType.EDITABLE);
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
