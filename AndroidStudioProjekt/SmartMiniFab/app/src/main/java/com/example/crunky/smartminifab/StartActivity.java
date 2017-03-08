package com.example.crunky.smartminifab;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
    public connectTask asycTask;
    public IFabCommunication m_currentFactory= CBlockFactory.getInstance().getFabCommunication();
    private boolean FistStart;



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


        if(m_currentFactory == null) {
            m_currentFactory = (IFabCommunication) (new TCPIPModule());
            CBlockFactory.getInstance().setFabCommunication(m_currentFactory);
        }
        if(asycTask == null) {
            asycTask = new connectTask();
            asycTask.execute("");
        }

        final Handler handler = new Handler();
        final Runnable r1 = new Runnable() {
            public void run() {
                handler.postDelayed(this, 100);
                if(m_currentFactory.getProtocol().getConnectionActive() && m_currentFactory.getProtocol().getSignedIn()) {
                    WarehouseButton.setEnabled(true);
                } else {
                    WarehouseButton.setEnabled(false);
                }

                if(!WifiAvaible()) {
                    ConnectButton.setEnabled(false);
                    DisconnectButton.setEnabled(false);
                    m_currentFactory.getProtocol().setUiStatus(0);
                    ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    ConnectionStatus.setText("Please activate your wifi.");
                } else if (!m_currentFactory.getProtocol().getConnectionActive()) {
                    ConnectButton.setEnabled(true);
                    DisconnectButton.setEnabled(false);
                    ConnectionStatus.setText("Disconnected");
                }

                switch (m_currentFactory.getProtocol().getUiStatus()) {
                    case 0:
                        break;

                    /*Verbindung erfolgreich aufgebaut*/
                    case 1:
                        ConnectButton.setEnabled(false);
                        DisconnectButton.setEnabled(true);
                        if(m_currentFactory.getProtocol().getOrdersOmStatus().equals("0")) {
                            ConnectionStatus.setText("Connected to: "+ m_currentFactory.getProtocol().getFactoryName() + ".");
                        } else {
                            ConnectionStatus.setText("Connected to: "+ m_currentFactory.getProtocol().getFactoryName() + ". \n There are " +
                                    m_currentFactory.getProtocol().getOrdersOmStatus() + " orders on the stack, it will take " +
                                    m_currentFactory.getProtocol().getTimeLeft() + "s to finish.");
                        }
                        ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                        m_currentFactory.getProtocol().setUiStatus(0);
                        break;

                    /*Verbindung erfolgreich getrennt*/
                    case 2:
                        ConnectButton.setEnabled(true);
                        DisconnectButton.setEnabled(false);
                        ConnectionStatus.setText("Disconnected from: " + m_currentFactory.getProtocol().getFactoryName() + ".");
                        ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                        m_currentFactory.getProtocol().setUiStatus(0);
                        break;

                    /*Fabrik antwortet nicht*/
                    case 3:
                        goToErrorWindowActivity(findViewById(android.R.id.content), "The factory you are trying to connect seems to be busy.");
                        ConnectButton.setEnabled(true);
                        DisconnectButton.setEnabled(false);
                        m_currentFactory.getProtocol().setUiStatus(0);
                        break;

                    /*Verbindungsabbruch*/
                    case 4:
                        goToErrorWindowActivity(findViewById(android.R.id.content), "Connection to " + m_currentFactory.getProtocol().getFactoryName() + " lost. The application will close after you press the BACK Button.");
                        ConnectButton.setEnabled(false);
                        DisconnectButton.setEnabled(false);
                        m_currentFactory.getProtocol().setUiStatus(0);
                        break;

                    /*Client ist aktiv aber Fabrik antwortet nicht*/
                    case 5:
                        goToErrorWindowActivity(findViewById(android.R.id.content), "The factory you are trying to connect is not answering.");
                        ConnectButton.setEnabled(true);
                        DisconnectButton.setEnabled(false);
                        m_currentFactory.getProtocol().setUiStatus(0);
                        break;

                    /*Falscher Identifier*/
                    case 6:
                        goToErrorWindowActivity(findViewById(android.R.id.content), "You entered the wrong Identifier.");
                        ConnectButton.setEnabled(true);
                        DisconnectButton.setEnabled(false);
                        m_currentFactory.getProtocol().setUiStatus(0);
                        break;

                    /*Eine Fremde Bestellung wird abgearbeitet.*/
                    case 7:
                        goToErrorWindowActivity(findViewById(android.R.id.content), "An external Order is in progress.");
                        ConnectButton.setEnabled(true);
                        DisconnectButton.setEnabled(false);
                        m_currentFactory.getProtocol().setUiStatus(0);
                        break;

                    /*Passwordtimeout ist aktiv.*/
                    case 8:
                        goToErrorWindowActivity(findViewById(android.R.id.content), "You entered the wrong Identifier to many times, the factory is no locked.");
                        ConnectButton.setEnabled(true);
                        DisconnectButton.setEnabled(false);
                        m_currentFactory.getProtocol().setUiStatus(0);
                        break;

                    /*Passwordtimeout ist aktiv.*/
                    case 9:
                        goToErrorWindowActivity(findViewById(android.R.id.content), "An unexpected ERROR eccored, please try to connect again.");
                        ConnectButton.setEnabled(true);
                        DisconnectButton.setEnabled(false);
                        m_currentFactory.getProtocol().setUiStatus(0);
                        break;

                                        /*Passwordtimeout ist aktiv.*/
                    case 10:
                        goToErrorWindowActivity(findViewById(android.R.id.content), "Factory does not respont on Order, please try again.");
                        ConnectButton.setEnabled(false);
                        DisconnectButton.setEnabled(true);
                        m_currentFactory.getProtocol().setUiStatus(0);
                        break;

                    default:
                    }
                }
            };
        handler.postDelayed(r1, 100);


        if(m_currentFactory.getProtocol().getConnectionActive()&& m_currentFactory.getProtocol().getSignedIn()) {
            ConnectButton.setEnabled(false);
            DisconnectButton.setEnabled(true);
            if(m_currentFactory.getProtocol().getOrdersOmStatus().equals("0")) {
                ConnectionStatus.setText("Connected to: "+ m_currentFactory.getProtocol().getFactoryName() + ".");
            } else {
                ConnectionStatus.setText("Connected to: "+ m_currentFactory.getProtocol().getFactoryName() + ". \n There are " +
                        m_currentFactory.getProtocol().getOrdersOmStatus() + " orders on the stack, it will take " +
                        m_currentFactory.getProtocol().getTimeLeft() + "s to finish.");
            }
            ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
            m_currentFactory.getProtocol().setUiStatus(0);
        }
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
        //Context context = (Context) new Object();
        //fab.connectToFab(Url.getText().toString());
        if(WifiAvaible()) {
            try {
                m_currentFactory.connect(Url.getText().toString(), Identifier.getText().toString());
                ConnectButton.setEnabled(false);
                CBlockFactory.getInstance().setFabCommunication(m_currentFactory);

            } catch (Exception e) {
                //ErrorWindow
            }
        }
    }




    /**
     * Handles the event if the DisconnectButton is clicked
     */
    private void DisconnectButton_onClick(View v) {
        if(WifiAvaible()) {
            try {
                m_currentFactory.disconnect();
            } catch (Exception e) {
                //ErrorWindow
            }
            DisconnectButton.setEnabled(false);
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

    /**
     * Goes to the HelpWindowActivity
     */
    public void goToHelpWindowActivity(View view) { //is called by onClick function of Button in activity_main.xml
        String s="\nFactory Select \n\nHere you can connect to a factory via Wifi.\n\n\n" +
                "Make sure you are connected to the internet. \n\n" +
                "Please enter the IP of the factory you want to connect to. Pressing the 'enter " +
                "predefined IP'-Button will enter the first part of your IP." +
                "\n\nBefore starting to connect, enter you password in " +
                "the 'Type in your Identifier'-Textbox. Afterwards press the 'Connect'-Button." +
                "\n\nPlease proceed to 'Warehouse' when successfully connected to the factory in order to enter the " +
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

    public void goToErrorWindowActivity(View view, String message) {
        Intent intent = new Intent(this, ErrorWindowActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }

    private boolean WifiAvaible() {
        boolean Wifi = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo NI : netInfo) {
            if (NI.getTypeName().equalsIgnoreCase("WIFI")){
                if (NI.isConnected()){
                    Wifi = true;
                }
            }
        }
        return Wifi;
    }

    class connectTask extends AsyncTask<String,String,Object> {

        @Override
        protected Object doInBackground(String... message) {
            while(!this.isCancelled()) {
                try {

                    if (!m_currentFactory.getProtocol().getSignedIn() && m_currentFactory.getProtocol().getConnectionActive()) {
                        if(m_currentFactory.getProtocol().getFab()!=null) {
                            m_currentFactory.getProtocol().setFab(null);
                        }
                        m_currentFactory.getProtocol().setFab(new WiFiConnection(InetAddress.getByName(m_currentFactory.getProtocol().getIpAdress()), 2000));
                        m_currentFactory.getProtocol().getFab().connect();
                        m_currentFactory.getProtocol().setSingedIn(true);
                    }

                    while (m_currentFactory.getProtocol().getSignedIn()&&m_currentFactory.getProtocol().getConnectionActive()&&m_currentFactory.getProtocol().getFab().getConnectionState()) {
                        m_currentFactory.getProtocol().messageHandling(m_currentFactory.getProtocol().splitMessage(m_currentFactory.getProtocol().getFab().readLine()));
                    }

                } catch (Exception e){

                    if(!m_currentFactory.getProtocol().getSignedIn()&& m_currentFactory.getProtocol().getConnectionActive()){
                        m_currentFactory.getProtocol().setUiStatus(3);
                    } else if (m_currentFactory.getProtocol().getSignedIn() && m_currentFactory.getProtocol().getConnectionActive()) {
                        m_currentFactory.getProtocol().setUiStatus(4);
                    } else {

                    }
                    m_currentFactory.getProtocol().setSingedIn(false);
                    m_currentFactory.getProtocol().setConnectionFaild(true);
                    m_currentFactory.getProtocol().setconnectionActive(false);

                }
            }

            return new Object();
        }

        @Override
        protected void onPostExecute(Object result) {

        }

        @Override
        protected void onCancelled(Object result) {
            /*
        }
            try {
                singedIn = false;
                connectionFaild = true;
                connectionStatus = false;
                fab = null;

            } catch (Exception e) {
                singedIn = false;
                connectionFaild = true;
                connectionStatus = false;
                connectionActive = false;
            }*/
        }

        @Override
        protected void onProgressUpdate(String... values) {

           /* super.onProgressUpdate(values);
            String messageType = "";

            //in the arrayList we add the messaged received from server
            messageType = messageHandling(splitMessage(values[0]));
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list

            switch (messageType) {
                case "SIGN_IN_RS":
                    if(getConnectionStatus() == true) {

                    }
                    break;

                case "ORDER_RS":

                    break;

                case "BROADCAST_RS":

                    break;

                default:
                    break;
            }*/
        }
    }
}
