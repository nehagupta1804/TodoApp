package com.example.nehagupta.todoapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    long id;
    int request_code=0;
    AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView= findViewById(R.id.list_item);
        android.support.v7.widget.Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton button = findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,AddExpenseActivity.class);
                startActivityForResult(intent,ADD_REQUEST_CODE);

            }
        });

        ExpenseOpenHelper expenseOpenHelper=new ExpenseOpenHelper(this);
        SQLiteDatabase database=expenseOpenHelper.getReadableDatabase();
        Cursor cursor =  database.query(Contract.Expense.TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            String title = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_NAME));
            int amount = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_AMOUNT));
            String camera = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_CAMERA));
            String ram = cursor.getString(cursor.getColumnIndex(Contract.Expense.COLUMN_RAM));
            long date=cursor.getLong(cursor.getColumnIndex(Contract.Expense.COLUMN_DATE));
            int id = cursor.getInt(cursor.getColumnIndex(Contract.Expense.COLUMN_ID));
            Expense expense = new Expense(title, amount, camera, ram);
            expense.setDate(date);
            expense.setId(id);
            expenses.add(expense);
        }
        //SMS
        BroadcastReceiver receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                String message="";
                long date =0;
                if(bundle!=null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    String senderNumber = null;
                    for (int i = 0; i < pdus.length; i++) {
                        SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        senderNumber = sms.getOriginatingAddress();
                        message = sms.getDisplayMessageBody();
                        date = sms.getTimestampMillis();
                    }
                    String[] splited = message.split("\\s+");
                    String name=senderNumber;
                    String price=splited[0];
                    int amount = 10000;
                    String camera = "5 MP";
                    String ram = "32 GB";
                    try {
                        amount = Integer.parseInt(price);
                    } catch (NumberFormatException e) {
                        camera = price;
                    }
                    if (splited.length > 1) {
                        camera = splited[1];
                    }
                    if (splited.length > 2) {
                        ram = splited[2];
                    }
                    Expense expense = new Expense(name, amount, camera, ram);
                    expense.setDate(date);
                    ExpenseOpenHelper e = new ExpenseOpenHelper(MainActivity.this);
                    SQLiteDatabase db= e.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(Contract.Expense.COLUMN_NAME, expense.getName());
                    contentValues.put(Contract.Expense.COLUMN_AMOUNT, expense.getAmount());
                    contentValues.put(Contract.Expense.COLUMN_CAMERA, expense.getCamera());
                    contentValues.put(Contract.Expense.COLUMN_RAM, expense.getRam());
                    contentValues.put(Contract.Expense.COLUMN_DATE, expense.getDate());
                    long id = db.insert(Contract.Expense.TABLE_NAME, null, contentValues);
                    if (id > -1) {
                        expense.setId(id);
                        expenses.add(expense);
                        adapter.notifyDataSetChanged();
                    }

                }


            }
        };
        IntentFilter intentFilter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver, intentFilter) ;

     /*  String selectedText="";
        Intent intent = getIntent();
        String action=intent.getAction();
        String type=intent.getType();
        if(type!=null && action!=null) {
            String n = intent.getStringExtra("name");
            int a = intent.getIntExtra("price", 0);
            String c = intent.getStringExtra("camera");
            String r = intent.getStringExtra("ram");
            Long d = intent.getLongExtra("date", 0);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                selectedText = intent.getClipData().getItemAt(0).getText().toString();
            }
            // if (!n.equals("") && !c.equals("") && !r.equals("") && a!=-1 && !d.equals("")) {
            Expense expense = new Expense(selectedText, a, c, r);
            expense.setDate(d);
            ExpenseOpenHelper e = new ExpenseOpenHelper(this);
            SQLiteDatabase db= e.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(Contract.Expense.COLUMN_NAME, expense.getName());
            contentValues.put(Contract.Expense.COLUMN_AMOUNT, expense.getAmount());
            contentValues.put(Contract.Expense.COLUMN_CAMERA, expense.getCamera());
            contentValues.put(Contract.Expense.COLUMN_RAM, expense.getRam());
            contentValues.put(Contract.Expense.COLUMN_DATE, expense.getDate());
            long id = db.insert(Contract.Expense.TABLE_NAME, null, contentValues);
            if (id > -1) {
                expense.setId(id);
                expenses.add(expense);

            }

        }*/

        adapter = new ExpenseAdapter(getApplicationContext(),expenses, new ExpenseItemClickListener() {
            @Override
            public void rowButtonClicked(View view, int i) {
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
                        expenses.remove(position);
                        adapter.notifyDataSetChanged();

                    }
                });
                AlertDialog dialog= builder.create();
                dialog.show();
            }
        });
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

                        final Expense expense=expenses.get(position);
                        final int nextPos=position+1;
                        ExpenseOpenHelper openHelper=new ExpenseOpenHelper(MainActivity.this);
                        SQLiteDatabase database= openHelper.getWritableDatabase();
                        final long id = expense.getId();
                        String[] selectionargs = {id + ""};
                        int result = database.delete(Contract.Expense.TABLE_NAME,Contract.Expense.COLUMN_ID + " = ? ",selectionargs);
                        if (result == 1) {
                            expenses.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                        Snackbar.make(listView, "1 item deleted", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ExpenseOpenHelper openHelper=new ExpenseOpenHelper(MainActivity.this);
                                        SQLiteDatabase database= openHelper.getWritableDatabase();
                                        //long id = expense.getId();
                                        //String[] selectionargs = {id + ""};
                                        String name=expense.getName();
                                        int amount=expense.getAmount();
                                        String camera=expense.getCamera();
                                        String ram=expense.getRam();
                                        long date=expense.getDate();
                                        ContentValues contentValues=new ContentValues();
                                        contentValues.put(Contract.Expense.COLUMN_ID,id);
                                        contentValues.put(Contract.Expense.COLUMN_NAME,name);
                                        contentValues.put(Contract.Expense.COLUMN_AMOUNT,amount);
                                        contentValues.put(Contract.Expense.COLUMN_CAMERA,camera);
                                        contentValues.put(Contract.Expense.COLUMN_RAM,ram);
                                        contentValues.put(Contract.Expense.COLUMN_DATE,date);
                                        long m=database.insert(Contract.Expense.TABLE_NAME,null,contentValues);
                                        Expense e=new Expense(name,amount,camera,ram);
                                        e.setDate(date);
                                        e.setId(id);
                                        if(position>=expenses.size())
                                        {
                                            expenses.add(e);
                                        }
                                        else {
                                            expenses.add(e);
                                            for(int i=expenses.size()-1;i> position;i--)
                                            {

                                                expenses.set(i,expenses.get(i-1));
                                            }
                                           expenses.set(position,e);
                                        }
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(MainActivity.this,"Item Restored", Toast.LENGTH_LONG).show();
                                    }
                                }).show();

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
        int g = 8;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent=new Intent(this,DescribeActivity.class);
        position=i;
        Expense expense = expenses.get(i);
        intent.putExtra("id",expense.getId());
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
                        long date=cursor.getLong(cursor.getColumnIndex(Contract.Expense.COLUMN_DATE));
                        long id=cursor.getLong(cursor.getColumnIndex(Contract.Expense.COLUMN_ID));
                        Expense expense=new Expense(title,amount,camera,ram);
                        expense.setId(id);
                        expense.setDate(date);
                        expenses.set(position, expense);
                        adapter.notifyDataSetChanged();
                    }
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
                    long id = data.getLongExtra("dbresult_id", 0);
                    long expenseDate = data.getLongExtra("date",0);
                    if (!name.equals("") && !camera.equals("")&& !ram.equals("")&& amount != -1 && expenseDate > 0) {
                        request_code++;
                        Expense expense = new Expense(name, amount,camera,ram);
                        expense.setDate(expenseDate);
                        alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
                        Intent intent=new Intent(this,MyReceiver.class);
                        intent.putExtra("id_alarm",id);
                        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,request_code,intent,0);
                        Long currenttime=System.currentTimeMillis();
                        alarmManager.set(AlarmManager.RTC_WAKEUP,expenseDate,pendingIntent);
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
        final SearchView searchView=(SearchView)MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager=(SearchManager)getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.name)
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
                    return Integer.compare(s1.getAmount(),s2.getAmount());
                }
            });
            adapter.notifyDataSetChanged();

        }
        else if(id == R.id.date)
        {
            Collections.sort(expenses, new Comparator<Expense>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public int  compare(Expense s1, Expense s2) {
                    return Long.compare(s1.getDate(),s2.getDate());
                }
            });
            adapter.notifyDataSetChanged();

        }
        else if(id==R.id.feebback)
        {
                Intent intent =new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                Uri uri = Uri.parse("mailto:gupta.neha1804@gmail.com");
                intent.setData(uri);
                startActivity(intent);
        }
        else if(id==R.id.aboutus)
        {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri=Uri.parse("https://codingninjas.in");
                intent.setData(uri);
                startActivity(intent);
        }
        else if(id==R.id.permit)
        {

            startActivity(new Intent(this,Settings.class));


        }
        else if(id==R.id.dial) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == (PackageManager.PERMISSION_GRANTED)) {
                call("9953247671");
            } else {
                String[] Permissions = {Manifest.permission.CALL_PHONE};
                ActivityCompat.requestPermissions(this, Permissions, 2);
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            int callGrantResult=grantResults[0];
            if(callGrantResult==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(this,"Grant Permission",Toast.LENGTH_LONG).show();
            }

        }
    }

    public void call(String call)
    {
        Intent intent=new Intent();
        intent.setAction(intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + call);
        intent.setData(uri);
        startActivity(intent);
    }


}
