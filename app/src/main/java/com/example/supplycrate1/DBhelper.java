package com.example.supplycrate1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

class DBhelper extends SQLiteOpenHelper {

    public DBhelper(@Nullable Context context) {
        super(context, "MyDB.db", null, 1);
    }

    public static final String DBNAME = "MyDB.db";




    public void onCreate(SQLiteDatabase MyDB) {
       MyDB.execSQL("create Table users(username TEXT primary key, password TEXT, mail TEXT, phoneno INT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
       MyDB.execSQL("drop Table if exists users");
    }

    public boolean insertData(String username,String password, String mail, Integer phoneno){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("mail", mail);
        contentValues.put("phoneno", phoneno);
        long result = MyDB.insert("users",null,contentValues);
         if(result==-1) return false;
         else
             return true;
    }

    public boolean checkusername(String username){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?",new String[]{username});
        if(cursor.getCount()>0)
            return true;
        else
            return false;

    }

    public boolean checkusernamepassword(String username, String password){

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username, password});
        if(cursor.getCount()>0)
                return true;
        else
            return false;

    }



}