package greenest.greenest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 1/25/2016.
 */
public class NewsFeed extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener{
    ListView list;
    InputStream inputStream;
    String home;
    byte[] data;
    HttpResponse response;
    private SwipeListAdapter listAdapter;
    private  StringBuffer buffer;
  SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<SingleRow> listItems;
    long img_size;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    String imgPath, fileName;
    Button post;
    Bitmap bitmap;
    ImageButton gallery;
    EditText edPost;
    String cond[]={"2"};
    String not_mes[];
    String name_not[];
    ListView listUp;// array of objects
    Bitmap dp_img[];

    Bitmap img[];
    int count;
    SharedPreferences pre;
    String cont,Not_path;
   // AsyncTask check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Activity activity=NewsFeed.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        cont=b.getString("jsonData");
        //Not_path=b.getString("noti");
        //check=new Do_notify().execute("");
        list = (ListView) findViewById(R.id.list);
        // listUp = (ListView) findViewById(R.id.listUpdate);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        edPost=(EditText)findViewById(R.id.edPost);
        gallery=(ImageButton)findViewById(R.id.upload_img);
        post=(Button)findViewById(R.id.btnPost);
        listItems=new ArrayList<SingleRow>();
   pre=getSharedPreferences("prefs",Context.MODE_PRIVATE);
home=pre.getString("ANDROID_HOME","");
        Log.i("check","NewsFeed value of home : "+home);
        listAdapter=new SwipeListAdapter(NewsFeed.this, listItems,pre.getString("fprint",null));
        list.setAdapter(listAdapter);

       // list.setOnScrollListener(new SampleScrollListener(activity));
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        //callImages(image,dp);
                                        try {
                                            Boolean done = true;

                                            Asyn async = new Asyn();
                                            async.execute(cond).get();
                                            Log.i("green", "async************************");


                                           /* do {
                                                if (!(async.getStatus() == AsyncTask.Status.RUNNING)) {*/
                                            try {
                                                count = json_parsing(cont);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            Log.i("green", "dismissed************************");

                                            done = false;

                                            /*    }
                                            }while (done);*/


                                        } catch (Exception e) {

                                        }


                                        Log.i("green", "adapter notified");
                                        swipeRefreshLayout.setRefreshing(false);


                                        //Toast.makeText(getApplicationContext(), list.getCount() + "", Toast.LENGTH_SHORT).show();


                                    }


                                }

        );


    }
    public void doPost(View v){
        if(edPost.getText().toString().trim().equalsIgnoreCase("")){
            Toast.makeText(this,"Post content is empty",Toast.LENGTH_SHORT).show();
        }else{
            Log.i("green","inside else ->post click");

            String toPost[]=new String[5];
            toPost[0]="11";
            toPost[1]=edPost.getText().toString().trim();
            toPost[2]=pre.getString("fprint",null);//////////////////////////////////////////////////////////////////////
            String getCal[]=getDate();
            toPost[3]=getCal[0];
            toPost[4]=getCal[1];

            try {
                StringBuffer b=new Home().getTask(toPost);
                Log.i("green","######### "+"be4 fetchUpdates");

                fetchUpdates();
                Log.i("green", "aftr fetch updates");

                Log.i("green",b.toString());
                edPost.setText("");
                edPost.setHint("What's on your mind?");




            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }



    }
    public void getGallery(){
        int RESULT_LOAD_IMG=1;
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


        Log.i("green", "get pick");

        // startActivityForResult(Intent.createChooser(getCropIntent(intent), "Complete action using"), 1);
        startActivityForResult(intent, RESULT_LOAD_IMG);
        Log.i("green", "after startAct");

    }

    public String[] getDate(){
        Calendar cal = Calendar.getInstance();

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);

        int month = cal.get(Calendar.MONTH)+1;
        String  calend[]=new String[2];
        calend[0]= cal.get(Calendar.HOUR)+":"+(cal.get(Calendar.MINUTE))+":"+cal.get(Calendar.SECOND);

        calend[1]=year+"-"+ month +"-"+ day;
        Toast.makeText(this,calend[1],Toast.LENGTH_SHORT).show();


        return calend;

    }

