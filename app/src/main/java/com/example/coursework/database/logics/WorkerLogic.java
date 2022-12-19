package com.example.coursework.database.logics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coursework.database.DatabaseHelper;
import com.example.coursework.database.models.WorkerModel;

import java.util.ArrayList;
import java.util.List;

public class WorkerLogic {
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    final String TABLE = "worker";
    final String COLUMN_ID = "id";
    final String COLUMN_NAME = "name";
    final String COLUMN_SALARY = "salary";

    public WorkerLogic(Context context) {
        sqlHelper = new DatabaseHelper(context);
        db = sqlHelper.getWritableDatabase();
    }

    public WorkerLogic open() {
        db = sqlHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public List<WorkerModel> getFullList() {
        Cursor cursor = db.rawQuery("select * from " + TABLE, null);
        List<WorkerModel> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return list;
        }
        do {
            WorkerModel obj = new WorkerModel();

            obj.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
            obj.setName(cursor.getString((int) cursor.getColumnIndex(COLUMN_NAME)));
            obj.setSalary(cursor.getInt((int) cursor.getColumnIndex(COLUMN_SALARY)));

            list.add(obj);
            cursor.moveToNext();
        } while (!cursor.isAfterLast());
        return list;
    }

    public WorkerModel getElement(int id) {
        Cursor cursor = db.rawQuery("select * from " + TABLE + " where "
                + COLUMN_ID + " = " + id, null);
        WorkerModel obj = new WorkerModel();
        if (!cursor.moveToFirst()) {
            return null;
        }

        obj.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
        obj.setName(cursor.getString((int) cursor.getColumnIndex(COLUMN_NAME)));
        obj.setSalary(cursor.getInt((int) cursor.getColumnIndex(COLUMN_SALARY)));

        return obj;
    }

    public void insert(WorkerModel model) {
        ContentValues content = new ContentValues();
        content.put(COLUMN_NAME,model.getName());
        content.put(COLUMN_SALARY,model.getSalary());
        db.insert(TABLE,null,content);
    }

    public void update(WorkerModel model) {
        ContentValues content=new ContentValues();
        content.put(COLUMN_NAME,model.getName());
        content.put(COLUMN_SALARY,model.getSalary());
        String where = COLUMN_ID + " = " + model.getId();
        db.update(TABLE,content,where,null);
    }

    public void delete(int id) {
        String where = COLUMN_ID+" = "+id;
        db.delete(TABLE,where,null);
    }
}
