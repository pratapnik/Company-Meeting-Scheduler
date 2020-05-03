package com.first.myapplication.cms;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class scheduleActivity extends AppCompatActivity {

    TextView date, start_time, end_time;
    EditText desc;
    SQLiteDatabase database;
    ContentValues values;
    MyHelper helper;
    String message, dayOfWeek, lowLimit, highLimit;
    Button submit;
    ImageView backImage;
    Bundle bundle;
    String[] days;
    String interval, checkInterval;
    SharedPreferences mPreferences;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //ACTION BAR

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.schedule_custom_action_bar);

        //SHARED PREFERENCES
        mPreferences = getSharedPreferences("IDValue", 0);


        date = findViewById(R.id.date);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        desc = findViewById(R.id.desc1);
        submit = findViewById(R.id.submit);
        backImage = findViewById(R.id.backImage);

        desc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handled = true;
                    submit.performClick();
                }

                return handled;
            }
        });


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleActivity.super.onBackPressed();
            }
        });

        //DATE
        Intent intent = getIntent();
        message = intent.getStringExtra("topdate");
        dayOfWeek = intent.getStringExtra("day");
        date.setText(message);


        //checkboxes
        days = new String[7];
        days[0] = mPreferences.getString("monday", "null");
        days[1] = mPreferences.getString("tuesday", "null");
        days[2] = mPreferences.getString("wednesday", "null");
        days[3] = mPreferences.getString("thursday", "null");
        days[4] = mPreferences.getString("friday", "null");
        days[5] = mPreferences.getString("saturday", "null");
        days[6] = mPreferences.getString("sunday", "null");
        lowLimit = mPreferences.getString("start", "null");
        highLimit = mPreferences.getString("end", null);
        interval = mPreferences.getString("interval", "null");

        //START TIME & END TIME
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");


            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerScheduleEndTime();
                newFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });

        helper = new MyHelper(this);
        database = helper.getWritableDatabase();
        values = new ContentValues();

//        values.put("DATE", message);
//        values.put("START", 0.0);
//        values.put("ENDTIME", 0.0);
//        values.put("DESCRIPTION", "Development team of Deal**y project will do a KT session of the last sprint.");
////        values.put("PARTICIPANTS", "\"Prashant Lehri\"," +
////                "      \"Jatin Makkar\"," +
////                "      \"Sumit Arora\"," +
////                "      \"Rajeev Kakkar\"");
////
//        database.insert("MEETINGS", null, values);

        Cursor c = database.rawQuery("SELECT * FROM MEETINGS WHERE DATE=?", new String[]{message});

//        if(c!=null){
//            c.moveToFirst();
//        }

        StringBuilder builder = new StringBuilder();
        while (c.moveToNext()) {
            // Toast.makeText(getApplicationContext(),"congrats",Toast.LENGTH_SHORT).show();
            String date = c.getString(1);
            double start = c.getDouble(2);
//                if(date.equals(message)){
            builder.append(date + " " + start + "\n");
        }

//         Toast.makeText(getApplicationContext(), builder.toString(), Toast.LENGTH_LONG).show();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = database.rawQuery("SELECT * FROM MEETINGS WHERE DATE=?", new String[]{message});

