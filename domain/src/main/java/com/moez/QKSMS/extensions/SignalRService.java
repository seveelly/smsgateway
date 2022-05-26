package com.moez.QKSMS.extensions;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;


public class SignalRService extends Service {
    private HubConnection hubConnection;
    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients

    public SignalRService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        startSignalR();
        return result;
    }

    @Override
    public void onDestroy() {
        hubConnection.stop();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        startSignalR();
        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SignalRService getService() {
            // Return this instance of SignalRService so clients can call public methods
            return SignalRService.this;
        }
    }

    /**
     * method for clients (activities)
     */
    public void sendMessage(String message) {
        hubConnection.send("Send", message);
    }

    /**
     * method for clients (activities)
     */
    public void sendMessage_To(String receiverName, String message) {
        hubConnection.send("Send",receiverName, message);
    }

    private void startSignalR() {

        String serverUrl = "http://192.168.0.196:8099/chathub";
        hubConnection = HubConnectionBuilder.create(serverUrl)
                .build();

        sendMessage("Hello from BNK!");


        String CLIENT_METHOD_BROADAST_MESSAGE = "broadcastMessage";
        hubConnection.on("Send", (message) -> {
            System.out.println("New Message: " + message);
        }, String.class);




        // Subscribe to the received event
/*        mHubConnection.received(new MessageReceivedHandler() {

            @Override
            public void onMessageReceived(final JsonElement json) {
                Log.e("onMessageReceived ", json.toString());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), json.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/

    }

}
