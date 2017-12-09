package greenest.greenest;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class m1 extends ActionBarActivity implements View.OnClickListener{
Button btnSub;
    EditText edName,edMob,edPass,edEmail,edConf;
TextView warn;
    TextView fileName;

    StringBuffer buffer;
    HttpResponse response;

    byte[] data;

    HttpClient httpclient;
    String proPic;
    InputStream inputStream;
    ImageButton upload;
    long img_size;
    Bitmap bitmap;
    String imgPath, filename;

    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m1);
        warn=(TextView)findViewById(R.id.warn);
        btnSub=(Button)findViewById(R.id.btnSubmit);
        edName=(EditText)findViewById(R.id.name);
        edEmail=(EditText)findViewById(R.id.email);
        edPass=(EditText)findViewById(R.id.password);
        fileName=(TextView)findViewById(R.id.imageName);
        upload=(ImageButton)findViewById(R.id.upload_img);
        edMob=(EditText)findViewById(R.id.mob);
        edConf=(EditText)findViewById(R.id.Confpassword);
        btnSub.setOnClickListener(this);
    }
    public void getGallery(){
        int RESULT_LOAD_IMG=1;
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Intent intent = new Intent();
        // call android default gallery
        //intent.setType("image/*");

        Log.i("green", "get pick");

        // startActivityForResult(Intent.createChooser(getCropIntent(intent), "Complete action using"), 1);
        startActivityForResult(intent, RESULT_LOAD_IMG);
        Log.i("green", "after startAct");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Bundle extras = data.getExtras();


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

//////////////////////////////
            Log.i("green", "inside if");

            filePath = data.getData();
            //callCrop(filePath);
            //bitmap=getBitmapFromData(data);

            String fp = filePath.toString();
            Cursor c = getContentResolver().query(Uri.parse(fp), null, null, null, null);
            c.moveToNext();
            String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
            int sizeIndex = c.getColumnIndex(OpenableColumns.SIZE);


            img_size = (long) ((c.getLong(sizeIndex) / 1024.0));
            final String fileNameSegments[] = path.split("/");
            filename = fileNameSegments[fileNameSegments.length - 1];//-------------------------filename
            Log.i("green", "---size" + fileName + "");
            fileName.setText(filename);
            c.close();
            if (img_size > 2500) {
                AlertDialog.Builder obj = new AlertDialog.Builder(m1.this);
                obj.setTitle("Selected Image");

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
                obj.create();
                obj.show();
            }

            try {

                Log.i("green", "reached try be4 bitmap");
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                 proPic=getStringImage(bitmap);
                // imageView.setImageBitmap(bitmap);*/
                Log.i("green", "---size" + img_size + "");







                } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



                //} catch (IOException e) {
                //  e.printStackTrace();
                // }


        }
    }
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();


        if (img_size>600) {
            bmp.compress(Bitmap.CompressFormat.JPEG, 12, baos);
        }
        else{
            bmp.compress(Bitmap.CompressFormat.JPEG, 15, baos);

        }
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.i("green", "enocoded img----" + encodedImage);
        return encodedImage;//----------------------------------------------------------------image string
    }
    public void upload(View v){

        showGallery();



    }
    @Override
    public void onClick(View v) {
            // check for validation
       if(edPass.getText().toString().equals(edConf.getText().toString()) && fileName.getText().toString().equalsIgnoreCase(filename))
       {
            warn.setText("");
           //################################################################################## encryption
            String b[]=new String[7];
           b[0]="0";
           b[1]=edName.getText().toString();
           b[2]=edEmail.getText().toString();
           b[3]=edMob.getText().toString();
           b[4]=edPass.getText().toString();
           b[5]=proPic;
           b[6]=filename;
           try {

               getTask(b);
               Log.i("green","be4 if buff"+buffer.toString());
               if (buffer.toString().trim().equalsIgnoreCase("1")){///////////////////////////////////////////////////return encrypted response via SP
                   Toast.makeText(getApplicationContext(),"Account created", Toast.LENGTH_LONG).show();
                    createDirectoryAndSaveFile(bitmap,"IMG_profile");
                   Intent i=new Intent(m1.this,M2.class);
                   Log.i("green","intent");

                   startActivity(i);

               }
           } catch (ExecutionException e) {
               e.printStackTrace();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
        else {
            edConf.setText("");
           edPass.setText("");
           warn.setText("Passwords mismatch");

       }






    }



    private class Asyn extends AsyncTask<String,Void,Void> {


        List<NameValuePair> nameValuePairs;

        @Override
        protected Void doInBackground(String... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://amigotechnologies.in/homepage/greenest/insertGreenest.php");
                // Add your data
                nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("condition", params[0].toString()));

                nameValuePairs.add(new BasicNameValuePair("name", params[1].toString()));
                nameValuePairs.add(new BasicNameValuePair("email", params[2].toString()));
                nameValuePairs.add(new BasicNameValuePair("mob", params[3].toString()));
                nameValuePairs.add(new BasicNameValuePair("pwd", params[4].toString()));
                nameValuePairs.add(new BasicNameValuePair("dp", params[5].toString()));
                nameValuePairs.add(new BasicNameValuePair("filename", params[6].toString()));

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
                Log.i("green",buffer.toString());







            }

            catch (Exception e)
            {
                // Toast.makeText(getApplicationContext(), "error in http" + e.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_LONG).show();
            //////////////////////response-----
            if(buffer.toString().equalsIgnoreCase("Valid")) {
                Toast.makeText(getApplicationContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Sorry, couldn't create account. Try again", Toast.LENGTH_SHORT).show();

            }




        }
    }


    public void getTask(String a[]) throws ExecutionException, InterruptedException {
        //encryption
        //encryption
        new Asyn().execute(a).get();



    }
    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {


        File file = new File(new File("/sdcard/GreenestTest/Profile"), fileName);
        if(!(file.exists())){
            file.mkdir();

        }
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

    public void showGallery(){
        getGallery();


                       /* Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/
    }

}
