package com.feliscatus909.coloryourschedules.database;

public class DBStructure {
    public static final String DB_NAME = "SCHEDULES_DB";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "SchedulesTable";
    public static final String SCHEDULE_VARIANT = "schedule_variant";
    public static final String SCHEDULE_COLOR = "schedule_color";
    public static final String SCHEDULE_DATE = "date";
    public static final String SCHEDULE_MONTH = "month";
    public static final String SCHEDULE_YEAR = "year";

    public static final String[] COLUMNS = {
            SCHEDULE_VARIANT,
            SCHEDULE_COLOR,
            SCHEDULE_DATE,
            SCHEDULE_MONTH,
            SCHEDULE_YEAR
    };
}
