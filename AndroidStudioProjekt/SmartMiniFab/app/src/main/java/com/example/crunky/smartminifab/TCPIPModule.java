package com.example.crunky.smartminifab;

import java.io.IOException;
import java.net.InetAddress;
import android.net.wifi.*;
import com.example.crunky.smartminifab.IFabCommunication;
import com.example.crunky.smartminifab.WiFiConnection;

/**
 * Created by oli on 10.01.2017.
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
                m_connection.close();
                m_connection.writeLine("BYE");
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

    public boolean transmit() {
        boolean transferred = false;
        if ((m_connected) && (m_loggedIn)) {
            try {
                m_connection.writeLine("SEEDBOX " + toString());
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
