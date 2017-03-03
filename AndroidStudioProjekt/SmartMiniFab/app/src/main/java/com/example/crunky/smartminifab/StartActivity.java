package com.example.crunky.smartminifab;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

import java.net.UnknownHostException;
import java.util.Enumeration;

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
    private Button LoadIP;
    private IFabCommunication m_currentFactory;

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
        LoadIP = (Button) (findViewById(R.id.ID_FactorySelectMode_LoadIP_Button));
        LoadIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLoadPredDefIP_onClick(v);
            }
        });
    }
    /**
     * Handles the event if the LoadPredefinedIPButton is clicked
     */
    private void goLoadPredDefIP_onClick(View v) {
        try {
            int prefixLength = 32;
            long localHost = 0L;
            int tmp;
            String[] ipParts;
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) networkInterfaces.nextElement();
                if (!networkInterface.isLoopback()) {
                    Enumeration inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        ipParts = ((InetAddress) inetAddresses.nextElement()).getHostAddress().split("\\.");
                        if (ipParts.length == 4) {
                            for (int i = 0; i < ipParts.length; i++) {
                                localHost += Long.parseLong(ipParts[i]);
                                localHost *= 256L;
                            }
                            localHost /= 256L;
                            for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                                tmp = address.getNetworkPrefixLength();
                                if (tmp < prefixLength) {
                                    prefixLength = tmp;
                                }
                            }
                        }
                    }
                }
            }
            // Calculate the maximum host part value of the possible IP addresses in the subnet
            long hostpartMax = ((long) (Math.pow(2, 32 - prefixLength))) - 1L;
            long subnetMask = ((long) (Math.pow(2, 32))) - 1L - hostpartMax;
            // Scan each IP address in the subnet
            long netPart = localHost & subnetMask;
            String ip = "";
            for (int i = 0; i < 4; i++) {
                ip = Long.toString(netPart % 256L) + "." + ip;
                netPart = netPart / 256L;
            }
            Url.setText(ip.substring(0, ip.length() - 1));
        } catch (SocketException e) {
            Url.setText("192.168.0.17");
        }
    }

    /**
     * Handles the event if the ConnectButton is clicked
     */
    private void ConnectButton_onClick(View v) {
        AsyncTask<Object, Object, Object> task = new AsyncTask<Object, Object, Object>() {
            private Exception exception;
            private String state = "";
            private Context context;
            private String identifier;
            private String ip;

            @Override
            public Object doInBackground(Object... Objects) {
                try {
                    context = (Context) (Objects[0]);
                    ip = (String)(Objects[1]);
                    identifier = (String) (Objects[2]);
                    m_currentFactory = (IFabCommunication) (new TCPIPModule(InetAddress.getByName(ip), TCPIPModuleManagement.Port));
                    if (m_currentFactory.connect()) {
                        // Try to login to the factory
                        if (m_currentFactory.login(identifier)) {
                            CBlockFactory.getInstance().setFabCommunication(m_currentFactory);
                            state = "ok";
                        } else {
                            // Show an error message if it does not work
                            m_currentFactory.disconnect();
                            CBlockFactory.getInstance().setFabCommunication(null);
                            state = "login failed";
                        }
                    } else {
                        // Show an error message if it does not work
                        CBlockFactory.getInstance().setFabCommunication(null);
                        state = "connect failed";
                    }
                } catch (Exception e) {
                    exception = e;
                }
                return new Object();
            }

            @Override
            public void onPostExecute(Object v) {
                if (exception != null) {
                    ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    ConnectionStatus.setText(getString(R.string.disconnected));
                    ErrorWindowActivity.show(context, getString(R.string.unknown_host));
                    CBlockFactory.getInstance().setFabCommunication(null);
                } else if (state.equals("ok")) {
                    ConnectButton.setEnabled(false);
                    DisconnectButton.setEnabled(true);
                    ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                    ConnectionStatus.setText(getString(R.string.connected));
                    CBlockFactory.getInstance().setFabCommunication(m_currentFactory);
                } else if (state.equals("login failed")) {
                    ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    ConnectionStatus.setText(getString(R.string.disconnected));
                    ErrorWindowActivity.show(context, getString(R.string.wrong_identifier));
                    CBlockFactory.getInstance().setFabCommunication(null);
                } else if (state.equals("connect failed")) {
                    // Show an error message if it does not work
                    ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    ConnectionStatus.setText(getString(R.string.disconnected));
                    ErrorWindowActivity.show(context, getString(R.string.connection_failed));
                    CBlockFactory.getInstance().setFabCommunication(null);
                }
            }
        };
        task.execute(this, Url.getText().toString(), Identifier.getText().toString());
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
