package com.example.todo;

import org.litepal.crud.LitePalSupport;

public class Matter extends LitePalSupport {

    private int     id;
    private String  title;
    private String  content;
    private int     year;
    private int     month;
    private int     day;
    private int     hour;
    private int     minute;
    private int     second;
    private int     importance;
    private boolean finished;

    public Matter(String initialTitle,
                  String initialContent,
                  int initialYear,
                  int initialMonth,
                  int initialDay,
                  int initialHour,
                  int initialMinute,
                  int initialSecond,
                  int initialImportance) {

        title = initialTitle;
        content = initialContent;
        importance = initialImportance;
        second = initialSecond;
        finished = false;
        year = initialYear;
        month = initialMonth;
        day = initialDay;
        hour = initialHour;
        minute = initialMinute;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Matter() {}


    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public void setFinished(boolean finished) {
        finished = false;
    }

    public boolean isFinished() {
        return finished;
    }

    public void cancel() {
        finished = true;
    }

}
