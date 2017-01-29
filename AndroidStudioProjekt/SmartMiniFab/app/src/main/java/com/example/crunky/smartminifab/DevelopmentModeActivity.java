package com.example.crunky.smartminifab;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.TextUtils;
import android.text.TextUtils.EllipsizeCallback;
import android.view.View;
import android.widget.AdapterView;
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
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.*;


public class DevelopmentModeActivity extends AppCompatActivity {

    // Initialize Variables

    String msg = "Android : ";
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
    public String preDefTestString = "3;3;1,3100;3,3301;1,1002";
    private int passEmpty = 0;
    private int stringEmpty = 0;
    private Spinner wifiSpinner;
    private ProgressBar connectStatus;
    private ProgressBar orderStatus;
    private TCPIPModuleManagement m_factoryManagement;
    private TCPIPModule m_currentFactory;
    private FabCommunicationListAdapter m_adapter;
    private Boolean isConnected = false;
    //public Handler handler;
    //public Runnable timeout;
    private TimeOutReconnectModule TimeOut;
    private int delay = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_development_mode);

        //Connect GUI elements to variables
        connectButton = (Button) findViewById(R.id.Connect);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectButton_onClick(v);
            }
        });
        scanNetworkButton = (Button) findViewById(R.id.ID_Dev_Mode_Scan_Button);
        scanNetworkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanNetworkButton_onClick(v);
            }
        });

        disconnectButton = (Button) findViewById(R.id.ID_Dev_Mode__Button);
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disconnectButton_onClick(v);
            }
        });
        helpButton = (Button) findViewById(R.id.ID_Dev_Mode_Help_Button);
        loadTestStringButton = (Button) findViewById(R.id.ID_Dev_Mode_LoadTestString_Button);
        seedTrayButton = (Button) findViewById(R.id.ID_Dev_Mode_SeedTray_Button);
        sendOrderButton = (Button) findViewById(R.id.ID_Dev_Mode_Order_Button);
        sendOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOrderButton_onClick(v);
            }
        });

        inpPassword = (EditText) findViewById(R.id.Username);
        inpUserTestString = (EditText) findViewById(R.id.TestString);
        orderSuccess = (TextView) findViewById(R.id.ID_Dev_Mode_OrderSucces_TextView);
        wifiSpinner = (Spinner) findViewById(R.id.ID_Dev_Mode_DropDownWifi_Spinner);
        wifiSpinner_setEnabled(false);
        connectStatus = (ProgressBar) findViewById(R.id.ID_Dev_Mode_Connecting_ProcessBar);
        orderStatus = (ProgressBar) findViewById(R.id.ID_Dev_Mode_InOperation_ProcessBar);

        wifiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wifiSpinner_onItemSelected(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                wifiSpinner_onNothingSelected(parent);
            }
        });

        // Empty Textfields Username or Teststring on User clicking into field

        inpPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    //Log.d("myTag", "Focus");
                    if (inpPassword.getText().toString().compareTo("Username") == 0) {
                        inpPassword.setText("");
                        //Log.d("myTag", "setEmpty Pass");
                    }
                }
            }
        });

        inpUserTestString.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true) {
                    //Log.d("myTag", "Focus");
                    if (inpUserTestString.getText().toString().compareTo("TestString") == 0) {
                        inpUserTestString.setText("");
                        //Log.d("myTag", "setEmpty String");
                    }
                }
            }
        });

        // Already implemented in Methods from Daniel

        // Track changes in textfield Username, if some input is there enable connectButton

        /*inpPassword.addTextChangedListener(new TextWatcher() {

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
        });*/

        // Track changes in textfield TestString, if some input is there enable sendOrderButton
        inpUserTestString.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
             }

            @Override
            public void afterTextChanged(Editable s) {
                userTestString = inpUserTestString.getText().toString();
                stringEmpty = inpUserTestString.getText().toString().trim().length();
                if (stringEmpty == 0) {
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

                //Activate for testing the timer
                /*isConnected = true;
                TimeOut = new TimeOutReconnectModule();
                TimeOut.TimeOut(delay, isConnected, inpPassword.getText.toString());*/


            }
        });



        m_factoryManagement = new TCPIPModuleManagement((Context) (this));
        m_factoryManagement.setIpScannedListener(new IIpScanningListener() {
            public void onStartScanning(TCPIPModuleManagement sender) {
                factoryManagement_onStartScanning(sender);
            }

            public void onIpScanned(TCPIPModuleManagement sender) {
                factoryManagement_onIpScanned(sender);
            }

            public void onAllIpsScanned(TCPIPModuleManagement sender) {
                factoryManagement_onAllIpsScanned(sender);
            }
        });

    }

        /**
         * Handles the event if no IFabCommunication is selected in the WifiSpinner
         */

    private void wifiSpinner_onNothingSelected(AdapterView<?> parent) {
        //RequestIdentifier.setEnabled(false);
        inpPassword.setEnabled(false);
        connectButton.setEnabled(false);
        disconnectButton.setEnabled(false);
        //Log.d("not selcted", Password);

    }

    /**
     * Handles the event if a IFabCommunication is selected in the WifiSpinner
     */
    private void wifiSpinner_onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //RequestIdentifier.setEnabled(true);
        inpPassword.setEnabled(true);
        connectButton.setEnabled(true);
        disconnectButton.setEnabled(false);
    }

    /**
     * As setting the enabled-state of a Spinner needs a special practice and it is done multiple times it is stored in this method
     */
    private void wifiSpinner_setEnabled(boolean enabled) {
        wifiSpinner.setEnabled(enabled);
        View v = wifiSpinner.getSelectedView();
        if (v != null) {
            v.setEnabled(enabled);
        }
    }

    
    /**
     * Handles the event if the scanning for factories was started
     */
    private void factoryManagement_onStartScanning(TCPIPModuleManagement sender) {
        if (m_currentFactory != null) {
            m_currentFactory.disconnect();
        }
        scanNetworkButton.setEnabled(false);
        wifiSpinner_setEnabled(false);
        //RequestIdentifier.setEnabled(false);
        inpPassword.setEnabled(false);
        connectButton.setEnabled(false);
        disconnectButton.setEnabled(false);
        // No Txtfield to show connection status in DevMode maybe include?
        //ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
        // ConnectionStatus.setText(getString(R.string.disconnected));
//        pbScanProgress.setMax(sender.getCountOfIpsToBeScanned());
//        pbScanProgress.setProgress(0);
    }

    /**
     * Handles the event if an IP was scanned
     */
    private void factoryManagement_onIpScanned(TCPIPModuleManagement sender) {
        /*pbScanProgress.setProgress(sender.getScannedIps());*/
    }

    /**
     * Handles the event if all IPs were scanned
     */
    private void factoryManagement_onAllIpsScanned(TCPIPModuleManagement sender) {
//        bButton.setVisibility(View.VISIBLE);
//        pbScanProgress.setVisibility(View.INVISIBLE);
        m_adapter = new FabCommunicationListAdapter(this, m_factoryManagement.getBlockFactories());
        wifiSpinner.setAdapter(m_adapter);
        // Check if any factory was found
        if (wifiSpinner.getCount() > 0) {
            wifiSpinner_setEnabled(true);
            wifiSpinner.setSelection(0);
        } else {
            goToErrorWindowActivity(wifiSpinner, getString(R.string.no_factory_found));
        }
    }

    /**
     * Handles the event if the ScanButton is clicked
     */
    private void scanNetworkButton_onClick(View v) {
        m_factoryManagement.scanBlockFactories();
    }

    /**
     * Handles the event if the ConnectButton is clicked
     */
    private void connectButton_onClick(View v) {
        // Try to connect to the factory
        if (m_currentFactory.connect()) {
            // Try to login to the factory
            if (m_currentFactory.login(inpPassword.getText().toString())) {
                connectButton.setEnabled(false);
                disconnectButton.setEnabled(true);
                wifiSpinner_setEnabled(false);
                inpPassword.setEnabled(false);
                //RequestIdentifier.setEnabled(false);
                //ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                // ConnectionStatus.setText(getString(R.string.connected));
                CBlockFactory.getInstance().setFabCommunication(m_currentFactory);

                /*isConnected = true;
                TimeOut = new TimeOutReconnectModule();
                TimeOut.TimeOut(delay, isConnected, inpPassword.getText().toString());*/
            } else {
                // Show an error message if it does not work
                m_currentFactory.disconnect();
                //ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                //ConnectionStatus.setText(getString(R.string.disconnected));
                goToErrorWindowActivity(connectButton, getString(R.string.wrong_password));
                CBlockFactory.getInstance().setFabCommunication(null);
                isConnected = false;
            }
        } else {
            // Show an error message if it does not work
            //ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
            //ConnectionStatus.setText(getString(R.string.disconnected));
            goToErrorWindowActivity(connectButton, getString(R.string.connection_failed));
            CBlockFactory.getInstance().setFabCommunication(null);
            isConnected = false;
        }
    }

    /**
     * Handles the event if the DisconnectButton is clicked
     */
    private void disconnectButton_onClick(View v) {
        m_currentFactory.disconnect();
        connectButton.setEnabled(true);
        disconnectButton.setEnabled(false);
        //ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
        //ConnectionStatus.setText(getString(R.string.disconnected));
        CBlockFactory.getInstance().setFabCommunication(null);
    }

    /**
     * Handles the event if Orderbutton is clicked
     */

    private void sendOrderButton_onClick(View v)  {
        //Try to transmit Order

            if (m_currentFactory.transmit(inpUserTestString.getText().toString())) {
                inpUserTestString.setEnabled(false);
                sendOrderButton.setEnabled(false);
                orderSuccess.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                orderSuccess.setText(getString(R.string.connected));
                CBlockFactory.getInstance().setFabCommunication(m_currentFactory);
            } else {
                // Show an error message if it does not work
                orderSuccess.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                orderSuccess.setText(getString(R.string.disconnected));
                goToErrorWindowActivity(sendOrderButton, "Order not successful");
                CBlockFactory.getInstance().setFabCommunication(null);
            }

    }





    /**
     * Called when the activity is about to become visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event");
    }

    /**
     * Called when the activity has become visible.
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
    }

    /**
     * Called when another activity is taking focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(msg, "The onPause() event");
    }

    /**
     * Called when the activity is no longer visible.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(msg, "The onStop() event");
    }

    /**
     * Called just before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy() event");
    }

    public void goToHelpWindowActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, HelpWindowActivity.class);
        startActivity(intent);
    }

    public void goToSeedBoxModeActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, SeedBoxModeActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to ErrorWindowActivity
     */
    public void goToErrorWindowActivity(View view, String message) {
        Intent intent = new Intent(this, ErrorWindowActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }

}

