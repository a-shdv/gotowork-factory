package com.example.coursework.database.logics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.coursework.database.DatabaseHelper;
import com.example.coursework.database.models.BossModel;

import java.util.ArrayList;
import java.util.List;

public class BossLogic {
    Context context;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    final String TABLE = "boss";
    final String COLUMN_ID = "id";
    final String COLUMN_LOGIN = "login";
    final String COLUMN_PASSWORD = "password";

    public BossLogic(Context context) {
        this.context = context;
        sqlHelper = new DatabaseHelper(context);
        db = sqlHelper.getWritableDatabase();
    }

    public BossLogic open() {
        db = sqlHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public List<BossModel> getFullList() {
        Cursor cursor = db.rawQuery("select * from " + TABLE, null);
        List<BossModel> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return list;
        }
        do {
            BossModel obj = new BossModel();

            obj.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
            obj.setLogin(cursor.getString((int) cursor.getColumnIndex(COLUMN_LOGIN)));
            obj.setPassword(cursor.getString((int) cursor.getColumnIndex(COLUMN_PASSWORD)));

            list.add(obj);
            cursor.moveToNext();
        } while (!cursor.isAfterLast());
        return list;
    }

    public BossModel getElement(BossModel model) {
        Cursor cursor = db.rawQuery("select * from " + TABLE + " where "
                + COLUMN_ID + " = " + model.getId(), null);
        BossModel obj = new BossModel();
        if (!cursor.moveToFirst()) {
            return null;
        }
        obj.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
        obj.setLogin(cursor.getString((int) cursor.getColumnIndex(COLUMN_LOGIN)));
        obj.setPassword(cursor.getString((int) cursor.getColumnIndex(COLUMN_PASSWORD)));
        return obj;
    }

    public void insert(BossModel model) {
        ContentValues content = new ContentValues();
        content.put(COLUMN_LOGIN,model.getLogin());
        content.put(COLUMN_PASSWORD,model.getPassword());
        db.insert(TABLE,null,content);
    }

    public void update(BossModel model) {
        ContentValues content=new ContentValues();
        content.put(COLUMN_LOGIN,model.getLogin());
        content.put(COLUMN_PASSWORD,model.getPassword());
        String where = COLUMN_ID + " = " + model.getId();
        db.update(TABLE,content,where,null);
    }

    public void delete(BossModel model) {
        String where = COLUMN_ID+" = "+model.getId();
        db.delete(TABLE,where,null);
        ShiftLogic shiftLogic = new ShiftLogic(context);
        shiftLogic.deleteByBossId(model.getId());
    }
}
