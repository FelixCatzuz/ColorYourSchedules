package com.feliscatus909.coloryourschedules;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.feliscatus909.coloryourschedules.adapter.DBAdapter;
import com.feliscatus909.coloryourschedules.adapter.GridViewAdapter;
import com.feliscatus909.coloryourschedules.database.DBOpenHelper;
import com.feliscatus909.coloryourschedules.database.DBStructure;
import com.feliscatus909.coloryourschedules.models.Schedule;
import com.feliscatus909.coloryourschedules.view.ScheduleCalendarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ScheduleCalendarView scheduleCalendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleCalendarView = (ScheduleCalendarView) findViewById(R.id.schedule_calendar_view);

    }


}