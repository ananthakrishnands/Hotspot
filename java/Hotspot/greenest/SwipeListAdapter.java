package greenest.greenest;

/**
 * Created by admin on 2/14/2016.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
 * Created by admin on 2/13/2016.
 */
public class SwipeListAdapter extends BaseAdapter {
    private LruCache<String, Bitmap> mMemoryCache;

    private Activity activity;
    int flag;
    ViewGroup.LayoutParams lp=new LinearLayout.LayoutParams(0,0);
    ViewGroup.LayoutParams lp2=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,50);


    AsyncTask dpd;
    String imgtag;
    String dpTag;
    private LayoutInflater inflater;
    Bitmap bitmap[];
    private List<SingleRow> Listitems;
    List<Integer>  pos_list = new ArrayList<>();
    String fprint;
    DB dbhelp;
    int addr[];
    public SwipeListAdapter(Activity activity, List<SingleRow> listitems,String f) {
        this.activity = activity;
        this.Listitems = listitems;
        fprint=f;
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

         dbhelp=new DB(activity.getApplicationContext());
//dbhelp.disp();
        // addr=new int[Listitems.size()];

    }
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
Log.i("key",key);
        //if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        //}
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    @Override
    public int getCount() {

        return Listitems.size();

    }

    @Override
    public Object getItem(int location) {
        return Listitems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View row, ViewGroup parent) {
        ViewHolder hold =null;
        if(row==null){


         inflater = (LayoutInflater) activity
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         row = inflater.inflate(R.layout.list_row, null);
         hold = new ViewHolder();
         hold.name = (TextView) row.findViewById(R.id.name);
         hold.msg = (TextView) row.findViewById(R.id.msg);
         hold.img = (ImageView) row.findViewById(R.id.img);
         hold.dp = (ImageView) row.findViewById(R.id.profile_pic);
         hold.date = (TextView) row.findViewById(R.id.date);
         hold.like = (TextView) row.findViewById(R.id.btnLike);
         //hold.btnReply = (Button) row.findViewById(R.id.btnReply);
         hold.status = (TextView) row.findViewById(R.id.statusCheck);
         //  hold.dislike = (Button) row.findViewById(R.id.btnDislike);
         lp2 = hold.img.getLayoutParams();
         row.setTag(hold);


     }

//}
else{

    hold=(ViewHolder)row.getTag();
}
hold.posi=position;
hold.like.setTextColor(Color.parseColor("#808080"));
    hold.like.setText("Like");
//        hold.dislike.setText("Dislike");

        final ViewHolder h=hold;

        hold.name.setText(Listitems.get(position).name);
////////////////////////////////db/////////////////////////////////////////
       flag= dbhelp.check(Listitems.get(position).name,Listitems.get(position).message);
        Log.i("flag","value== "+flag);
        if(flag==1){
            hold.like.setTextColor(Color.parseColor("#6495ed"));
            hold.like.setText("Liked");

        }
        ///////////////////////////////db////////////////////////////////////


       String message=Listitems.get(position).message;
       if(message.toString().trim().length()>=13 && message.substring(0,13).equalsIgnoreCase("<status%%@>##")){
                hold.status.setText(" updated status");
                hold.msg.setText(message.substring(13));


        }else {
           hold.status.setText("");
                hold.msg.setText(Listitems.get(position).message);
            }
        hold.date.setText("Posted on " + Listitems.get(position).date);

       /* if(Listitems.get(position).image.toString().equalsIgnoreCase("http://amigotechnologies.in/homepage/greenestnull")) {
        }else{*/
            hold.img.setImageResource(R.drawable.bell1);
            hold.img.setTag(Listitems.get(position).image);
            imgtag = Listitems.get(position).image.toString();

          //  new ImgDownload(hold.img,position).execute(hold.img);
            //



        dpTag = Listitems.get(position).dp.toString();
        hold.dp.setImageResource(R.drawable.placeholder);
        hold.dp.setTag(Listitems.get(position).dp);
       /* final String imageKey = String.valueOf(position);
        Log.i("key","imageKey "+imageKey);

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {

            hold.img.setImageBitmap(bitmap);

        }else {*/
                    if (imgtag.toString().equalsIgnoreCase("http://amigotechnologies.in/homepage/greenestnull")) {

                        hold.img.setLayoutParams(lp);
                        hold.img.setVisibility(View.INVISIBLE);
                        //addBitmapToMemoryCache(String.valueOf(position), null);

                    } else {

                        hold.img.setLayoutParams(lp2);

                        hold.img.setVisibility(View.VISIBLE);
                        //new ImgDownload(hold.img, position).execute(hold.img);
                        // new DpDownload(hold.dp,position).execute(hold.dp);
                        picDownload(activity,hold.img.getTag().toString(),hold.img);
                    }
       // }

        File found2 = new File(Environment.getExternalStorageDirectory()+"/GreenestTest/Pictures/"+ hold.dp.getTag().toString().substring(58).trim());

        if (found2.exists()) {
            try {
                Log.i("file","inside file 2"+hold.dp.getTag().toString().substring(58).trim());
                hold.dp.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(found2)));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        else{
            dpd=new DpDownload(hold.dp,position).execute(hold.dp);



            //createDirectoryAndSaveFile(hold.dp.getDrawingCache(), s.substring(58));


        }




      /*  TextView serial = (TextView) convertView.findViewById(R.id.serial);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        serial.setText(String.valueOf(movieList.get(position).id));
        title.setText(movieList.get(position).title);*/

//////////////////////////////////////////////////////////////
// /*
        /*String Sname=hold.name.getText().toString();
        String Sdate=hold.date.getText().toString();
        String Smsg=hold.msg.getText().toString();*/



       /* hold.btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bundle b = new Bundle();
                    b.putString("name",h.name.getText().toString() );
                    b.putString("date", h.date.getText().toString().trim());
                    b.putString("msg",h.msg.getText().toString().trim());
                    b.putString("img", Listitems.get(position).image);
                    b.putString("dp", Listitems.get(position).dp);


                    b.putString("dp", dpTag);
                    Intent i = new Intent(activity, Solutions.class);
                    i.putExtras(b);
                    activity.startActivity(i);

                }
            });*/
        hold.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog nagDialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.preview);
                ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.previewImg);
                ivPreview.setBackgroundDrawable(h.img.getDrawable());


                nagDialog.show();
            }
        });
            hold.like.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

            int f=dbhelp.checkLike(h.name.getText().toString(),h.msg.getText().toString());
                    Log.i("flo","value=="+f);
                   // LinearLayout rl = (LinearLayout) v.getParent();
                    // Toast.makeText(context,msg.getText(),Toast.LENGTH_SHORT).show();
                   /////////////////////////// hold.like.setTextColor(Color.parseColor("#0066ff"));
                    String likesInfo[] = new String[4];
                    likesInfo[0] = "8";
                    likesInfo[1] = h.msg.getText().toString();
                    likesInfo[2] = h.name.getText().toString();
                    likesInfo[3] = "a12@a.com";

                    try {
                        StringBuffer buff = new Home().getTask(likesInfo);
                        if(f==1){
                            h.like.setTextColor(Color.parseColor("#6495ed"));
                        }
                        h.like.setText(buff.toString().trim() + " Likes");
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //////display count

                }


            });
          /*  hold.dislike.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String likesInfo[] = new String[4];
                    likesInfo[0] = "88";
                    likesInfo[1] = h.msg.getText().toString();
                    likesInfo[2] = h.name.getText().toString();
                    likesInfo[3] = fprint;


                    try {
                        StringBuffer buff = new Home().getTask(likesInfo);
                        h.dislike.setText(buff.toString().trim() + " Dislikes");
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


            });*/


            return row;
        }

