package com.example.coursework.database.models;

public class ShiftModel {
    private int id;
    private String type;
    private long date;
    private int bossid;

    public ShiftModel(String type, long date, int bossid){
        this.type = type;
        this.date = date;
        this.bossid = bossid;
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

    public int getBossid() {
        return bossid;
    }

    public void setBossid(int bossid) {
        this.bossid = bossid;
    }
}
