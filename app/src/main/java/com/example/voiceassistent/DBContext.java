package com.example.voiceassistent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBContext extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "messageDatabase";
    public static final String TABLE_NAME = "messages";
    public static final String FIELD_ID = "id";
    public static final String FIELD_MESSAGE = "message";
    public static final String FIELD_SEND = "wasSend";
    public static final String FIELD_DATETIME = "dateTime";

    public DBContext(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE [" + TABLE_NAME + "] (\n" +
                " [" + FIELD_ID + "] INTEGER primary key, " +
                " [" + FIELD_MESSAGE + "] TEXT, " +
                " [" + FIELD_SEND + "] INTEGER, " +
                " [" + FIELD_DATETIME + "] TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
