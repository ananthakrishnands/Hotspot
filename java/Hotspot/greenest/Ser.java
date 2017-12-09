package greenest.greenest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by admin on 3/27/2016.
 */
public class Ser extends Service{

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("Notification");
Log.i("ananthu","==================================Called Service=======================================");

        registerReceiver(receiver, filter);












        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);

    }

    public static void generateNotification(Context context, String message) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        android.app.Notification notification = new android.app.Notification(R.mipmap.hotspot, message, System.currentTimeMillis());
        // Hide the notification after its selected
        notification.flags |= android.app.Notification.FLAG_AUTO_CANCEL;

        //adding LED lights to notification
        notification.defaults |= android.app.Notification.DEFAULT_LIGHTS;
        notification.defaults |= android.app.Notification.DEFAULT_SOUND;


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(message));
       // startActivity(browserIntent);

        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.setLatestEventInfo(context, "Hotspot", message.trim(), pendingIntent);
        notificationManager.notify(1, notification);
    }



    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("android.net.conn.CONNECTIVITY_CHANGE")|| action.equals("Notification")){
                //action for sms received
                final ConnectivityManager connMgr = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

                final android.net.NetworkInfo wifi = connMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                final android.net.NetworkInfo mobile = connMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if (wifi.isAvailable() || mobile.isAvailable()) {

                    String n[] = {"100"};
                    try {
                        Log.i("ananthu", "STARTED");
                        // StringBuffer b = new Home().getTask(n);
                        String b="Testing Hotspot Beta";
                        if (b.toString().equalsIgnoreCase("") || b.toString().equalsIgnoreCase("0")) {
                            //   generateNotification(context, b.toString());

                        } else {
                            Log.i("ananthu","else");
                            generateNotification(getApplicationContext(), b.toString());
                            Log.i("ananthu", "SToped");
                            Log.i("ananthu", "SToped");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                //action for phone state changed

            }
        }
    };


}
