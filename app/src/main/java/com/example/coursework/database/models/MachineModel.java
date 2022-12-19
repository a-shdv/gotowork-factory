package com.example.coursework.database.models;

import java.util.LinkedList;
import java.util.List;

public class MachineModel {
    private int id;
    private long receiving_date;
    private long discharge_date;
    private int supplierId;
    private String supplierName;
    private List<MachineWorkersModel> receiptMedicines = new LinkedList<>();

    public MachineModel(){

    }

    public MachineModel(long receiving_date, long discharge_date, int supplierId, String supplierName, List<MachineWorkersModel> receiptMedicines){
        this.receiving_date = receiving_date;
        this.discharge_date = discharge_date;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.receiptMedicines = receiptMedicines;
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

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public List<MachineWorkersModel> getReceiptMedicines() {
        return receiptMedicines;
    }

    public void setReceiptMedicines(List<MachineWorkersModel> receiptMedicines) {
        this.receiptMedicines = receiptMedicines;
    }
}
