package com.example.nehagupta.todoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Expense> expenses = new ArrayList<>();
    ExpenseAdapter adapter;
    Button button;
    ListView listView;
    public static final String  NAME_KEY="name";
    public static final String  AMOUNT_KEY="amount";
    public static final int EDIT_REQUEST_CODE=567;
    public static final int ADD_REQUEST_CODE=678;
    public int position;
    int day,month,year,hour,minute;
    int dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.click);
       /* button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c= Calendar.getInstance();
                year=c.get(Calendar.YEAR);
                month =c.get(Calendar.MONTH);
                day= c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this,MainActivity.this,year,month,day);
                datePickerDialog.show();
            }
        });*/
        listView= findViewById(R.id.list_item);
        ExpenseOpenHelper expenseOpenHelper=new ExpenseOpenHelper(this);
        SQLiteDatabase database=expenseOpenHelper.getReadableDatabase();
        Cursor cursor =  database.query(Contract.Expense.TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            String title = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_NAME));
            int amount = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_AMOUNT));
            String camera = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_CAMERA));
            String ram = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_RAM));
            int id = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_ID));
            Expense expense=new Expense(title,amount,camera,ram);
            expense.setId(id);
            expenses.add(expense);
        }

        /*for(int i=0;i<7;i++)
        {
            Expense expense = new Expense("Item"+i,(i+3)*1000);
            expenses.add(expense);
        }*/
        adapter = new ExpenseAdapter(this,expenses);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int position=i;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirm Delete");
                builder.setMessage("Do you really want to delete ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)  {

                        Expense expense=expenses.get(position);
                        ExpenseOpenHelper openHelper=new ExpenseOpenHelper(MainActivity.this);
                        SQLiteDatabase database= openHelper.getWritableDatabase();
                        long id = expense.getId();
                        String[] selectionargs = {id + ""};
                        int result = database.delete(Contract.Expense.TABLE_NAME,Contract.Expense.COLUMN_ID + " = ? ",selectionargs);
                        if (result == 1) {
                            expenses.remove(position);
                            adapter.notifyDataSetChanged();
                        }

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO

                    }
                });
                AlertDialog dialog= builder.create();
                dialog.show();
                return(true);

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this,DescribeActivity.class);
        position=i;
        Expense expense = expenses.get(i);
        intent.putExtra("id",expense.getId());
        /*intent.putExtra(NAME_KEY,name.getName());
        intent.putExtra(AMOUNT_KEY,name.getAmount());*/
        //startActivity(intent);
        startActivityForResult(intent,EDIT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_REQUEST_CODE)
        {
            if(resultCode==DescribeActivity.EDIT_RESULT_CODE) {
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
                        ContentValues contentValues= new ContentValues();
                        contentValues.put(Contract.Expense.COLUMN_NAME,title);
                        contentValues.put(Contract.Expense.COLUMN_AMOUNT,amount);
                        contentValues.put(Contract.Expense.COLUMN_CAMERA,camera);
                        contentValues.put(Contract.Expense.COLUMN_RAM,ram);
                        Expense expense=new Expense(title,amount,camera,ram);
                        //int id = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_ID));
                        database.update(Contract.Expense.TABLE_NAME, contentValues, Contract.Expense.COLUMN_ID  + "=" + id,null);
                        expenses.set(position, expense);
                        adapter.notifyDataSetChanged();
                    }
                   /* Expense expense = expenses.get(position);;
                    String name = data.getStringExtra(Edit_item.NAME_KEY);
                    String amount = data.getStringExtra(Edit_item.PRICE_KEY);
                    int a=-1;
                    if(!amount.equals(""))
                      a = Integer.parseInt(amount);
                    // Expense expense = new Expense(name, a);
                    if (!name.equals("") && a != -1) {
                        expense.name = name;
                        expense.amount = a;
                       ExpenseOpenHelper expenseOpenHelper=new ExpenseOpenHelper(this);
                        SQLiteDatabase database=expenseOpenHelper.getWritableDatabase();
                        ContentValues contentValues=new ContentValues();
                        long id = expense.getId();
                            //String[] arguments={ id+"",};
                        contentValues.put(Contract.Expense.COLUMN_NAME,name);
                        contentValues.put(Contract.Expense.COLUMN_AMOUNT,amount);
                        database.update(Contract.Expense.TABLE_NAME, contentValues, Contract.Expense.COLUMN_ID  + "=" + id,null);
                        expenses.set(position, expense);
                        adapter.notifyDataSetChanged();
                    }*/
                }
            }
        }
        else if(requestCode == ADD_REQUEST_CODE)
        {
            if(resultCode==AddExpenseActivity.ADD_RESULT_CODE)
            {
                if(data!=null) {
                    String name = data.getStringExtra("name");
                    int amount = data.getIntExtra("amount", 0);
                    String camera = data.getStringExtra("camera");
                    String ram = data.getStringExtra("ram");
                    if (!name.equals("") && !camera.equals("")&& !ram.equals("")&& amount != -1) {
                        Expense expense = new Expense(name, amount,camera,ram);
                        ExpenseOpenHelper expenseOpenHelper=new ExpenseOpenHelper(this);
                        SQLiteDatabase database=expenseOpenHelper.getWritableDatabase();
                        ContentValues contentValues=new ContentValues();
                        contentValues.put(Contract.Expense.COLUMN_NAME,expense.getName());
                        contentValues.put(Contract.Expense.COLUMN_AMOUNT,expense.getAmount());
                        contentValues.put(Contract.Expense.COLUMN_CAMERA,expense.getCamera());
                        contentValues.put(Contract.Expense.COLUMN_RAM,expense.getRam());
                        long id=database.insert(Contract.Expense.TABLE_NAME,null,contentValues);
                        if(id>-1) {
                            expense.setId(id);
                            expenses.add(expense);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        int id = item.getItemId();
        if(id==R.id.add)
        {
            Intent intent = new Intent(this,AddExpenseActivity.class);
            startActivityForResult(intent,ADD_REQUEST_CODE);
        }
        else if(id==R.id.name)
        {
            Collections.sort(expenses, new Comparator<Expense>() {
                @Override
                public int compare(Expense s1, Expense s2) {
                    return s1.getName().compareToIgnoreCase(s2.getName());
                }
            });
            adapter.notifyDataSetChanged();
        }
        else if(id == R.id.amount)
        {
            Collections.sort(expenses, new Comparator<Expense>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public int  compare(Expense s1, Expense s2) {
                    //return s1.getAmount().s2.getAmount();
                    return Integer.compare(s1.getAmount(),s2.getAmount());
                }
            });
            adapter.notifyDataSetChanged();

        }

       /*  AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText input1 = new EditText(this);
        input1.setHint("Enter name of item");
        linearLayout.addView(input1);
        final EditText input2 = new EditText(this);
        input2.setHint("Enter price of item");
        linearLayout.addView(input2);
        builder.setTitle("ADD ITEM");
        builder.setView(linearLayout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               String name = input1.getText().toString();
               String amount=input2.getText().toString();
               Expense expense = new Expense(name,Integer.parseInt(amount));
               expenses.add(expense);
                adapter.notifyDataSetChanged();

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();*/


        return super.onOptionsItemSelected(item);
    }

   /*public void onTimeSet(TimePicker timePicker,int i,int i1)
    {
        hourFinal=i;
        minuteFinal=i1;

    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


    }*/
    public void click_me(View view)
    {
       final  Button btn =view.findViewById(R.id.click);
        Calendar c= Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month =c.get(Calendar.MONTH);
        day= c.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog=new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                yearFinal=i;
                monthFinal=i1+1;
                dayFinal=i2;
                Calendar c=Calendar.getInstance();
                hour=c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
               // btn.setText( dayFinal + "/" +monthFinal  + "/" + yearFinal );
                TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {


                                        btn.setText(dayFinal + "/" +monthFinal  + "/" + yearFinal+ "\n" +i + ":" + i1);

                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(MainActivity.this));
                timePickerDialog.show();
            }
        }, year, month, day);
        datePickerDialog.show();

    }
}
