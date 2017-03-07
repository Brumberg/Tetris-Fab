/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tciptest;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
/**
 * The class extends the Thread class so we can receive and send messages at the same time
 */
public class TCPServer extends Thread {
 
    public static final int SERVERPORT = 2000;
    private boolean running = false;
    private PrintWriter mOut;
    private OnMessageReceived messageListener;
    private JTextArea textsend;
 
    public static void main(String[] args) {
 
        //opens the window where the messages will be received and sent
        ServerBoard frame = new ServerBoard();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
 
    }
 
    /**
     * Constructor of the class
     * @param messageListener listens for the messages
     */
    public TCPServer(OnMessageReceived messageListener, JTextArea texts) {
        this.messageListener = messageListener;
        textsend = texts;
    }
 
    /**
     * Method to send the messages from server to client
     * @param message the message sent by the server
     */
    public void sendMessage(String message){
        if (mOut != null && !mOut.checkError()) {
            mOut.println(message);
            mOut.flush();
        }
    }
    
    public String[] splitMessage(String messageToCheck) {
        String splitted[]= messageToCheck.split("%");
        splitted[0]=splitted[0].substring(1,splitted[0].length());
        splitted[splitted.length-1]=splitted[splitted.length-1].substring(0,splitted[splitted.length-1].length()-1);
        
        return splitted;
    }
 
    public void messageHandling(String[] incomingMessage) {
        switch (incomingMessage[0]) {

            case "SIGN_IN":
                sendMessage("[SIGN_IN_RS%0%0%SUCCESSFUL]");
                textsend.append("\n[SIGN_IN_RS%0%0%SUCCESSFUL]");
              //  messageListener.messageReceived("Server: [SIGN_IN_RS%0%0%SUCCESSFUL]");
                //sendMessage("[SIGN_IN_RS%0%0%BLOCKED]");
                //sendMessage("[SIGN_IN_RS%0%0%PW_WRONG]");
                //sendMessage("[SIGN_IN_RS%0%0%EXT_ORDER]");
                //sendMessage("[SIGN_IN_RS%0%0%PW_TIMEOUT]");
                //sendMessage("[SIGN_IN_RS%0%0%ERROR]");
                break;

            case "ORDER":
                sendMessage("[ORDER_RS%1%180%SUCCESSFUL]");
                textsend.append("\n[ORDER_RS%1%180%SUCCESSFUL]");
              //  messageListener.messageReceived("Server: [ORDER_RS%1%180%SUCCESSFUL]");
                //sendMessage("[ORDER_RS%1%180%ORDER_WRONG]");
                //sendMessage("[ORDER_RS%1%180%PW_WRONG]");
                //sendMessage("[ORDER_RS%1%180%ERROR]");
                break;

            case "BROADCAST":
                sendMessage("[BROADCAST_RS%Manfred]");
                textsend.append("\n[BROADCAST_RS%Manfred]");
                //messageListener.messageReceived("Server: [BROADCAST_RS%Manfred]");
                break;
                
            case "SIGN_OUT":
                break;

            default:
                sendMessage("Falsche Eingabe");
                break;
        }
    }
    
    @Override
    public void run() {
        super.run();
 
        running = true;
 
        try {
            System.out.println("S: Connecting...");
 
            //create a server socket. A server socket waits for requests to come in over the network.
            ServerSocket serverSocket = new ServerSocket(SERVERPORT);
 
            //create client socket... the method accept() listens for a connection to be made to this socket and accepts it.
            Socket client = serverSocket.accept();
            System.out.println("S: Receiving...");
 
            try {
 
                //sends the message to the client
                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
 
                //read the message received from client
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
 
                //in this while we wait to receive messages from client (it's an infinite loop)
                //this while it's like a listener for messages
                while (running) {
                    String message = in.readLine();
 
                    if (message != null && messageListener != null) {
                        //call the method messageReceived from ServerBoard class
                        messageListener.messageReceived(message);
                    }
                }
 
            } catch (Exception e) {
                System.out.println("S: Error");
                e.printStackTrace();
            } finally {
                client.close();
                System.out.println("S: Done.");
            }
 
        } catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
        }
 
    }
 
    //Declare the interface. The method messageReceived(String message) will must be implemented in the ServerBoard
    //class at on startServer button click
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
 
}