package greenest.greenest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by admin on 12/13/2015.
 */
public class M2 extends ActionBarActivity{
    Button login;
    StringBuffer k;
    EditText edEmail;
    EditText edPass;
    Button signup;
    String name;
String fp,home;
    SharedPreferences pre;

    TextView warn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l1);
        login=(Button)findViewById(R.id.login);
        signup=(Button)findViewById(R.id.sign);
        edPass=(EditText)findViewById(R.id.pass);
        warn=(TextView)findViewById(R.id.warn);
        pre=getSharedPreferences("prefs",Context.MODE_PRIVATE);
        home=pre.getString("ANDROID_HOME",null);
        edEmail=(EditText)findViewById(R.id.user);

        String a[]={"000"};
        try {
            k=new Home().getTask(a);
            key=k.toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    String key;

    private String encrypt(String data){

        String encryptionDecryptionKey ="1234567891234567";
        String ivs =  "\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0";

        byte[] encryptedData = encrypt(data.getBytes(), encryptionDecryptionKey.getBytes(),
                ivs.getBytes());
        String s= null;
        try {
            s = byteCon(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
return s;

    }
    public  String byteCon(byte []encryptedByte)
            throws Exception {

        String encryptedText = Base64.encodeToString(encryptedByte, Base64.DEFAULT);
        return encryptedText;
    }
    public static byte[] encrypt(byte[] data, byte[] key, byte[] ivs) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            byte[] finalIvs = new byte[16];
            int len = ivs.length > 16 ? 16 : ivs.length;
            System.arraycopy(ivs, 0, finalIvs, 0, len);
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivps);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public void json_parsing(String path) throws JSONException {
        Log.i("green", " entered json_parse");
        JSONArray jsonArray = new JSONArray(path);


        try {
            Log.i("green", " entered json_parse");

            /* parsing JSON and filling views */
            Log.i("green", "Number of surveys in feed: " + jsonArray.length() + ";;;" + path);


            // ---print out the content of the json feed---
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                /*dynamic text */
                name=jsonObject.getString("var_UserName");
                fp=jsonObject.getString("encr_key");

                Log.i("green", "+++++++++++++++++++++++++++++++++++++++++++" + name+"");


            }


        }catch (Exception e){
            e.getMessage();
        }
    }
            String email;
    String pwd;
    StringBuffer b;
    public void doLogin(View v) {

        pwd=edPass.getText().toString();
        String info[]=new String[4];
        info[0]="1";
        info[1]=encrypt(edEmail.getText().toString());
        info[2]=encrypt(edPass.getText().toString());
        try {
Log.i("green","encr value=== "+info[1]+"");
             b=new Home().getTask(info);
            if (b.toString().trim().equalsIgnoreCase("0")){
                Log.i("green", "wrong detaiiiiiiiiiiiils");
                warn.setText("Invalid details");
               // Toast.makeText(getApplicationContext(),"Invalid details",Toast.LENGTH_SHORT).show();

            }
            else{
                 json_parsing(b.toString());
                    Log.i("green", "corrrrecttttt detaiiiiiiiiiiiils%%%%%%%%%%%" + name);
                SharedPreferences prefs=getSharedPreferences("prefs", Context.MODE_PRIVATE);

                SharedPreferences.Editor ed=prefs.edit();
                ed.putInt("has",121);
                ed.putString("fprint",fp);
                ed.putString("name",name);
                ed.commit();


                Intent i = new Intent(M2.this, Splash.class);

                Log.i("green","corrrrecttttt detaiiiiiiiiiiiils"+fp);

                startActivity(i);
                    Log.i("green", "corrrrecttttt detaiiiiiiiiiiiils");


            }

        } catch (Exception e){

        }


    }
    public void doSignup(View v){
        Intent i=new Intent(this,m1.class);
        startActivity(i);

    }


}
