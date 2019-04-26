package com.xlip.pegasusflightchecker.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class MyDatePickerFragment extends DialogFragment {
    DatePickerCallback datePickerCallback;
    private Calendar calendar;

    public MyDatePickerFragment() {
        super();
        calendar = Calendar.getInstance();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
    }

    public void setDate(Calendar calendar) {
        this.calendar = calendar;
        datePickerCallback.onSet(dateToString());

    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void oneDayNext() {

        calendar.add(Calendar.DAY_OF_MONTH, 1);

        datePickerCallback.onSet(dateToString());

    }

    public void oneDayBefore() {

        calendar.add(Calendar.DAY_OF_MONTH, -1);

        datePickerCallback.onSet(dateToString());
    }

    public void setCalandarWithValues(int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            (DatePicker view, int year, int month, int day) -> {
                setCalandarWithValues(year, month, day);
                datePickerCallback.onSet(dateToString());
            };

    private String dateToString() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year + "-" + (month < 9 ? "0" : "") + (month + 1) + "-" + (day < 10 ? "0" : "") + day;
    }

    public DatePickerCallback getDatePickerCallback() {
        return datePickerCallback;
    }

    public void setDatePickerCallback(DatePickerCallback datePickerCallback) {
        this.datePickerCallback = datePickerCallback;
        datePickerCallback.onSet(dateToString());
    }

    public interface DatePickerCallback {
        void onSet(String date);
    }
}