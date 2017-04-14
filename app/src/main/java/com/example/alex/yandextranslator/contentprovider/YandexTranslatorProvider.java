package com.example.alex.yandextranslator.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.alex.yandextranslator.helper.DBHelper;

import java.net.URI;

import static com.example.alex.yandextranslator.database.tables.LanguageTable.COLUMN_LANGUAGE;
import static com.example.alex.yandextranslator.database.tables.LanguageTable.COLUMN_LANGUAGE_ID;
import static com.example.alex.yandextranslator.database.tables.LanguageTable.DATABASE_TABLE;

/**
 * Created by alex on 13.04.17.
 */

public class YandexTranslatorProvider extends ContentProvider {
    private Context context;
    private final String LOG_TAG = context.getClass().getSimpleName();

    // Uri authority
    private static final String SCHEME = "content://";
    private static final String AUTHORITY = "com.example.alex.yandextranstator.contentprovider" +
            ".YandexTranslatorProvider";

    // path
    private static final String PATH = DATABASE_TABLE; //TODO для работы с разными таблицами

    // Общий Uri
    private static final Uri CONTENT_URI = Uri.parse(SCHEME
            + AUTHORITY + "/" + PATH);

    // Типы данных
    // набор строк //TODO пересмотреть урок, уточнить что это за хрень
    private static final String TYPE_ALL_ROW = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + DATABASE_TABLE;

    // одна строка
    private static final String TYPE_SINGLE_ROW = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + DATABASE_TABLE;

    //// UriMatcher
    // общий Uri
    private static final int URI_MATCHER_ALL_ROWS = 1;

    // Uri с указанным ID
    private static final int URI_MATCHER_SINGLE_ROW = 2;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH, URI_MATCHER_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH + "/#", URI_MATCHER_SINGLE_ROW);
    }

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public void openDatabase () throws SQLiteException {
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            database = dbHelper.getReadableDatabase();
        }
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Log.d(LOG_TAG, "Start query");
        Log.d(LOG_TAG, "query, " + uri.toString());

        openDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_ALL_ROWS:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = COLUMN_LANGUAGE + " ASC";
                }
                break;
            case URI_MATCHER_SINGLE_ROW:
                String rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_LANGUAGE_ID + " = " + rowId;
                } else {
                    selection = selection + " AND " + COLUMN_LANGUAGE_ID + " = " + rowId;
                }
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        Cursor cursor = database.query(DATABASE_TABLE, projection, selection,
                    selectionArgs, null, null, sortOrder);
        try {
            cursor.setNotificationUri(context.getContentResolver(), CONTENT_URI);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException:" + e.getLocalizedMessage());
        } finally {
            database.close();
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_SINGLE_ROW:
                return TYPE_SINGLE_ROW;
            case URI_MATCHER_ALL_ROWS:
                return TYPE_ALL_ROW;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());

        if (uriMatcher.match(uri) != URI_MATCHER_SINGLE_ROW) {
            throwIllegalArgumentException(uri);
        }

        openDatabase();
        long rowId = database.insert(DATABASE_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
        try {
            context.getContentResolver().notifyChange(resultUri, null);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException:" + e.getLocalizedMessage());
        } finally {
            database.close();
        }

        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private void throwIllegalArgumentException (Uri uri){
        throw new IllegalArgumentException("Wrong URI: " + uri);
    }
}
