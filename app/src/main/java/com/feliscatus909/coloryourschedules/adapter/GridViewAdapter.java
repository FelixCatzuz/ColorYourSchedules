package com.feliscatus909.coloryourschedules.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.feliscatus909.coloryourschedules.R;
import com.feliscatus909.coloryourschedules.database.DBOpenHelper;
import com.feliscatus909.coloryourschedules.models.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GridViewAdapter extends ArrayAdapter {
    List<Schedule> schedules;
    List<Date> dates;
    Calendar currentDay;
    LayoutInflater inflater;
    TextView textView_calendarDay;
    TextView textView_schedule;

    DBAdapter dbAdapter;


    public GridViewAdapter(@NonNull Context context, Calendar currentDay,
                           List<Date> dates, List<Schedule> schedules) {
        super(context, R.layout.single_cell_layout);
        this.currentDay = currentDay;
        this.dates = dates;
        this.schedules = schedules;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public int getPosition(@Nullable Object item) {
        return dates.indexOf(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //For dates of current month in calendar view
        Date monthDay = dates.get(position);
        Calendar datesCalendar = Calendar.getInstance();
        datesCalendar.setTime(monthDay);
        int day_num = datesCalendar.get(Calendar.DAY_OF_MONTH);
        int display_month = datesCalendar.get(Calendar.MONTH) + 1;
        int display_year = datesCalendar.get(Calendar.YEAR);
        int current_month = datesCalendar.get(Calendar.MONTH) + 1;
        int current_year = datesCalendar.get(Calendar.YEAR);


        View view = convertView;

        if (view == null){
            view = inflater.inflate(R.layout.single_cell_layout, null);
        }


        //TextView for each day of current month
        textView_calendarDay = view.findViewById(R.id.textView_calendarDay);
        textView_calendarDay.setText(String.valueOf(day_num));

        //TextView for schedule variant (day/night) in each day
        textView_schedule = view.findViewById(R.id.textView_schedule);

        //Set text color for days of current month
        if (current_month != display_month && current_year != display_year){
            textView_calendarDay.setTextColor(getContext().getColor(R.color.gray));
        }

        ArrayList<String> arrayList = new ArrayList<>();
        //ArrayList<Integer> colors = new ArrayList<>();


        Calendar scheduleCalendar = Calendar.getInstance();


        //download data from database
        dbAdapter = new DBAdapter(getContext().getApplicationContext());
        dbAdapter.open();
        schedules = dbAdapter.getSchedules();
        //colors = dbAdapter.getColors();

        for (int i = 0; i < schedules.size(); i++){
            scheduleCalendar.setTime(convertStringToDate(schedules.get(i).getSchedule_date()));
            if (day_num == scheduleCalendar.get(Calendar.DAY_OF_MONTH)
            && display_month == scheduleCalendar.get(Calendar.MONTH) + 1
            && display_year == scheduleCalendar.get(Calendar.YEAR)){
                arrayList.add(schedules.get(i).getSchedule_variant());
                textView_schedule.setText(schedules.get(i).getSchedule_variant());

                //Set backgrounds to variants of colors from checkBoxes

                int color;

                switch (schedules.get(i).getSchedule_color()){

                    case "purple":
                        color = getContext().getColor(R.color.purple_200);
                        break;
                    default:
                        color = getContext().getColor(R.color.teal_200);
                }

                view.setBackgroundColor(color);
                System.out.println("Color saved AFTER CONDITION");

            }

        }


        dbAdapter.close();

        return view;
    }

    private Date convertStringToDate(String scheduleDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = null;
        try{
            date = format.parse(scheduleDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public void clearDB(){
        DBOpenHelper helper = new DBOpenHelper(getContext().getApplicationContext());
        SQLiteDatabase database = helper.getWritableDatabase();
        helper.deleteAll(database);
        helper.close();
    }


}
