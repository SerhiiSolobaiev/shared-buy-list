package com.example.serg.myapplinux;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Main extends ActionBarActivity{
    DBHelper dbHelper;
    ListView list;
    private double sum = 0;
    private static String TAG = "LIST_TO_BUY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button buttonAdd = (Button) findViewById(R.id.add_button);
        OnClickListener add = new OnClickListener() {
            @Override
            public void onClick(View v) {
                add_purchase();
            }
        };
        buttonAdd.setOnClickListener(add);
        update_list();
    }
    private void add_purchase(){
        final EditText name_add = (EditText) findViewById(R.id.name_add);
        final EditText number_add = (EditText) findViewById(R.id.number_add);
        final EditText price_add = (EditText) findViewById(R.id.price_add);
        final TextView text_sum = (TextView) findViewById(R.id.textView_sum);
        String name_add_string = name_add.getText().toString();
        String number_add_string = number_add.getText().toString();
        String price_add_string = price_add.getText().toString();
        String date_time = get_current_date_time();

        if (name_add_string.equals("")) {
            Toast.makeText(getApplicationContext(),R.string.not_entered, Toast.LENGTH_SHORT).show();
        }
        else {
            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.NAME,name_add_string);
            cv.put(DBHelper.AMOUNT,number_add_string);
            cv.put(DBHelper.PRICE,price_add_string);
            cv.put(DBHelper.DATE_TIME,date_time);
            db.insert(DBHelper.TABLE_NAME, null, cv);
            Log.v(TAG, " db.insert(DBHelper.TABLE_NAME, null, cv);");

            /*int maxLength = 10;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            edit1.setFilters(FilterArray);
            edit2.setFilters(FilterArray);
            edit3.setFilters(FilterArray);

            */

            if(price_add_string == null || price_add_string.isEmpty())
            {
                sum = sum + 0;
            } else {
                sum = sum + Double.parseDouble(price_add_string);
                text_sum.setText("Total = " + sum);
            }

            name_add.setText("");
            number_add.setText("");
            price_add.setText("");
            update_list();
        }
    }
    private void update_list(){
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TABLE_NAME, new String[] {DBHelper.ID_key, DBHelper.NAME,
                DBHelper.AMOUNT, DBHelper.PRICE, DBHelper.DATE_TIME},
                null, null, null, null, null);
        String[] from = new String[] {DBHelper.ID_key, DBHelper.NAME, DBHelper.AMOUNT, DBHelper.PRICE,
                DBHelper.DATE_TIME};
        int[] to = new int[] {R.id.n_in_list, R.id.name_custom,R.id.number_custom,R.id.price_custom,
                R.id.date_custom};
        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this, R.layout.custom_list, c,
                from,
                to,
                0
        );
        Log.v(TAG, " after SimpleCursorAdapter listAdapter = new");
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(listAdapter);
    }
    private String get_current_date_time(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MMMM:yyyy HH:mm");
        String strDateTime = sdf.format(c.getTime());
        return strDateTime;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

