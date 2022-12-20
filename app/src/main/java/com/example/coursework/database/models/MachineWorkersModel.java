package com.example.coursework.database.models;

public class MachineWorkersModel {
    private int id;
    private int machineId;
    private int workerId;
    private String workerName;
    private int hours;

    public MachineWorkersModel() {

    }

    public MachineWorkersModel(int machineId, int workerId, String workerName, int hours) {
        this.machineId = machineId;
        this.workerId = workerId;
        this.workerName = workerName;
        this.hours = hours;
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

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    @Override
    public String toString() {
        return workerName;
    }
}
