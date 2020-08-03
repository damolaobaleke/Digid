package com.eazifunds.efid;
/**Written by Oyindamola Obaleke
 * 1/14/2020
 * All rights reserved Softroniiks inc*/
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.logging.LogRecord;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Admin.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, 1);
        SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Admin(id INTEGER PRIMARY KEY, username VARCHAR ,password VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DELETE FROM Admin");
        onCreate(db);
    }

    public boolean signUp(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        long result = db.insert("Admin", null, cv);
        if (result == -1) {
            Log.e("Database", "Didn't store");
            return false;
        } else {
            Log.i("Database", "Stored");
            return true;
        }

    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Admin",null);
        return cursor;
    }

    public Boolean auth(String username, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Admin where username = ? AND password = ?",new String[]{username,password});
        //checks for username and password in the cursor, if its > 0
        if(cursor.getCount() > 0){
            //Wont work
            Log.i("Auth", "already in database");
            return false;
        }else{
            return true;
        }
    }

    public Integer DeleteData(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Admin", "username = ?",new String[]{username});
    }

}
