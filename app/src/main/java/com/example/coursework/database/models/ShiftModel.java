package com.example.coursework.database.models;

public class ShiftModel {
    private int id;
    private String type;
    private long date;
    private int userid;

    public ShiftModel(String type, long date, int userid){
        this.type = type;
        this.date = date;
        this.userid = userid;
    }

    public ShiftModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
