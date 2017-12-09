package greenest.greenest;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by admin on 3/20/2016.
 */
public class Not extends BroadcastReceiver {
int flag=0;
    String mes,id;
    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String a[]={"100"};
            try {
                StringBuffer b=new Home().getTask(a);

                if (b.toString().trim().equalsIgnoreCase("")){

                }else {
                    json_parsing(b.toString().trim());

                        generateNotification(context, mes);

                }
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Toast.makeText(context, "Network Available Do operations", Toast.LENGTH_LONG).show();

        }








}
    private void json_parsing(String path) {

        try {
            /* parsing JSON and filling views */
            JSONArray jsonArray = new JSONArray(path);
            Log.i("green",
                    "Number of surveys in feed: " + jsonArray.length());

            // ---print out the content of the json feed---
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                /*dynamic text */
                id=jsonObject.getString("sint_notId");
                mes=jsonObject.getString("var_notify");



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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


}