//////////////////////////////
private  class Do_notify extends AsyncTask<String,Void,Void> {
    String a[]={"400"};
String path;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            StringBuffer bu = new Home().getTask(a);
            path=bu.toString().trim();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Void doInBackground(String... params) {
        try {
            parsing(path);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void parsing(String path) throws JSONException {
        Log.i("green", " entered json_parse");

        JSONArray jsonArray = new JSONArray(path);


        try {
            Log.i("green", " entered json_parse");

            /* parsing JSON and filling views */
            Log.i("green", "Number of surveys in feed: " + jsonArray.length() + ";;;" + path);

            Log.i("green", " be4 loop in parsing");
            not_mes = new String[jsonArray.length()];
            name_not = new String[jsonArray.length()];

            // ---print out the content of the json feed---
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                /*dynamic text */
                //msg=jsonObject.getString("var_UserName");
                not_mes[i] = jsonObject.getString("var_cmd");
                name_not[i] = jsonObject.getString("username");


            }

            // img=new Home().getTaskImg(image);
            // dp_img=new Home().getTaskImg(dp);


            ////////////////////////////////////////////////////////DO WHILE LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOP


        } catch (Exception e) {
            Log.i("green", " EXCEPTION======= " + e.getMessage() + "");
        }

        //return jsonArray.length();

    }


}
    //////////////////////

    public int json_parsing(String path) throws JSONException {
        String[]name;

        String[]msg;
        String[] image;
        String[] dp;
        String []date;
        JSONArray jsonArray = new JSONArray(path);


        try {

            /* parsing JSON and filling views */
            name=new String[jsonArray.length()];
            msg=new String[jsonArray.length()];
            image=new String[jsonArray.length()];
            date=new String[jsonArray.length()];
            dp=new String[jsonArray.length()];

            // ---print out the content of the json feed---
            for ( int i = 0; i <jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                /*dynamic text */
                name[i]=jsonObject.getString("var_UserName");
                msg[i]=jsonObject.getString("var_msg");
                image[i]=home.substring(0,45).toString()+jsonObject.getString("text_pic").toString();
                dp[i]=home.substring(0,45).toString()+jsonObject.getString("text_dp").toString();
                date[i]=jsonObject.getString("date");


            }

           // img=new Home().getTaskImg(image);
           // dp_img=new Home().getTaskImg(dp);


            ////////////////////////////////////////////////////////DO WHILE LOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOP

            for (int i=0; i<name.length;i++){
                Log.i("green","inside loop for obj init");
                SingleRow temp=new SingleRow(name[i],msg[i],image[i],dp[i],date[i]);
                listItems.add(0,temp);
            }

            Log.i("green", "4");


            listAdapter.notifyDataSetChanged();
            Log.i("green", "5");

            Log.i("green",image[0]+","+image[1]);


        } catch (Exception e) {
            Log.i("green", " EXCEPTION======= "+ e.getMessage()+"");
        }

        return jsonArray.length();

    }



