package com.example.crunky.smartminifab;

/**
 * Created by oli on 10.01.2017.
 * The idea behind this interface is to provide a basic communication facility.
 * It does not support much more than transmit/receive functions even the connect/disconnect
 * function should be removed (as the interface should be used across the application it would
 * be annoying if some parts use the disconnect function while other parts rely on the communication
 * channel).
 * Remarks:
 * Daniel may modify this interface to fulfill the requirements.
 */


public interface IFabCommunication {
    boolean connect();
    boolean disconnect();
    boolean status();
    boolean login(String password);
    boolean transmit();
}
