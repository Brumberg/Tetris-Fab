package com.example.crunky.testapp;

import android.content.*;
/**
 * Created by Daniel on 31.12.2016.
 */

public class ContextDependentObject {
    private final Context m_context;

    public ContextDependentObject(Context context) {
        m_context=context;
    }

    public Context getContext() {
        return m_context;
    }
}
