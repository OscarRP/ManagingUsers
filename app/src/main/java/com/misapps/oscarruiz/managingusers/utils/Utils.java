package com.misapps.oscarruiz.managingusers.utils;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TimePicker;

import com.misapps.oscarruiz.managingusers.interfaces.AppInterfaces;
import com.misapps.oscarruiz.managingusers.models.User;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Oscar Ruiz on 28/09/2017.
 */

public class Utils implements com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    /**
     * Formatted date
     */
    private String formattedDate;

    /**
     * Formatted time
     */
    private String formattedTime;

    /**
     * Set date listener
     */
    private AppInterfaces.ISetDate listener;

    /**
     * Set time listener
     */
    private AppInterfaces.ISetTime timeListener;

    /**
     * Context
     */
    private Activity activity;

    public Utils(Activity activity) {
        this.activity = activity;
    }

    /**
     * Method to show date picker dialog
     */
    public void setDate(AppInterfaces.ISetDate listener) {
        this.listener = listener;

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog = com.fourmob.datetimepicker.date.DatePickerDialog.newInstance(this, year, month, day);
        datePickerDialog.setYearRange(1930, 2020);
        datePickerDialog.show(((AppCompatActivity) activity).getSupportFragmentManager(), "Date Picker");
    }

    /**
     * Method to show time picker dialog
     */
    public void setTime(final AppInterfaces.ISetTime timeListener) {
        this.timeListener = timeListener;

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        com.sleepbot.datetimepicker.time.TimePickerDialog timePickerDialog = com.sleepbot.datetimepicker.time.TimePickerDialog.newInstance(new com.sleepbot.datetimepicker.time.TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hour, int minute) {
                //time selected string
                if (minute < 10) {
                    //add a "0" before minute
                    formattedTime = String.valueOf(hour)+":0"+String.valueOf(minute);
                } else {
                    formattedTime = String.valueOf(hour)+":"+String.valueOf(minute);
                }

               timeListener.setTime(formattedTime);
            }
        }, hour, minute, true);
        timePickerDialog.show(((AppCompatActivity) activity).getSupportFragmentManager(), "Time Picker");
    }

    @Override
    public void onDateSet(com.fourmob.datetimepicker.date.DatePickerDialog datePickerDialog, int year, int month, int day) {

        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        //date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = simpleDateFormat.format(c.getTime());

        listener.setDate(formattedDate);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
    }

    /**
     * Method to search users
     */
    public ArrayList<User> searchUsers (ArrayList<User> usersList, String text) {
        //init aux vars
        ArrayList<User> finalUserList = new ArrayList<>();
        String auxString;

        if (usersList != null) {
            //search coincidences trhough hole users list
            for (int i=0; i<usersList.size(); i++) {
                //check text contains any character
                if (text.length() > 0) {
                    if (usersList.get(i).getName().length() >= text.length()){
                        auxString = usersList.get(i).getName().substring(0, text.length()).toLowerCase();
                        if (text.equals(auxString)) {
                            finalUserList.add(usersList.get(i));
                        }
                    }
                } else {
                    finalUserList.add(usersList.get(i));
                }
            }
        }
        return finalUserList;
    }

    /**
     * Method to hide Keyboard
     */
    public void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
