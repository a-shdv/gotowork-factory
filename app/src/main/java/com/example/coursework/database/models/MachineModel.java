package com.example.coursework.database.models;

import java.util.LinkedList;
import java.util.List;

public class MachineModel {
    private int id;
    private String shift_begin_date;
    private String shift_end_date;
    private int shiftId;
    private String shiftName;
    private List<MachineWorkersModel> machineWorkers = new LinkedList<>();

    public MachineModel() {

    }

    public MachineModel(String shift_begin_date, String shift_end_date, int shiftId, String shiftName,
                        List<MachineWorkersModel> machineWorkers) {
        this.shift_begin_date = shift_begin_date;
        this.shift_end_date = shift_end_date;
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

    public String getShift_begin_date() {
        return shift_begin_date;
    }

    public void setShift_begin_date(String shift_begin_date) {
        this.shift_begin_date = shift_begin_date;
    }

    public String getShift_end_date() {
        return shift_end_date;
    }

    public void setShift_end_date(String shift_end_date) {
        this.shift_end_date = shift_end_date;
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
