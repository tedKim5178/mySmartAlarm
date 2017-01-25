package com.example.mk.myalarmmanagertest.Data;

/**
 * Created by mk on 2017-01-23.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mk on 2017-01-23.
 */

public class AlarmDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "alarmlist.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;
    //Constructor
    public AlarmDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold waitlist data
        final String SQL_CREATE_ALARMLIST_TABLE = "CREATE TABLE " + AlarmContract.AlarmlistEntry.TABLE_NAME + " (" +
                AlarmContract.AlarmlistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                AlarmContract.AlarmlistEntry.COLUMN_HOUR + " INTEGER NOT NULL, " +
                AlarmContract.AlarmlistEntry.COLUMN_MINUTE + " INTEGER NOT NULL, " +
                AlarmContract.AlarmlistEntry.COLUMN_MEMO + " TEXT, " +
                AlarmContract.AlarmlistEntry.COLUMN_REPEAT + " TEXT" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_ALARMLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AlarmContract.AlarmlistEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
