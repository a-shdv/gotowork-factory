package com.example.coursework.database.models;

public class WorkerModel {
    private int id;
    private String name;
    private String type;
    private float salary;

    public WorkerModel(String name, float salary){
        this.name = name;
        this.type = type;
        this.salary = salary;
    }

    public WorkerModel(){

    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
