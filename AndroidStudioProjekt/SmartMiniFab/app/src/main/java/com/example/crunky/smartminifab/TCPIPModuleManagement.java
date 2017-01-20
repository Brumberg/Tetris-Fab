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

interface IIpScanningListener {
    public void onStartScanning(TCPIPModuleManagement sender);
    public void onIpScanned(TCPIPModuleManagement sender);
    public void onAllIpsScanned(TCPIPModuleManagement sender);
}

public class TCPIPModuleManagement extends ContextDependentObject {
    private LinkedList<IFabCommunication> m_fabCommunications;
    private static final int port=1000;
    private int m_countOfIpsToBeScanned;
    private int m_scannedIps;
    private IIpScanningListener m_ipScannedListener;

    public TCPIPModuleManagement(Context context) {
        super(context);
        m_fabCommunications = new LinkedList<IFabCommunication>();
        m_countOfIpsToBeScanned=
            m_scannedIps=-1;
        m_ipScannedListener=null;
        scanBlockFactories();
    }

    public void scanBlockFactories() {
        DhcpInfo dhcpInfo = ((WifiManager) (getContext().getSystemService(Context.WIFI_SERVICE))).getDhcpInfo();
        int subnetmask = dhcpInfo.netmask;
        int hostpartMax = ((2 ^ ((int) (Math.ceil(Math.log(subnetmask) / Math.log(2))))) - 1) - subnetmask;
        m_countOfIpsToBeScanned=hostpartMax+1;
        m_scannedIps=0;
        if(m_ipScannedListener!=null) {
            m_ipScannedListener.onStartScanning(this);
        }
        int ip = dhcpInfo.ipAddress;
        int netPart = ip & subnetmask;
        m_fabCommunications.clear();
        for (int hostPart = 0; hostPart <= hostpartMax; hostPart++) {
            try {
                // TODO: Check whether context must be passed for using wifi
                IFabCommunication fabCommunication = new TCPIPModule(InetAddress.getByAddress(BigInteger.valueOf(netPart + hostPart).toByteArray()), port);
                if ((fabCommunication.connect()) && (fabCommunication.disconnect())) {
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

    public int getCountOfIpsToBeScanned() {
        return m_countOfIpsToBeScanned;
    }

    public int getScannedIps() {
        return m_scannedIps;
    }

    public void setIpScannedListener(IIpScanningListener listener)
    {
        m_ipScannedListener=listener;
    }

    public IFabCommunication[] getBlockFactories() {
        return ((IFabCommunication[]) (m_fabCommunications.toArray()));
    }


}
