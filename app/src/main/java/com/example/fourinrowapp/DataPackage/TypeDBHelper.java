package com.example.fourinrowapp.DataPackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class TypeDBHelper extends SQLiteOpenHelper {

    private static final String FourInRow_TABLE_NAME = "FourInRow";
    private static final String FourInRow_ID = "ID";
    private static final String FourInRow_TYPE = "TYPE";
    private Context context;

    public TypeDBHelper(Context context) {
        super(context, FourInRow_TABLE_NAME, null, 1);

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + FourInRow_TABLE_NAME + "(" +
                FourInRow_ID + " INTEGER PRIMARY KEY, " +
                FourInRow_TYPE + " TEXT " + ")";
        try {
            db.execSQL(CREATE_TABLE);
        } catch (SQLiteException ex) {
            Log.e("SQLiteException", ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FourInRow_TABLE_NAME);
        onCreate(db);
    }

    public void addType(String type) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FourInRow_TYPE, type);

        try {
            db.insertOrThrow(FourInRow_TABLE_NAME, null, contentValues);
        } catch (SQLiteException ex) {
            Log.e("MovieDBHelper", ex.getMessage());
        } finally {
            db.close();
        }
    }

    public ArrayList<String> getAllTypes() {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(FourInRow_TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int colID = cursor.getColumnIndex(FourInRow_ID);
            int id = cursor.getInt(colID);
            String type = cursor.getString(1);
            arrayList.add(type);
        }
        cursor.close();
        return arrayList;
    }

}
