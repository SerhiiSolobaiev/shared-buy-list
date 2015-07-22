package com.example.serg.myapplinux;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Main extends ActionBarActivity{
    DBHelper dbHelper;
    ListView list;
    private static String TAG = "LIST_TO_BUY";
    final Context context = this; // разобраться что это и зачем надо!!!
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
        long_clicked_on_item();
    }
    private void add_purchase(){
        final EditText name_add = (EditText) findViewById(R.id.name_add);
        final EditText number_add = (EditText) findViewById(R.id.number_add);
        final EditText price_add = (EditText) findViewById(R.id.price_add);
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
            cv.put(DBHelper.NAME, name_add_string);
            cv.put(DBHelper.AMOUNT, number_add_string);
            cv.put(DBHelper.PRICE, price_add_string);
            cv.put(DBHelper.DATE_TIME, date_time);
            db.insert(DBHelper.TABLE_NAME, null, cv);
            Log.v(TAG, " db.insert(DBHelper.TABLE_NAME, null, cv);");

            /*int maxLength = 10;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            edit1.setFilters(FilterArray);
            edit2.setFilters(FilterArray);
            edit3.setFilters(FilterArray);

            */
            name_add.setText("");
            number_add.setText("");
            price_add.setText("");
            update_list();
        }
    }
    private void update_list(){
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(DBHelper.TABLE_NAME, null,
                null, null, null, null, null);
        String[] from = new String[] {DBHelper.NAME, DBHelper.AMOUNT,
                DBHelper.PRICE,DBHelper.DATE_TIME};
        int[] to = new int[] {R.id.name_custom,R.id.number_custom,R.id.price_custom,
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
        calculate_sum();
    }


    private void long_clicked_on_item(){
        dbHelper = new DBHelper(this);
        list = (ListView) findViewById(R.id.listView);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                final CharSequence list_actions[] = {getText(R.string.action_delete),
                        getText(R.string.action_edit)};
                AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                builder.setTitle(R.string.action_title)
                        .setItems(list_actions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                LinearLayout ll = (LinearLayout) view;
                                TextView name_long_clicked = (TextView) ll.findViewById(R.id.name_custom);
                                TextView number_long_clicked = (TextView) ll.findViewById(R.id.number_custom);
                                TextView price_long_clicked = (TextView) ll.findViewById(R.id.price_custom);
                                if (which == 0) {
                                    delete_record(name_long_clicked.getText().toString());
                                } else {
                                    Toast.makeText(getApplicationContext(), "Must be edit function",
                                            Toast.LENGTH_SHORT).show();
                                    edit_purchase(name_long_clicked.getText().toString(),
                                            number_long_clicked.getText().toString(),
                                            price_long_clicked.getText().toString());
                                }
                            }
                        });
                builder.show();
                return true;
            }
        });
        update_list();
    }
    private String get_current_date_time(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM HH:mm");
        String strDateTime = sdf.format(c.getTime());
        return strDateTime;
    }

    private void calculate_sum(){
        final TextView text_sum = (TextView) findViewById(R.id.textView_sum);
        double sum = 0;
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT " + DBHelper.PRICE + " FROM " + DBHelper.TABLE_NAME, null);
        if (c != null){
            if (c.moveToFirst()){
                do{
                    double price_from_bd = c.getDouble(c.getColumnIndex(DBHelper.PRICE));
                    sum += price_from_bd;
                }while (c.moveToNext());
            }
        }
        if (sum != 0) {
            String total_sum = getResources().getString(R.string.total_sum);
            text_sum.setText(String.format(total_sum + " %.2f", sum));
        }else text_sum.setText("");
    }

    private void edit_purchase(String name,String number,String price){
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.custom_alertdialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);

        final EditText name_in_alert = (EditText)promptsView.findViewById(R.id.name_in_alert);
        final EditText number_in_alert = (EditText)promptsView.findViewById(R.id.number_in_alert);
        final EditText price_in_alert = (EditText)promptsView.findViewById(R.id.price_in_alert);
        name_in_alert.setText(name);
        number_in_alert.setText(number);
        price_in_alert.setText(price);

        final int id_purchase = find_id_purchase(name_in_alert.getText().toString());
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(R.string.okey,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                //result.setText(userInput.getText());
                                Toast.makeText(getApplicationContext(), "Must be writing to bd and update_list",
                                        Toast.LENGTH_SHORT).show();
                                save_edit_purchase(id_purchase,name_in_alert.getText().toString(),
                                        number_in_alert.getText().toString(),
                                        price_in_alert.getText().toString());
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void delete_record(String name){
        String query = String.format("DELETE FROM %s WHERE %s = '%s'", DBHelper.TABLE_NAME,
                DBHelper.NAME, name);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(query);
        update_list();
    }
    private void save_edit_purchase(int id, String name,String number, String price){
        if (name.equals("")) {
            Toast.makeText(getApplicationContext(),R.string.not_entered, Toast.LENGTH_SHORT).show();
        }
        else {
            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.NAME, name);
            cv.put(DBHelper.AMOUNT, number);
            cv.put(DBHelper.PRICE, price);
            cv.put(DBHelper.DATE_TIME, get_current_date_time());
            db.update(DBHelper.TABLE_NAME, cv, DBHelper.ID_key +" = "+id,null);
            Log.v(TAG, " db.update(DBHelper.TABLE_NAME, cv, DBHelper.ID_key = id,null);");
        }
        update_list();
        /*
        * Добавить сохранение состояний checkbox
        * Добавить редактирование
        * Внешний вид
        * ОБЩИЙ СПИСОК ПОКУПОК(peer to peer)
        * */
    }
    private int find_id_purchase(String name){
        int id = 0;
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DBHelper.NAME + " = '" + name + "'";
        Cursor c = db.query(DBHelper.TABLE_NAME, null, selection, null, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    id = c.getInt(c.getColumnIndex(DBHelper.ID_key));
                } while (c.moveToNext());
            }
            c.close();
        }
        return id;
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

