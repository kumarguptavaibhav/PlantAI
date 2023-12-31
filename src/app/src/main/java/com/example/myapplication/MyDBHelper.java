package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="infoDB";
    private static final int DATABASE_ID=3;
    private static final String TABLE_CONTACT="contacts";
    private static final String KEY_NAME="name";
    private static final String KEY_EMAIL="email";
    private static final String KEY_PHONE_NO="phone_no";
    private static final String KEY_PASSWORD="password";

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_ID);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CONTACT + "(" + KEY_NAME + " TEXT, " + KEY_EMAIL + " TEXT PRIMARY KEY, " + KEY_PHONE_NO + " TEXT, " + KEY_PASSWORD + " TEXT " + ")");
        //do not close database in oncreate method
        //SQLiteDatabase database = this.getWritableDatabase();
        //database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT);
        onCreate(db);
    }

    public void addInfo(String name, String email, String phone_no, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PHONE_NO, phone_no);
        values.put(KEY_PASSWORD, password);

        db.insert(TABLE_CONTACT, null, values);
        db.close();
    }

    public boolean checkInfo(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Boolean check = false;

        Log.d("uday", "beforeQuery()");
        //Checking for Correct username and password
        Cursor cursor = db.query(TABLE_CONTACT, new String[] { "*" }, KEY_EMAIL + "=?",
                new String[] { email } , null, null, null, null);


        Log.d("uday", "afterQuery()");

        if (cursor != null && cursor.moveToFirst()) {
            String emailDb = cursor.getString(cursor.getColumnIndex(KEY_EMAIL) >=0 ? cursor.getColumnIndex(KEY_EMAIL) : 0 );
            String passDb = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD) > 0 ? cursor.getColumnIndex(KEY_PASSWORD) : 1);

            check = (email.equals(emailDb) && password.equals(passDb));
        }
        cursor.close();

        return check;
    }
}
