package greenest.greenest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 2/19/2016.
 */
public class Notification extends ActionBarActivity {
    ListView list;
    String msg[],name[];
    StringBuffer b;
    SharedPreferences pre;
    String home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        list=(ListView)findViewById(R.id.list);
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        pre=getSharedPreferences("prefs", Context.MODE_PRIVATE);
        home=pre.getString("ANDROID_HOME",null);
        msg=bundle.getStringArray("notMsg");
        name=bundle.getStringArray("name");



        try {String a[]={"100"};
             b=new Home().getTask(a);
            json_parsing(b.toString());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public int json_parsing(String path) throws JSONException {
        Log.i("green", " entered json_parse");

        JSONArray jsonArray = new JSONArray(path);


        try {
            Log.i("green", " entered json_parse");

            /* parsing JSON and filling views */
            Log.i("green", "Number of surveys in feed: " + jsonArray.length()+";;;"+path);

            Log.i("green", " be4 loop in parsing");

            // ---print out the content of the json feed---
            for ( int i = 0; i <jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                /*dynamic text */
             //   msg=jsonObject.getString("var_UserName");


            }

            // img=new Home().getTaskImg(image);
            // dp_img=new Home().getTaskImg(dp);


            ////////////////////////////////////////////////////////DO WHILE LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOP




        } catch (Exception e) {
            Log.i("green", " EXCEPTION======= " + e.getMessage() + "");
        }

        return jsonArray.length();

    }



}
