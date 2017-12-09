package greenest.greenest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 1/31/2016.
 */
public class SolnAdapter extends BaseAdapter {
    ArrayList<QuestionRow> qnList;
    String[]name;
    String[]msg;
    String inputs[]=new String[3];

    String[] dp;

    Bitmap[] dp_img;
    Context context;

    //Get getObj;
    StringBuffer buff;
    @Override
    public int getCount() {
        return qnList.size();
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
        View row=convertView;
        if(row==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.qn_list_row,parent,false);

        }

       final TextView name=(TextView)row.findViewById(R.id.name);
       final TextView msg=(TextView)row.findViewById(R.id.msg);
        ImageView dp=(ImageView)row.findViewById(R.id.profile_pic);

        QuestionRow temp=qnList.get(position);/////////////////////////////////////////get the position
        name.setText(temp.name);

        msg.setText(temp.question);
        Log.i("green", "msg in getView:" + msg.getText().toString());
        dp.setImageBitmap(temp.dp);


        return row;
    }

    public SolnAdapter(Context c,String b[]) throws ExecutionException, InterruptedException {
        inputs=b;
        Log.i("green",inputs[0]);
        context=c;
        qnList=new ArrayList<QuestionRow>();// array of objects
        //inputs=new Solutions().collectIntent();

        String result=callGet();
        Log.i("green",result);//gets d json
        json_parsing(result);

        callAsyncDP();

        for(int i=0;i<name.length;i++){
//-----------each row object is added to the array list..
            Log.i("green","inside 4loop be4 getView");//gets d json

            qnList.add(new QuestionRow(name[i],dp_img[i],msg[i]));

        }






    }




    public void json_parsing(String path) {
        Log.i("green", " entered json_parse");


        try {
            Log.i("green", "path--------"+path);
            int j=1;
            /* parsing JSON and filling views */
            JSONArray jsonArray = new JSONArray(path);
            Log.i("green", "Number of surveys in feed: " + jsonArray.length());
            name=new String[jsonArray.length()];
            msg=new String[jsonArray.length()];
            dp=new String[jsonArray.length()];
            // ---print out the content of the json feed---
            for ( int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                /*dynamic text */

                name[i]=jsonObject.getString("var_UserName");
                msg[i]=jsonObject.getString("solutions");
                dp[i]="http://amigotechnologies.in/homepage/greenest"+jsonObject.getString("sol_text_dp").toString();

            }




        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void callAsyncDP() throws ExecutionException, InterruptedException {
        for (int i=0;i<dp.length;i++){
            Log.i("green", i+name[i]+"");
            Log.i("green", i+dp[i]+"");


        }
        dp_img=new Home().getTaskImg(dp);
        Log.i("green", " ending async2");


    }

    public String callGet() throws ExecutionException, InterruptedException {
        Log.i("green", " entered get.."+inputs[0]);

        buff=new Home().getTask(inputs);
                                                                       /* Get asyncTask=new Get(context, new AsynData() {
                                                                            @Override
                                                                            public void processFinish(StringBuffer output) {
                                                                                s =output.toString();
                                                                            }
                                                                        });*/
        Log.i("green","buff:"+ buff.toString());
        return buff.toString();
    }

}
