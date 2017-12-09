package greenest.greenest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 3/20/2016.
 */
public class Control extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File found = new File(Environment.getExternalStorageDirectory(),"GreenestTest");
        if (!(found.exists())) {
            found.mkdir();

        }
        File found1 = new File(Environment.getExternalStorageDirectory()+"/GreenestTest/","Pictures");
        if (!(found1.exists())) {
            found1.mkdir();

        }
        Context context = getApplicationContext();
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            SharedPreferences pref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
            String b[]={"2016"};
            StringBuffer k=null;
            try {
                k=new Home().getTask(b);
                if((pref.getString("ANDROID_HOME","no").toString().equalsIgnoreCase(k.toString()))) {

                    Log.i("check", "its in here");

                }else{
                    SharedPreferences.Editor ed = pref.edit();
                    ed.putString("ANDROID_HOME", k.toString());
                    ed.commit();

                    Log.i("check", "adding "+k.toString());
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (pref.getInt("has", 0) == 0) {

                Intent i = new Intent(Control.this, M2.class);
                startActivity(i);


            } else {
                Intent i = new Intent(Control.this, Splash.class);

                startActivity(i);

            }

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setIcon(R.mipmap.hotspot);
            alert.setTitle("Connection Error");
            alert.setMessage("Unable to connect. Check your internet connection and try again.");
            alert.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onDestroy();
                }
            });
            alert.create();
            alert.show();
        }


    }
}
