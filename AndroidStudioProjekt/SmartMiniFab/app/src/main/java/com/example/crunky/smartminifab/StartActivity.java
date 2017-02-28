package com.example.crunky.smartminifab;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import java.net.*;

import java.net.UnknownHostException;

/**
 * StartActivity is the first Activity shown to the user and contains the functionality for scanning for factories and selecting the current one.
 */
public class StartActivity extends AppCompatActivity {
    String msg = "Android: ";
    private TextView RequestUrl;
    private EditText Url;
    private TextView RequestIdentifier;
    private EditText Identifier;
    private Button ConnectButton;
    private Button DisconnectButton;
    private TextView ConnectionStatus;
    private Button WarehouseButton;
    private TCPIPModule m_currentFactory;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.d(msg, "The onCreate() event");
        // Initialize controls
        RequestUrl = (TextView) (findViewById(R.id.ID_FactorySelectMode_RequestUrl_TextView));
        Url = (EditText) (findViewById(R.id.ID_FactorySelectMode_Url_PlainText));
        RequestIdentifier = (TextView) (findViewById(R.id.ID_FactorySelectMode_RequestIdentifier_TextView));
        Identifier = (EditText) (findViewById(R.id.ID_FactorySelectMode_Identifier_PlainText));
        ConnectButton = (Button) (findViewById(R.id.ID_FactorySelectMode_Connect_Button));
        ConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectButton_onClick(v);
            }
        });
        DisconnectButton = (Button) (findViewById(R.id.ID_FactorySelectMode_DisConnect_Button));
        DisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisconnectButton_onClick(v);
            }
        });
        ConnectionStatus = (TextView) (findViewById(R.id.ID_FactorySelectMode_ConnectionStatus_TextView));
        WarehouseButton = (Button) (findViewById(R.id.ID_FactorySelectMode_Warehouse_Button));
    }
    /**
     * Handles the event if the LoadPredefinedIPButton is clicked
     */
    private void goLoadPredDefIP_onClick(View v) {
        try {
            //
        } catch (Exception e) {
          //
        }
    }
    /**
     * Handles the event if the ConnectButton is clicked
     */
    private void ConnectButton_onClick(View v) {
        try
        {
            InetAddress ia=InetAddress.getByName(Url.getText().toString());
            m_currentFactory=new TCPIPModule(ia, TCPIPModuleManagement.Port);
            // Try to connect to the factory
            if (m_currentFactory.connect()) {
                // Try to login to the factory
                if (m_currentFactory.login(Identifier.getText().toString())) {
                    ConnectButton.setEnabled(false);
                    DisconnectButton.setEnabled(true);
                    ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                    ConnectionStatus.setText(getString(R.string.connected));
                    CBlockFactory.getInstance().setFabCommunication(m_currentFactory);
                } else {
                    // Show an error message if it does not work
                    m_currentFactory.disconnect();
                    ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    ConnectionStatus.setText(getString(R.string.disconnected));
                    ErrorWindowActivity.show(this, getString(R.string.wrong_identifier));
                    CBlockFactory.getInstance().setFabCommunication(null);
                }
            } else {
                // Show an error message if it does not work
                ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                ConnectionStatus.setText(getString(R.string.disconnected));
                ErrorWindowActivity.show(this, getString(R.string.connection_failed));
                CBlockFactory.getInstance().setFabCommunication(null);
            }
        }
        catch(UnknownHostException uhe)
        {
            // Show an error message if it does not work
            ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
            ConnectionStatus.setText(getString(R.string.disconnected));
            ErrorWindowActivity.show(this, getString(R.string.unknown_host));
            CBlockFactory.getInstance().setFabCommunication(null);
        }
    }

    /**
     * Handles the event if the DisconnectButton is clicked
     */
    private void DisconnectButton_onClick(View v) {
        m_currentFactory.disconnect();
        ConnectButton.setEnabled(true);
        DisconnectButton.setEnabled(false);
        ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
        ConnectionStatus.setText(getString(R.string.disconnected));
        CBlockFactory.getInstance().setFabCommunication(null);
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

    /**
     * Goes to the HelpWindowActivity
     */
    public void goToHelpWindowActivity(View view) { //is called by onClick function of Button in activity_main.xml
        String s="\nFactory Select \n\nHere you can scan for available factories and connect to one of them via Wifi.\n\n\n" +
                "Make sure you are connected to the internet. \n\n" +
                "Please press 'Scan Network'-Button to Scan for IPs of potential Factories." +
                "The progress of the scanning process is visible in the progressbar. When the scanning is finished " +
                "select the factory you want to connect to.\n\nBefore starting to connect, enter you password in " +
                "the 'Type in your Identifier'-Textbox. Afterwards press the 'Connect'-Button." +
                "\n\nPlease proceed to 'Warehouse' when successfully connected to one factory in order to enter the " +
                "factory's stock.";
        Intent intent = new Intent(this, HelpWindowActivity.class);
        intent.putExtra("message", s);
        startActivity(intent);

    }

    /**
     * Goes to the WarehouseActivity
     */
    public void goToWarehouseActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, WarehouseActivity.class);
        startActivity(intent);
    }
}
