package com.example.crunky.smartminifab;

import android.content.*;

import java.net.*;
import java.util.*;

/**
 * Created by Daniel on 31.12.2016.
 */

/*
 * Event listener for the scanning process
 */
interface IIpScanningListener {
    /*
     * Raised if an IP was scanned
     */
    public void onIpScanned(TCPIPModuleManagement sender);
}

/*
 * Manages the factory scanning in the network
 */
public class TCPIPModuleManagement extends ContextDependentObject {
    private LinkedList<IFabCommunication> m_fabCommunications;
    private static final int port=1000;
    private int m_countOfIpsToBeScanned;
    private int m_scannedIps;
    private IIpScanningListener m_ipScannedListener;
    private boolean m_stopScanning;

    /*
     * Creates a new factory scanning management
     */
    public TCPIPModuleManagement(Context context) {
        super(context);
        m_fabCommunications = new LinkedList<IFabCommunication>();
        m_countOfIpsToBeScanned=
            m_scannedIps=-1;
        m_ipScannedListener=null;
    }

    public String debug = "";

    /*
     * Scans the network for factories
     */
    public void scanBlockFactories() throws Exception {
        m_stopScanning=false;
        // First get the subnet mask
        int prefixLength=32;
        long localHost = 0L;
        int tmp;
        String[] ipParts;
        Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while(networkInterfaces.hasMoreElements()){
            NetworkInterface networkInterface=(NetworkInterface) networkInterfaces.nextElement();
            if(!networkInterface.isLoopback()) {
                Enumeration inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    ipParts=((InetAddress) inetAddresses.nextElement()).getHostAddress().split("\\.");
                    if(ipParts.length==4) {
                        for(int i=0; i<ipParts.length; i++) {
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
        long hostpartMax = ((long)(Math.pow(2, 32 - prefixLength))) - 1L;
        long subnetMask = ((long)(Math.pow(2, 32))) - 1L - hostpartMax;
        m_countOfIpsToBeScanned = 22; // (int)(hostpartMax + 1);
        m_scannedIps = 0;
        m_fabCommunications.clear();
        // Scan each IP address in the subnet
        long netPart = localHost & subnetMask;
        for (long hostPart = 0L; shouldScanningBeResumed(hostPart, hostpartMax); hostPart++) {
            IFabCommunication fabCommunication = new TCPIPModule(InetAddress.getByName(toIpString(netPart | hostPart)), port);
            // If a factory is accessible at the current IP
            // TODO: remove debug changes
            boolean c = fabCommunication.connect();
            boolean d = false; //fabCommunication.disconnect();
            if (c && d) {
                // Add it to the list
                m_fabCommunications.add(fabCommunication);
            }
            debug="onPostExecute executed";
            m_scannedIps++;
            if(m_ipScannedListener!=null) {
                m_ipScannedListener.onIpScanned(this);
            }
        }
    }

    private String toIpString(long ip)
    {
        String result="";
        for(int i=0; i<4; i++){
            result=Long.toString(ip % 256L) + "." + result;
            ip = ip / 256L;
        }
        return result.substring(0, result.length()-1);
    }

    private boolean shouldScanningBeResumed(long hostPart, long hostpartMax) {
        boolean result;
        synchronized(this) {
            result = (hostPart <= hostpartMax) && (!m_stopScanning);
        }
        return result;
    }

    /*
     * Returns the count of IPs which has to be scanned
     */
    public int getCountOfIpsToBeScanned() {
        return m_countOfIpsToBeScanned;
    }

    /*
     * Returns the count of already scanned IPs
     */
    public int getScannedIps() {
        return m_scannedIps;
    }

    /*
     * Sets the event listener of the scanning process
     */
    public void setIpScannedListener(IIpScanningListener listener)
    {
        m_ipScannedListener=listener;
    }

    /*
     * Returns all found factories
     */
    public IFabCommunication[] getBlockFactories() {
        return m_fabCommunications.toArray(new IFabCommunication[m_fabCommunications.size()]);
    }

    public void cancelScanning() {
        synchronized(this) {
            m_stopScanning = true;
        }
    }
}
