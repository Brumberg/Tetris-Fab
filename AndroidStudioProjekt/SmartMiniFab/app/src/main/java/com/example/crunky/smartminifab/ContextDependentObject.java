package com.example.crunky.smartminifab;

import android.content.*;
/**
 * Created by Daniel on 31.12.2016.
 */

/*
 * Base class for a context dependent object
 */
public abstract class ContextDependentObject {
    private final Context m_context;

    /*
     * Creates a new context dependent object
     */
    public ContextDependentObject(Context context) {
        m_context=context;
    }

    /*
     * Returns the context of the object
     */
    public Context getContext() {
        return m_context;
    }
}
