package com.example.serg.myapplinux;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "buy_list.db";
    private static final int DATABASE_VERSION = 28;
    public static final String TABLE_NAME = "purchases";
    public static final String ID_key = "_id";
    public static final String NAME = "name";
    public static final String AMOUNT = "amount";
    public static final String PRICE = "price";
    public static final String DATE_TIME = "date";
    public static final String BOUGHT = "bought";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ING_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + ID_key + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " TEXT,"
                + PRICE + " REAL,"
                + DATE_TIME + " TEXT,"
                + BOUGHT + " TEXT,"
                + AMOUNT + " REAL" + ")";
        db.execSQL(CREATE_ING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
