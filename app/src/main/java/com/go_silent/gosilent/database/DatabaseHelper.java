package com.go_silent.gosilent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pial on 8/25/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "GoSilent.db";
    public static final int VERSION = 1;
    public static final String CREATE_PLACES_TABLE_QUERY = "CREATE TABLE " + DatabaseContract.PlaceEntry.TABLE_NAME + " (" +
            DatabaseContract.PlaceEntry._ID + " PRIMARY KEY AUTOINCREMENT," +
            DatabaseContract.PlaceEntry.COLUMN_LAT + " TEXT," +
            DatabaseContract.PlaceEntry.COLUMN_LONG + " TEXT," +
            DatabaseContract.PlaceEntry.COLUMN_ADDRESS + " TEXT," +
            DatabaseContract.PlaceEntry.COLUMN_GEOFENCE_ID + " INTEGER)";
    public static final String DELETE_PLACES_TABLE_QUERY = "DROP TABLE IF EXITS " + DatabaseContract.PlaceEntry.TABLE_NAME;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PLACES_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DELETE_PLACES_TABLE_QUERY);
        onCreate(sqLiteDatabase);
    }
}
