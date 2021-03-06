package com.example.nehagupta.todoapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddExpenseActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText amountEditText;
    EditText cameraEditText;
    EditText ramEditText;
    int day,month,year,hour,minute;
    int dayFinal,monthFinal,yearFinal,hourFinal,minuteFinal;
    long date;

    public static final int ADD_RESULT_CODE=7789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        nameEditText = findViewById(R.id.name);
        String selectedText="";
        Intent intent = getIntent();
        String action=intent.getAction();
        String type=intent.getType();
        if(type!=null && action!=null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                selectedText = intent.getClipData().getItemAt(0).getText().toString();
            }
            nameEditText.setText(selectedText);
        }
    }
    public void startActivityResult(View view)
    {
         amountEditText = findViewById(R.id.amount);
         cameraEditText= findViewById(R.id.camera);
         ramEditText=findViewById(R.id.ram);
         String name1= nameEditText.getText().toString();
         String  amount1 = amountEditText.getText().toString();
         String  camera1 = cameraEditText.getText().toString();
         String  ram1 = ramEditText.getText().toString();

          int amount = -1;
          if (!amount1.equals("")) {
              amount = Integer.parseInt(amount1);
          }

            Intent data = new Intent();
         data.putExtra("name",name1);
         data.putExtra("amount",amount);
         data.putExtra("camera",camera1);
         data.putExtra("ram",ram1);
         data.putExtra("date",date);
         addExpenseToDB(data);
         setResult(ADD_RESULT_CODE,data);
         finish();
    }

    private void addExpenseToDB(Intent data) {
        if(data!=null) {
            String name = data.getStringExtra("name");
            int amount = data.getIntExtra("amount", 0);
            String camera = data.getStringExtra("camera");
            String ram = data.getStringExtra("ram");
            long expenseDate = data.getLongExtra("date",0);
            if (!name.equals("") && !camera.equals("")&& !ram.equals("")&& amount != -1 && expenseDate > 0) {
                Expense expense = new Expense(name, amount,camera,ram);
                expense.setDate(expenseDate);
                ExpenseOpenHelper expenseOpenHelper=new ExpenseOpenHelper(this);
                SQLiteDatabase database=expenseOpenHelper.getWritableDatabase();
                ContentValues contentValues=new ContentValues();
                contentValues.put(Contract.Expense.COLUMN_NAME,expense.getName());
                contentValues.put(Contract.Expense.COLUMN_AMOUNT,expense.getAmount());
                contentValues.put(Contract.Expense.COLUMN_CAMERA,expense.getCamera());
                contentValues.put(Contract.Expense.COLUMN_RAM,expense.getRam());
                contentValues.put(Contract.Expense.COLUMN_DATE,expense.getDate());
                long id=database.insert(Contract.Expense.TABLE_NAME,null,contentValues);
                data.putExtra("dbresult_id", id);
            }
        }
    }



    public void click_me(final View view)
    {
        final Button btn = view.findViewById(R.id.click);
        Calendar c= Calendar.getInstance();
        year=c.get(Calendar.YEAR);
        month =c.get(Calendar.MONTH);
        day= c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog=new DatePickerDialog(AddExpenseActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                yearFinal=i;
                monthFinal=i1;
                dayFinal=i2;
                Calendar c=Calendar.getInstance();
                hour=c.get(Calendar.HOUR_OF_DAY);
                minute = c.get(Calendar.MINUTE);
                android.app.TimePickerDialog timePickerDialog=new android.app.TimePickerDialog(AddExpenseActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                        Date expenseDate = new Date(yearFinal-1900, monthFinal, dayFinal, i, i1);
                        date = expenseDate.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat();
                        String dateText = sdf.format(expenseDate);
                        btn.setText(dateText);


                    }
                }, hour, minute, android.text.format.DateFormat.is24HourFormat(AddExpenseActivity.this));
                timePickerDialog.show();
            }
        }, year, month, day);
        datePickerDialog.show();

    }
}
