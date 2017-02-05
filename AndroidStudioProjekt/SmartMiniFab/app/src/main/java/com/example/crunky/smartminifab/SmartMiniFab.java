package com.example.crunky.smartminifab;

import android.app.Application;

/**
 * Created by Crunky on 05.02.2017.
 */

public class SmartMiniFab extends Application {
    private SeedBoxSize size;

    public SeedBoxSize getSeedBoxSize() {
        return size;
    }

    public void setSeedBoxSize(SeedBoxSize size) {
        this.size = size;
    }
}
