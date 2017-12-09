package greenest.greenest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 1/30/2016.
 */

public class Solutions extends ActionBarActivity {
    String inputs[]=new String[4];
    private ReplyA listAdapter;
    LinearLayout lin;
    ArrayList<Reply> listItems;
    Bitmap dp_img[];
    TextView pname,msg,time;
    String dp_path;
    EditText edReply;
    Button reply;
    ListView list;
    ImageView dp,imgPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_reply_soln);
lin=(LinearLayout)findViewById(R.id.layout1);
        pname = (TextView) findViewById(R.id.name);
        edReply=(EditText)findViewById(R.id.edReply);
        msg = (TextView) findViewById(R.id.msg);
        time = (TextView) findViewById(R.id.date);
        pname = (TextView) findViewById(R.id.name);
       // imgPost = (ImageView) findViewById(R.id.img);
        list = (ListView) findViewById(R.id.list);
        dp=(ImageView)findViewById(R.id.profile_pic);
        listItems=new ArrayList<Reply>();
    reply=(Button)findViewById(R.id.btnReply);

        listAdapter=new ReplyA(Solutions.this, listItems);

        list.setAdapter(listAdapter);



        Intent i = getIntent();
        Bundle b = i.getExtras();
        inputs[0] = "4";
        inputs[1] = b.getString("msg");
        inputs[2] = b.getString("name");



        getpics(b.getString("img"), null);
        getdp(b.getString("dp"), dp);

dp_path=b.getString("dp");

        pname.setText(inputs[2]);
        time.setText( b.getString("date"));
        msg.setText(inputs[1]);
        try {
            StringBuffer buff= new Home().getTask(inputs);
            int c=json_parsing(buff.toString());

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref=getSharedPreferences("prefs", Context.MODE_PRIVATE);
                String data[] = new String[5];
                data[0] = "14";
                data[1] = edReply.getText().toString();
                data[2] = pname.getText().toString();
                data[3] =pref.getString("fprint",null);
                data[4] = msg.getText().toString();
                edReply.setText("");
                /////////////////////////////////////////////get the encr text here
                try {               //     Log.i("reply","###############inside try of reply2"+data[2]+"dddddddd "+data[3]);

                    StringBuffer buff=new Home().getTask(data);
                    String a=buff.toString().trim();

                    Log.i("green","buffer from reply : "+buff.toString());
                    if(a.trim().equalsIgnoreCase("1")) {
                        Reply temp = new Reply("Ananthu", data[1], dp_path);
                        listItems.add(0, temp);
                        listAdapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Sorry, Try again",Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e1) {
                    e1.printStackTrace();
                    Log.i("green", "error=================== "+e1.getMessage());

                }

                Log.i("green", "4");


                Log.i("green", "5");


            }
        });
    }


            private void getpics(String path, ImageView img) {
                if (path.equalsIgnoreCase("http://amigotechnologies.in/homepage/greenestnull")) {
//            img.setVisibility(View.INVISIBLE);
                    // img.setImageBitmap(null);
                } else {
                    File found = new File("/sdcard/GreenestTest/", path.substring(58).toString().trim());
                    ImageView img1 = new ImageView(Solutions.this);
                    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            180);
                    img1.setLayoutParams(lparams);


                    try {

                        img1.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(found)));
                        lin.addView(img1);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
            }

            private void getdp(String path, ImageView img) {
                if (path.equalsIgnoreCase("http://amigotechnologies.in/homepage/greenestnull")) {
//            img.setVisibility(View.INVISIBLE);
                    // img.setImageBitmap(null);
                } else {
                    File found = new File("/sdcard/GreenestTest/", path.substring(58).toString().trim());
                    if (found.exists()) {
                        try {

                            img.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(found)));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                    } else {
                       new ImgDownload().execute(path);

                    }
                }
            }
    private class ImgDownload extends AsyncTask<String, String, Bitmap> {
        Bitmap bitm;
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "GreenestTest");
        String path = folder.getAbsolutePath();


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dp.setImageBitmap(bitm);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //////////
            if (params[0].equalsIgnoreCase("http://amigotechnologies.in/homepage/greenestnull")) {


            } else {
                ////check whether its in the folder///////////////////////////////////


                try {
                    bitm = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
                    createDirectoryAndSaveFile(bitm, params[0].substring(58));


                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }


            ////////

            return bitm;
        }




        private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {


            File file = new File(new File("/sdcard/GreenestTest/Pictures"), fileName);

            try {

                FileOutputStream out = new FileOutputStream(file);
                imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }



        }
    }
            public int json_parsing(String path) throws JSONException {
                Log.i("green", " entered json_parse");
                String[] name;

                String[] msg;
                String[] image;
                String[] dp;
                String[] date;
                JSONArray jsonArray = new JSONArray(path);


                try {
                    Log.i("green", " buffer" + path.toString());

            /* parsing JSON and filling views */
                    Log.i("green", "Number of surveys in feed: " + jsonArray.length() + ";;;" + path);
                    name = new String[jsonArray.length()];
                    msg = new String[jsonArray.length()];
                    dp = new String[jsonArray.length()];
                    Log.i("green", " be4 loop in parsing");

                    // ---print out the content of the json feed---
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                /*dynamic text */
                        name[i] = jsonObject.getString("var_UserName");
                        msg[i] = jsonObject.getString("var_cmd");
                        dp[i] = "http://amigotechnologies.in/homepage/greenest" + jsonObject.getString("text_dp").toString();


                    }

                    // img=new Home().getTaskImg(image);
                    // dp_img=new Home().getTaskImg(dp);


                    ////////////////////////////////////////////////////////DO WHILE LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOP

                    for (int i = 0; i < name.length; i++) {
                        Log.i("green", "inside loop for obj init");
                        Reply temp = new Reply(name[i], msg[i], dp[i]);
                        listItems.add(0, temp);
                        Log.i("green", "inside loop for obj init");

                    }

                    Log.i("green", "4");


                    listAdapter.notifyDataSetChanged();
                    Log.i("green", "5");


                } catch (Exception e) {
                    Log.i("green", " EXCEPTION======= " + e.getMessage() + "");
                }

                return jsonArray.length();

            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_m1, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuChoice(item);
    }



    private boolean menuChoice(MenuItem item){
        switch(item.getItemId()){
            case R.id.home:
                Toast.makeText(this, "Clicked home", Toast.LENGTH_LONG).show();


                return true;
           /* case R.id.notification:
                Toast.makeText(this, "Clicked settings", Toast.LENGTH_LONG).show();
                return true;
            case R.id.ask:
                return true;
            case R.id.Options:
                return true;*/

        }
        return false;
    }









}
