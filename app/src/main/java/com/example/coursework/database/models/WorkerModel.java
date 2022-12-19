package com.example.coursework.database.models;

public class WorkerModel {
    private int id;
    private String name;
    private String type;
    private float price_per_package;

    public WorkerModel(String name, float price_per_package){
        this.name = name;
        this.type = type;
        this.price_per_package = price_per_package;
    }

    public WorkerModel(){

    }

    public float getPrice_per_package() {
        return price_per_package;
    }

    public void setPrice_per_package(float price_per_package) {
        this.price_per_package = price_per_package;
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
