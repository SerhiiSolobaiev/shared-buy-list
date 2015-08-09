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
                + ID_key + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME + " TEXT UNIQUE,"
                + PRICE + " REAL,"
                + DATE_TIME + " TEXT,"
                + BOUGHT + " INTEGER DEFAULT 0,"
                + AMOUNT + " REAL" + ")";
        db.execSQL(CREATE_ING_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
/*
Log.v("check_bought","clicked on position"+position);
        LinearLayout ll = (LinearLayout) view;
final TextView name_long_clicked = (TextView) ll.findViewById(R.id.name_custom);
final CheckBox c = (CheckBox)ll.findViewById(R.id.checkBox_bought_custom);
        Toast.makeText(getApplicationContext(), "Clicked on name "/* + name_long_clicked.getText()
        , Toast.LENGTH_SHORT).show();
        Log.v("check_bought", "after Toast.makeText");
                /*c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            Log.v("check_bought", "Checked on name"+ name_long_clicked.getText());
                            Toast.makeText(getApplicationContext(), "Checked on name " /*+ name_long_clicked.getText()
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/