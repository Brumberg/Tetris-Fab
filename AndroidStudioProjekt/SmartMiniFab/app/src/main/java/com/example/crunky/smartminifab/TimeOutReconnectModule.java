package com.example.crunky.smartminifab;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;

/**
 * Created by Edith on 29.01.2017.
 */

public class TimeOutReconnectModule {
    public Handler timeOutHandler;
    public Runnable timeOutRun;
    public String Password;
    private int delay;
    private boolean isConnected;
    private TCPIPModule m_currentFactory;


    public void TimeOut(int delayTime, boolean connectStatus, String password) {
/*        Log.d("Delaytimer", "10s");
        delay = delayTime;
        isConnected = connectStatus;
        Password = password;
        timeOutHandler = new Handler();
        timeOutRun = new Runnable() {
            @Override
            public void run() {
                Log.d("starttimer", "10s");
                if (isConnected == true) {
                    timeOutHandler.postDelayed(timeOutRun, delay);
                    // Insert ConnectCode
                    if (m_currentFactory.connect()) {
                        // Try to login to the factory
                        if (m_currentFactory.login(Password)) {
                            CBlockFactory.getInstance().setFabCommunication(m_currentFactory);
                            isConnected = true;
                        } else {
                            // Show an error message if it does not work
                            m_currentFactory.disconnect();
                            //goToErrorWindowActivity(connectButton, getString(R.string.wrong_password));
                            CBlockFactory.getInstance().setFabCommunication(null);
                            isConnected = false;
                        }
                    } else {
                        //goToErrorWindowActivity(connectButton, getString(R.string.connection_failed));
                        CBlockFactory.getInstance().setFabCommunication(null);
                        isConnected = false;
                    }
                }
            }
        };
        if(isConnected==true) {
            timeOutHandler.post(timeOutRun);
        }*/
    }

}
