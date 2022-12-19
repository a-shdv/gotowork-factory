package com.example.coursework.database.logics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coursework.database.DatabaseHelper;
import com.example.coursework.database.models.ShiftModel;

import java.util.ArrayList;
import java.util.List;

public class ShiftLogic {
    Context context;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    final String TABLE = "shift";
    final String COLUMN_ID = "id";
    final String COLUMN_NAME = "name";
    final String COLUMN_SHIFT_DATE = "shift_date";
    final String COLUMN_USERID = "user_id";

    public ShiftLogic(Context context) {
        this.context = context;
        sqlHelper = new DatabaseHelper(context);
        db = sqlHelper.getWritableDatabase();
    }

    public ShiftLogic open() {
        db = sqlHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public List<ShiftModel> getFullList() {
        Cursor cursor = db.rawQuery("select * from " + TABLE, null);
        List<ShiftModel> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return list;
        }
        do {
            ShiftModel obj = new ShiftModel();

            obj.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
            obj.setName(cursor.getString((int) cursor.getColumnIndex(COLUMN_NAME)));
            obj.setContractExecution(cursor.getLong((int) cursor.getColumnIndex(COLUMN_SHIFT_DATE)));
            obj.setUserid(cursor.getInt((int) cursor.getColumnIndex(COLUMN_USERID)));

            list.add(obj);
            cursor.moveToNext();
        } while (!cursor.isAfterLast());
        return list;
    }

    public List<ShiftModel> getFilteredList(int userId) {
        Cursor cursor = db.rawQuery("select * from " + TABLE + " where "
                + COLUMN_USERID + " = " + userId, null);
        List<ShiftModel> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return list;
        }
        do {
            ShiftModel obj = new ShiftModel();

            obj.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
            obj.setName(cursor.getString((int) cursor.getColumnIndex(COLUMN_NAME)));
            obj.setContractExecution(cursor.getLong((int) cursor.getColumnIndex(COLUMN_SHIFT_DATE)));
            obj.setUserid(cursor.getInt((int) cursor.getColumnIndex(COLUMN_USERID)));

            list.add(obj);
            cursor.moveToNext();
        } while (!cursor.isAfterLast());
        return list;
    }

    public ShiftModel getElement(int id) {
        Cursor cursor = db.rawQuery("select * from " + TABLE + " where "
                + COLUMN_ID + " = " + id, null);
        ShiftModel obj = new ShiftModel();
        if (!cursor.moveToFirst()) {
            return null;
        }
        obj.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
        obj.setName(cursor.getString((int) cursor.getColumnIndex(COLUMN_NAME)));
        obj.setContractExecution(cursor.getLong((int) cursor.getColumnIndex(COLUMN_SHIFT_DATE)));
        obj.setUserid(cursor.getInt((int) cursor.getColumnIndex(COLUMN_USERID)));
        return obj;
    }

    public void insert(ShiftModel model) {
        ContentValues content = new ContentValues();
        content.put(COLUMN_NAME,model.getName());
        content.put(COLUMN_SHIFT_DATE,model.getContractExecution());
        content.put(COLUMN_USERID,model.getUserid());
        db.insert(TABLE,null,content);
    }

    public void update(ShiftModel model) {
        ContentValues content=new ContentValues();
        content.put(COLUMN_NAME,model.getName());
        content.put(COLUMN_SHIFT_DATE,model.getContractExecution());
        content.put(COLUMN_USERID,model.getUserid());
        String where = COLUMN_ID + " = " + model.getId();
        db.update(TABLE,content,where,null);
    }

    public void delete(int id) {
        String where = COLUMN_ID+" = "+id;
        db.delete(TABLE,where,null);
        MachineLogic machineLogic = new MachineLogic(context);
        machineLogic.deleteByShiftId(id);
    }
    public void deleteByUserId(int userId) {
        String where = COLUMN_USERID+" = "+userId;
        db.delete(TABLE,where,null);
    }

}
