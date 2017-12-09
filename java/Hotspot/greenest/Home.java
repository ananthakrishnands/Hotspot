package greenest.greenest;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.LinearLayout;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 1/24/2016.
 */
public class Home extends ActionBarActivity {
    LinearLayout LL;
    StringBuffer buffer;
    HttpResponse response;
    String imagePath;
    String home;
    int p;
    HttpClient httpclient;
    InputStream inputStream;
    String jsonData = "";
    byte[] data;
    Bitmap[] bitmap;
    String[] image;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // LL = (LinearLayout) findViewById(R.id.lin1);
        /*http post----------------*/
        Log.i("green", "call fn");

        //getTask(2);


    }


    private class ImgDownload extends AsyncTask<String, String, Bitmap[]> {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "GreenestTest");
        String path = folder.getAbsolutePath();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();



        }

        protected Bitmap[] doInBackground(String... args) {
            Log.i("green", "inside async 1 downloading image 1." + args[0]);

            for (int i = 0; i < args.length; i++) {//
                if (args[i].equalsIgnoreCase("http://amigotechnologies.in/homepage/greenestnull")) {
                    Log.d("green", "null");

                    bitmap[i] = null;
                } else {
                    ////check whether its in the folder///////////////////////////////////
                    File found = new File("/sdcard/GreenestTest/", args[i].substring(58).toString().trim());

                    if (found.exists()) {
                        try {
                            Log.d("green", "file exists------------------------/*/**//*/**/*/*");

                            bitmap[i] = BitmapFactory.decodeStream(new FileInputStream(found));
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }


                    } else {
                        Log.d("green", "not exist");

                        try {
                            bitmap[i] = BitmapFactory.decodeStream((InputStream) new URL(args[i]).getContent());
                            Log.d("green", "image downloaded");

                            createDirectoryAndSaveFile(bitmap[i], args[i].substring(58));


                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }


                    }
                }


            }

            Log.i("green", "ending async 1 downloading image");


            return bitmap;
        }

        protected void onPostExecute(Bitmap image[]) {
            Log.i("green", "onPost");

        }

        private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {


            File file = new File(new File("/sdcard/GreenestTest/"), fileName);

                try {
                    Log.i("green", "saved file");

                    FileOutputStream out = new FileOutputStream(file);
                    imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }



        }
    }

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
                    Log.i("green", "passing cond value (in Questions.java");
                    if (params[0].equalsIgnoreCase("4")) {

                        nameValuePairs.add(new BasicNameValuePair("question", params[1].toString()));
                        nameValuePairs.add(new BasicNameValuePair("qn_username", params[2].toString()));
                        Log.i("green", "passing qn and username  value (in Questions.java");


                    } else {
                        if (params[0].equalsIgnoreCase("5")) {
                            nameValuePairs.add(new BasicNameValuePair("userid", "1"));
                            Log.i("green", "passing userId (in Questions.java");

                        } else {
                            if (params[0].equalsIgnoreCase("16")) {
                                nameValuePairs.add(new BasicNameValuePair("feedback", params[1].toString()));
                                nameValuePairs.add(new BasicNameValuePair("id", params[2].toString()));

                                Log.i("green", "passing feedback (in home.java");

                            } else {
                                if (params[0].equalsIgnoreCase("7")) {
                                    nameValuePairs.add(new BasicNameValuePair("stringImage", params[1].toString()));
                                    nameValuePairs.add(new BasicNameValuePair("fileName", params[2].toString()));
                                    nameValuePairs.add(new BasicNameValuePair("msg", params[3].toString()));
                                    nameValuePairs.add(new BasicNameValuePair("email", params[4].toString()));
                                    nameValuePairs.add(new BasicNameValuePair("time", params[5].toString()));
                                    nameValuePairs.add(new BasicNameValuePair("date", params[6].toString()));


                                    Log.i("green", "passing uploaded images (in home.java");

                                } else {
                                    if (params[0].equalsIgnoreCase("8") || params[0].equalsIgnoreCase("9")) {
                                        nameValuePairs.add(new BasicNameValuePair("msg", params[1].toString()));
                                        nameValuePairs.add(new BasicNameValuePair("Name", params[2].toString()));
                                        nameValuePairs.add(new BasicNameValuePair("LikedBy", params[3].toString()));
                                        Log.i("green", "passing likes info (in Home.java");

                                    } else {
                                        if (params[0].equalsIgnoreCase("10")) {
                                            nameValuePairs.add(new BasicNameValuePair("LikedBy", params[1].toString()));
                                            Log.i("green", "passing likes info (in Home.java");

                                        } else {
                                            if (params[0].equalsIgnoreCase("11")) {
                                                nameValuePairs.add(new BasicNameValuePair("msg", params[1].toString()));
                                                nameValuePairs.add(new BasicNameValuePair("user", params[2].toString()));
                                                nameValuePairs.add(new BasicNameValuePair("time", params[3].toString()));
                                                nameValuePairs.add(new BasicNameValuePair("date", params[4].toString()));

                                                Log.i("green", "passing post info (in Home.java");


                                            } else {
                                                if (params[0].trim().equalsIgnoreCase("1")) {
                                                    Log.i("green", "login");

                                                    nameValuePairs.add(new BasicNameValuePair("email", params[1].toString()));
                                                    nameValuePairs.add(new BasicNameValuePair("pass", params[2].toString()));
                                                    Log.i("green", "login details");
                                                } else {
                                                    if (params[0].equalsIgnoreCase("12")) {
                                                        nameValuePairs.add(new BasicNameValuePair("count", params[1].toString()));
                                                    } else {
                                                        if (params[0].equalsIgnoreCase("14")) {
                                                            nameValuePairs.add(new BasicNameValuePair("msg", params[1].toString()));
                                                            nameValuePairs.add(new BasicNameValuePair("qnUserName", params[2].toString()));
                                                            nameValuePairs.add(new BasicNameValuePair("replyname", params[3].toString()));
                                                            nameValuePairs.add(new BasicNameValuePair("qnMsg", params[4].toString()));



                                                        }
                                                        else{
                                                            if (params[0].equalsIgnoreCase("222")) {
                                                                nameValuePairs.add(new BasicNameValuePair("dp", params[1].toString()));
                                                                nameValuePairs.add(new BasicNameValuePair("fname", params[2].toString()));
                                                                nameValuePairs.add(new BasicNameValuePair("fprint", params[3].toString()));
                                                                nameValuePairs.add(new BasicNameValuePair("date", params[4].toString()));
                                                                nameValuePairs.add(new BasicNameValuePair("time", params[5].toString()));




                                                            }
                                                            else {
                                                                if (params[0].equalsIgnoreCase("88")) {
                                                                    nameValuePairs.add(new BasicNameValuePair("msg", params[1].toString()));
                                                                    nameValuePairs.add(new BasicNameValuePair("Name", params[2].toString()));
                                                                    nameValuePairs.add(new BasicNameValuePair("LikedBy", params[3].toString()));






                                                                }




                                                            }

                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ///////////////////// need an if else to give inputs for php
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                // Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_LONG).show();


            }


        }


        public Bitmap[] getTaskImg(String img[]) throws ExecutionException, InterruptedException {
            bitmap = new Bitmap[img.length];


            new ImgDownload().execute(img).get();

            return bitmap;


        }


        public StringBuffer getTask(String a[]) throws ExecutionException, InterruptedException {
            Log.i("green", "enterd gettask for soln...############################." + a[0]);

            new Asyn().execute(a).get();
            return buffer;


        }


    }

