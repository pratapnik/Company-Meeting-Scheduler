package com.first.myapplication.cms;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

//    private static final String URL_DATA = "https://api.simplifiedcoding.in/course-api/movies";
    Calendar cal = Calendar.getInstance();
    Date c = new Date(cal.getTimeInMillis());
    SimpleDateFormat df = new SimpleDateFormat("dd%2FMM%2Fyyyy", Locale.ENGLISH);
    String formattedDate= df.format(c);
    private String URL_DATA = "http://fathomless-shelf-5846.herokuapp.com/api/schedule?date="+formattedDate;

   // private final String URL_DATA = "http://fathomless-shelf-5846.herokuapp.com/api/schedule?date=\"7/8/2015\" "+formattedDate;
//recycler view
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItems;

    //intent data
    private final static int REQUEST_CODE_1 = 1;
    String fdate;

   //actionbar items
    TextView topDate;
    TextView next, prev;
    ImageView nextImage, prevImage;

    //database
    SQLiteDatabase database;
    ContentValues values;
    MyHelper helper;

    //swipe to refresh
    SwipeRefreshLayout swipeRefreshLayout;

    //floating  action button
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

//action bar items
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        fdate = sdf.format(c);
        topDate = findViewById(R.id.topdate);
        topDate.setText(fdate);

        next = findViewById(R.id.next);
        nextImage = findViewById(R.id.nextImage);
        prev = findViewById(R.id.prev);
        prevImage = findViewById(R.id.prevImage);

        //database
        helper = new MyHelper(this);
        database = helper.getWritableDatabase();
        values  = new ContentValues();

        //floating action button
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
            }
        });


        //recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        loadRecyclerViewData();



        //swiperefresh

//        swipeRefreshLayout = findViewById(R.id.pullToRefresh);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                listItems.clear();
//                loadRecyclerViewData();
//                adapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
                listItems.clear();
                loadRecyclerViewData();

            }
        });

        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
                listItems.clear();
                loadRecyclerViewData();

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prev();
                listItems.clear();
                loadRecyclerViewData();

            }
        });

        prevImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prev();
                listItems.clear();
                loadRecyclerViewData();

            }
        });

    }

    private void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data....");
        progressDialog.show();

        //fetching data from the server

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Log.d("RRRRRR", "TTTTT > response > " + response);
                        progressDialog.dismiss();
                        try {


                            JSONArray array = new JSONArray(response);

                            for(int i=0; i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                ListItem item = new ListItem(
                                        o.getString("start_time"),
                                        o.getString("end_time"),
                                        o.getString("description")
                                );
                                listItems.add(item);
                                String[] hourMin= o.getString("start_time").split(":");
                                String[] hourMin2 = o.getString("end_time").split(":");
                                int hour = Integer.parseInt(hourMin[0]);
                                int mins = Integer.parseInt(hourMin[1]);
                                int hour2 = Integer.parseInt(hourMin2[0]);
                                int mins2 = Integer.parseInt(hourMin2[1]);

                                Double strt = hour + 0.01*mins;
                                Double endt = hour2 + 0.01*mins2;
                                Cursor cursor = database.rawQuery("SELECT * FROM MEETINGS WHERE DATE=?", new String[]{formattedDate});
//                                int x = cursor.getCount();
                                Date cd = new Date(cal.getTimeInMillis());

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                String d = simpleDateFormat.format(cd);

                                values.put("DATE", d);
                                values.put("START", strt);
                                values.put("ENDTIME", endt);
                                values.put("DESCRIPTION", o.getString("description"));

                                database.insert("MEETINGS", null,values);



//                                else{
//                                    Toast.makeText(getApplicationContext(),"Nothing inserted",Toast.LENGTH_SHORT).show();
//                                }


                            }

                            adapter = new MyAdapter(listItems, getApplicationContext());
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"error error",Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    public void schedule(View v){

        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String cc= df.format(date);
        String[] hourMin= fdate.split("-");
        String[] hourMin2 = cc.split("-");
        int day = Integer.parseInt(hourMin[0]);
        int month = Integer.parseInt(hourMin[1]);
        int day2 = Integer.parseInt(hourMin2[0]);
        int month2 = Integer.parseInt(hourMin2[1]);

        Intent i = new Intent(getApplicationContext(), scheduleActivity.class);
        if(day < day2 && month<=month2 ){
            Toast.makeText(getApplicationContext(), "Past date not allowed", Toast.LENGTH_SHORT).show();
        }
        else{
            i.putExtra("topdate",fdate);
            startActivityForResult(i,REQUEST_CODE_1);
        }


    }

    private void prev(){
            cal.add(Calendar.DATE, -1);
            Date c = new Date(cal.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        fdate = sdf.format(c);
        topDate.setText(fdate);
        formattedDate = fdate;
        URL_DATA = "http://fathomless-shelf-5846.herokuapp.com/api/schedule?date="+formattedDate;

    }

    private void next(){
        cal.add(Calendar.DATE, 1);
        Date c = new Date(cal.getTimeInMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        fdate = sdf.format(c);
        topDate.setText(fdate);
        formattedDate = fdate;
        URL_DATA = "http://fathomless-shelf-5846.herokuapp.com/api/schedule?date="+formattedDate;
    }

}
