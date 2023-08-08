package com.feliscatus909.coloryourschedules.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.feliscatus909.coloryourschedules.models.Schedule;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper {

    SQLiteDatabase database;
    private static final String CREATE_SCHEDULES_TABLE = "CREATE TABLE "
            + DBStructure.TABLE_NAME
            + " (_id INTEGER PRIMARY KEY, "
            + DBStructure.SCHEDULE_VARIANT + " TEXT, "
            + DBStructure.SCHEDULE_COLOR + " TEXT, "
            + DBStructure.SCHEDULE_DATE + " TEXT, "
            + DBStructure.SCHEDULE_MONTH + " TEXT, "
            + DBStructure.SCHEDULE_YEAR + " TEXT);";

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + DBStructure.TABLE_NAME;

    public DBOpenHelper(@Nullable Context context) {
        super(context, DBStructure.DB_NAME, null, DBStructure.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_SCHEDULES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void delete(String date, SQLiteDatabase database){
        String selection = DBStructure.SCHEDULE_DATE + "=?";
        String[] selectionArgs = {date};
        database.delete(DBStructure.TABLE_NAME, selection, selectionArgs);
    }

    public void deleteAll(SQLiteDatabase database){
        database.delete(DBStructure.TABLE_NAME, null, null);
    }


    public void saveSchedule(String variant, String color, String date, String month, String year,
                             SQLiteDatabase sqLiteDatabase){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.SCHEDULE_VARIANT, variant);
        contentValues.put(DBStructure.SCHEDULE_COLOR, color);
        contentValues.put(DBStructure.SCHEDULE_DATE, date);
        contentValues.put(DBStructure.SCHEDULE_MONTH, month);
        contentValues.put(DBStructure.SCHEDULE_YEAR, year);

        //sqLiteDatabase.beginTransaction();
        sqLiteDatabase.insert(DBStructure.TABLE_NAME, null, contentValues);
        //sqLiteDatabase.setTransactionSuccessful();
        //sqLiteDatabase.endTransaction();

        System.out.println("Schedule saved: " + sqLiteDatabase.isDatabaseIntegrityOk() + " Database path: "
        + sqLiteDatabase.getPath());

        System.out.println("Database size: " + sqLiteDatabase.getPageSize());
    }

    public Cursor readSchedules(String scheduleDate, SQLiteDatabase sqLiteDatabase){
        String[] projections = {DBStructure.SCHEDULE_VARIANT, DBStructure.SCHEDULE_COLOR,
                DBStructure.SCHEDULE_DATE, DBStructure.SCHEDULE_MONTH, DBStructure.SCHEDULE_YEAR};
        String selection = DBStructure.SCHEDULE_DATE + "=?";
        String[] selectionArgs = {scheduleDate};

        return sqLiteDatabase.query(DBStructure.TABLE_NAME, projections, selection, selectionArgs,
                null, null, null);
    }

    public Cursor readSchedulesPerMonth(String scheduleMonth, String scheduleYear,
                                        SQLiteDatabase sqLiteDatabase){
        String[] projections = {DBStructure.SCHEDULE_VARIANT, DBStructure.SCHEDULE_COLOR,
                DBStructure.SCHEDULE_DATE, DBStructure.SCHEDULE_MONTH, DBStructure.SCHEDULE_YEAR};
        String selection = DBStructure.SCHEDULE_DATE + "=? and " + DBStructure.SCHEDULE_YEAR + "=?";
        String[] selectionArgs = {scheduleMonth, scheduleYear};

        return sqLiteDatabase.query(DBStructure.TABLE_NAME, projections, selection, selectionArgs,
                null, null, null);
    }

}
