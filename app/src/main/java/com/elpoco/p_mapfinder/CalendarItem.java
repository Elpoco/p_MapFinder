package com.elpoco.p_mapfinder;

public class CalendarItem {
    private String day;
    private String name;
    private String icon;

    public CalendarItem(String day, String name,String icon) {
        this.day = day;
        this.name = name;
        this.icon = icon;
    }

    public String getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }
}
