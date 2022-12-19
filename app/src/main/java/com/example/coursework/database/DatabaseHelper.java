package com.example.coursework.database;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "GoToWork.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS user (\n" +
                "    id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "    login character(100) NOT NULL,\n" +
                "    password character(100) NOT NULL);\n");

        db.execSQL("CREATE TABLE supplier (\n" +
                "    id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "    name character(100) NOT NULL,\n" +
                "    contract_execution long NOT NULL,\n" +
                "    user_id integer NOT NULL,\n" +
                "    CONSTRAINT user_fk FOREIGN KEY (user_id)\n" +
                "    REFERENCES user(id) ON DELETE CASCADE);");

        db.execSQL("CREATE TABLE receipt (" +
                "    id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "    receiving_date long NOT NULL,\n" +
                "    discharge_date long NOT NULL,\n" +
                "    supplier_id integer NOT NULL,\n" +
                "    supplier_name character(100) NOT NULL,\n" +
                "    CONSTRAINT supplier_fk FOREIGN KEY (supplier_id)\n" +
                "    REFERENCES supplier(id) ON DELETE CASCADE);");

        db.execSQL("CREATE TABLE medicine (" +
                "    id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "    name character(100) NOT NULL,\n" +
                "    type character(100) NOT NULL,\n" +
                "    price_per_package real NOT NULL);");

        db.execSQL("CREATE TABLE receipt_medicines (" +
                "    id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "    receipt_id integer NOT NULL,\n" +
                "    medicine_id integer NOT NULL,\n" +
                "    count integer NOT NULL,\n" +
                "    CONSTRAINT receipt_fk FOREIGN KEY (receipt_id)\n" +
                "    REFERENCES receipt(id) ON DELETE CASCADE,\n"  +
                "    CONSTRAINT medicine_fk FOREIGN KEY (medicine_id)\n" +
                "    REFERENCES medicine(id) ON DELETE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}
