package greenest.greenest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 3/15/2016.
 */
public class ReplyA extends BaseAdapter{

    private Activity activity;
        String imgtag;
        String dpTag;
        private LayoutInflater inflater;
        Bitmap bitmap[];
        private List<Reply> Listitems;
        List<Integer>  pos_list = new ArrayList<>();


        int addr[];
        int flag;
        public ReplyA(Activity activity, List<Reply> listitems) {
            this.activity = activity;
            this.Listitems = listitems;
            // addr=new int[Listitems.size()];
            flag=0;

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
        public View getView(int position, View row, ViewGroup parent) {
            Log.i("green", "inside get view");


            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.qn_list_row, null);

            final TextView name=(TextView)row.findViewById(R.id.name);
            final TextView msg=(TextView)row.findViewById(R.id.msg);
            ImageView dp=(ImageView)row.findViewById(R.id.profile_pic);

            Log.i("green", "be4 picasssooooooo");
            if (flag==0){
                flag=Listitems.size();
                Log.i("green", "#########################list size"+ flag+"");

            }
            name.setText(Listitems.get(position).name);
            Log.i("green", "name-----------------" + name.getText().toString() + "position----------" + Listitems.get(position) + "");
            msg.setText(Listitems.get(position).message);
            Log.i("green", "msg-----------------" + msg.getText().toString() + "position----------" + Listitems.get(position) + "");


            dpTag=Listitems.get(position).dp.toString();
            dp.setImageResource(R.drawable.placeholder);
            dp.setTag(Listitems.get(position).dp);
            Log.i("green", "Img async task called ");



            Log.i("green", "dp Img async task called ");

            new ImgDownload().execute(dp);

            Log.i("green", "Img async task called ");


      /*  Picasso.with(activity.getApplicationContext())
                .load(Listitems.get(position).image)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(img);*/
      /*  TextView serial = (TextView) convertView.findViewById(R.id.serial);
        TextView title = (TextView) convertView.findViewById(R.id.title);

        serial.setText(String.valueOf(movieList.get(position).id));
        title.setText(movieList.get(position).title);*/

//////////////////////////////////////////////////////////////
// /*
            Log.i("green", "position....array : " + pos_list + "");






            //addr[i]=position;
            ////////////////////////////////////////////////////////////

            // dp.setImageBitmap(Listitems.get(position).dp);



            Log.i("green", "POSITION---------------///********::: "+position+"");

            // Log.i("green", "value of------ i"+addr[i]+"");



            return row;
        }




        private class ImgDownload extends AsyncTask<ImageView, String, Bitmap> {
            Bitmap bitm;
            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "GreenestTest");
            String path = folder.getAbsolutePath();
            ImageView img;

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);

            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                img.setImageBitmap(bitmap);
            }

            @Override
            protected Bitmap doInBackground(ImageView... params) {
                this.img=params[0];
                String args=img.getTag().toString();
                //////////
                if (args.equalsIgnoreCase("http://amigotechnologies.in/homepage/greenestnull")) {
                    Log.d("green", "null");


                } else {
                    ////check whether its in the folder///////////////////////////////////
                    File found = new File("/sdcard/GreenestTest/", args.substring(58).toString().trim());

                    if (found.exists()) {
                        try {
                            Log.d("green", "file exists------------------------/*/**//*/**/*/*");

                            bitm = BitmapFactory.decodeStream(new FileInputStream(found));
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }


                    } else {
                        Log.d("green", "not exist");

                        try {
                            bitm = BitmapFactory.decodeStream((InputStream) new URL(args).getContent());
                            Log.d("green", "image downloaded");

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


    }



