package com.example.alex.yandextranslator.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.alex.yandextranslator.database.tables.LanguageTable.COLUMN_CODE_LANGUAGE;
import static com.example.alex.yandextranslator.database.tables.LanguageTable.COLUMN_LANGUAGE;
import static com.example.alex.yandextranslator.database.tables.LanguageTable.COLUMN_LANGUAGE_ID;
import static com.example.alex.yandextranslator.database.tables.LanguageTable.DATABASE_TABLE;
import static com.example.alex.yandextranslator.database.tables.LanguageTable.DATABASE_VERSION;

/**
 * Created by alex on 14.04.17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_CREATE ="CREATE TABLE " +
            DATABASE_TABLE + " ("
            + COLUMN_LANGUAGE_ID + " integer primary key autoincrement,"
            + COLUMN_CODE_LANGUAGE + " text,"
            + COLUMN_LANGUAGE + " text);";

    public DBHelper(Context context){
        super(context, DATABASE_TABLE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }
}
