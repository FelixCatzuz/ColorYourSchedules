package com.feliscatus909.coloryourschedules.models;

public class Schedule {
    public String schedule_variant = "variant";
    public String schedule_color = "color";
    public String schedule_date = "date";
    public String schedule_month = "month";
    public String schedule_year = "year";

    public Schedule(String schedule_variant, String schedule_color, String schedule_date,
                    String schedule_month, String schedule_year) {
        this.schedule_variant = schedule_variant;
        this.schedule_color = schedule_color;
        this.schedule_date = schedule_date;
        this.schedule_month = schedule_month;
        this.schedule_year = schedule_year;
    }

    public String getSchedule_variant() {
        return schedule_variant;
    }

    public void setSchedule_variant(String schedule_variant) {
        this.schedule_variant = schedule_variant;
    }

    public String getSchedule_color() {
        return schedule_color;
    }

    public void setSchedule_color(String schedule_color) {
        this.schedule_color = schedule_color;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public String getSchedule_month() {
        return schedule_month;
    }

    public void setSchedule_month(String schedule_month) {
        this.schedule_month = schedule_month;
    }

    public String getSchedule_year() {
        return schedule_year;
    }

    public void setSchedule_year(String schedule_year) {
        this.schedule_year = schedule_year;
    }
}
