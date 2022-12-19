package com.example.coursework.database.models;

public class MachineWorkersModel {
    private int id;
    private int machineId;
    private int workerId;
    private int count;

    public MachineWorkersModel(){

    }

    public MachineWorkersModel(int machineId, int workerId, int count){
        this.machineId = machineId;
        this.workerId = workerId;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
