package com.example.nehagupta.todoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddExpenseActivity extends AppCompatActivity {

    EditText nameEditText;
    EditText amountEditText;
    EditText cameraEditText;
    EditText ramEditText;

    public static final int ADD_RESULT_CODE=7789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
    }
    public void startActivityResult(View view)
    {
        nameEditText = findViewById(R.id.name);
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
        //if(name1!=null && amount1!=null)
        setResult(ADD_RESULT_CODE,data);
        finish();
    }
}
