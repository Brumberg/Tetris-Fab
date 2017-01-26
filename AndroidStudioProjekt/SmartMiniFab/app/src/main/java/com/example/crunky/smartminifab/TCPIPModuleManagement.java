package com.example.crunky.smartminifab;

import android.content.*;
import android.net.wifi.*;
import android.net.*;

import java.math.*;
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
     * Raised if the scanning process was started
     */
    public void onStartScanning(TCPIPModuleManagement sender);

    /*
     * Raised if an IP was scanned
     */
    public void onIpScanned(TCPIPModuleManagement sender);

    /*
     * Raised if all IPs were scanned and so the scanning was done
     */
    public void onAllIpsScanned(TCPIPModuleManagement sender);
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

    /*
     * Creates a new factory scanning management
     */
    public TCPIPModuleManagement(Context context) {
        super(context);
        m_fabCommunications = new LinkedList<IFabCommunication>();
        m_countOfIpsToBeScanned=
            m_scannedIps=-1;
        m_ipScannedListener=null;
        scanBlockFactories();
    }

    /*
     * Scans the network for factories
     */
    public void scanBlockFactories() {
        // First get the subnet mask
        DhcpInfo dhcpInfo = ((WifiManager) (getContext().getSystemService(Context.WIFI_SERVICE))).getDhcpInfo();
        int subnetmask = dhcpInfo.netmask;
        // Calculate the maximum host part value of the possible IP addresses in the subnet
        int hostpartMax = ((2 ^ ((int) (Math.ceil(Math.log(subnetmask) / Math.log(2))))) - 1) - subnetmask;
        m_countOfIpsToBeScanned=hostpartMax+1;
        m_scannedIps=0;
        if(m_ipScannedListener!=null) {
            m_ipScannedListener.onStartScanning(this);
        }
        int ip = dhcpInfo.ipAddress;
        int netPart = ip & subnetmask;
        m_fabCommunications.clear();
        // Scan each IP address in the subnet
        for (int hostPart = 0; hostPart <= hostpartMax; hostPart++) {
            try {
                // TODO: Check whether context must be passed for using wifi
                IFabCommunication fabCommunication = new TCPIPModule(InetAddress.getByAddress(BigInteger.valueOf(netPart + hostPart).toByteArray()), port);
                // If a factory is accessible at the current IP
                if ((fabCommunication.connect()) && (fabCommunication.disconnect())) {
                    // Add it to the list
                    m_fabCommunications.add(fabCommunication);
                }
            } catch (UnknownHostException e) {
            }
            m_scannedIps++;
            if(m_ipScannedListener!=null) {
                m_ipScannedListener.onIpScanned(this);
            }
        }
        if(m_ipScannedListener!=null) {
            m_ipScannedListener.onAllIpsScanned(this);
        }
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
        return ((IFabCommunication[]) (m_fabCommunications.toArray()));
    }


}
