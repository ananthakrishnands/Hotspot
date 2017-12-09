package greenest.greenest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;


/**
 * Created by admin on 1/27/2016.
 */
public class Questions extends ActionBarActivity{
    Button btnAsk;
    ListView list;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        btnAsk=(Button)findViewById(R.id.btnPost);
        btnAsk.setText("Ask");
        Log.i("green", "in Questions.java");

        list = (ListView) findViewById(R.id.list);
        try {
            String a[]={"3"};

            list.setAdapter(new GetQuestions(this,a));

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.i("green", "@Timeline ex");
        }

    }
    public void jump(String qn_msg,String name){

        Bundle bundle=new Bundle();
        Log.i("green", qn_msg+" "+name);

        bundle.putString("msg", qn_msg);
        bundle.putString("name",name);
        Log.i("green", "inside  jump");
try {
     i = new Intent(Questions.this, Solutions.class);
    Log.i("green", "called intent");

    i.putExtras(bundle);
    Log.i("green", "put bundle");

    startActivity(i);
    Log.i("green", "startd acti");
}catch (Exception e){
    Log.i("green",e.getMessage());
}



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_m1, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return menuChoice(item);
    }



    private boolean menuChoice(MenuItem item){
        switch(item.getItemId()){
            case R.id.home:

                Intent intent = getIntent();
                finish();
                startActivity(getIntent());

                return true;
            case R.id.notification:
                Toast.makeText(this, "Clicked notification", Toast.LENGTH_LONG).show();
                return true;


            case R.id.Settings:
                i=new Intent(Questions.this,Settings.class);
                startActivity(i);

                return true;


        }
        return false;
    }

}

