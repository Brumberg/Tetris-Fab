package com.example.crunky.testapp;

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
    public void onStartScanning(BlockFactoryManagement sender);
    public void onIpScanned(BlockFactoryManagement sender);
    public void onAllIpsScanned(BlockFactoryManagement sender);
}

public class BlockFactoryManagement extends ContextDependentObject {
    private LinkedList<BlockFactory> m_blockFactories;
    private static final int port=1000;
    private int m_countOfIpsToBeScanned;
    private int m_scannedIps;
    private IIpScanningListener m_ipScannedListener;

    public BlockFactoryManagement(Context context) {
        super(context);
        m_blockFactories = new LinkedList<BlockFactory>();
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
        m_blockFactories.clear();
        for (int hostPart = 0; hostPart <= hostpartMax; hostPart++) {
            try {
                BlockFactory factory = new BlockFactory(getContext(), InetAddress.getByAddress(BigInteger.valueOf(netPart + hostPart).toByteArray()), port);
                if ((factory.connect()) && (factory.disconnect())) {
                    m_blockFactories.add(factory);
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

    public BlockFactory[] getBlockFactories() {
        return ((BlockFactory[]) (m_blockFactories.toArray()));
    }


}
