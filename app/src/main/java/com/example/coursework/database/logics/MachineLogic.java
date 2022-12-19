package com.example.coursework.database.logics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coursework.database.DatabaseHelper;
import com.example.coursework.database.models.MachineWorkersModel;
import com.example.coursework.database.models.MachineModel;

import java.util.ArrayList;
import java.util.List;

public class MachineLogic {
    Context context;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    final String TABLE = "receipt";
    final String COLUMN_ID = "id";
    final String COLUMN_RECEIVING_DATE = "receiving_date";
    final String COLUMN_DISCHARGE_DATE = "discharge_date";
    final String COLUMN_SUPPLIER_ID = "supplier_id";
    final String COLUMN_SUPPLIER_NAME = "supplier_name";

    public MachineLogic(Context context) {
        sqlHelper = new DatabaseHelper(context);
        db = sqlHelper.getWritableDatabase();
        this.context = context;
    }

    public MachineLogic open() {
        db = sqlHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public List<MachineModel> getFullList() {
        Cursor cursor = db.rawQuery("select * from " + TABLE, null);
        List<MachineModel> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return list;
        }
        do {
            MachineModel obj = new MachineModel();
            int id = cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID));
            obj.setId(id);
            obj.setReceiving_date(cursor.getLong((int) cursor.getColumnIndex(COLUMN_RECEIVING_DATE)));
            obj.setDischarge_date(cursor.getLong((int) cursor.getColumnIndex(COLUMN_DISCHARGE_DATE)));
            obj.setSupplierId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_SUPPLIER_ID)));
            obj.setSupplierName(cursor.getString((int) cursor.getColumnIndex(COLUMN_SUPPLIER_NAME)));

            MachineWorkersLogic machineWorkersLogic = new MachineWorkersLogic(context);
            machineWorkersLogic.open();
            obj.setReceiptMedicines(machineWorkersLogic.getFilteredList(id));
            machineWorkersLogic.close();
            list.add(obj);
            cursor.moveToNext();
        } while (!cursor.isAfterLast());
        return list;
    }

    public List<MachineModel> getFilteredList(long dateFrom, long dateTo) {
        Cursor cursor = db.rawQuery("select * from " + TABLE + " where "
                + COLUMN_RECEIVING_DATE + " > " + dateFrom + " and " + COLUMN_RECEIVING_DATE + " < " + dateTo, null);
        List<MachineModel> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return list;
        }
        do {
            MachineModel obj = new MachineModel();
            int id = cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID));
            obj.setId(id);
            obj.setReceiving_date(cursor.getLong((int) cursor.getColumnIndex(COLUMN_RECEIVING_DATE)));
            obj.setDischarge_date(cursor.getLong((int) cursor.getColumnIndex(COLUMN_DISCHARGE_DATE)));
            obj.setSupplierId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_SUPPLIER_ID)));
            obj.setSupplierName(cursor.getString((int) cursor.getColumnIndex(COLUMN_SUPPLIER_NAME)));

            MachineWorkersLogic machineWorkersLogic = new MachineWorkersLogic(context);
            machineWorkersLogic.open();
            obj.setReceiptMedicines(machineWorkersLogic.getFilteredList(id));
            machineWorkersLogic.close();
            list.add(obj);
            cursor.moveToNext();
        } while (!cursor.isAfterLast());
        return list;
    }

    public MachineModel getElement(int id) {
        Cursor cursor = db.rawQuery("select * from " + TABLE + " where "
                + COLUMN_ID + " = " + id, null);
        MachineModel obj = new MachineModel();
        if (!cursor.moveToFirst()) {
            return null;
        }

        obj.setId(id);
        obj.setReceiving_date(cursor.getLong((int) cursor.getColumnIndex(COLUMN_RECEIVING_DATE)));
        obj.setDischarge_date(cursor.getLong((int) cursor.getColumnIndex(COLUMN_DISCHARGE_DATE)));
        obj.setSupplierId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_SUPPLIER_ID)));
        obj.setSupplierName(cursor.getString((int) cursor.getColumnIndex(COLUMN_SUPPLIER_NAME)));

        MachineWorkersLogic machineWorkersLogic = new MachineWorkersLogic(context);
        machineWorkersLogic.open();
        obj.setReceiptMedicines(machineWorkersLogic.getFilteredList(id));
        machineWorkersLogic.close();

        return obj;
    }

    public void insert(MachineModel model) {
        ContentValues content = new ContentValues();
        content.put(COLUMN_RECEIVING_DATE,model.getReceiving_date());
        content.put(COLUMN_DISCHARGE_DATE,model.getDischarge_date());
        content.put(COLUMN_SUPPLIER_ID,model.getSupplierId());
        content.put(COLUMN_SUPPLIER_NAME, model.getSupplierName());

        db.insert(TABLE,null,content);

        MachineWorkersLogic machineWorkersLogic = new MachineWorkersLogic(context);
        machineWorkersLogic.open();
        for(MachineWorkersModel machineWorkersModel : model.getReceiptMedicines()){
            machineWorkersLogic.insert(machineWorkersModel);
        }
        machineWorkersLogic.close();
    }

    public void update(MachineModel model) {
        ContentValues content = new ContentValues();
        content.put(COLUMN_RECEIVING_DATE,model.getReceiving_date());
        content.put(COLUMN_DISCHARGE_DATE,model.getDischarge_date());
        content.put(COLUMN_SUPPLIER_ID,model.getSupplierId());
        content.put(COLUMN_SUPPLIER_NAME,model.getSupplierName());
        String where = COLUMN_ID + " = " + model.getId();

        db.update(TABLE,content,where,null);

        MachineWorkersLogic machineWorkersLogic = new MachineWorkersLogic(context);
        machineWorkersLogic.open();
        machineWorkersLogic.deleteByReceiptId(model.getId());
        for(MachineWorkersModel machineWorkersModel : model.getReceiptMedicines()){
            machineWorkersLogic.insert(machineWorkersModel);
        }
        machineWorkersLogic.close();
    }

    public void delete(int id) {
        String where = COLUMN_ID+" = "+id;
        db.delete(TABLE,where,null);
        MachineWorkersLogic machineWorkersLogic = new MachineWorkersLogic(context);
        machineWorkersLogic.deleteByReceiptId(id);
    }

    public void deleteBySupplierId(int supplierId) {
        String where = COLUMN_SUPPLIER_ID +" = "+supplierId;
        db.delete(TABLE,where,null);
    }
}
