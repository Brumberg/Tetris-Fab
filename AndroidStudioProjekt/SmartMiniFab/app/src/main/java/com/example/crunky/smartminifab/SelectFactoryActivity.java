package com.example.crunky.smartminifab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import static com.example.crunky.smartminifab.R.id.*;

public class SelectFactoryActivity extends AppCompatActivity {

    private Button bScan;
    private ProgressBar pbScanProgress;
    private TCPIPModuleManagement m_factoryManagement;
    private TCPIPModule m_currentFactory;
    private ListView lvFactories;
    private FabCommunicationListAdapter m_adapter;
    private TextView tvEnterPassword;
    private EditText etPassword;
    private TextView tvConnected;
    private TextView tvDisconnected;
    private Button bConnect;
    private Button bDisconnect;
    private Button bStorage;
    private LinearLayout llSelectFactory;
    private LinearLayout llMessage;
    private TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_factory);
        bScan = (Button) (findViewById(R.id.bScan));
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
        pbScanProgress = (ProgressBar) (findViewById(R.id.pbScanProgress));
        lvFactories = (ListView) (findViewById(R.id.lvFactories));
        lvFactories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvFactories_onItemClick(view);
            }
        });
        tvEnterPassword=(TextView)(findViewById(R.id.tvEnterPassword));
        etPassword=(EditText)(findViewById(R.id.etPassword));
        tvConnected=(TextView)(findViewById(R.id.tvConnected));
        tvDisconnected=(TextView)(findViewById(R.id.tvDisconnected));
        bConnect=(Button)(findViewById(R.id.bConnect));
        bDisconnect=(Button)(findViewById(R.id.bDisconnect));
        bStorage=(Button)(findViewById(R.id.bStorage));
        llSelectFactory=(LinearLayout)(findViewById(R.id.llSelectFactory));
        llMessage=(LinearLayout)(findViewById(R.id.llMessage));
        tvMessage=(TextView)(findViewById(R.id.tvMessage));
    }

    private void bScan_onClick(View v) {
        m_factoryManagement.scanBlockFactories();
    }

    private void factoryManagement_onStartScanning(TCPIPModuleManagement sender) {
        if(m_currentFactory!=null) {
            m_currentFactory.disconnect();
        }
        pbScanProgress.setMax(sender.getCountOfIpsToBeScanned());
        pbScanProgress.setProgress(0);
        bScan.setVisibility(View.INVISIBLE);
        pbScanProgress.setVisibility(View.VISIBLE);
        lvFactories.setAdapter(null);
        lvFactories.setEnabled(false);
        tvEnterPassword.setEnabled(false);
        etPassword.setEnabled(false);
        tvConnected.setVisibility(View.INVISIBLE);
        bDisconnect.setVisibility(View.INVISIBLE);
        tvDisconnected.setVisibility(View.VISIBLE);
        bConnect.setVisibility(View.VISIBLE);
        bConnect.setEnabled(false);
        bStorage.setEnabled(false);
    }

    private void factoryManagement_onIpScanned(TCPIPModuleManagement sender) {
        pbScanProgress.setProgress(sender.getScannedIps());
    }

    private void factoryManagement_onAllIpsScanned(TCPIPModuleManagement sender) {
        bScan.setVisibility(View.VISIBLE);
        pbScanProgress.setVisibility(View.INVISIBLE);
        m_adapter=new FabCommunicationListAdapter(this, m_factoryManagement.getBlockFactories());
        lvFactories.setAdapter(m_adapter);
        lvFactories.setEnabled(true);
        if(lvFactories.getCount()>0) {
            lvFactories.setSelection(0);
        }
        bStorage.setEnabled(true);
    }

    private void lvFactories_onItemClick(View view) {
        m_currentFactory=(TCPIPModule)(lvFactories.getSelectedItem());
        if(m_currentFactory!=null) {
            tvEnterPassword.setEnabled(true);
            etPassword.setEnabled(true);
            bConnect.setEnabled(true);
        } else {
            tvEnterPassword.setEnabled(true);
            etPassword.setEnabled(true);
            bConnect.setEnabled(true);
        }
    }

    private void bConnect_onClick(View v) {
        if(m_currentFactory.connect())
        {
            if(m_currentFactory.login(etPassword.getText().toString())) {
                tvDisconnected.setVisibility(View.INVISIBLE);
                bConnect.setVisibility(View.INVISIBLE);
                tvConnected.setVisibility(View.VISIBLE);
                bDisconnect.setVisibility(View.VISIBLE);
                CBlockFactory.getInstance().setFabCommunication(m_currentFactory);
            }
            else {
                m_currentFactory.disconnect();
                showMessage(getString(R.string.wrong_password));
                CBlockFactory.getInstance().setFabCommunication(null);
            }
        }
        else {
            showMessage(getString(R.string.connection_failed));
            CBlockFactory.getInstance().setFabCommunication(null);
        }
    }

    private void bDisconnect_onClick(View v) {
        if(m_currentFactory.disconnect()) {
            tvConnected.setVisibility(View.INVISIBLE);
            bDisconnect.setVisibility(View.INVISIBLE);
            tvDisconnected.setVisibility(View.VISIBLE);
            bConnect.setVisibility(View.VISIBLE);
        }
        else {
            showMessage(getString(R.string.connection_failed));
        }
        CBlockFactory.getInstance().setFabCommunication(null);
    }

    private void bStorage_onClick(View v) {
        Intent intent = new Intent(this, StorageActivity.class);
        startActivity(intent);
    }

    private void bOk_onClick(View v) {
        llSelectFactory.setEnabled(true);
        llMessage.setVisibility(View.INVISIBLE);
    }

    private void showMessage(String s) {
        llSelectFactory.setEnabled(false);
        llMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(s);
    }
}
