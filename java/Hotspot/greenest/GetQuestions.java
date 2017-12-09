package greenest.greenest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 1/30/2016.
 */
public class GetQuestions extends BaseAdapter{

    ArrayList<QuestionRow> qnList;
    String[]name;
    String[]msg;

    String[] dp;
    Bitmap[] img;
    Bitmap[] dp_img;
    Context context;

    //Get getObj;
    StringBuffer buff;
    @Override
    public int getCount() {

        return qnList.size();
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

        return qnList.get(position);
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
        if(row==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.qn_list_row,parent,false);

        }

        // init each elements
        TextView name=(TextView)row.findViewById(R.id.name);
        TextView msg=(TextView)row.findViewById(R.id.msg);
        ImageView dp=(ImageView)row.findViewById(R.id.profile_pic);

        // position starts with 0 denoting first view
        QuestionRow temp=qnList.get(position);/////////////////////////////////////////get the position
        name.setText(temp.name);
        msg.setText(temp.question);
        dp.setImageBitmap(temp.dp);

        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout rl = (LinearLayout) v.getParent();
                TextView tv = (TextView) rl.findViewById(R.id.msg);
                TextView tvname = (TextView) rl.findViewById(R.id.name);
                Log.i("green", "on click (in Questions.java");


                String qn_msg = tv.getText().toString();
                String tname = tvname.getText().toString();
                Log.i("green", "going 2 call jump");



                Log.i("green", "inside  jump");
               if(context instanceof Questions){
                    ((Questions)context).jump(qn_msg,tname);
                }



            }
        });



        return row;

    }

    public GetQuestions(Context c,String a[]) throws ExecutionException, InterruptedException {
        context=c;
        qnList=new ArrayList<QuestionRow>();// array of objects
        Log.i("green", "in getQ constructor for in Questions.java");

        String result=callGet(a);//gets d json
        json_parsing(result);

        callAsyncDP();



        for(int i=0;i<name.length;i++){
//-----------each row object is added to the array list..
             qnList.add(new QuestionRow(name[i],dp_img[i],msg[i]));

        }



    }

    //JSON parsing

    public void json_parsing(String path) {
        Log.i("green", " entered json_parse");


        try {
            Log.i("green", "path--------"+path);

            /* parsing JSON and filling views */
            JSONArray jsonArray = new JSONArray(path);
            Log.i("green", "Number of surveys in feed: " + jsonArray.length());
            name=new String[jsonArray.length()];
            msg=new String[jsonArray.length()];
            dp=new String[jsonArray.length()];
            // ---print out the content of the json feed---
            for ( int i = 0; i < name.length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                /*dynamic text */
                name[i]=jsonObject.getString("var_UserName");
                msg[i]=jsonObject.getString("var_question");
                dp[i]="http://amigotechnologies.in/homepage/greenest"+jsonObject.getString("text_dp").toString();

            }



        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void callAsyncDP() throws ExecutionException, InterruptedException {
        dp_img=new Home().getTaskImg(dp);
        Log.i("green", " ending async2");


    }
    String s;
    public String callGet(String a[]) throws ExecutionException, InterruptedException {
        Log.i("green", " entered get");
        Log.i("green", "passing 3 for getting json (in Questions.java)");

        buff=new Home().getTask(a);
                                                                       /* Get asyncTask=new Get(context, new AsynData() {
                                                                            @Override
                                                                            public void processFinish(StringBuffer output) {
                                                                                s =output.toString();
                                                                            }
                                                                        });*/
        //Log.i("green",buff.toString());
        return buff.toString();
    }

//Json parsing endddd

    //image download------------------------




    //image download end---------------------------





}
