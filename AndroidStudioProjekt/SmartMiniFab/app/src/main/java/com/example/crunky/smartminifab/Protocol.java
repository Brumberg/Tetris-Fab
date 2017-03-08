package com.example.crunky.smartminifab;

/**
 * Created by Christian on 25.02.2017.
 */

import android.os.AsyncTask;
import android.os.Handler;

public class Protocol {
    private boolean connectionStatus = false;
    private boolean connectionActive = false;
    private boolean singedIn = false;
    private boolean connectionFaild = false;
    private boolean waitForOrderRs = false;
    private int  uiStatus = 0;
    private String signInRsStatus = "Default";
    private String orderRsStatus = "Default";
    private String ordersOnStack = "Default";
    private String timeLeft = "Default";
    private String factoryName = "Default";
    private String ipAdress = "192.168.0.38";
    private String id = "";
    private AsyncTask runningTask;

    WiFiConnection fab;

    Protocol() {

    }

    public int getUiStatus() {
        return uiStatus;
    }

    public void setUiStatus(int value) {
        uiStatus = value;
    }

    boolean getConnectionStatus() {
        return connectionActive;
    }

    public void setconnectionActive (boolean value) {
        connectionActive = value;
    }

    String getIpAdress() {
        return ipAdress;
    }

    /*Returns the values of private attributes.*/
    String getSingInRsStatus () {
        return signInRsStatus;
    }

    String getOrderRsStatus () {
        return orderRsStatus;
    }

    public void resetOrderRsStatus() {
        orderRsStatus ="Default";
    }

    String getOrdersOmStatus () {
        return ordersOnStack;
    }

    public void setConnectionFaild(boolean value) {
        connectionFaild = value;
    }

    String getTimeLeft () {
        return timeLeft;
    }

    String getFactoryName () {
        return factoryName;
    }

    boolean getSignedIn() {
        return singedIn;
    }

    public void setSingedIn(boolean value) {
        singedIn = value;
    }

    /*set the identifier*/
    public void setId(String fabID) {
        id = fabID;
    }

    public WiFiConnection getFab() {
        return fab;
    }

    public void setFab(WiFiConnection actualFab) {
        fab = actualFab;
    }

    /*In the following all send massages of the App are created and send out*/

    public void sendSignIn(String pasword) throws Exception{
        if(fab != null && fab.getConnectionState()) {
            fab.writeLine("[SIGN_IN%" + pasword + "]");
        }
        else
            throw new Exception();
    }

    public void sendBroadcast() throws Exception{
        if(fab != null && fab.getConnectionState()){
            fab.writeLine("[BROADCAST]");
        }
        else
            throw new Exception();
    }

    public void sendSignOut() throws Exception{
        if(fab != null && fab.getConnectionState()) {
            fab.writeLine("[SIGN_OUT]");
        }
        connectionActive = false;
        connectionStatus = false;
        singedIn = false;
        connectionFaild = true;
        fab.close();
        uiStatus = 2;
    }

    public void sendOrder(String OrderString) throws Exception{
        if(fab != null && fab.getConnectionState()) {
            fab.writeLine("[ORDER%"+ id +"%"+OrderString+"]");
            Handler handler = new Handler();
            Runnable r1 = new Runnable() {
                public void run() {
                    if(waitForOrderRs) {

                    }
                }
            };
            handler.postDelayed(r1, 200);

        }
        else
            throw new Exception();
    }

    /*In the following method the incoming frame is split into single signals*/

    public String[] splitMessage(String messageToCheck) {
        if(messageToCheck.contains("*HELLO*")) {
            messageToCheck = messageToCheck.substring(7);
        }
        String splitted[]= messageToCheck.split("%");
        splitted[0]=splitted[0].substring(1,splitted[0].length());
        splitted[splitted.length-1]=splitted[splitted.length-1].substring(0,splitted[splitted.length-1].length()-1);

        return splitted;
    }

    /*The following method gives the actual status of connection*/

    public boolean getConnectionActive() {
        return connectionActive;
    }

    public boolean getConnectionFailed() {
        return connectionFaild;
    }

    public void connectToFab(String ipAdressToCon) throws Exception {
        connectionFaild = false;
        connectionActive = true;
        ipAdress = ipAdressToCon;

        Handler handler = new Handler();
        Runnable r1 = new Runnable() {
            public void run() {
                if(!singedIn && !connectionFaild) {
                    connectionFaild = true;
                    singedIn = false;
                    try {
                        fab.close();
                    } catch (Exception e) {

                    }
                    uiStatus = 9;
                }
            }
        };
        handler.postDelayed(r1, 500);

        while (!singedIn && !connectionFaild) {
        }

        if(getConnectionFailed()) {
            //signInRsStatus = "getConnectionFailedExeption";
            connectionActive = false;
            connectionStatus = false;
            throw new Exception();
        }

    }

