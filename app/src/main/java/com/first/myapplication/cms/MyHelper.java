package com.first.myapplication.cms;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyHelper extends SQLiteOpenHelper {

    private static final String dbname = "mydb";
    private static final int version = 1;

    public MyHelper(Context context){
        super(context,dbname,null,version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
          String sql = "CREATE TABLE MEETINGS(_id INTEGER PRIMARY KEY AUTOINCREMENT, DATE TEXT, START REAL, ENDTIME REAL, DESCRIPTION TEXT)";
          db.execSQL(sql);


    }


//    private void insertData(String date, double start, double end, String desc,SQLiteDatabase database){
//        ContentValues values = new ContentValues();
//        values.put("DATE", date);
//        values.put("START", start);
//        values.put("ENDTIME", end);
//        values.put("DESCRIPTION", desc);
//
//        database.insert("MEETINGS", null, values);
//
//    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
