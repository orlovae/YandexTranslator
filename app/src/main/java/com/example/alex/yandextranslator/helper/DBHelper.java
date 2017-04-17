package com.example.alex.yandextranslator.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.alex.yandextranslator.data.tables.LanguageTable.COLUMN_CODE_LANGUAGE;
import static com.example.alex.yandextranslator.data.tables.LanguageTable.COLUMN_ID;
import static com.example.alex.yandextranslator.data.tables.LanguageTable.COLUMN_LANGUAGE;
import static com.example.alex.yandextranslator.data.tables.LanguageTable.TABLE_NAME;
import static com.example.alex.yandextranslator.data.tables.LanguageTable.TABLE_VERSION;

/**
 * Created by alex on 14.04.17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "yandexTranslator.db";
    private final String SQL_CREATE_LAGUAGE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_CODE_LANGUAGE + " text,"
            + COLUMN_LANGUAGE + " text);";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_LAGUAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
