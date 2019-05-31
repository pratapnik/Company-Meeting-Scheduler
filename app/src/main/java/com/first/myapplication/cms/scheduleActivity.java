package com.first.myapplication.cms;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class scheduleActivity extends AppCompatActivity{

    TextView date, start_time, end_time;
    EditText desc;
    SQLiteDatabase database;
    ContentValues values;
    MyHelper helper;
    String message;
    Button submit;
    ImageView backImage;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //ACTION BAR

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.schedule_custom_action_bar);

        date = findViewById(R.id.date);
        start_time = findViewById(R.id.start_time);
        end_time = findViewById(R.id.end_time);
        desc = findViewById(R.id.desc1);
        submit = findViewById(R.id.submit);
        backImage = findViewById(R.id.backImage);
        back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(scheduleActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(scheduleActivity.this,MainActivity.class);
                startActivity(in);
            }
        });

        //DATE
        Intent intent = getIntent();
        message = intent.getStringExtra("topdate");
        date.setText(message);



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

                DialogFragment newFragment = new TimePickerFragmentEndTime();
                newFragment.show(getSupportFragmentManager(), "timePicker");

            }
        });

        helper = new MyHelper(this);
        database = helper.getWritableDatabase();
        values  = new ContentValues();

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
        while(c.moveToNext()) {
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


                String st = start_time.getText().toString();
                String et = end_time.getText().toString();

                if(et.equals("End Time") || st.equals("Start Time")){
                    Toast.makeText(getApplicationContext(),"One of the field is empty", Toast.LENGTH_SHORT).show();


                }

                else{
                    String[] hourMin= st.split(":");
                    String[] hourMin2 = et.split(":");
                    int hour = Integer.parseInt(hourMin[0]);
                    int mins = Integer.parseInt(hourMin[1]);
                    int hour2 = Integer.parseInt(hourMin2[0]);
                    int mins2 = Integer.parseInt(hourMin2[1]);

                    Double strt = hour + 0.01*mins;
                    Double endt = hour2 + 0.01*mins2;

                    int flag=1;

                    if(cursor.moveToFirst()){
                        do{
                            // String date = c.getString(1);

                            Double start = cursor.getDouble(2);
                            Double end = cursor.getDouble(3);

                            if((start <= strt && strt <= end )|| (start <= endt && endt <= end )){
                                flag =0;
                                break;
                            }
                            else if(strt<= start && endt>=end){
                                flag =0;
                                break;
                            }
                            else{
                                flag =1;
                            }
                        }while(cursor.moveToNext());
                    }


                    if(flag==1){
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
                    }
                    else if(flag==0){
                        Toast.makeText(getApplicationContext(), "Timing Clash", Toast.LENGTH_SHORT).show();
                    }

                }

            }


        });


    }




}
