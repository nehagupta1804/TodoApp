package com.example.nehagupta.todoapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ExpenseAdapter extends ArrayAdapter {

    ArrayList<Expense> items;
    LayoutInflater inflater;

    public ExpenseAdapter(@NonNull Context context,  ArrayList<Expense> items) {
        super(context, 0,items);
        this.items = items;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
    }
    public int getCount()
    {
        return(items.size());
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View output = convertView;
        if(output==null) {
             output = inflater.inflate(R.layout.expense_row_layout, parent, false);
            TextView nameTextView = output.findViewById(R.id.expenseName);
            TextView amountTextView = output.findViewById(R.id.expenseAmount);
            Button btn = output.findViewById(R.id.click);
           // TextView tv = output.findViewById(R.id.pick);
            ExpenseViewHolder viewHolder = new ExpenseViewHolder();
            viewHolder.title = nameTextView;
            viewHolder.button=btn;
            viewHolder.amount = amountTextView;
            //viewHolder.pick = tv;
            output.setTag(viewHolder);
        }
        ExpenseViewHolder viewHolder =(ExpenseViewHolder)output.getTag();
        Expense expense = items.get(position);
        viewHolder.title.setText(expense.getName());
        viewHolder.amount.setText(expense.getAmount()+"Rs");
       // viewHolder.pick.setText("");
        return(output);

    }

}
