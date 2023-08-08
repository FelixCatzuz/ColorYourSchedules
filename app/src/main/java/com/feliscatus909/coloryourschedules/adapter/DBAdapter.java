package com.feliscatus909.coloryourschedules.adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.feliscatus909.coloryourschedules.R;
import com.feliscatus909.coloryourschedules.database.DBOpenHelper;
import com.feliscatus909.coloryourschedules.database.DBStructure;
import com.feliscatus909.coloryourschedules.models.Schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBAdapter {
    private DBOpenHelper dbOpenHelper;
    private SQLiteDatabase sqLiteDatabase;

    public DBAdapter(Context context) {
        dbOpenHelper = new DBOpenHelper(context.getApplicationContext());
    }

    public DBAdapter open(){
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbOpenHelper.close();
    }
    private Cursor getData(){
        String[] columns = DBStructure.COLUMNS;
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        return sqLiteDatabase.query(DBStructure.TABLE_NAME, columns, null,
                null, null, null, null);
    }

    public List<Schedule> getSchedules(){
        ArrayList<Schedule> schedules = new ArrayList<>();
        Cursor cursor = getData();
        while (cursor.moveToNext()){
            @SuppressLint("Range") String schedule_variant = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_VARIANT));
            @SuppressLint("Range") String schedule_color = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_COLOR));
            @SuppressLint("Range") String schedule_date = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_DATE));
            @SuppressLint("Range") String schedule_month = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_MONTH));
            @SuppressLint("Range") String schedule_year = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_YEAR));
            schedules.add(new Schedule(schedule_variant, schedule_color, schedule_date, schedule_month, schedule_year));
        }
        cursor.close();
        return schedules;
    }

    @SuppressLint("Range")
    public ArrayList<String> getColors(){
        ArrayList<String> colors = new ArrayList<>();
        Cursor cursor = getData();
        while (cursor.moveToNext()){
            colors.add(cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_COLOR)));
        }
        cursor.close();
        return colors;
    }

    public long update(Schedule schedule){
        String whereClause = DBStructure.SCHEDULE_DATE + "=" + schedule.getSchedule_date();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.SCHEDULE_VARIANT, schedule.getSchedule_variant());
        contentValues.put(DBStructure.SCHEDULE_COLOR, schedule.getSchedule_color());
        contentValues.put(DBStructure.SCHEDULE_MONTH, schedule.getSchedule_month());
        contentValues.put(DBStructure.SCHEDULE_YEAR, schedule.getSchedule_year());

        return sqLiteDatabase.update(DBStructure.TABLE_NAME, contentValues, whereClause, null);
    }

/**
    public void delete(int scheduleDate){
       /*return sqLiteDatabase.delete(DBStructure.TABLE_NAME,
               DBStructure.SCHEDULE_DATE + " = ?",
               new String[]{scheduleDate});


        sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        sqLiteDatabase.delete(DBStructure.TABLE_NAME, DBStructure.SCHEDULE_DATE + " =?",
                new String[]{String.valueOf(scheduleDate)});
    }
*/


    @SuppressLint("Range")
    public ArrayList<View> getColoredSchedules(View view){
        ArrayList<View> coloredSchedules = new ArrayList<>();
        Cursor cursor = getData();
        while (cursor.moveToNext()){
            String c = cursor.getString(cursor.getColumnIndex(DBStructure.SCHEDULE_COLOR));
            if (c == "teal"){
                view.setBackgroundColor(view.getContext().getColor(R.color.teal_200));
                notifyAll();
            } else if (c == "purple") {
                view.setBackgroundColor(view.getContext().getColor(R.color.purple_200));
                notifyAll();
            }
            coloredSchedules.add(view);
        }
        cursor.close();
        return coloredSchedules;

    }

}
