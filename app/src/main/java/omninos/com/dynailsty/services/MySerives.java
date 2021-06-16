package omninos.com.dynailsty.services;

import android.app.NotificationManager;
import android.arch.lifecycle.LifecycleService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import omninos.com.dynailsty.activities.NoInternetActivity;
import omninos.com.dynailsty.util.ConstantData;


public class MySerives extends LifecycleService {

    static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    NotificationManager manager;

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Let it continue running until it is stopped.
//        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        super.onStartCommand(intent, flags, startId);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (CONNECTIVITY_CHANGE_ACTION.equals(action)) {
                    //check internet connection
                    if (!ConnectionHelper.isConnectedOrConnecting(context)) {
                        if (context != null) {
                            boolean show = false;
                            if (ConnectionHelper.lastNoConnectionTs == -1) {//first time
                                show = true;
                                ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis();
                            } else {
                                if (System.currentTimeMillis() - ConnectionHelper.lastNoConnectionTs > 1000) {
                                    show = true;
                                    ConnectionHelper.lastNoConnectionTs = System.currentTimeMillis();
                                }
                            }

                            if (show && ConnectionHelper.isOnline) {
                                ConnectionHelper.isOnline = false;
                                Log.i("NETWORK123", "Connection lost");
                                //manager.cancelAll();
                                ConstantData.EnableInternet = "1";
                                Intent intent1=new Intent(MySerives.this, NoInternetActivity.class);
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent1);
                            }
                        }
                    } else {
                        Log.i("NETWORK123", "Connected");
//                        showNotifications("APP" , "It is working");
                        // Perform your actions here
                        ConnectionHelper.isOnline = true;
                        ConstantData.EnableInternet = "0";
                        Intent intent1 = new Intent("update");
                        LocalBroadcastManager.getInstance(MySerives.this).sendBroadcast(intent1);
                    }
                }
            }
        };
        registerReceiver(receiver, filter);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

}
