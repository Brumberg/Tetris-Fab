package com.example.crunky.smartminifab;

import java.io.IOException;
import java.net.InetAddress;
import android.net.wifi.*;
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
    private WiFiConnection m_connection;
    private boolean m_connected;
    private boolean m_loggedIn;

    public TCPIPModule(InetAddress address, int port) {
        m_connection = new WiFiConnection(address, port);
        m_connected = m_loggedIn = false;
    }

    public boolean connect() {
        try {
            m_connection.connect();
            // TODO: remove debug changes
            m_connection.writeLine("HELLO");
            m_connected = m_connection.readLine().equals("ARDUINO");
        } catch (IOException e) {
            m_connected = false;
        }
        return m_connected;
    }

    public boolean disconnect() {
        if (m_connected) {
            try {
                m_connection.writeLine("BYE");
                m_connection.close();
            } catch (IOException e) {
            }
            m_connected =
                    m_loggedIn = false;
        }
        return !m_connected;
    }

    public boolean status() {
        return true;
    }
    public boolean login(String password)
            throws IllegalArgumentException {
        if ((m_connected) && (!m_loggedIn)) {
            try {
               m_connection.writeLine("PASSWORD " + password);
                m_loggedIn = m_connection.readLine().equals("OK");
            } catch (IOException e) {
                disconnect();
            }
        }
        return m_loggedIn;
    }

    public boolean transmit(String OrderString) {
        boolean transferred = false;
        if ((m_connected) && (m_loggedIn)) {
            try {
                m_connection.writeLine("SEEDBOX " + OrderString);
                transferred = m_connection.readLine().equals("OK");
            } catch (IOException e) {
                disconnect();
            }
        }
        return transferred;
    }

    public InetAddress getHost() {
        return m_connection.getHost();
    }
}
