package com.elpoco.p_mapfinder;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv;

    String serverUrl ="http://m.inven.co.kr/lostark/timer/";

    Document doc;
    Elements conTime,conName, conIcon;

    ArrayList<CalendarItem> items = new ArrayList<>();
    CalendarAdapter adapter;
    ProgressBar progressBar;
    timerThread timerThread;

    AdView adView;

    boolean isData;

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
        adView=findViewById(R.id.adView);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        timerThread = new timerThread();
        timerThread.start();

    }

    class timerThread extends Thread {
        boolean isRun = true;

        @Override
        public void run() {
            try {
                while (isRun) {
                    new AsyncTask(){
                        @SuppressLint("WrongThread")
                        @Override
                        protected Object doInBackground(Object[] objects) {
                            try {
                                doc = Jsoup.connect(serverUrl).get();
                                conTime=doc.select("span.gentime");
                                conName = doc.select("span.npcname");
                                conIcon =doc.select("img.npcicon");
                                loadData();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            super.onPostExecute(o);
                            if(isData) {
                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    }.execute();
                    if(!isData)Thread.sleep(1000 * 60);
                    else {
                        Thread.sleep(1000*5);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void loadData() {
        int i=0;
        if(conTime!=null) {
            isData=true;
            items.clear();
            String icon;
            String[] iconName;
            for (Element element : conTime) {
                icon=conIcon.get(i).attr("src");
                iconName=icon.split("/");
                items.add(new CalendarItem(element.text(), conName.get(i).text(), iconName[iconName.length-1]));
                i += 1;
            }
            isData=true;
        } else {
            isData=false;
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
