package com.feliscatus909.coloryourschedules.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.feliscatus909.coloryourschedules.R;
import com.feliscatus909.coloryourschedules.database.DBOpenHelper;
import com.feliscatus909.coloryourschedules.models.Schedule;

import java.util.ArrayList;

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.ScheduleViewHolder> {
    ArrayList<Schedule> arrayList = new ArrayList<>();
    Context context;

    public ScheduleRecyclerAdapter(ArrayList<Schedule> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_cell_layout, null);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = arrayList.get(position);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void deleteSchedule(String scheduleDate){
        DBOpenHelper helper = new DBOpenHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        helper.delete(scheduleDate, database);
        helper.close();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
