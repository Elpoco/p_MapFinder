package com.elpoco.p_mapfinder;

public class CalendarItem {
    private String day;
    private String time;
    private String name;

    public CalendarItem(String day, String time, String name) {
        this.day = day;
        this.time = time;
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
