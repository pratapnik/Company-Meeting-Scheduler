package com.first.myapplication.cms;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    TextView starttime, endtime;
    ImageView backImage;

    String message, dayOfWeek;
    Switch thirty, sixty;
    private final static int REQUEST_CODE_1 = 1;
    private static double interval;

    CheckBox monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    Button apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.settings_custom_action_bar);

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
                if(isChecked){
                    sixty.setChecked(false);
                    interval = 30.00;
                }
            }
        });

        sixty.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    thirty.setChecked(false);
                    interval = 60.00;
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
        Toast.makeText(getApplicationContext(), dayOfWeek, Toast.LENGTH_SHORT).show();


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, scheduleActivity.class);

                Bundle bundle = new Bundle();
                if(monday.isChecked()){
                      bundle.putString("monday", "Monday");
                }
                else{
                    bundle.putString("monday", "null");
                }
                if(tuesday.isChecked()){
                    bundle.putString("tuesday", "Tuesday");
                }
                else{
                    bundle.putString("tuesday", "null");
                }
                if(wednesday.isChecked()){
                    bundle.putString("wednesday", "Wednesday");
                }
                else{
                    bundle.putString("wednesday", "null");
                }
                if(thursday.isChecked()){
                    bundle.putString("thursday", "Thursday");
                }
                else{
                    bundle.putString("thursday", "null");
                }
                if(friday.isChecked()){
                    bundle.putString("friday", "Friday");
                }
                else{
                    bundle.putString("friday", "null");
                }
                if(saturday.isChecked()){
                    bundle.putString("saturday", "Saturday");
                }
                else{
                    bundle.putString("saturday", "null");
                }
                if(sunday.isChecked()){
                    bundle.putString("sunday", "Sunday");
                }
                else{
                    bundle.putString("sunday", "null");
                }
                i.putExtras(bundle);
                i.putExtra("topdate", message);
                i.putExtra("interval", interval);
                i.putExtra("day",dayOfWeek);
                i.putExtra("start", starttime.getText().toString());
                i.putExtra("end",endtime.getText().toString());
                i.putExtra("settings", "settings");
                startActivityForResult(i, REQUEST_CODE_1);
            }
        });
    }
}
