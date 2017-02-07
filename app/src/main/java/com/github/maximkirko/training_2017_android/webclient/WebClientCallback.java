package com.github.maximkirko.training_2017_android.webclient;

/**
 * Created by MadMax on 04.02.2017.
 */

public interface WebClientCallback<T> {
    void onUrlLoading(T result);
}
