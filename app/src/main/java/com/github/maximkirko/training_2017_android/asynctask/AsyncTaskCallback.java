package com.github.maximkirko.training_2017_android.asynctask;

/**
 * Created by MadMax on 04.02.2017.
 */

public interface AsyncTaskCallback<T> {
    void onAsyncTaskFinished(T result);
}
