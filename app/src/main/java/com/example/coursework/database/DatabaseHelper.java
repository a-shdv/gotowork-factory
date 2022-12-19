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

        db.execSQL("CREATE TABLE shift (\n" +
                "    id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "    name character(100) NOT NULL,\n" +
                "    shift_date long NOT NULL,\n" +
                "    user_id integer NOT NULL,\n" +
                "    CONSTRAINT user_fk FOREIGN KEY (user_id)\n" +
                "    REFERENCES user(id) ON DELETE CASCADE);");

        db.execSQL("CREATE TABLE machine (" +
                "    id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "    receiving_date long NOT NULL,\n" +
                "    discharge_date long NOT NULL,\n" +
                "    shift_id integer NOT NULL,\n" +
                "    shift_name character(100) NOT NULL,\n" +
                "    CONSTRAINT shift_fk FOREIGN KEY (shift_id)\n" +
                "    REFERENCES shift(id) ON DELETE CASCADE);");

        db.execSQL("CREATE TABLE worker (" +
                "    id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "    name character(100) NOT NULL,\n" +
                "    salary real NOT NULL);");

        db.execSQL("CREATE TABLE machine_workers (" +
                "    id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "    machine_id integer NOT NULL,\n" +
                "    worker_id integer NOT NULL,\n" +
                "    CONSTRAINT machine_fk FOREIGN KEY (machine_id)\n" +
                "    REFERENCES machine(id) ON DELETE CASCADE,\n"  +
                "    CONSTRAINT worker_fk FOREIGN KEY (worker_id)\n" +
                "    REFERENCES worker(id) ON DELETE CASCADE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }
}
