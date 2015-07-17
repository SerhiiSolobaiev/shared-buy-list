package com.example.serg.myapplinux;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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
        delete_record();
        //long_clicked_on_item();
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
        String[] from = new String[] {DBHelper.ID_key, DBHelper.NAME, DBHelper.AMOUNT,
                DBHelper.PRICE,DBHelper.DATE_TIME};
        int[] to = new int[] {R.id.n_in_list, R.id.name_custom,R.id.number_custom,R.id.price_custom,
                R.id.date_custom};
        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this, R.layout.custom_list, c,
                from,
                to,
                0
        );
        list = (ListView) findViewById(R.id.listView);
        list.setAdapter(listAdapter);
        Log.v(TAG, "list updated");
    }

    private void delete_record(){
        dbHelper = new DBHelper(this);
        list = (ListView) findViewById(R.id.listView);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /*LinearLayout ll = (LinearLayout) view;
                TextView name_long_clicked = (TextView) ll.findViewById(R.id.name_custom);
                String query = String.format("DELETE FROM %s WHERE %s = '%s'",
                        DBHelper.TABLE_NAME,
                        DBHelper.NAME,
                        name_long_clicked.getText().toString());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL(query);
                //Toast.makeText(getApplicationContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
                */
                //long_clicked_on_item();
                final CharSequence list_actions[] = {String.valueOf(R.string.action_delete),
                        String.valueOf(R.string.action_edit)};
                AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                builder.setTitle(R.string.action_title)
                        .setItems(list_actions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                Toast.makeText(getApplicationContext(), "Good!!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                //builder.create();
                builder.show();
                return true;
            }
        });
        update_list();
    }
    private void long_clicked_on_item(){

        final CharSequence list_actions[] = {String.valueOf(R.string.action_delete),
                String.valueOf(R.string.action_edit)};
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(R.string.action_title);
        adb.setSingleChoiceItems(list_actions,-1,new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                Toast.makeText(getApplicationContext(),
                        "You Choose : "+ list_actions[arg1],
                        Toast.LENGTH_LONG).show();
            }});
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),
                        "You Have Cancel the Dialog box", Toast.LENGTH_LONG)
                        .show();
            }
        });
        adb.create();
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

