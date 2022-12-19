package com.example.coursework.database.models;

import java.util.LinkedList;
import java.util.List;

public class MachineModel {
    private int id;
    private long receiving_date;
    private long discharge_date;
    private int shiftId;
    private String shiftName;
    private List<MachineWorkersModel> machineWorkers = new LinkedList<>();

    public MachineModel(){

    }

    public MachineModel(long receiving_date, long discharge_date, int shiftId, String shiftName, List<MachineWorkersModel> machineWorkers){
        this.receiving_date = receiving_date;
        this.discharge_date = discharge_date;
        this.shiftId = shiftId;
        this.shiftName = shiftName;
        this.machineWorkers = machineWorkers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getReceiving_date() {
        return receiving_date;
    }

    public void setReceiving_date(long receiving_date) {
        this.receiving_date = receiving_date;
    }

    public long getDischarge_date() {
        return discharge_date;
    }

    public void setDischarge_date(long discharge_date) {
        this.discharge_date = discharge_date;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public List<MachineWorkersModel> getMachineWorkers() {
        return machineWorkers;
    }

    public void setMachineWorkers(List<MachineWorkersModel> machineWorkers) {
        this.machineWorkers = machineWorkers;
    }
}
