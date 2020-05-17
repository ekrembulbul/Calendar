package com.example.donemodev;

import java.io.Serializable;

public class MyEvent implements Serializable {
    public String name;
    public String description;
    public int startDay, startMonth, startYear, startHour, startMinute;
    public int endDay, endMonth, endYear, endHour, endMinute;
    public int alarmDay, alarmMonth, alarmYear, alarmHour, alarmMinute;
    public int frequencyId;
    String place;

    MyEvent(
            String name,
            String description,
            int startDay, int startMonth, int startYear, int startHour, int startMinute,
            int endDay, int endMonth, int endYear, int endHour, int endMinute,
            int alarmDay, int alarmMonth, int alarmYear, int alarmHour, int alarmMinute,
            int frequencyId,
            String place
    ) {
        this.name = name;
        this.description = description;
        this.startDay = startDay;
        this.startMonth = startMonth;
        this.startYear = startYear;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endDay = endDay;
        this.endMonth = endMonth;
        this.endYear = endYear;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.alarmDay = alarmDay;
        this.alarmMonth = alarmMonth;
        this.alarmYear = alarmYear;
        this.alarmHour = alarmHour;
        this.alarmMinute = alarmMinute;
        this.frequencyId = frequencyId;
        this.place = place;
    }
}
