package com.example.crunky.smartminifab;

/**
 * Created by oli on 10.01.2017.
 */

public interface IFabCommunication {
    boolean connect();
    boolean disconnect();
    boolean status();
    boolean login(String password);
    boolean transmit();
}
