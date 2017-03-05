package com.example.crunky.smartminifab;

import java.io.IOException;
import java.net.InetAddress;
import android.net.wifi.*;
import android.os.AsyncTask;
import android.os.Handler;

import com.example.crunky.smartminifab.IFabCommunication;
import com.example.crunky.smartminifab.WiFiConnection;

/**
 * Created by oli on 10.01.2017.
 * TCPIPModule is based on Daniels implementation. The module should support all settings required
 * to establish a TCPIP connection. This class is strongly related to an activity (needs to be
 * implemented). This settings are not required by other system components. However, the module
 * implements an interface called IFabCommunication that should be used by other components
 * requiring access to the Arduino. Thus, implementing Bluetooth would just mean substituting
 * TCPIPModule with a corresponding Bluetooth module implementing IFabCommunication.
 * There may be a better description than TCPIP Module. As the module just forms a container holding
 * all relevant TCPIP stuff it should be quite easy to adopt the naming (job for an expert).
 *
 * Open questions:
 *  support of DHCP or static IP?
 *  what happens if the Fab is busy (placing blocks)? If the job requires more than 30secs?
 *  Does it block the GUI? Are there cyclic telegrams (up to now we have send/request pairs -
 *  you send something and get an answer to that request. If there are cyclic requests you
 *  could get answers that are related to different requests)?
 *
 * The module is just provided as it is - that means it has to be extended to take additional
 * requirements into account (maybe Daniel can put some effort into the module).
 */

public class TCPIPModule implements IFabCommunication {
    private Protocol m_connection;
    private boolean m_connected;


    public TCPIPModule() {
        m_connection = new Protocol();
        m_connected = false;
    }

    public boolean connect(String IpAdress, String password) throws Exception {
        m_connection.connectToFab(IpAdress);
        m_connection.setId(password);
        m_connection.sendBroadcast();
        m_connection.signIn(password);
        m_connected = true;

        return m_connected;
    }

    public boolean disconnect() throws Exception{
        if (m_connected) {
            m_connection.sendSignOut();
            m_connected = false;
        }
        return !m_connected;
    }

    public boolean status() {
        return m_connection.getSignedIn();
    }

    public String getConnectionStatus() {
        return m_connection.getSingInRsStatus();
    }


    public boolean transmit(String OrderString) throws Exception{
        boolean transferred = false;
        if ((m_connected) && m_connection.getConnectionActive()) {
            m_connection.sendOrder(OrderString);
        }
        return transferred;
    }

    public Protocol getProtocol() {
        return m_connection;
    }

}
