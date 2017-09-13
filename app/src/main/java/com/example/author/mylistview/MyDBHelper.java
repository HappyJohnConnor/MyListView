package com.example.author.mylistview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by author on 2017/08/25.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "reminder.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "reminder_record";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BODY = "body";

    private static final String CREATE_TABLE_SQL =
            "create table " + TABLE_NAME + " "
                    +"("+COLUMN_ID+" integer primary key autoincrement,"
                    +COLUMN_TITLE+" text not null,"
                    +COLUMN_BODY+" text not null)";

    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }



}
