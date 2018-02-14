package com.example.zimzik.newsqlitesorting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "countriesDb";
    public static final String TABLE_COUNTRIES = "countries";
    public static final int DATABASE_VERSION = 1;

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_POPULATION = "population";
    public static final String KEY_REGION = "region";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_COUNTRIES + "(" +
                KEY_ID + " integer primary key," +
                KEY_NAME + " text," +
                KEY_POPULATION + " integer," +
                KEY_REGION + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table " + TABLE_COUNTRIES);
        onCreate(sqLiteDatabase);
    }
}