//                if(cursor!=null){
//                    cursor.moveToFirst();
//                }

                //for textview start and end time
                String st = start_time.getText().toString();
                String et = end_time.getText().toString();

                String[] hourMin = st.split(":");
                String[] hourMin2 = et.split(":");
                if (et.equals("End Time") || st.equals("Start Time")) {
                    Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_SHORT).show();
                    return;

                }

                int hour = Integer.parseInt(hourMin[0]);
                int mins = Integer.parseInt(hourMin[1]);
                int hour2 = Integer.parseInt(hourMin2[0]);
                int mins2 = Integer.parseInt(hourMin2[1]);

                Double strt = hour + 0.01 * mins;
                Double endt = hour2 + 0.01 * mins2;


                String set = "settings";

                if (set != null) {


                    String[] hMin = lowLimit.split(":");
                    String[] hMin2 = highLimit.split(":");
                    int h = Integer.parseInt(hMin[0]);
                    int m = Integer.parseInt(hMin[1]);
                    int h2 = Integer.parseInt(hMin2[0]);
                    int m2 = Integer.parseInt(hMin2[1]);

                    Double s = h + 0.01 * m;
                    Double e = h2 + 0.01 * m2;
                    int hourDiff = hour2 - hour;
                    int minDiff = Math.abs(mins2 - mins);
                    Log.v("strt", "strt= " + strt);
                    Log.v("endt", "endt= " + endt);
                    Log.v("start mins", "mins" + mins);

                    if (hourDiff == 0 && minDiff == 30) {
                        checkInterval = "thirty";
                    } else if (hourDiff != 0 && minDiff == 30) {
                        checkInterval = "thirty";
                    } else if (hourDiff == 1 && minDiff == 0) {
                        checkInterval = "sixty";
                    } else {
                        checkInterval = "null";
                    }


                    int k = 0;
                    for (int j = 0; j < 7; j++) {
                        if (days[j].equals(dayOfWeek)) {
                            k = 1;
                            break;
                        }
                    }

                    if (k == 0) {
                        Toast.makeText(getApplicationContext(), "Date should be from working days", Toast.LENGTH_SHORT).show();
                    } else {
                        if (et.equals("End Time") || st.equals("Start Time")) {
                            Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_SHORT).show();


                        }

//                else if(diff != interval){
//                    Toast.makeText(getApplicationContext(), "Slot interval should be "+interval+" minutes", Toast.LENGTH_SHORT).show();
//
//                }
                        else {

                            if (strt < s || endt < s || strt > e || endt > e) {
                                Toast.makeText(getApplicationContext(), "Meeting should be between office timings", Toast.LENGTH_SHORT).show();
                            } else {

                                if (!interval.equals(checkInterval)) {
                                    Toast.makeText(getApplicationContext(), "Time Slot should match interval", Toast.LENGTH_SHORT).show();
                                } else {
                                    int flag = 1;

                                    if (cursor.moveToFirst()) {
                                        do {
                                            // String date = c.getString(1);

                                            Double start = cursor.getDouble(2);
                                            Double end = cursor.getDouble(3);

                                            if ((start <= strt && strt <= end) || (start <= endt && endt <= end)) {
                                                flag = 0;
                                                break;
                                            } else if (strt <= start && endt >= end) {
                                                flag = 0;
                                                break;
                                            } else {
                                                flag = 1;
                                            }
                                        } while (cursor.moveToNext());
                                    }


                                    if (flag == 1) {
                                        values.put("DATE", message);
                                        values.put("START", strt);
                                        values.put("ENDTIME", endt);
                                        values.put("DESCRIPTION", desc.getText().toString());
//        values.put("PARTICIPANTS", "\"Prashant Lehri\"," +
//                "      \"Jatin Makkar\"," +
//                "      \"Sumit Arora\"," +
//                "      \"Rajeev Kakkar\"");

                                        database.insert("MEETINGS", null, values);
                                        Toast.makeText(getApplicationContext(), "Successfully Scheduled", Toast.LENGTH_SHORT).show();
                                    } else if (flag == 0) {
                                        Toast.makeText(getApplicationContext(), "Timing Clash", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }


                        }
                    }

                } else {
                    if (et.equals("End Time") || st.equals("Start Time")) {
                        Toast.makeText(getApplicationContext(), "One of the field is empty", Toast.LENGTH_SHORT).show();


                    } else {
                        int flag = 1;

                        if (cursor.moveToFirst()) {
                            do {
                                // String date = c.getString(1);

                                Double start = cursor.getDouble(2);
                                Double end = cursor.getDouble(3);

                                if ((start <= strt && strt <= end) || (start <= endt && endt <= end)) {
                                    flag = 0;
                                    break;
                                } else if (strt <= start && endt >= end) {
                                    flag = 0;
                                    break;
                                } else {
                                    flag = 1;
                                }
                            } while (cursor.moveToNext());
                        }


                        if (flag == 1) {
                            values.put("DATE", message);
                            values.put("START", strt);
                            values.put("ENDTIME", endt);
                            values.put("DESCRIPTION", desc.getText().toString());
//        values.put("PARTICIPANTS", "\"Prashant Lehri\"," +
//                "      \"Jatin Makkar\"," +
//                "      \"Sumit Arora\"," +
//                "      \"Rajeev Kakkar\"");

                            database.insert("MEETINGS", null, values);
                            Toast.makeText(getApplicationContext(), "Successfully Scheduled", Toast.LENGTH_SHORT).show();
                        } else if (flag == 0) {
                            Toast.makeText(getApplicationContext(), "Timing Clash", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }


        });


    }


}
