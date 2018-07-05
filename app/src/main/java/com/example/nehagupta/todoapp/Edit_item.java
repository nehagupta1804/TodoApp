package com.example.nehagupta.todoapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Edit_item extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    Button btn;
    //EditText editText5;
    public static final String NAME_KEY="name";
    public static final String PRICE_KEY="price";
    public static final String CAMERA_KEY="camera";
    public static final String RAM_KEY="ram";
    public  static final int EDIT_RESULT_CODE=500;
    long id;
    int day,month,year,hour,minute;
    int dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    long date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        editText1=findViewById(R.id.name);
        editText2=findViewById(R.id.price);
        editText3=findViewById(R.id.camera);
        editText4=findViewById(R.id.ram);
        btn=findViewById(R.id.click);
        //editText5=findViewById(R.id.date);
        ExpenseOpenHelper expenseOpenHelper=new ExpenseOpenHelper(this);
        SQLiteDatabase database=expenseOpenHelper.getReadableDatabase();
        Intent intent = getIntent();
        id = intent.getLongExtra("edit_id",0);
        String[] selectionargs={id + ""};
        //int amountGreaterThan =0;
        //String[] selectionArgument={amountGreaterThan + "",};
        //String[] columns={Contract.Expense.COLUMN_NAME,Contract.Expense.COLUMN_AMOUNT,Contract.Expense.COLUMN_ID};
        Cursor cursor = database.query(Contract.Expense.TABLE_NAME,null,Contract.Expense.COLUMN_ID + " = ? ",selectionargs,null,null,null);
        while(cursor.moveToNext())
        {
            String title = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_NAME));
            Integer amount = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_AMOUNT));
            String camera = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_CAMERA));
            String ram = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_RAM));
            date=cursor.getLong(cursor.getColumnIndex(Contract.Expense.COLUMN_DATE));
            editText1.setText(title);
            editText2.setText(amount+"");
            editText3.setText(camera);
            editText4.setText(ram);
            Date d = new Date(date);
            SimpleDateFormat sdf = new SimpleDateFormat();
            String expenseDate = sdf.format(d);
            btn.setText(expenseDate);
            /*long id=cursor.getLong(cursor.getColumnIndex(Contract.Expense.COLUMN_ID));
            Expense expense = new Expense(title,amount);
            expense.setId(id);
            expenses.add(expense);*/
        }

    }
    public void click_me(final View view)
    {
        //final Button btn = view.findViewById(R.id.click);
        Calendar c= Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month =c.get(Calendar.MONTH);
        day= c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog=new DatePickerDialog(Edit_item.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                yearFinal=i;
                monthFinal=i1;
                dayFinal=i2;
                Calendar c=Calendar.getInstance();
                hour=c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                // btn.setText( dayFinal + "/" +monthFinal  + "/" + yearFinal );
                android.app.TimePickerDialog timePickerDialog=new android.app.TimePickerDialog(Edit_item.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        Date expenseDate = new Date(yearFinal, monthFinal, dayFinal, i, i1);
                        date = expenseDate.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat();
                        String dateText = sdf.format(expenseDate);
                        btn.setText(dateText);

                        //date=  dayFinal + "/" +monthFinal  + "/" + yearFinal+ "\n" +i + ":" + i1;
                        //btn.setText(dayFinal + "/" +monthFinal  + "/" + yearFinal+ "\n" +i + ":" + i1);

                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(Edit_item.this));
                timePickerDialog.show();
            }
        }, year, month, day);
        datePickerDialog.show();

    }
    public void saveDetails(View view)
    {
        String name = editText1.getText().toString();
        String price = editText2.getText().toString();
        int amount=Integer.parseInt(price);
        String camera = editText3.getText().toString();
        String ram = editText4.getText().toString();
       /* String date=editText5.getText().toString();
        Double d= Double.parseDouble(date);
        */
        ExpenseOpenHelper expenseOpenHelper=new ExpenseOpenHelper(this);
        SQLiteDatabase database=expenseOpenHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        //String[] arguments={ id+"",};
        contentValues.put(Contract.Expense.COLUMN_NAME,name);
        contentValues.put(Contract.Expense.COLUMN_AMOUNT,amount);
        contentValues.put(Contract.Expense.COLUMN_CAMERA,camera);
        contentValues.put(Contract.Expense.COLUMN_RAM,ram);
        contentValues.put(Contract.Expense.COLUMN_DATE,date);
        int result=database.update(Contract.Expense.TABLE_NAME, contentValues, Contract.Expense.COLUMN_ID  + "=" + id,null);
        Intent data = new Intent();
        data.putExtra("update_id",id);
       /* data.putExtra(NAME_KEY,name);
        data.putExtra(PRICE_KEY,price);
        data.putExtra(CAMERA_KEY,camera);
        data.putExtra(RAM_KEY,ram)*/

        setResult(EDIT_RESULT_CODE,data);
        finish();

    }
}
