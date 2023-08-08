package com.feliscatus909.coloryourschedules.view;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.feliscatus909.coloryourschedules.R;
import com.feliscatus909.coloryourschedules.adapter.DBAdapter;
import com.feliscatus909.coloryourschedules.adapter.GridViewAdapter;
import com.feliscatus909.coloryourschedules.adapter.ScheduleRecyclerAdapter;
import com.feliscatus909.coloryourschedules.database.DBOpenHelper;
import com.feliscatus909.coloryourschedules.database.DBStructure;
import com.feliscatus909.coloryourschedules.models.Schedule;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ScheduleCalendarView extends LinearLayout {

    ImageButton imageButton_back;
    ImageButton imageButton_forward;
    ImageButton imageButton_deleteAll;
    TextView textView_currentMonth;
    GridView gridView_calendar;

    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;
    DBOpenHelper helper;
    GridViewAdapter adapter;
    AlertDialog alertDialog;

    String scheduleVariant;
    String scheduleColor;

    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    SimpleDateFormat scheduleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public static List<Schedule> scheduleList = new ArrayList<>();
    List<Date> scheduleDateList = new ArrayList<>();

    ArrayList<Schedule> coloredSchedules = new ArrayList<>();

    List<String> colorList = new ArrayList<>();

    public static final int MAX_CALENDAR_DAYS = 42;


    public ScheduleCalendarView(Context context) {
        super(context);
        initLayout();
    }

    public ScheduleCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initLayout();
        setupCalendar();

        //Previews month
        imageButton_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                setupCalendar();
            }
        });

        //Next month
        imageButton_forward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                setupCalendar();
            }
        });


        gridView_calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);

                //New schedule view
                View addView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.new_schedule_view, null);
                Button button_day = addView.findViewById(R.id.button_day);
                Button button_night = addView.findViewById(R.id.button_night);
                CheckBox checkBox_color1 = addView.findViewById(R.id.checkBox_color1);
                CheckBox checkBox_color2 = addView.findViewById(R.id.checkBox_color2);
                Button button_add = addView.findViewById(R.id.button_add);


                button_day.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scheduleVariant = "day";
                    }
                });

                button_night.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scheduleVariant = "night";
                    }
                });

                checkBox_color1.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scheduleColor = "teal";
                    }
                });

                checkBox_color2.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scheduleColor = "purple";
                    }
                });


                String scheduleDate = scheduleDateFormat.format(scheduleDateList.get(position));
                String scheduleMonth = monthFormat.format(scheduleDateList.get(position));
                String scheduleYear = yearFormat.format(scheduleDateList.get(position));

                //Add new schedule
                button_add.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveSchedule(scheduleVariant, scheduleColor, scheduleDate, scheduleMonth,
                                scheduleYear);
                        //scheduleList.add()
                        System.out.println("Variant: " + scheduleVariant + " Color: " + scheduleColor
                        + " Date: " + scheduleDateList.get(position)
                                + " ID: " + scheduleList.indexOf(position)
                        + " Is array empty: " + String.valueOf(scheduleList.isEmpty())
                        + " ArraySize: " + scheduleList.size());
                        setupCalendar();
                        alertDialog.dismiss();
                    }
                });
                builder.setView(addView);
                alertDialog = builder.create();
                alertDialog.show();


            }
        });

        //Delete focused schedule
        gridView_calendar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("Range")
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                View delView = LayoutInflater.from(context).inflate(R.layout.show_schedules_view, null);
                String date = scheduleDateFormat.format(scheduleDateList.get(i));
                RecyclerView recyclerView = delView.findViewById(R.id.recyclerView_schedules);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(delView.getContext());
                recyclerView.setLayoutManager(layoutManager);
                ScheduleRecyclerAdapter recyclerAdapter = new ScheduleRecyclerAdapter(collectSchedules(date), context);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();

                recyclerAdapter.deleteSchedule(date);
                scheduleDateList.remove(i);
                recyclerAdapter.notifyDataSetChanged();
                setupCalendar();
                return true;
            }
        });

    }


    public ScheduleCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
    }

    private void initLayout() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.schedule_calendar_view, this);
        imageButton_back = view.findViewById(R.id.imageButton_back);
        imageButton_forward = view.findViewById(R.id.imageButton_forward);
        imageButton_deleteAll = view.findViewById(R.id.imageButton_deleteAll);
        textView_currentMonth = view.findViewById(R.id.textView_currentMonth);
        gridView_calendar = view.findViewById(R.id.gridView_calendar);

        //Delete all schedules
        imageButton_deleteAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.clearDB();
                scheduleDateList.clear();
                adapter.notifyDataSetChanged();
                setupCalendar();
                Toast.makeText(context, R.string.toast_db_clear, Toast.LENGTH_SHORT).show();
            }
        });

        setupCalendar();

    }

    private void setupCalendar() {
        String currentDate = dateFormat.format(calendar.getTime());
        textView_currentMonth.setText(currentDate);
        scheduleDateList.clear();
        Calendar monthScheduleCalendar = (Calendar) calendar.clone();
        monthScheduleCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthScheduleCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        monthScheduleCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        //download data from database
        collectSchedulesPerMonth(monthFormat.format(calendar.getTime()), yearFormat.format(calendar.getTime()));
        collectSchedules(scheduleDateFormat.format(calendar.getFirstDayOfWeek()));
        //

        while (scheduleDateList.size() < MAX_CALENDAR_DAYS) {
            scheduleDateList.add(monthScheduleCalendar.getTime());
            monthScheduleCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        adapter = new GridViewAdapter(context, calendar, scheduleDateList, scheduleList);
        gridView_calendar.setAdapter(adapter);

        System.out.println("Schedule array size: " + scheduleList.size()
                + " Is array empty:" + scheduleList.isEmpty());

    }

    private void saveSchedule(String scheduleVariant, String scheduleColor, String scheduleDate,
                              String scheduleMonth, String scheduleYear) {
        helper = new DBOpenHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        helper.saveSchedule(scheduleVariant, scheduleColor, scheduleDate,
                scheduleMonth, scheduleYear, database);
        scheduleList.add(new Schedule(scheduleVariant, scheduleColor, scheduleDate,
                scheduleMonth, scheduleYear));
        colorList.add(scheduleColor);
        helper.close();
        Toast.makeText(context, R.string.toast_complete, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("Range")
    private void collectSchedulesPerMonth(String month, String year){
        helper = new DBOpenHelper(context);
        SQLiteDatabase database = helper.getReadableDatabase();
        Cursor cursor = helper.readSchedulesPerMonth(month, year, database);
        while (cursor.moveToNext()){
            @SuppressLint("Range") String schedule_variant = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_VARIANT));
            @SuppressLint("Range") String schedule_color = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_COLOR));
            @SuppressLint("Range") String schedule_date = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_DATE));
            @SuppressLint("Range") String schedule_month = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_MONTH));
            @SuppressLint("Range") String schedule_year = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_YEAR));
            Schedule schedule1 = new Schedule(schedule_variant, schedule_color, schedule_date, schedule_month, schedule_year);
            scheduleList.add(schedule1);
        }
        cursor.close();
        helper.close();
    }

    //color schedules in db
   private ArrayList<Schedule> collectSchedules(String date){
        DBOpenHelper db = new DBOpenHelper(context);
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = db.readSchedules(date, database);

        for (int i = calendar.getFirstDayOfWeek(); i < 365; i++) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String schedule_variant = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_VARIANT));
                @SuppressLint("Range") String schedule_color = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_COLOR));
                @SuppressLint("Range") String schedule_date = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_DATE));
                @SuppressLint("Range") String schedule_month = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_MONTH));
                @SuppressLint("Range") String schedule_year = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_YEAR));
                Schedule schedule = new Schedule(schedule_variant, schedule_color, schedule_date, schedule_month, schedule_year);
                coloredSchedules.add(schedule);

                ContentValues contentValues = new ContentValues();
                contentValues.put(DBStructure.SCHEDULE_VARIANT, schedule_variant);
                contentValues.put(DBStructure.SCHEDULE_COLOR, schedule_color);
                contentValues.put(DBStructure.SCHEDULE_DATE, schedule_date);
                contentValues.put(DBStructure.SCHEDULE_MONTH, schedule_month);
                contentValues.put(DBStructure.SCHEDULE_YEAR, schedule_year);
                database.insert(DBStructure.TABLE_NAME, null, contentValues);
            }
        }
        cursor.close();
        helper.close();

        return coloredSchedules;
    }

}
