package greenest.greenest;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 1/25/2016.
 */



public class ImgDown{
    Bitmap[] bitmap;
    String[] image;
    ProgressDialog pDialog;

    private class ImgDownload extends AsyncTask<String,String,Bitmap[]>{



        //private final Context Asyntaskcontext;

        /* public ImgDown(Context context,AsyncResponse asyncResponse){

             Asyntaskcontext = context;
             delegate = asyncResponse;//Assigning call back interfacethrough constructor

         }*/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();*/

        }
        protected Bitmap[] doInBackground(String... args) {
            Log.i("green","inside async 1 downloading image");
            try {
                for (int i=0;i<args.length;i++) {
                    if (args[i].equalsIgnoreCase("http://192.168.1.36:80/0")){
                        bitmap[i]=null;
                    }
                    else {
                        bitmap[i] = BitmapFactory.decodeStream((InputStream) new URL(args[i]).getContent());
                    }
                }
                Log.i("green","ending async 1 downloading image");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image[]) {
            Log.i("green", "onPost");
            if(image != null){
                //img.setImageBitmap(image);
                /*ImageView im=new ImageView(Asyntaskcontext);
                im.setImageBitmap(image);
                LL.addView(im);
                pDialog.dismiss(); */
                //delegate.processFinish(image);

            }else{

             /*   pDialog.dismiss();
                Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();*/

            }
        }





    }


    public Bitmap[] getTask(String img[]) throws ExecutionException, InterruptedException {

        new ImgDownload().execute(img).get();
        return bitmap;


    }




}




