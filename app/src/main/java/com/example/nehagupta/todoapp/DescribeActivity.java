package com.example.nehagupta.todoapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DescribeActivity extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView textView5;
    public static final int EDIT_REQUEST_CODE=900;
    public static final int EDIT_RESULT_CODE=678;
   String name;
   String amount;
    long id=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_describe);
         textView1 = findViewById(R.id.name);
        textView2 = findViewById(R.id.price);
        textView3 = findViewById(R.id.camera);
        textView4 = findViewById(R.id.ram);
        textView5 = findViewById(R.id.date);
        ExpenseOpenHelper expenseOpenHelper=new ExpenseOpenHelper(this);
        SQLiteDatabase database=expenseOpenHelper.getReadableDatabase();
        Intent intent = getIntent();
        id = intent.getLongExtra("id", 0);

       /* if(id == 0)
        {
            id=intent.getLongExtra("recieve_id",0);
        }*/
       /* if(intent.getStringExtra("string").equals("string")) {
            id = intent.getLongExtra("id", 0);
        }
        else
            id=  intent.getLongExtra("id_receive", 0);
            */
        String[] selectionargs={id + ""};
        //int amountGreaterThan =
        //String[] selectionArgument={amountG0;reaterThan + "",};
        //String[] columns={Contract.Expense.COLUMN_NAME,Contract.Expense.COLUMN_AMOUNT,Contract.Expense.COLUMN_ID};
        Cursor cursor = database.query(Contract.Expense.TABLE_NAME,null,Contract.Expense.COLUMN_ID + " = ? ",selectionargs,null,null,null);
        while(cursor.moveToNext())
        {
            String title = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_NAME));
            Integer amount = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_AMOUNT));
            String camera = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_CAMERA));
            String ram = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_RAM));
            long date=cursor.getLong(cursor.getColumnIndex(Contract.Expense.COLUMN_DATE));
            textView1.setText(title);
            textView2.setText(amount+"");
            textView3.setText(camera);
            textView4.setText(ram);
            Date d = new Date(date);
            SimpleDateFormat sdf = new SimpleDateFormat();
            String expenseDate = sdf.format(d);
            textView5.setText(expenseDate);
            /*long id=cursor.getLong(cursor.getColumnIndex(Contract.Expense.COLUMN_ID));
            Expense expense = new Expense(title,amount);
            expense.setId(id);
            expenses.add(expense);*/
        }

       /* textView1 = findViewById(R.id.name);
        textView2 = findViewById(R.id.price);
        textView3 = findViewById(R.id.camera);
        textView4 = findViewById(R.id.ram);
        Intent intent = getIntent();
        String name =intent.getStringExtra(MainActivity.NAME_KEY);
        int amount = intent.getIntExtra(MainActivity.AMOUNT_KEY,0);
        if(name.equals("Item0"))
        {
            textView1.setText("RedmiNote3");
            textView2.setText(amount+"");
            textView3.setText("12px");
            textView4.setText("3GB");
        }
        if(name.equals("Item1"))
        {
            textView1.setText("Redmi5A");
            textView2.setText(amount+"");
            textView3.setText("10px");
            textView4.setText("4GB");
        }
        if(name.equals("Item2"))
        {
            textView1.setText("OnePlus 6");
            textView2.setText(amount+"");
            textView3.setText("16px");
            textView4.setText("6GB");
        }
        if(name.equals("Item3"))
        {
            textView1.setText("Samsung");
            textView2.setText(amount+"");
            textView3.setText("10px");
            textView4.setText("3GB");
        }
        if(name.equals("Item4"))
        {
            textView1.setText("RedmiNote5");
            textView2.setText(amount+"");
            textView3.setText("12px");
            textView4.setText("4GB");
        }
        if(name.equals("Item5"))
        {
            textView1.setText("HTC");
            textView2.setText(amount+"");
            textView3.setText("12px");
            textView4.setText("3GB");
        }
        if(name.equals("Item6"))
        {
            textView1.setText("Lenovo K6");
            textView2.setText(amount+"");
            textView3.setText("12px");
            textView4.setText("4GB");
        }*/
    }

   /* public void onNewIntent(Intent intent){

        ExpenseOpenHelper expenseOpenHelper=new ExpenseOpenHelper(this);
        SQLiteDatabase database=expenseOpenHelper.getReadableDatabase();
        id = intent.getLongExtra("id", 0);

        String[] selectionargs={id + ""};
        Cursor cursor = database.query(Contract.Expense.TABLE_NAME,null,Contract.Expense.COLUMN_ID + " = ? ",selectionargs,null,null,null);
        while(cursor.moveToNext())
        {
            String title = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_NAME));
            Integer amount = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_AMOUNT));
            String camera = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_CAMERA));
            String ram = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_RAM));
            long date=cursor.getLong(cursor.getColumnIndex(Contract.Expense.COLUMN_DATE));
            textView1.setText(title);
            textView2.setText(amount+"");
            textView3.setText(camera);
            textView4.setText(ram);
            Date d = new Date(date);
            SimpleDateFormat sdf = new SimpleDateFormat();
            String expenseDate = sdf.format(d);
            textView5.setText(expenseDate);

        }


    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==EDIT_REQUEST_CODE)
        {
            if(resultCode==Edit_item.EDIT_RESULT_CODE)
            {
                if(data!=null) {

                    ExpenseOpenHelper expenseOpenHelper=new ExpenseOpenHelper(this);
                    SQLiteDatabase database=expenseOpenHelper.getReadableDatabase();
                    id = data.getLongExtra("update_id",0);
                    String[] selectionargs = {id + ""};
                    Cursor cursor =  database.query(Contract.Expense.TABLE_NAME,null,Contract.Expense.COLUMN_ID + " = ? ",selectionargs,null,null,null);
                    while (cursor.moveToNext())
                    {
                        String title = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_NAME));
                        int amount = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_AMOUNT));
                        String camera = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_CAMERA));
                        String ram = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_RAM));
                        long date=cursor.getLong(cursor.getColumnIndex(Contract.Expense.COLUMN_DATE));
                        //int id = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_ID));
                        textView1.setText(title);
                        textView2.setText(amount + "");
                        textView3.setText(camera);
                        textView4.setText(ram);
                        Date d = new Date(date);
                        SimpleDateFormat sdf = new SimpleDateFormat();
                        String expenseDate = sdf.format(d);
                        textView5.setText(expenseDate);
                    }
                    /*name = data.getStringExtra(Edit_item.NAME_KEY);
                    textView1.setText(name);
                    amount = data.getStringExtra(Edit_item.PRICE_KEY);
                    textView2.setText(amount);
                    String camera = data.getStringExtra(Edit_item.CAMERA_KEY);
                    textView3.setText(camera);
                    String ram = data.getStringExtra(Edit_item.RAM_KEY);
                    textView4.setText(ram);
                    */

                }
            }
        }
    }
    public void onclick_back(View view)
    {
        Intent intent = new Intent();
        /*intent.putExtra(Edit_item.NAME_KEY,name);
        intent.putExtra(Edit_item.PRICE_KEY,amount);*/
        intent.putExtra("update_id",id);
        setResult(EDIT_RESULT_CODE,intent);
        finish();
    }
   /* public void showTimePickerDialog(View view)
    {
        DialogFragment newFragment = new TimePickerDialog();
        newFragment.show(getSupportFragmentManager(),"timePicker");
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int menu_id = item.getItemId();
        if(menu_id == R.id.edit)
        {
            Intent intent = new Intent(this,Edit_item.class);
            intent.putExtra("edit_id",id);
            startActivityForResult(intent,EDIT_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);

    }
}
