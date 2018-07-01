package com.example.nehagupta.todoapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Edit_item extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    public static final String NAME_KEY="name";
    public static final String PRICE_KEY="price";
    public static final String CAMERA_KEY="camera";
    public static final String RAM_KEY="ram";
    public  static final int EDIT_RESULT_CODE=500;
    long id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        editText1=findViewById(R.id.name);
        editText2=findViewById(R.id.price);
        editText3=findViewById(R.id.camera);
        editText4=findViewById(R.id.ram);
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
            editText1.setText(title);
          editText2.setText(amount+"");
            editText3.setText(camera);
            editText4.setText(ram);
            /*long id=cursor.getLong(cursor.getColumnIndex(Contract.Expense.COLUMN_ID));
            Expense expense = new Expense(title,amount);
            expense.setId(id);
            expenses.add(expense);*/
        }

    }
    public void saveDetails(View view)
    {
        String name = editText1.getText().toString();
        String price = editText2.getText().toString();
        int amount=Integer.parseInt(price);
        String camera = editText3.getText().toString();
        String ram = editText4.getText().toString();
        ExpenseOpenHelper expenseOpenHelper=new ExpenseOpenHelper(this);
        SQLiteDatabase database=expenseOpenHelper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();

        //String[] arguments={ id+"",};
        contentValues.put(Contract.Expense.COLUMN_NAME,name);
        contentValues.put(Contract.Expense.COLUMN_AMOUNT,amount);
        contentValues.put(Contract.Expense.COLUMN_CAMERA,camera);
        contentValues.put(Contract.Expense.COLUMN_RAM,ram);
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
