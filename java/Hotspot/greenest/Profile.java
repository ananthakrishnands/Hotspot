package greenest.greenest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 2/3/2016.
 */
public class Profile extends ActionBarActivity {

    SharedPreferences prefs;
    long img_size;
    private int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    String encodedString;
    String imgPath, fileName;
    Button post;
    Bitmap bitmap;
    ImageButton gallery;
    EditText edPost;
    String cond[]={"2"};
    ListView listUp;// array of objects

    String msg[];
    String image[];
    Bitmap dp_img[];
    String dp[];
    Bitmap img[];
    Bitmap bitmaps[];
    int count;
    String cont;

    /////
ListView list;
TextView name;
    ImageButton statusEdit;
    ImageView pic;
    String b;
    ArrayList<SingleRow> listitems;
TextView tvstatus;
    ImageButton picEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        list = (ListView) findViewById(R.id.list);
        pic=(ImageView)findViewById(R.id.pro_pic);
        name=(TextView)findViewById(R.id.proName);
        statusEdit=(ImageButton)findViewById(R.id.statusEdit);
        tvstatus=(TextView)findViewById(R.id.status);
        picEdit=(ImageButton)findViewById(R.id.edit);
        prefs=getSharedPreferences("prefs", Context.MODE_PRIVATE);
        name.setText(prefs.getString("name", ""));
        tvstatus.setText(prefs.getString("status",""));

File f=new File("/sdcard/GreenestTest/Profile/IMG_profile");
        if (f.exists()){

            try {
                pic.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(f)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }

        picEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
getGallery();
            }
        });






        statusEdit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                AlertDialog.Builder obj = new AlertDialog.Builder(Profile.this);
                obj.setTitle("Update Status");
                final EditText input = new EditText(Profile.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                input.setHint("Enter new status");
                obj.setView(input);
                obj.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().trim().equalsIgnoreCase("")) {

                        } else {
                            prefs=getSharedPreferences("prefs",Context.MODE_PRIVATE);
                            SharedPreferences.Editor ed=prefs.edit();
                            String status[] = new String[5];
                            status[0] = "11";
                            String s = input.getText().toString().trim();
                            status[1] = "<status%%@>##" + s;
                            status[2] = "a12@a.com";//////////////////////////////////////////////////////////////////////
                            String getCal[] = getDate();
                            status[3] = getCal[0];
                            status[4] = getCal[1];
                            try {
                                StringBuffer b = new Home().getTask(status);
                                if (b.toString().trim().equalsIgnoreCase("1")) {

                                    ed.putString("status",s);
                                    ed.commit();
                                    tvstatus.setText(s);
                                    Toast.makeText(getApplicationContext(), "Status updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error! Try again", Toast.LENGTH_SHORT).show();

                                }


                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                obj.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                obj.create();
                obj.show();

            }

        });
        String []cond={"5"};





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

    ////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Bundle extras = data.getExtras();


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

//////////////////////////////
            Log.i("green", "inside if");

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
                Log.i("green","reached try be4 bitmap");
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // imageView.setImageBitmap(bitmap);*/
                Log.i("green","---size"+img_size+"");
                AlertDialog.Builder obj = new AlertDialog.Builder(Profile.this);
                obj.setTitle("Set as Profile Image");

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

                    final ImageView input = new ImageView(Profile.this);

                    LayoutInflater lp = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = lp.inflate(R.layout.alert, null, false);
                    ImageView imgalert=(ImageView)view.findViewById(R.id.alertimg);
                    final EditText edalert=(EditText)view.findViewById(R.id.edAlertImg);
                    edalert.setVisibility(View.INVISIBLE);


                    imgalert.setImageBitmap(bitmap);

                    obj.setView(view);
                    obj.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            /*******************************************************send it 2 db----------------*/
                            /////////////////////////////////////////////////////////bitmap compression
                            Log.i("green", "on click Send image posting in Timeline class");

                            String []par=new String[6];
                            par[0]="222";
                            par[1]= getStringImage(bitmap);
                            par[2]=fileName.toString();

                            par[3]=prefs.getString("fprint", null);
                            par[4]=getDate()[1];
                            par[5]=getDate()[0];


                            Log.i("green", "initized string array in Timeline class");

                            try {
                                Log.i("green", "calling gettask for image posting in Timeline class");

                                 StringBuffer b=new Home().getTask(par);
                                Log.i("green", "finished gettask for image posting in Timeline class");

                                ////////////////// fetchUpdates();
                                if (b.toString().trim().equalsIgnoreCase("1")) {
                                    Log.i("green", "finished gettask for image posting in Timeline class2222222");
/////////////copy image in greenest folder
                                    Toast.makeText(Profile.this, "Posted successfully", Toast.LENGTH_SHORT).show();

                                    pic.setImageBitmap(bitmap);
                                    createDirectoryAndSaveFile(bitmap,"IMG_profile");
                                }
                                Log.i("green", "finished gettask for image posting in Timeline class333333");


                            }catch (Exception e){

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
        Log.i("green", "enocoded img----" + encodedImage);
        return encodedImage;//----------------------------------------------------------------image string
    }
    ///////


    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {


        File file = new File(new File("/sdcard/GreenestTest/Profile"), fileName);

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
    public void setName(String a){


    }


}
