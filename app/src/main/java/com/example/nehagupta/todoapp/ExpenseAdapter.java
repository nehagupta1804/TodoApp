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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseAdapter extends ArrayAdapter {

    ArrayList<Expense> items;
    LayoutInflater inflater;
    Context context;
    ExpenseItemClickListener clickListener;

    public ExpenseAdapter(@NonNull Context context,  ArrayList<Expense> items,ExpenseItemClickListener clickListener) {
        super(context, 0,items);
        this.items = items;
        this.context=context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        this.clickListener=clickListener;
    }
    public int getCount()
    {
        return(items.size());
    }
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View output = convertView;
        if(output==null) {
             output = inflater.inflate(R.layout.expense_row_layout, parent, false);
            TextView nameTextView = output.findViewById(R.id.expenseName);
            TextView amountTextView = output.findViewById(R.id.expenseAmount);
            TextView dateTextView =output.findViewById(R.id.pick);
            Button button = output.findViewById(R.id.deleteButton);

           // TextView tv = output.findViewById(R.id.pick);
            ExpenseViewHolder viewHolder = new ExpenseViewHolder();
            viewHolder.title = nameTextView;
            viewHolder.pick=dateTextView;
            viewHolder.amount = amountTextView;
            viewHolder.btn=button;
            //viewHolder.pick = tv;
            output.setTag(viewHolder);
        }
        ExpenseViewHolder viewHolder =(ExpenseViewHolder)output.getTag();
        Expense expense = items.get(position);
        viewHolder.title.setText(expense.getName());
        viewHolder.amount.setText(expense.getAmount()+"Rs");
        viewHolder.btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                clickListener.rowButtonClicked(view, position);
                return (true);
            }
        });
        long date = expense.getDate();
        Date d = new Date(date);
        SimpleDateFormat sdf = new SimpleDateFormat();
        String expenseDate = sdf.format(d);
        viewHolder.pick.setText(expenseDate);

       // viewHolder.pick.setText("");
        return(output);

    }

}
