package com.example.alex.yandextranslator.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alex.yandextranslator.data.Contract;

/**
 * Created by alex on 14.04.17.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "yandexTranslator.db";
    public static final int TABLE_VERSION = 1;
    private final String SQL_CREATE_LAGUAGE_TABLE = "CREATE TABLE "
            + Contract.Language.TABLE_NAME + " ("
            + Contract.Language._ID + " integer primary key autoincrement,"
            + Contract.Language.COLUMN_CODE_LANGUAGE + " text,"
            + Contract.Language.COLUMN_LANGUAGE + " text);";
    private final String SQL_CREATE_HISTORY_FAVORITES_TABLE = "CREATE TABLE "
            + Contract.HistoryFavorites.TABLE_NAME + " ("
            + Contract.HistoryFavorites._ID + " integer primary key autoincrement,"
            + Contract.HistoryFavorites.COLUMN_TRANSLATABLE_TEXT + " text,"
            + Contract.HistoryFavorites.COLUMN_TRANSLATED_TEXT + " text,"
            + Contract.HistoryFavorites.COLUMN_TRANSLATION_DIRECTION + " text,"
            + Contract.HistoryFavorites.CULUMN_FAVORITE + " integer);";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_LAGUAGE_TABLE);
        database.execSQL(SQL_CREATE_HISTORY_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + Contract.Language.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.HistoryFavorites.TABLE_NAME);
        onCreate(database);
    }
}
