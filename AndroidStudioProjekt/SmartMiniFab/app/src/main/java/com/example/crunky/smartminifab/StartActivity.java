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

/**
 * StartActivity is the first Activity shown to the user and contains the functionality for scanning for factories and selecting the current one.
 */
public class StartActivity extends AppCompatActivity {

    String msg = "Android : ";
    private Spinner WifiSpinner;
    private Button ScanButton;
    private TextView RequestIdentifier;
    private EditText Identifier;
    private Button ConnectButton;
    private Button DisconnectButton;
    private TextView ConnectionStatus;
    private Button WarehouseButton;
    //private Button DevModeButton;
    //private Button PlacementButton;
    //private Button SeedBoxButton;
    private TCPIPModuleManagement m_factoryManagement;
    private TCPIPModule m_currentFactory;
    private FabCommunicationListAdapter m_adapter;
    private ProgressBar ProgressBar;
    private ScanNetworkAsyncTask m_task;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.d(msg, "The onCreate() event");
        // Initialize controls
        ScanButton = (Button) (findViewById(R.id.ID_FactorySelectMode_Scan_Button));
        ScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanButton_onClick(v);
            }
        });
        WifiSpinner = (Spinner) (findViewById(R.id.ID_FactorySelectMode_WIFI_Spinner));
        WifiSpinner_setEnabled(false);
        WifiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                WifiSpinner_onItemSelected(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                WifiSpinner_onNothingSelected(parent);
            }
        });
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
        //DevModeButton=(Button)(findViewById(R.id.button));
        //PlacementButton=(Button)(findViewById(R.id.button4));
        //SeedBoxButton=(Button)(findViewById(R.id.button5));
        // Initialize factory management
        m_factoryManagement = new TCPIPModuleManagement((Context) (this));
        ProgressBar = (ProgressBar) (findViewById(R.id.ID_FactorySelectMode_Scan_progressBar));
    }

    /**
     * Handles the event if no IFabCommunication is selected in the WifiSpinner
     */
    private void WifiSpinner_onNothingSelected(AdapterView<?> parent) {
        RequestIdentifier.setEnabled(false);
        Identifier.setEnabled(false);
        ConnectButton.setEnabled(false);
        DisconnectButton.setEnabled(false);
    }

    /**
     * Handles the event if a IFabCommunication is selected in the WifiSpinner
     */
    private void WifiSpinner_onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        RequestIdentifier.setEnabled(true);
        Identifier.setEnabled(true);
        ConnectButton.setEnabled(true);
        DisconnectButton.setEnabled(false);
    }

    /**
     * As setting the enabled-state of a Spinner needs a special practice and it is done multiple times it is stored in this method
     */
    private void WifiSpinner_setEnabled(boolean enabled) {
        WifiSpinner.setEnabled(enabled);
        View v = WifiSpinner.getSelectedView();
        if (v != null) {
            v.setEnabled(enabled);
        }
    }

    /**
     * Handles the event if the ScanButton is clicked
     */
    private void ScanButton_onClick(View v) {
        try {
            // Create and start the background scanning task
            m_task = new ScanNetworkAsyncTask((Context) (this));
            m_task.execute("");
        } catch (Exception e) {
            // If an exception was thrown show an error message in the error window
            ErrorWindowActivity.show(this, getString(R.string.scanning_failed));
        }
    }

    /**
     * Handles the event if the ConnectButton is clicked
     */
    private void ConnectButton_onClick(View v) {
        // Try to connect to the factory
        if (m_currentFactory.connect()) {
            // Try to login to the factory
            if (m_currentFactory.login(Identifier.getText().toString())) {
                ConnectButton.setEnabled(false);
                DisconnectButton.setEnabled(true);
                WifiSpinner_setEnabled(false);
                Identifier.setEnabled(false);
                RequestIdentifier.setEnabled(false);
                ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                ConnectionStatus.setText(getString(R.string.connected));
                CBlockFactory.getInstance().setFabCommunication(m_currentFactory);
            } else {
                // Show an error message if it does not work
                m_currentFactory.disconnect();
                ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                ConnectionStatus.setText(getString(R.string.disconnected));
                ErrorWindowActivity.show(this, getString(R.string.wrong_password));
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
        if (m_task != null) {
            m_task.cancel(true);
        }
    }

    /**
     * Goes to the DevelopmentModeActivity
     */
   /* public void goToDevelopmentModeActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, DevelopmentModeActivity.class);
        startActivity(intent);
    }*/

    /**
     * Goes to the PlacementModeActivity
     */
   /* public void goToPlacementModeActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, PlacementModeActivity.class);
        startActivity(intent);
    }*/

    /**
     * Goes to the SeedBoxModeActivity
     */
   /* public void goToSeedBoxModeActivity(View view) { //is called by onClick function of Button in activity_main.xml
        Intent intent = new Intent(this, SeedBoxModeActivity.class);
        startActivity(intent);
    }*/

    /**
     * Goes to the HelpWindowActivity
     */
    public void goToHelpWindowActivity(View view) { //is called by onClick function of Button in activity_main.xml
        String s="\nFactory Select \n\n\nMake sure you are connected to the internet. \n\n" +
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

    /*
     * AsyncTask class for representing the scanning background process
     */
    private class ScanNetworkAsyncTask extends AsyncTask<String, Integer, NullableAsyncTaskResult<Exception>> {
        /*
         * The context of the current StartActivity
         */
        private Context m_context;

        /*
         * Creates a new ScanNetworkAsyncTask
         */
        public ScanNetworkAsyncTask(Context context) {
            super();
            m_context = context;
        }

        /*
         * Executes the background task
         */
        @Override
        protected NullableAsyncTaskResult<Exception> doInBackground(String... message) {
            Exception result = null;
            try {
                // Disconnect from the current factory if necessary
                if (m_currentFactory != null) {
                    m_currentFactory.disconnect();
                }
                // Add the listener for updating the progress bar
                m_factoryManagement.setIpScannedListener(new IIpScanningListener() {
                    @Override
                    public void onIpScanned(TCPIPModuleManagement sender) {
                        publishProgress(m_factoryManagement.getScannedIps(), m_factoryManagement.getCountOfIpsToBeScanned());
                    }
                });
                // Scan
                m_factoryManagement.scanBlockFactories();
            } catch (Exception e) {
                result = e;
            }
            // NullableAsyncTaskResult must be used as if null is returned by doInBackground the app crashes
            return new NullableAsyncTaskResult<Exception>(result);
        }

        /*
         * Sets the control properties to the state at starting the scan
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ScanButton.setEnabled(false);
            WifiSpinner_setEnabled(false);
            RequestIdentifier.setEnabled(false);
            Identifier.setEnabled(false);
            ConnectButton.setEnabled(false);
            DisconnectButton.setEnabled(false);
            ConnectionStatus.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
            ConnectionStatus.setText(getString(R.string.disconnected));
            ProgressBar.setMax(m_factoryManagement.getCountOfIpsToBeScanned());
        }

        /*
         * Updates the progress bar
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            ProgressBar.setProgress(values[0]);
            ProgressBar.setMax(values[1]);
        }

        /*
         * Stops the loop execution of the factory management while scanning
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            m_factoryManagement.cancelScanning();
        }

        /*
         * Sets the controls to the state if the scan was fulfilled
         */
        @Override
        protected void onPostExecute(NullableAsyncTaskResult<Exception> result) {
            super.onPostExecute(result);
            // If an error occured
            if (result.Result != null) {
                // Show it in the error window
                ErrorWindowActivity.show(m_context, getString(R.string.scanning_failed));
            } else {
                m_adapter = new FabCommunicationListAdapter(m_context, m_factoryManagement.getBlockFactories());
                WifiSpinner.setAdapter(m_adapter);
                // Check if any factory was found
                if (WifiSpinner.getCount() > 0) {
                    WifiSpinner_setEnabled(true);
                    WifiSpinner.setSelection(0);
                } else {
                    ErrorWindowActivity.show(m_context, getString(R.string.no_factory_found));
                }
            }
            // TODO: remove debug changes
            ConnectionStatus.setText(m_factoryManagement.debug);
        }
    }
}
