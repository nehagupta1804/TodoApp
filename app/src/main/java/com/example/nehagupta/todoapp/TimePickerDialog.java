package com.example.nehagupta.todoapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;
 public  class TimePickerDialog extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final Calendar c =  Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return(new android.app.TimePickerDialog(getActivity(),this,hour,minute, DateFormat.is24HourFormat(getActivity())));
    }
    public void onTimeSet(TimePicker view,int hourOfDay,int minute)
    {

    }


}
