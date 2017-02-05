package com.example.crunky.smartminifab;

/**
 * Created by Daniel on 29.01.2017.
 */

/*
 * Is neccessary for returning a null-value from the doInBackground-method of AsyncTasks
 *
 * T: Type of the actual result
 */
public class NullableAsyncTaskResult<T> {
    /*
     * Contains the nullable value which should be returned
     */
    public final T Result;

    /*
     * Creates a new NullableAsyncTaskResult-object
     */
    public NullableAsyncTaskResult(T result) {
        Result=result;
    }
}
