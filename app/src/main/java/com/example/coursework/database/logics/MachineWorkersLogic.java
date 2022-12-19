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

public class MachineWorkersLogic {
    Context context;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    final String TABLE = "machine_workers";
    final String COLUMN_ID = "id";
    final String COLUMN_MACHINE_ID = "machine_id";
    final String COLUMN_WORKER_ID = "worker_id";
    final String COLUMN_HOURS = "hours";

    public MachineWorkersLogic(Context context) {
        this.context = context;
        sqlHelper = new DatabaseHelper(context);
        db = sqlHelper.getWritableDatabase();
    }

    public MachineWorkersLogic open() {
        db = sqlHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public List<MachineWorkersModel> getFullList() {
        Cursor cursor = db.rawQuery("select * from " + TABLE, null);
        List<MachineWorkersModel> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return list;
        }
        do {
            MachineWorkersModel obj = new MachineWorkersModel();

            obj.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
            obj.setMachineId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_MACHINE_ID)));
            obj.setWorkerId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_WORKER_ID)));
            obj.setHours(cursor.getInt((int) cursor.getColumnIndex(COLUMN_HOURS)));

            list.add(obj);
            cursor.moveToNext();
        } while (!cursor.isAfterLast());
        return list;
    }

    public List<MachineWorkersModel> getFilteredList(int machineId) {
        Cursor cursor = db.rawQuery("select * from " + TABLE + " where "
                + COLUMN_MACHINE_ID + " = " + machineId, null);
        List<MachineWorkersModel> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return list;
        }
        do {
            MachineWorkersModel obj = new MachineWorkersModel();

            obj.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
            obj.setMachineId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_MACHINE_ID)));
            obj.setWorkerId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_WORKER_ID)));
            obj.setHours(cursor.getInt((int) cursor.getColumnIndex(COLUMN_HOURS)));

            list.add(obj);
            cursor.moveToNext();
        } while (!cursor.isAfterLast());
        return list;
    }

    public MachineWorkersModel getElement(int id) {
        Cursor cursor = db.rawQuery("select * from " + TABLE + " where "
                + COLUMN_ID + " = " + id, null);

        MachineWorkersModel obj = new MachineWorkersModel();

        if (!cursor.moveToFirst()) {
            return null;
        }

        obj.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
        obj.setMachineId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_MACHINE_ID)));
        obj.setWorkerId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_WORKER_ID)));
        obj.setHours(cursor.getInt((int) cursor.getColumnIndex(COLUMN_HOURS)));

        return obj;
    }

    public void insert(MachineWorkersModel model) {
        MachineLogic machineLogic = new MachineLogic(context);
        List<MachineModel> machines = machineLogic.getFullList();
        ContentValues content = new ContentValues();
        if (model.getMachineId() == 0) {
            content.put(COLUMN_MACHINE_ID, machines.get(machines.size() - 1).getId());
        } else {
            content.put(COLUMN_MACHINE_ID, model.getMachineId());
        }
        content.put(COLUMN_WORKER_ID, model.getWorkerId());
        content.put(COLUMN_HOURS, model.getHours());
        db.insert(TABLE, null, content);
    }

    public void update(MachineWorkersModel model) {
        ContentValues content = new ContentValues();
        content.put(COLUMN_MACHINE_ID, model.getMachineId());
        content.put(COLUMN_WORKER_ID, model.getWorkerId());
        content.put(COLUMN_HOURS, model.getHours());
        String where = COLUMN_ID + " = " + model.getId();
        db.update(TABLE, content, where, null);
    }

    public void delete(int id) {
        String where = COLUMN_ID + " = " + id;
        db.delete(TABLE, where, null);
    }

    public void deleteByMachineId(int machineId) {
        String where = COLUMN_MACHINE_ID + " = " + machineId;
        db.delete(TABLE, where, null);
    }
}