    private class ImgDownload extends AsyncTask<String,String,Bitmap[]> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();*/


        }
        protected Bitmap[] doInBackground(String... args) {
            Log.i("green","inside async 1 downloading image 1."+args[0]);
            try {
                for (int i=0;i<args.length;i++) {//
                    if (args[i].equalsIgnoreCase(home.substring(0,45).toString()+"null")){
                        img[i]=null;
                    }
                    else {
                        img[i] = BitmapFactory.decodeStream((InputStream) new URL(args[i]).getContent());
                    }
                }
                Log.i("green","ending async 1 downloading image");

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("green", "ending async 1 downloading image"+e.getLocalizedMessage());

            }
            return img;
        }

        protected void onPostExecute(Bitmap image[]) {
            Log.i("green", "onPost");

        }





    }
    private class DpDownload extends AsyncTask<String,String,Bitmap[]> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Bitmap[] doInBackground(String... args) {
            try {
                for (int i=0;i<args.length;i++) {//
                    if (args[i].equalsIgnoreCase(home.substring(0,45).toString()+"null")){
                        dp_img[i]=null;
                    }
                    else {
                        dp_img[i] = BitmapFactory.decodeStream((InputStream) new URL(args[i]).getContent());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
            return dp_img;
        }

        protected void onPostExecute(Bitmap image[]) {
            Log.i("green", "onPost");

        }





    }

    public void showGallery(View v){
        getGallery();

    }

    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Bundle extras = data.getExtras();


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

//////////////////////////////

            filePath = data.getData();

            String fp = filePath.toString();
            Cursor c = getContentResolver().query(Uri.parse(fp), null, null, null, null);
            c.moveToNext();
            String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
            int sizeIndex = c.getColumnIndex(OpenableColumns.SIZE);


            img_size = (long) ((c.getLong(sizeIndex) / 1024.0));
            final String fileNameSegments[] = path.split("/");
            fileName = fileNameSegments[fileNameSegments.length - 1];//-------------------------filename
            Log.i("green","---size"+fileName+"");

            c.close();

            try {
                // bitmap = extras.getParcelable("data");
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // imageView.setImageBitmap(bitmap);*/
                AlertDialog.Builder obj = new AlertDialog.Builder(NewsFeed.this);
                obj.setTitle("Selected Image");

                if (img_size > 2500) {
                    obj.setMessage("Image size should be less than 2.5MB");
                    obj.setPositiveButton("Choose Another", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            /*******************************************************send it 2 db----------------*/
                            getGallery();

                        }
                    });
                    obj.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                        }
                    });
                }
                else {

                    final ImageView input = new ImageView(NewsFeed.this);
                    final EditText edmsg_img=new EditText(NewsFeed.this);


                    LayoutInflater lp = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View view = lp.inflate(R.layout.alert, null, false);
                    final EditText edalert=(EditText)view.findViewById(R.id.edAlertImg);
                    ImageView imgalert=(ImageView)view.findViewById(R.id.alertimg);



                    imgalert.setImageBitmap(bitmap);

                    obj.setView(view);
                    obj.setPositiveButton("Send", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            /*******************************************************send it 2 db----------------*/
                            /////////////////////////////////////////////////////////bitmap compression
                            Log.i("green","on click Send image posting in Timeline class");
                            String cal[]=getDate();
                            String []par=new String[7];
                            par[0]="7";
                            par[1]= getStringImage(bitmap);
                            par[2]=fileName.toString();
                            par[3]=edalert.getText().toString().trim();

                            par[4]=pre.getString("fprint",null);
                            par[5]=cal[0];
                            par[6]=cal[1];


                            try {
                                Log.i("green","onclick key: "+home);

                                StringBuffer b=new Home().getTask(par);

                                    fetchUpdates();
                                if (b.toString().equalsIgnoreCase("1")){
                                    Toast.makeText(NewsFeed.this,"Posted successfully",Toast.LENGTH_SHORT).show();
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    obj.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub

                        }
                    });
                }
                obj.create();
                obj.show();


                //} catch (IOException e) {
                //  e.printStackTrace();
                // }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();


        if (img_size>600) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 14, baos);
        }
        else{
            bmp.compress(Bitmap.CompressFormat.JPEG, 19, baos);

        }
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;//----------------------------------------------------------------image string
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_m1, menu);
        return true;

    }
    Intent i;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuChoice(item);
    }



    private boolean menuChoice(MenuItem item) {
        switch(item.getItemId()){
            case R.id.home:
                i=new Intent(NewsFeed.this,Profile.class);
                startActivity(i);
                return true;
            case R.id.notification:


                   /* Bundle bun = new Bundle();
                    // bun.putString("msg",Not_msg);
                    bun.putStringArray("notMsg",not_mes);
                    bun.putStringArray("name",name_not);*/
                    i = new Intent(NewsFeed.this, Notification.class);
                    //i.putExtras(bun);
                    startActivity(i);

                return true;


            case R.id.Settings:
                i=new Intent(NewsFeed.this,Settings.class);
                startActivity(i);

                return true;


        }
        return false;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);


        fetchUpdates();

        swipeRefreshLayout.setRefreshing(false);



    }
 int a=1;

   /* @Override
    protected void onRestart() {
        super.onRestart();

        fetchUpdates();
    }*/

    public void fetchUpdates(){
        Log.i("check","valur of b2");

        String pro[]={"12",count+""};
        StringBuffer b;
        try {

            Log.i("check","valur of b1"+home);

            b=new Home().getTask(pro);

            Log.i("check","valur of b## : "+b.toString());
            if (b.toString().trim().equalsIgnoreCase("") || b.toString().trim().equalsIgnoreCase("[]")){
                Toast.makeText(getApplicationContext(),"Refreshed",Toast.LENGTH_SHORT).show();

            }
            else {
                try {
                    int c=json_parsing(b.toString());

                    count=c+count;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }




        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }








    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private class Asyn extends AsyncTask<String, Void, Void> {


        List<NameValuePair> nameValuePairs;

        @Override
        protected Void doInBackground(String... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
//
                HttpPost httppost = new HttpPost(home);
                Log.i("green", "url" + params[0].toString());
                nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("condition", params[0].toString()));
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_LONG).show();


        }


    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public class SampleScrollListener implements AbsListView.OnScrollListener {
        private final Context context;

        public SampleScrollListener(Context context) {
            this.context = context;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            final Picasso picasso = Picasso.with(context);
            if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                picasso.resumeTag(context);
            } else {
                picasso.pauseTag(context);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            // Do nothing.
        }
    }
///////////////////////////////////////////////////////////////////////////////////////


}
