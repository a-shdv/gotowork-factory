package com.example.coursework.database.models;

import java.util.LinkedList;
import java.util.List;

public class MachineModel {
    private int id;
    private String machine_type;
    private String shift_begin_time;
    private String shift_end_time;
    private int shiftId;
    private String shiftName;
    private List<MachineWorkersModel> machineWorkers = new LinkedList<>();

    public MachineModel() {

    }

    public MachineModel(String machine_type, String shift_begin_time, String shift_end_time, int shiftId, String shiftName,
                        List<MachineWorkersModel> machineWorkers) {
        this.machine_type = machine_type;
        this.shift_begin_time = shift_begin_time;
        this.shift_end_time = shift_end_time;
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

    public String getMachine_type() {
        return machine_type;
    }

    public void setMachine_type(String machine_type) {
        this.machine_type = machine_type;
    }

    public String getShift_begin_time() {
        return shift_begin_time;
    }

    public void setShift_begin_time(String shift_begin_time) {
        this.shift_begin_time = shift_begin_time;
    }

    public String getShift_end_time() {
        return shift_end_time;
    }

    public void setShift_end_time(String shift_end_time) {
        this.shift_end_time = shift_end_time;
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

/*    @Override
    public String toString() {
        for (int i = 0; i < machineWorkers.size(); i++) {
            machineWorkers.get(i).;
        };
        return "";
    }*/
}
