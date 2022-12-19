package com.example.coursework.database.models;

public class MachineWorkersModel {
    private int id;
    private int receiptId;
    private int medicineId;
    private int count;

    public MachineWorkersModel(){

    }

    public MachineWorkersModel(int receiptId, int medicineId, int count){
        this.receiptId = receiptId;
        this.medicineId = medicineId;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(int medicineId) {
        this.medicineId = medicineId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
