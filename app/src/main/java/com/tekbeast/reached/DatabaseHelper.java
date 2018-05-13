package com.tekbeast.reached;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Virus on 04-03-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="reached.db";
    public static final String TABLE_NAME="location_table";
    public static final String COL_1="ID";
    public static final String COL_2="NUMBER";
    public static final String COL_3="PLACE";
    public static final String COL_4="LAT";
    public static final String COL_5="LON";
    public static final String COL_6="MSG";
    public static final String COL_7="STATUS";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NUMBER VARCHAR,PLACE VARCHAR,LAT DOUBLE,LON DOUBLE ,MSG VARCHAR,STATUS VARCHAR)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DATABASE_NAME);
        onCreate(sqLiteDatabase);

    }

    public boolean insertData(String number,String place,Double lat,Double lon ,String msg, String status){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,number);
        contentValues.put(COL_3,place);
        contentValues.put(COL_4,lat);
        contentValues.put(COL_5,lon);
        contentValues.put(COL_6,msg);
        contentValues.put(COL_7,status);
        Long result = db.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }

    public Cursor getAllData(){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;

//        String[] projection={MapContract.MapEntry.ID,MapContract.MapEntry.NUMBER,MapContract.MapEntry.PLACE, MapContract.MapEntry.MSG, MapContract.MapEntry.STATUS};
//        Cursor cursor=db.query(MapContract.MapEntry.TABLE_NAME,projection,null,null,null,null,null);
//        return cursor;

    }
public Cursor getData(int id){
    SQLiteDatabase database =this.getWritableDatabase();
    String query = "SELECT * FROM "+TABLE_NAME+" WHERE id=" + id;
    Cursor  cursor = database.rawQuery(query,null);
    if (cursor != null) {
        cursor.moveToFirst();
    }
    return cursor;
}
    public Cursor get(){

        SQLiteDatabase database =this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+ COL_7+ "='active'";
        Cursor  cursor = database.rawQuery(query,null);
        if (cursor != null) {
            //cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor getin(){

        SQLiteDatabase database =this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+ COL_7+ "='inactive'";
        Cursor  cursor = database.rawQuery(query,null);
        if (cursor != null) {
            //cursor.moveToFirst();
        }
        return cursor;
    }


    public boolean updateData(String id,String number,String place,Double lat,Double lon, String msg){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,number);
        contentValues.put(COL_3,place);
        contentValues.put(COL_4,lat);
        contentValues.put(COL_5,lon);
        contentValues.put(COL_6,msg);
        String status="active";
        contentValues.put(COL_7,status);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{id});
        return  true;

    }

    public boolean upData(String id){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String status="inactive";
        contentValues.put(COL_7,status);
        db.update(TABLE_NAME,contentValues,"id=?",new String[]{id});
        return  true;

    }

    public Integer deleteData (String id){
        SQLiteDatabase db =this.getWritableDatabase();
       return db.delete(TABLE_NAME,"id=?",new String[]{id});

    }

    public Cursor deleteallData (){
        SQLiteDatabase database =this.getWritableDatabase();
        String query = "DELETE FROM "+TABLE_NAME+" WHERE "+ COL_7+ "='inactive'";
        Cursor  cursor = database.rawQuery(query,null);

        return cursor;

    }
}