public void picDownload(Context context,String url, final ImageView img){
final String name=img.getTag().toString().substring(58).trim();
    File found = new File(Environment.getExternalStorageDirectory()+"/GreenestTest/Pictures/", img.getTag().toString().substring(58).trim());
    if (found.exists()) {
        try {

            Bitmap bitm = BitmapFactory.decodeStream(new FileInputStream(found));
            img.setLayoutParams(lp2);
            img.setVisibility(View.VISIBLE);
            img.setImageBitmap(bitm);
            // img.setImageBitmap(bitm);
        } catch (Exception e) {

        }
    }else {
        img.setVisibility(View.VISIBLE);
img.setLayoutParams(lp2);

        Picasso
                    .with(context)
                    .load(url)
                    .tag(context)
                    .into(new Target() {

                        @Override
                        public void onPrepareLoad(Drawable arg0) {
                            // TODO Auto-generated method stub
Log.i("pics","entered picasso fn");
                        }

                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
                            img.setImageBitmap(bitmap);
                            Log.i("pics","entered");
                            createDirectoryAndSaveFile(bitmap,name);
                            /*File file = new File(Environment.getExternalStorageDirectory()+"/GreenestTest/Pictures/" + name);
                            try
                            {
                                file.createNewFile();
                                FileOutputStream ostream = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, ostream);
                                ostream.close();
                            }
                            catch (Exception e)
                            {
                                Log.e("green",e.getMessage()+"");

                            }*/
                        }

                        @Override
                        public void onBitmapFailed(Drawable arg0) {
                            // TODO Auto-generated method stub

                        }
                    });


        }


}

    void setImage(Bitmap bitm,ImageView img){
        if (bitm == null) {
            img.setLayoutParams(lp);
        } else {
            img.setLayoutParams(lp2);
            img.setImageBitmap(bitm);

        }
        //img.setImageBitmap(bitmap);

        //  if (v.posi == vPosition) {

            //addBitmapToMemoryCache(String.valueOf(vPosition), bitmap);


    }

    private class ImgDownload extends AsyncTask<ImageView, String, Bitmap> {
        Bitmap bitm;

        ImageView img;
        String args;
        private ViewHolder v;
        int vPosition;
        public ImgDownload(ImageView img, int pos) {
            this.img = img;
            args=img.getTag().toString();
            vPosition=pos;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap == null) {
                img.setLayoutParams(lp);
            } else {
                img.setLayoutParams(lp2);

            }
            //img.setImageBitmap(bitmap);

        //  if (v.posi == vPosition) {
            if (bitmap == null) {

            }else {
                //addBitmapToMemoryCache(String.valueOf(vPosition), bitmap);

                img.setImageBitmap(bitmap);
            }
         //  }
        }

        @Override
        protected Bitmap doInBackground(ImageView... params) {
            this.img=params[0];
            //////////
                if (args.equalsIgnoreCase("http://amigotechnologies.in/homepage/greenestnull")) {

                bitm=null;

                } else {
                    ////check whether its in the folder///////////////////////////////////
                    File found = new File(Environment.getExternalStorageDirectory()+"/GreenestTest/Pictures/", args.substring(58).toString().trim());

                    if (found.exists()) {
                        try {

                           bitm = BitmapFactory.decodeStream(new FileInputStream(found));
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }


                    } else {

                        try {
                            bitm = BitmapFactory.decodeStream((InputStream) new URL(args).getContent());

                            createDirectoryAndSaveFile(bitm, args.substring(58));


                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }


                    }
                }


                ////////

            return bitm;
        }



        private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {


            File file = new File(Environment.getExternalStorageDirectory()+"/GreenestTest/Pictures/", fileName);

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
    private class DpDownload extends AsyncTask<ImageView, String, Bitmap> {
        Bitmap bitm;

        ImageView img;
        String args;
        private ViewHolder v;
        int vPosition;
        public DpDownload(ImageView img, int pos) {
            this.img = img;
            args=img.getTag().toString();
            vPosition=pos;

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img.setImageBitmap(bitmap);

            //  }
        }

        @Override
        protected Bitmap doInBackground(ImageView... params) {
            this.img=params[0];
            //////////
            if (args.equalsIgnoreCase("http://amigotechnologies.in/homepage/greenestnull")) {

                bitm=null;

            } else {
                ////check whether its in the folder///////////////////////////////////
                File found = new File(Environment.getExternalStorageDirectory()+"/GreenestTest/Pictures/", args.substring(58).toString().trim());

                if (found.exists()) {
                    try {
Log.i("file",vPosition+"=="+args.substring(58).toString());
                        bitm = BitmapFactory.decodeStream(new FileInputStream(found));
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }


                } else {

                    try {
                        bitm = BitmapFactory.decodeStream((InputStream) new URL(args).getContent());

                        createDirectoryAndSaveFile(bitm, args.substring(58));


                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }


                }
            }


            ////////

            return bitm;
        }



        private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {


            File file = new File(new File(Environment.getExternalStorageDirectory()+"/GreenestTest/Pictures/"), fileName);

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
    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {


        File file = new File(new File(Environment.getExternalStorageDirectory()+"/GreenestTest/Pictures/"), fileName);

        try {

            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    private static class ViewHolder {

        public TextView name;
        public TextView msg;
        public ImageView img;
        public ImageView dp;
        public TextView date;
        public TextView like;
        public TextView status;
public int posi;

        public Button dislike;




    }

}



