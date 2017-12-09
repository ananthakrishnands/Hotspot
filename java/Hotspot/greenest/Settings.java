package greenest.greenest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

/**
 * Created by admin on 2/4/2016.
 */
public class Settings extends ActionBarActivity {
    String []contents={"Feedback","Help","About"};

    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);
        list=(ListView)findViewById(R.id.list);
        ArrayAdapter<String> arrayList=new ArrayAdapter<String>(Settings.this,android.R.layout.simple_list_item_1,contents);
        list.setAdapter(arrayList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){

                    case 0://Feedback
                        AlertDialog.Builder obj=new AlertDialog.Builder(Settings.this);
                        obj.setTitle("Feedback");
                        //obj.setIcon(R.drawable.ic_launcher);
                        final EditText input = new EditText(Settings.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);

                        input.setHint("Enter your feedback");
                        obj.setView(input);
                        //obj.setMessage("Dialog Created");
                        obj.setPositiveButton("Send", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                /*******************************************************send it 2 db----------------*/
                                String fd=input.getText().toString();
                                String a[]={"16",fd,"1"};
                                try {
                                    String resp=putInto(a);
                                    if (resp.trim().equalsIgnoreCase("s")){
                                        Toast.makeText(getApplicationContext(),"Feedback sent",Toast.LENGTH_SHORT).show();
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
                        obj.create();
                        obj.show();

                            break;
                    case 1://Help
                        AlertDialog.Builder obj2=new AlertDialog.Builder(Settings.this);
                        obj2.setTitle("Help");
                        //obj.setIcon(R.drawable.ic_launcher);

                        obj2.setMessage("Greenest is a spider networking platform, for disclosing your thoughts and steps that can save our mother nature, that will rise up the environmental awareness to the next levels");
                        obj2.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                /*******************************************************send it 2 db----------------*/
                            }
                        });

                        obj2.create();
                        obj2.show();

                        break;
                    case 2:
                        Intent i=new Intent(Settings.this,About.class);
                        startActivity(i);
                        break;
                }
            }
        });


    }
    public String putInto(String a[]) throws ExecutionException, InterruptedException {
        StringBuffer buff=new Home().getTask(a);
        return buff.toString();
    }
}
