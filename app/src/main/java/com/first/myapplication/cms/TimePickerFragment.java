package com.first.myapplication.cms;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    TimePickerDialog dialog;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        dialog = new TimePickerDialog(getActivity(), R.style.TimePickerTheme, this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
        return dialog;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
         TextView tv = getActivity().findViewById(R.id.start_time);
         getActivity().findViewById(R.id.end_time).performClick();
         tv.setText(hourOfDay+":"+minute);


    }


}
