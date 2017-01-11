package com.github.maximkirko.training_2017_android.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

/**
 * Created by MadMax on 11.01.2017.
 */

public class FriendsAsyncLoader extends AsyncTaskLoader<String> {

    private static final String LOG_REQUEST_COMPLETE = "REQUEST COMPLETE";
    private static final String LOG_REQUEST_ERROR = "REQUEST ERROR";

    private VKParameters params;
    private String jsonFriendsList;
    private static boolean flag = false;

    public FriendsAsyncLoader(@NonNull Context context, @NonNull VKParameters params) {
        super(context);
        this.params = params;
    }

    @Override
    public String loadInBackground() {
        final VKRequest request = VKApi.friends().get(this.params);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                jsonFriendsList = response.json.toString();
                Log.i(LOG_REQUEST_COMPLETE, response.json.toString());
                flag = true;
            }

            @Override
            public void onError(VKError error) {
                Log.e(LOG_REQUEST_ERROR, error.errorMessage);
                flag = true;
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Log.e(LOG_REQUEST_ERROR, "attempt failed");
                flag = true;
            }
        });
        while (!flag) {

        }
        return jsonFriendsList;
    }
}
