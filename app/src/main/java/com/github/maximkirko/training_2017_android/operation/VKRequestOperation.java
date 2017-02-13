package com.github.maximkirko.training_2017_android.operation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by MadMax on 13.02.2017.
 */

public class VKRequestOperation {

    protected Context context;
    protected Intent serviceIntent;
    protected BroadcastReceiver broadcastReceiver;

    public static Request newRequest() {
        return new VKRequestOperation().new Request();
    }

    public class Request {

        private Request() {
        }

        public Request setContext(Context context) {
            VKRequestOperation.this.context = context;
            return this;
        }

        public Request setServiceIntent(Intent serviceIntent) {
            VKRequestOperation.this.serviceIntent = serviceIntent;
            return this;
        }

        public Request setBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
            VKRequestOperation.this.broadcastReceiver = broadcastReceiver;
            return this;
        }

        public VKRequestOperation execute() {
            VKRequestOperation vkRequestOperation = new VKRequestOperation();
            vkRequestOperation.context = VKRequestOperation.this.context;
            vkRequestOperation.serviceIntent = VKRequestOperation.this.serviceIntent;
            vkRequestOperation.broadcastReceiver = VKRequestOperation.this.broadcastReceiver;
            startServiceIntent(vkRequestOperation.serviceIntent);
            return vkRequestOperation;
        }
    }

    protected void startServiceIntent(Intent serviceIntent) {
        context.startService(serviceIntent);
    }


    /*private void initUserDataDownloadServiceBroadcastReceiver() {
        userDataDownloadServiceBroadcastReceiver = new UserDataDownloadServiceBroadcastReceiver(this);
    }

    private void initFriendsDownloadServiceBroadcastReceiver() {
        friendsDownloadServiceBroadcastReceiver = new FriendsDownloadServiceBroadcastReceiver(this);
    }

    private void initUserDataServiceIntent() {
        userDataServiceIntent = new Intent(this, UserDataDownloadService.class);
    }

    private void initFriendsServiceIntent(boolean isFirstLoading) {
        friendsServiceIntent = new Intent(this, FriendsDownloadService.class);
        friendsServiceIntent.putExtra(FriendsDownloadService.IS_FIRST_LOADING_EXTRAS, isFirstLoading);
    }*/
}
