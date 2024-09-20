package com.mirea.elizarovnm.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class MyTimeDialogFragment extends DialogFragment {

    private TimeSelectedListener timeSelectedListener;

    public void setTimeSelectedListener(TimeSelectedListener listener) {
        this.timeSelectedListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(requireActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time= hourOfDay + " : " + minute;
                        if (timeSelectedListener != null){
                            timeSelectedListener.onTimeSelected(time);
                        }
                    }
                }, Calendar.HOUR_OF_DAY, Calendar.MINUTE, false);
    }

}
