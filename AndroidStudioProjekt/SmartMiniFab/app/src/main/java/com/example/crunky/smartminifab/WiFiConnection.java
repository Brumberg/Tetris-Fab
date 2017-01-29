package com.example.crunky.smartminifab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.content.*;
import android.net.wifi.*;
import android.net.*;

import java.io.*;
import java.math.*;
import java.net.*;
import android.os.AsyncTask;

/**
 * Created by Daniel on 31.12.2016.
 * This class simply extends the TCPIP module. It was extracted from Daniel´s development branch.
 * Can be modified as required. See TCPIP Module.
 */

public class WiFiConnection {
    private Socket m_socket;
    private BufferedReader m_reader;
    private OutputStreamWriter m_writer;
    private final int m_port;
    private final InetAddress m_host;

    public WiFiConnection(InetAddress host, int port)
    {
        m_host=host;
        m_port=port;
    }

    public InetAddress getHost() {
        return m_host;
    }

    public int getPort() {
        return m_port;
    }

    public void connect()
            throws IOException
    {
        IOException result=null;
        try {
            result = new AsyncTask<String,String,IOException>() {
                @Override
                protected IOException doInBackground(String... message) {
                    IOException result=null;
                    try {
                        if((m_socket!=null)&&(m_socket.isConnected())) {
                            close();
                        }
                        m_socket = new Socket(m_host, m_port);
                        m_reader=new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
                        m_writer=new OutputStreamWriter(m_socket.getOutputStream());
                    }
                    catch(IOException e) {
                        result=e;
                    }
                    return result;
                }
            }.execute("").get();
        }+
        catch(Exception e) {
            result=new IOException(e);
        }
        if(result!=null)
        {
            throw result;
        }
    }

    public void writeLine(final String line)
            throws IOException
    {
        IOException exception=null;
        try {
            exception = new AsyncTask<String,String,IOException>() {
                @Override
                protected IOException doInBackground(String... message) {
                    IOException result=null;
                    try {
                        m_writer.write(line + "\r\n");
                        m_writer.flush();
                    }
                    catch(IOException e) {
                        result=e;
                    }
                    return result;
                }
            }.execute("").get();
        }
        catch(Exception e) {
            exception=new IOException(e);
        }
        if(exception!=null)
        {
            throw exception;
        }
    }

    public String readLine()
            throws IOException
    {
        Object result=null;
        try {
            result = new AsyncTask<String,String,Object>() {
                @Override
                protected Object doInBackground(String... message) {
                    Object result=null;
                    try {
                        result=m_reader.readLine();
                    }
                    catch(IOException e) {
                        result=e;
                    }
                    return result;
                }
            }.execute("").get();
        }
        catch(Exception e) {
            result=new IOException(e);
        }
        if(result instanceof IOException)
        {
            throw ((IOException)(result));
        }
        return result.toString();
    }

    public void close()
            throws IOException
    {
        IOException exception=null;
        try {
            exception = new AsyncTask<String,String,IOException>() {
                @Override
                protected IOException doInBackground(String... message) {
                    IOException result=null;
                    try {
                        m_socket.close();
                    }
                    catch(IOException e) {
                        result=e;
                    }
                    return result;
                }
            }.execute("").get();
        }
        catch(Exception e) {
            exception=new IOException(e);
        }
        if(exception!=null)
        {
            throw exception;
        }
    }
}
