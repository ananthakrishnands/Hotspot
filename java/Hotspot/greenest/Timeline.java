package greenest.greenest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 1/25/2016.
 */
public class Timeline extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener{
ListView list;
    private FeedListAdapter listAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<SingleRow> listItems=new ArrayList<SingleRow>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        list = (ListView) findViewById(R.id.list);
       // listUp = (ListView) findViewById(R.id.listUpdate);

        edPost=(EditText)findViewById(R.id.edPost);
        gallery=(ImageButton)findViewById(R.id.upload_img);
        post=(Button)findViewById(R.id.btnPost);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        try {
                                            listAdapter=new FeedListAdapter(Timeline.this, cond,listItems);
                                            list.setAdapter(listAdapter);
                                            Toast.makeText(getApplicationContext(),list.getCount()+"",Toast.LENGTH_SHORT).show();
                                            swipeRefreshLayout.setRefreshing(false);


                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                            Log.i("green", "@Timeline ex");
                                        }

                                    }
                                }
        );


    }
    public String[] getDate(){
        Calendar cal = Calendar.getInstance();

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);

        int month = cal.get(Calendar.MONTH)+1;
        String  calend[]=new String[2];
        long time=(cal.get(Calendar.HOUR)*3600)+(cal.get(Calendar.MINUTE)*60)+cal.get(Calendar.SECOND);
        calend[0]=time+"";
        Toast.makeText(this,calend[0],Toast.LENGTH_SHORT).show();
        calend[1]=day+"-"+month+"-"+year+"";

        return calend;

    }
    public void doPost(View v){
        if(edPost.getText().toString().trim().equalsIgnoreCase("")){
            Toast.makeText(this,"Post content is empty",Toast.LENGTH_SHORT).show();
        }else{
            Log.i("green","inside else ->post click");

            String toPost[]=new String[5];
            toPost[0]="11";
            toPost[1]=edPost.getText().toString().trim();
            toPost[2]="a12@a.com";
            String getCal[]=getDate();
            toPost[3]=getCal[0];
            toPost[4]=getCal[1];

            try {
                StringBuffer b=new Home().getTask(toPost);
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

   private Intent getCropIntent(Intent intent) {
       intent.putExtra("crop", "true");
       intent.putExtra("aspectX", 1);
       intent.putExtra("aspectY", 1);
       intent.putExtra("outputX", 320);
       intent.putExtra("outputY", 320);
       intent.putExtra("return-data", true);
       Log.i("green", "get crop intent");

       return intent;
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
                AlertDialog.Builder obj = new AlertDialog.Builder(Timeline.this);
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

                    //obj.setIcon(R.drawable.ic_launcher);
                    final ImageView input = new ImageView(Timeline.this);
                    final EditText edmsg_img=new EditText(Timeline.this);


                    LayoutInflater lp = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View view = lp.inflate(R.layout.alert, null, false);
                    final EditText edalert=(EditText)view.findViewById(R.id.edAlertImg);
                    ImageView imgalert=(ImageView)view.findViewById(R.id.alertimg);



                    imgalert.setImageBitmap(bitmap);

                    obj.setView(view);

                    //input.setLayoutParams(lp);

                   // input.setImageBitmap(bitmap);
                   //obj.setView(input);
                    //obj.setMessage("Dialog Created");
                    obj.setPositiveButton("Send", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // TODO Auto-generated method stub
                            /*******************************************************send it 2 db----------------*/
                      /////////////////////////////////////////////////////////bitmap compression
                            Log.i("green","on click Send image posting in Timeline class");

                            String []par=new String[5];
                            par[0]="7";
                            par[1]= getStringImage(bitmap);
                            par[2]=fileName.toString();
                            par[3]=edalert.getText().toString().trim();
                            par[4]="a12@a.com";

                            Log.i("green","initized string array in Timeline class");

                            try {
                                Log.i("green","calling gettask for image posting in Timeline class");

                                StringBuffer b=new Home().getTask(par);
                                Log.i("green","finished gettask for image posting in Timeline class");

                                if (b.toString().equalsIgnoreCase("1")){
                                    Toast.makeText(Timeline.this,"Posted successfully",Toast.LENGTH_SHORT).show();
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
        Log.i("green", "enocoded img----" + encodedImage);
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
////////////////////////////////////////////////////////updated query
              /*  String update[]= new String[2];
                update[0]="12";
                update[1]=(list.getCount())+"";
                Log.i("green","reached update string array");

                try{
                StringBuffer buff=new Home().getTask(update);
                    Log.i("green","buff....."+buff.toString().trim());

                    if(buff.toString().trim().equalsIgnoreCase("[]")){
                    Toast.makeText(getApplicationContext(),"Nothing",Toast.LENGTH_SHORT).show();
                        Log.i("green", "breaking");



                }
                else {
                        String []a={"000",buff.toString().trim()};
                    new FeedListAdapter(this,a);

                }
                }catch (Exception e){
                    Log.i("green","error in menu refresh---"+ e.getLocalizedMessage());
                }

               //////////////////////////////////////////////////////////////*/

                return true;
            case R.id.notification:
                Toast.makeText(this, "Clicked notification", Toast.LENGTH_LONG).show();
                return true;


            case R.id.Settings:
                i=new Intent(Timeline.this,Settings.class);
                startActivity(i);

                return true;

        }
        return false;
    }

    @Override
    public void onRefresh() {



fetchMovies();




    }
    public void fetchMovies(){
        swipeRefreshLayout.setRefreshing(true);
Log.i("green","inside fetch movie..................................");
        Bitmap dp_img[]=new Bitmap[1];
        String pro[]={"http://amigotechnologies.in/homepage/greenest/quiz_images/irfan_pathan.jpg"};
        try {
           dp_img=new Home().getTaskImg(pro);
            Log.i("green","after getTask");

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /////SingleRow temp = new SingleRow("Alan","Sample text123",dp_img[0],dp_img[0],"" );
        Log.i("green","inside 3");

      //  listItems.add(temp);
        Log.i("green", "4");


        listAdapter.notifyDataSetChanged();
        Log.i("green", "5");

        swipeRefreshLayout.setRefreshing(false);


    }
}
