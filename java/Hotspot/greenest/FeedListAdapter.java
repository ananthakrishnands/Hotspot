package greenest.greenest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 1/25/2016.
 */
public class FeedListAdapter extends BaseAdapter {

int likeCount;
    int btnLike_status;
    int btnDislike_status;

    int flag=0;
    ArrayList<SingleRow> list;
    String[]name;
    String []likedMsg;
    String[]msg;
    String[] image;
    String[] dp;
    Bitmap[] img;
    Bitmap[] dp_img;
    Context context;
    Home homeObj;
    //Get getObj;
    StringBuffer buff;
    @Override
    public int getCount() {

        return list.size();
    }
    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //called for each row
        Log.i("green", " in getview");
        View row=convertView;
       /* if(row==null){


        }*/
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row=inflater.inflate(R.layout.list_row,parent,false);

        // init each elements
        final TextView name=(TextView)row.findViewById(R.id.name);
        final TextView msg=(TextView)row.findViewById(R.id.msg);
        ImageView img=(ImageView)row.findViewById(R.id.img);
        ImageView dp=(ImageView)row.findViewById(R.id.profile_pic);
       // final Button like=(Button)row.findViewById(R.id.btnLike);

        //final Button dislike=(Button)row.findViewById(R.id.btnDislike);
        final Button btnReply=(Button)row.findViewById(R.id.btnReply);



        // position starts with 0 denoting first view
        SingleRow temp=list.get(position);/////////////////////////////////////////get the position
        name.setText(temp.name);
        Log.i("green", "name-----------------" + name.getText().toString());
        msg.setText(temp.message);
        Log.i("green", "msg-----------------" + msg.getText().toString());

       // img.setImageBitmap(temp.image);
       // dp.setImageBitmap(temp.dp);



      /*  like.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                LinearLayout rl = (LinearLayout) v.getParent();
                Log.i("green", "direct--and flag---" + msg.getText().toString() + "-----------" + flag);
               // Toast.makeText(context,msg.getText(),Toast.LENGTH_SHORT).show();
                    Log.i("green","msg:"+msg.getText().toString());

                    like.setTextColor(Color.parseColor("#0066ff"));

                String likesInfo[]=new String[4];
                    likesInfo[0]="8";
                    likesInfo[1]=msg.getText().toString();
                    likesInfo[2]=name.getText().toString();
                    likesInfo[3]="a12@a.com";

                try {

                      StringBuffer buff=new Home().getTask(likesInfo);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //////display count

                }





        });
   /*     dislike.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i("green","----------------------in dislikes---1-----------------");

                LinearLayout rl = (LinearLayout) v.getParent();
                Log.i("green", "direct--and flag---" + msg.getText().toString()+"-----------"+flag);
               // Toast.makeText(context,msg.getText(),Toast.LENGTH_SHORT).show();

                Log.i("green","msg:"+msg.getText().toString());
                Log.i("green","----------------------in dislikes----2----------------");

                dislike.setTextColor(Color.parseColor("#0066ff"));
/*

                    e.printStackTrace();
                }*/
                //////display count
/*
            }



        }); */



        return row;





    }
            String likeInfo;
    public FeedListAdapter(Context c,String b[], ArrayList<SingleRow> List) throws ExecutionException, InterruptedException {

            context=c;
            this.list=List;
            list = new ArrayList<SingleRow>();// array of objects

            String result = callGet(b);

            Log.i("green", " going 2 call json_parse");
            Log.i("green", result);

            if (result.toString().trim().equalsIgnoreCase("0")) {

            } else {
                json_parsing(result);


                Log.i("green", " call async1");

                callAsync(image);
                Log.i("green", " call async2");

                callAsyncDP(dp);

                Log.i("green", " init single row objects");


                for (int i = 0; i < name.length; i++) {
//-----------each row object is added to the array list..

                 //   list.add(new SingleRow(name[i], msg[i], img[i], dp_img[i], ""));

                }


                //new Profile().setName(s);

            }
        }




    //JSON parsing

    public void json_parsing(String path) {
        Log.i("green", " entered json_parse");


        try {
            Log.i("green", " entered json_parse");

            /* parsing JSON and filling views */
            JSONArray jsonArray = new JSONArray(path);
            Log.i("green", "Number of surveys in feed: " + jsonArray.length());
            name=new String[jsonArray.length()];
            msg=new String[jsonArray.length()];
            image=new String[jsonArray.length()];
            dp=new String[jsonArray.length()];
            // ---print out the content of the json feed---
            for ( int i = 0; i <jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                /*dynamic text */
                name[i]=jsonObject.getString("var_UserName");

                msg[i]=jsonObject.getString("var_msg");
                image[i]="http://amigotechnologies.in/homepage/greenest"+jsonObject.getString("text_pic").toString();
                dp[i]="http://amigotechnologies.in/homepage/greenest"+jsonObject.getString("text_dp").toString();
//   http://192.168.1.34:80
            }
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            if(s.equalsIgnoreCase("5")){
                Log.i("green","to set inside name function"+jsonObject.getString("var_UserName").toString());
                s=jsonObject.getString("var_UserName").toString();
            }

            Log.i("green",image[0]+","+image[1]);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void json_parsingLikedMsg(String path) {
        Log.i("green", " entered json_parse");


        try {
            Log.i("green", " entered json_parse");

            /* parsing JSON and filling views */
            JSONArray jsonArray = new JSONArray(path);
            Log.i("green", "Number of surveys in feed: " + jsonArray.length());

            likedMsg = new String[jsonArray.length()];
            // ---print out the content of the json feed---
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                /*dynamic text */

                likedMsg[i] = jsonObject.getString("var_msg");


            }


            Log.i("green", image[0] + "," + image[1]);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void callAsyncDP(String []pro) throws ExecutionException, InterruptedException {
        dp_img=new Home().getTaskImg(pro);
        Log.i("green", " ending async2");


    }
    String s;
    public String callGet(String a[]) throws ExecutionException, InterruptedException {
        Log.i("green", " entered get");

        buff=new Home().getTask(a);
                                                                       /* Get asyncTask=new Get(context, new AsynData() {
                                                                            @Override

                                                                   public void processFinish(StringBuffer output) {
                                                                                s =output.toString();
                                                                            }
                                                                        });*/
        //Log.i("green",buff.toString());
        Log.i("green", " buff:"+buff.toString());
        return buff.toString();
    }
    public void callAsync(String[] im) throws ExecutionException, InterruptedException {
        Log.i("green", " in async1");
        img=new Home().getTaskImg(im);


        Log.i("green", " ending async1");


    }
//Json parsing endddd

    //image download------------------------




    //image download end---------------------------





}