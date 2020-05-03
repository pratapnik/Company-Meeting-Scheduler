package com.first.myapplication.cms;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    TextView starttime, endtime;
    ImageView backImage;

    String message, dayOfWeek;
    Switch thirty, sixty;
    private final static int REQUEST_CODE_1 = 1;
    //    private static double interval;
    String interval;
    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    Button apply;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.settings_custom_action_bar);

        //SHARED PREFERENCES
        mPreferences = getSharedPreferences("IDValue", 0);
        mEditor = mPreferences.edit();

        backImage = findViewById(R.id.backImage);

        thirty = findViewById(R.id.thirty);
        sixty = findViewById(R.id.sixty);

        starttime = findViewById(R.id.start_time);
        endtime = findViewById(R.id.end_time);

        apply = findViewById(R.id.apply);

        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.super.onBackPressed();
            }
        });

        thirty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sixty.setChecked(false);
                    interval = "thirty";
                }
            }
        });

        sixty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    thirty.setChecked(false);
                    interval = "sixty";
                }
            }
        });

        //START TIME & END TIME
        starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");


            }
        });
        endtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragmentEndTime();
                newFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });

        Intent intent = getIntent();
        message = intent.getStringExtra("topdate");
        dayOfWeek = intent.getStringExtra("day");

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, MainActivity.class);

                if (monday.isChecked()) {
                    mEditor.putString("monday", "Monday");

                } else {
                    mEditor.putString("monday", "null");
                }
                if (tuesday.isChecked()) {
                    mEditor.putString("tuesday", "Tuesday");
                } else {
                    mEditor.putString("tuesday", "null");
                }
                if (wednesday.isChecked()) {
                    mEditor.putString("wednesday", "Wednesday");
                } else {
                    mEditor.putString("wednesday", "null");
                }
                if (thursday.isChecked()) {
                    mEditor.putString("thursday", "Thursday");
                } else {
                    mEditor.putString("thursday", "null");
                }
                if (friday.isChecked()) {
                    mEditor.putString("friday", "Friday");
                } else {
                    mEditor.putString("friday", "null");
                }
                if (saturday.isChecked()) {
                    mEditor.putString("saturday", "Saturday");
                } else {
                    mEditor.putString("saturday", "null");
                }
                if (sunday.isChecked()) {
                    mEditor.putString("sunday", "Sunday");
                } else {
                    mEditor.putString("sunday", "null");
                }

                //i.putExtra("topdate", message);
                mEditor.putString("interval", interval);
                mEditor.putString("day", dayOfWeek);
                mEditor.putString("start", starttime.getText().toString());
                mEditor.putString("end", endtime.getText().toString());
                // mEditor.putString("settings", "settings");
                mEditor.commit();
                String name = mPreferences.getString("start", "null");
                Log.v("Start TIme", name);
                startActivity(i);

            }
        });
    }
}
