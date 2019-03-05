package com.elpoco.p_mapfinder;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv;

    String serverUrl = "http://elpoco1.dothome.co.kr/loadData.php";

    ArrayList<CalendarItem> items = new ArrayList<>();
    CalendarAdapter adapter;

    String mDay, mTime;
    long now;
    Date date;
    timerThread timerThread;

    ProgressBar progressBar;
    int loadData=20,lastData;
    boolean isData=true, isFirst=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.calendar_rv);
        adapter = new CalendarAdapter(this, getLayoutInflater(), items);
        rv.setAdapter(adapter);

        progressBar=findViewById(R.id.calendar_progressbar);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)&&isFirst) {
                    isFirst=false;
                    if(!isData) return;
                    loading();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        timerThread = new timerThread();
        timerThread.start();

    }

    void loadData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(serverUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String day, time, name, cDay, cTime;
                    if(response.length()<20)loadData=response.length();
                    for (int i = 0; i < loadData; i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        day = jsonObject.getString("day");
                        time = jsonObject.getString("time");
                        name = jsonObject.getString("name");
                        mDay = mDay.replace("-", "");
                        cDay = day.replace("-", "");
                        mTime = mTime.replace(":", "");
                        cTime = time.replace(":", "");
                        if (mDay.compareTo(cDay) > 0) continue;
                        if (mDay.compareTo(cDay) == 0) if (mTime.compareTo(cTime) > 0) continue;
                        items.add(new CalendarItem(day, time, name));
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    void loading() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(serverUrl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    String day, time, name, cDay, cTime;
                    lastData=loadData+10;
                    if(response.length()<lastData) {lastData=response.length(); isData=false;}
                    for (int i = loadData; i < lastData; i++) {
                        JSONObject jsonObject = response.getJSONObject(i);

                        day = jsonObject.getString("day");
                        time = jsonObject.getString("time");
                        name = jsonObject.getString("name");
                        mDay = mDay.replace("-", "");
                        cDay = day.replace("-", "");
                        mTime = mTime.replace(":", "");
                        cTime = time.replace(":", "");
                        if (mDay.compareTo(cDay) > 0) continue;
                        if (mDay.compareTo(cDay) == 0) if (mTime.compareTo(cTime) > 0) continue;
                        items.add(new CalendarItem(day, time, name));
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        isFirst=true;
                        loadData=lastData;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    class timerThread extends Thread {
        boolean isRun = true;

        @Override
        public void run() {
            try {
                while (isRun) {
                    now = System.currentTimeMillis();
                    date = new Date(now);
                    mDay = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    mTime = new SimpleDateFormat("HH:mm").format(date);
                    loadData();
                    Thread.sleep(1000 * 60);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        timerThread.isRun = false;
        super.onStop();
    }

    public void clickLogo(View view) {
        finish();
    }
}
