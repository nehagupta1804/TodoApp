package com.example.nehagupta.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpenseOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="items";
    public static final int version=2;

    public ExpenseOpenHelper(Context context) {
        super(context,DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String expensesql =" CREATE TABLE " + Contract.Expense.TABLE_NAME + " ( " + Contract.Expense.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contract.Expense.COLUMN_NAME + " TEXT, " +
                Contract.Expense.COLUMN_AMOUNT + " INTEGER, " +  Contract.Expense.COLUMN_CAMERA + " TEXT, " + Contract.Expense.COLUMN_RAM + " TEXT "  +" ) ";
        sqLiteDatabase.execSQL(expensesql);




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


    }


}
