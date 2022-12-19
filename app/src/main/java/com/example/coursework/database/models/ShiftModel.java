package com.example.coursework.database.models;

public class ShiftModel {
    private int id;
    private String name;
    private long contractExecution;
    private int userid;

    public ShiftModel(String name, long contractExecution, int userid){
        this.name = name;
        this.contractExecution = contractExecution;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getContractExecution() {
        return contractExecution;
    }

    public void setContractExecution(long contractExecution) {
        this.contractExecution = contractExecution;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
