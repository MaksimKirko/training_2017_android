package com.github.maximkirko.training_2017_android.asynctask;

import android.os.AsyncTask;

/**
 * Created by MadMax on 14.02.2017.
 */

public interface TaskFinishedCallback {
    void onTaskFinished(Class<? extends AsyncTask> asyncTaskClass);
}
