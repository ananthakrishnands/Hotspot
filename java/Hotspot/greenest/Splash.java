package greenest.greenest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2/2/2016.
 */
public class Splash extends Activity {
    Bitmap[] img;
    Intent i;
    String id;
    String mes;
    Bitmap[] dp_img;
    InputStream inputStream;
    byte[] data;
    HttpResponse response;
    private SwipeListAdapter listAdapter;
    private StringBuffer buffer;
    ProgressDialog pg;

TextView tv;
    String cond[]=new String[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        startService(new Intent(this,Ser.class));
        tv=(TextView)findViewById(R.id.tvSpace);
        SharedPreferences prefs=getSharedPreferences("prefs", Context.MODE_PRIVATE);
        /*Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {*/
        cond[0]="2";
        cond[1]=prefs.getString("fprint",null);
                try {

                    pg=new ProgressDialog(Splash.this,R.style.MyTheme);
                    pg.setMessage("Loading...");
                  //  pg.setContentView(R.layout.progress_bar);

                    pg.getWindow().setGravity(Gravity.BOTTOM);
                    pg.show();
                    Log.i("green", "splash be4 loading data");
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                             //   String a[]={"400"};
                              //  StringBuffer bu=new Home().getTask(a);
                                //json_parsing(bu.toString());
                                new Asyn().execute(cond).get();
                                Thread.sleep(1800);
                                Log.i("green", "splash after loading data");
                                i= new Intent(Splash.this, NewsFeed.class);
                                Bundle b=new Bundle();
                                b.putString("jsonData", buffer.toString());
                                //b.putString("noti",bu.toString().trim());
                                i.putExtras(b);
                                pg.dismiss();
                                pg.cancel();
                                startActivity(i);
                                onDestroy();


                            } catch (Exception e) {
                                //e.printStackTrace();
                                pg.dismiss();
                                pg.cancel();
                                errorConn();
                            }


                        }
                    });
                    thread.start();

                } catch (Exception e) {
                    e.printStackTrace();

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
      /*  });
        thread.start();


    }*/

    private class Asyn extends AsyncTask<String, Void, Void> {


        List<NameValuePair> nameValuePairs;

        @Override
        protected Void doInBackground(String... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
//
                HttpPost httppost = new HttpPost("http://amigotechnologies.in/homepage/greenest/insertGreenest.php");
                Log.i("green", "url" + params[0].toString());
                nameValuePairs = new ArrayList<NameValuePair>(3);
                    nameValuePairs.add(new BasicNameValuePair("condition", params[0].toString()));
                    nameValuePairs.add(new BasicNameValuePair("fprint", params[1].toString()));

                Log.i("green", "passing cond value (in Questions.java");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ///////////////////// need an if else to give inputs for ph
                // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                // Execute HTTP Post Request
                response = httpclient.execute(httppost);
                Log.i("green", "resp");
                inputStream = response.getEntity().getContent();
                Log.i("green", "getcontent");
                data = new byte[256];
                buffer = new StringBuffer();
                int len = 0;
                while (-1 != (len = inputStream.read(data))) {
                    buffer.append(new String(data, 0, len));
                }

                ////////////////////////////////////////////          json_parsing(buffer.toString());
                Log.i("green", "buffer output :" + buffer.toString());
            } catch (Exception e) {
                // Toast.makeText(getApplicationContext(), "error in http" + e.toString(), Toast.LENGTH_LONG).show();







            }

            return null;
        }

    }










    private void errorConn(){
      /*  AlertDialog.Builder obj = new AlertDialog.Builder(Splash.this);
        obj.setTitle("Network Error");


        obj.setMessage("Error while connecting to server! Check your Network Connections");
        obj.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                /*******************************************************send it 2 db----------------*/
/*
            }
        });
        obj.create();
        obj.show();*/
//        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();



    }
}