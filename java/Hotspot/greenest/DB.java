package greenest.greenest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ananthakrishnan D.S. on 4/5/2016.
 */
public class DB extends SQLiteOpenHelper{
    static final String DATABASE_NAME = "Greenest";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_CREATE = "CREATE TABLE like(id INTEGER, name varchar(50), msg varchar(200));";
    static final String TABLE_NAME="like";

    Context context;

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(DATABASE_CREATE);
        } catch (Exception e) {
            // TODO: handle exception
            Log.i("db", e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
public int check(String name, String msg){
    Cursor c;
    String check_query="SELECT id from "+TABLE_NAME+ " where name='"+name+"' and msg='"+msg+"'";
    SQLiteDatabase db=this.getWritableDatabase();
    c=db.rawQuery(check_query,null);
    if(c==null){
        //entry exist
        Log.i("db","null cursor");
        return 0;
    }else{
        while (c.moveToNext()) {
            Log.i("db","cursor..data exists");

            int id=c.getInt(0);
            return id;

        }
        return 0;
        //add entry

        //addData(name,msg);
    }



}
    public void disp(){
        Cursor c;
        String check_query="SELECT * from "+TABLE_NAME;
        SQLiteDatabase db=this.getWritableDatabase();
        c=db.rawQuery(check_query,null);
        int i=0;
        do{
            Log.i("db",c.getString(i));
        }while (c.moveToNext());

    }
    public int checkLike(String name, String msg){
        Cursor c;
        String check_query="SELECT id from "+TABLE_NAME+ " where name='"+name+"' and msg='"+msg+"'";
        SQLiteDatabase db=this.getWritableDatabase();
        c=db.rawQuery(check_query,null);
        if(c.getCount()>0){
            //delete entry
            return 0;

        }else{
            addData(name,msg);

            return 1;

        }



    }


    public void addData(String name, String msg){
        Cursor c;
        String insert_query="INSERT INTO "+TABLE_NAME+ " VALUES(1,'"+name+"','"+msg+"')";
        Log.i("db", "name==="+name);

        SQLiteDatabase db=this.getWritableDatabase();
        Toast.makeText(context, "sql", Toast.LENGTH_SHORT).show();
        try {
            db.execSQL(insert_query);
        }catch (Exception e){
            Log.i("db", e.getMessage());

        }




    }






}