    /*The following method handles the connction, it sends the signIn in an defined intervall*/

    public void signIn(final String pasword) {
        final Handler handler = new Handler();
        Runnable r1 = new Runnable() {
                public void run() {
                   /* if (!mTcpClient.getmRun()) {
                        connectionActive = false;
                        singedIn = false;
                    }*/

                    if (connectionStatus&&connectionActive) {
                        connectionStatus = false;
                        try {
                            sendSignIn(pasword);
                        } catch (Exception e) {}
                        handler.postDelayed(this, 5000);
                    } else if(!connectionStatus && connectionActive && singedIn) {
                        if (uiStatus == 0) {
                            uiStatus = 5;
                        }
                        handler.removeCallbacks(this);
                        connectionActive = false;
                        singedIn = false;
                    } else {
                        handler.removeCallbacks(this);
                        connectionActive = false;
                        singedIn = false;
                    }
                }
        };
        try {
            sendSignIn(pasword);
        } catch (Exception e) {
            //ErrorWindow
        }

        handler.postDelayed(r1, 5000);
    }


    /*The following method the SIGN_IN_RS message gets interpreted*/
    public String handleSignInRs(String nOrdersOnStack, String tToFinish, String status) {
        switch (status){
            case "SUCCESSFUL":
                if(connectionActive == true) {
                    connectionStatus = true;
                }
                if(connectionActive == true) {
                    uiStatus = 1;
                }

                return status;


            case "BLOCKED":
                connectionStatus = false;
                connectionActive = false;
                singedIn = false;
                connectionFaild = true;
                try {
                    fab.close();
                } catch (Exception e) {}
                uiStatus = 3;
                return status;


            case "PW_WRONG":
                connectionStatus = false;
                connectionActive = false;
                singedIn = false;
                connectionFaild = true;
                try {
                    fab.close();
                } catch (Exception e) {}
                uiStatus = 6;
                return status;


            case "EXT_ORDER":
                connectionStatus = false;
                connectionActive = false;
                singedIn = false;
                connectionFaild = true;
                try {
                    fab.close();
                } catch (Exception e) {}
                uiStatus = 7;
                return status;


            case "PW_TIMEOUT":
                connectionStatus = false;
                connectionActive = false;
                singedIn = false;
                connectionFaild = true;
                try {
                    fab.close();
                } catch (Exception e) {}
                uiStatus = 8;
                return status;


            default:
                connectionStatus = false;
                connectionActive = false;
                singedIn = false;
                connectionFaild = true;
                try {
                    fab.close();
                } catch (Exception e) {}
                uiStatus = 9;
                status = "ERROR";
                return status;

        }

    }

       /*The following method the ORDER_RS message gets interpreted*/

    public String handleOrderRs(String status) {
        switch (status){
            case "SUCCESSFUL":
                return status;

            case "ORDER_WRONG":
                return status;

            case "PW_WRONG":
                return status;

            default:
                status = "ERROR";
                return status;
        }
    }


    /*The following method the BROADCAST_RS message gets interpreted*/

    public void handleBroadcastRs(String password) {
    }


    /*The following method the incoming message gets indentyfied and the corrensponding function is called*/

    public String messageHandling(String[] incomingMessage) {
        switch (incomingMessage[0]) {

            case "SIGN_IN_RS":
                if(incomingMessage.length==4){
                    handleSignInRs(incomingMessage[1], incomingMessage[2], incomingMessage[3]);
                    ordersOnStack = incomingMessage[1];
                    timeLeft = incomingMessage[2];
                }
                else {
                    connectionStatus = false;
                    connectionActive = false;
                    //Fehler
                }
                return incomingMessage[0];

            case "ORDER_RS":
                if(incomingMessage.length==4){
                    orderRsStatus = handleOrderRs(incomingMessage[3]);
                    ordersOnStack = incomingMessage[2];
                    timeLeft = incomingMessage[3];
                }
                else {
                    //sendSignOut("Falsche Eingabe");
                    //Fehler
                }
                return incomingMessage[0];

            case "BROADCAST_RS":
                if(incomingMessage.length==2){
                    factoryName = incomingMessage[1];
                    handleBroadcastRs(id);
                }
                else {
                    //Fehler
                }
                return incomingMessage[0];

            default:
               // sendSignOut("Falsche Eingabe");
                incomingMessage[0] = "ERROR";
                return incomingMessage[0];
        }
    }

}
