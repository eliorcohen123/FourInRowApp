package com.example.fourinrowapp.DataPackage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

// SQLiteOpenHelper - a helper class to manage database creation
public class TypeDBHelper extends SQLiteOpenHelper {

    private static final String FourInRow_TABLE_NAME = "FourInRow"; // Set the TABLE name
    private static final String FourInRow_ID = "ID"; // Set the ID of the TABLE
    private static final String FourInRow_TYPE = "TYPE"; // Set the String that will contain data below

    public TypeDBHelper(Context context) {
        super(context, FourInRow_TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // onCreate - Called when SQLiteDBOpenHelper is first created
        // Create the String that contains the query to create SQL
        String CREATE_TABLE = "CREATE TABLE " + FourInRow_TABLE_NAME + "(" +
                FourInRow_ID + " INTEGER PRIMARY KEY, " +
                FourInRow_TYPE + " TEXT " + ")";
        try { // Try to perform task
            db.execSQL(CREATE_TABLE); // Create the TABLE
        } catch (SQLiteException ex) { // Catch Exception if "try" failed
            Log.e("SQLiteException", ex.getMessage()); // Show the error in "LogCat"
        }
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FourInRow_TABLE_NAME);
        onCreate(db);
    }

    // Add data to the SQLiteDBOpenHelper
    public void addType(String type) {
        SQLiteDatabase db = getWritableDatabase(); // Write data into the SQLiteDBOpenHelper
        ContentValues contentValues = new ContentValues(); // Initialize ContentValues - contains the data

        contentValues.put(FourInRow_TYPE, type); // Put data in ContentValues

        try { // Try to perform task
            db.insertOrThrow(FourInRow_TABLE_NAME, null, contentValues); // Add all the data that ContentValues contains to the TABLE
        } catch (SQLiteException ex) { // Catch Exception if "try" failed
            Log.e("MovieDBHelper", ex.getMessage()); // Show the error in "LogCat"
        } finally { // After "try"/"catch" finish
            db.close(); // Close SQLiteDatabase
        }
    }

    // Get all the data in the TABLE
    public ArrayList<String> getAllTypes() {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase(); // Read data from the SQLiteDBOpenHelper
        // Query the given table, returning a cursor over the result set
        Cursor cursor = db.query(FourInRow_TABLE_NAME, null, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String type = cursor.getString(1);
            arrayList.add(type);
        }
        cursor.close(); // Close Cursor
        return arrayList;
    }

}
