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
            + Contract.HistoryFavorites.COLUMN_FAVORITE + " integer);";

    private final String SQL_CREATE_DE_TABLE = "CREATE TABLE "
            + Contract.DictionaryEntries.TABLE_NAME_DE + " ("
            + Contract.DictionaryEntries.COLUMN_DE_ID + " integer primary key autoincrement,"
            + Contract.DictionaryEntries.COLUMN_DE_TEXT + " text,"
            + Contract.DictionaryEntries.COLUMN_DE_PART_OF_SPEECH + " text,"
            + Contract.DictionaryEntries.COLUMN_DE_TRANSCRIPTION + " text);";

    private final String SQL_CREATE_TRANSLATE_TABLE = "CREATE TABLE "
            + Contract.DictionaryEntries.TABLE_NAME_TRANSLATE + " ("
            + Contract.DictionaryEntries.COLUMN_TRANSLATE_ID + " integer primary key autoincrement,"
            + Contract.DictionaryEntries.COLUMN_TRANSLATE_TEXT + " text,"
            + Contract.DictionaryEntries.COLUMN_TRANSLATE_PART_OF_SPEECH + " text,"
            + Contract.DictionaryEntries.COLUMN_TRANSLATE_GENDER + " text,"
            + Contract.DictionaryEntries.COLUMN_TRANSLATE_DE_ID + " integer, FOREIGN KEY("
            + Contract.DictionaryEntries.COLUMN_TRANSLATE_DE_ID + ") REFERENCES "
            + Contract.DictionaryEntries.TABLE_NAME_DE + " ( "
            + Contract.DictionaryEntries.COLUMN_DE_ID + " ));";

    private final String SQL_CREATE_SYNONYM_TABLE = "CREATE TABLE "
            + Contract.DictionaryEntries.TABLE_NAME_SYNONYM + " ("
            + Contract.DictionaryEntries.COLUMN_SYNONYM_ID + " integer primary key autoincrement,"
            + Contract.DictionaryEntries.COLUMN_SYNONYM_TEXT + " text,"
            + Contract.DictionaryEntries.COLUMN_SYNONYM_PART_OF_SPEECH + " text,"
            + Contract.DictionaryEntries.COLUMN_SYNONYM_GENDER + " text,"
            + Contract.DictionaryEntries.COLUMN_SYNONYM_TRANSLATE_ID + " integer, FOREIGN KEY("
            + Contract.DictionaryEntries.COLUMN_SYNONYM_TRANSLATE_ID + ") REFERENCES "
            + Contract.DictionaryEntries.TABLE_NAME_TRANSLATE + " ( "
            + Contract.DictionaryEntries.COLUMN_TRANSLATE_ID + " ));";

    private final String SQL_CREATE_MEANING_TABLE = "CREATE TABLE "
            + Contract.DictionaryEntries.TABLE_NAME_MEANING + " ("
            + Contract.DictionaryEntries.COLUMN_MEANING_ID + " integer primary key autoincrement,"
            + Contract.DictionaryEntries.COLUMN_MEANING_TEXT + " text,"
            + Contract.DictionaryEntries.COLUMN_MEANING_TRANSLATE_ID + " integer, FOREIGN KEY("
            + Contract.DictionaryEntries.COLUMN_MEANING_TRANSLATE_ID + ") REFERENCES "
            + Contract.DictionaryEntries.TABLE_NAME_TRANSLATE + " ( "
            + Contract.DictionaryEntries.COLUMN_TRANSLATE_ID + " ));";

    private final String SQL_CREATE_EXAMPLE_TABLE = "CREATE TABLE "
            + Contract.DictionaryEntries.TABLE_NAME_EXAMPLE + " ("
            + Contract.DictionaryEntries.COLUMN_EXAMPLE_ID + " integer primary key autoincrement,"
            + Contract.DictionaryEntries.COLUMN_EXAMPLE_TEXT + " text,"
            + Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATE_ID + " integer, FOREIGN KEY("
            + Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATE_ID + ") REFERENCES "
            + Contract.DictionaryEntries.TABLE_NAME_TRANSLATE + " ( "
            + Contract.DictionaryEntries.COLUMN_TRANSLATE_ID + " ));";

    private final String SQL_CREATE_EXAMPLE_TRANSLATION_TABLE = "CREATE TABLE "
            + Contract.DictionaryEntries.TABLE_NAME_EXAMPLE_TRANSLATION + " ("
            + Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATION_ID + " integer primary key autoincrement,"
            + Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATION_TEXT + " text,"
            + Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATE_EXAMPLE_ID + " integer, FOREIGN KEY("
            + Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATE_EXAMPLE_ID + ") REFERENCES "
            + Contract.DictionaryEntries.TABLE_NAME_EXAMPLE + " ( "
            + Contract.DictionaryEntries.COLUMN_EXAMPLE_ID + " ));";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, TABLE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_LAGUAGE_TABLE);
        database.execSQL(SQL_CREATE_HISTORY_FAVORITES_TABLE);
        database.execSQL(SQL_CREATE_DE_TABLE);
        database.execSQL(SQL_CREATE_TRANSLATE_TABLE);
        database.execSQL(SQL_CREATE_SYNONYM_TABLE);
        database.execSQL(SQL_CREATE_MEANING_TABLE);
        database.execSQL(SQL_CREATE_EXAMPLE_TABLE);
        database.execSQL(SQL_CREATE_EXAMPLE_TRANSLATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + Contract.Language.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.HistoryFavorites.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.DictionaryEntries.TABLE_NAME_DE);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.DictionaryEntries.TABLE_NAME_TRANSLATE);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.DictionaryEntries.TABLE_NAME_SYNONYM);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.DictionaryEntries.TABLE_NAME_MEANING);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.DictionaryEntries.TABLE_NAME_EXAMPLE);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.DictionaryEntries.TABLE_NAME_EXAMPLE_TRANSLATION);
        onCreate(database);
    }
}
