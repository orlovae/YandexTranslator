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

import com.example.alex.yandextranslator.data.Contract;
import com.example.alex.yandextranslator.helper.DBHelper;

import static com.example.alex.yandextranslator.data.Contract.AUTHORITY;
import static com.example.alex.yandextranslator.data.Contract.Language.CONTENT_URI;
import static com.example.alex.yandextranslator.data.Contract.Language.PATH;
import static com.example.alex.yandextranslator.data.Contract.Language.COLUMN_ID;
import static com.example.alex.yandextranslator.data.Contract.Language.TABLE_NAME;
import static com.example.alex.yandextranslator.data.Contract.Language.TYPE_ALL_ROW;
import static com.example.alex.yandextranslator.data.Contract.Language.TYPE_SINGLE_ROW;

/**
 * Created by alex on 13.04.17.
 */

public class YandexTranslatorProvider extends ContentProvider {
    private final String LOG_TAG = YandexTranslatorProvider.class.getSimpleName();

    //// UriMatcher
    // общий Uri
    private static final int URI_MATCHER_ALL_ROWS = 1;

    // Uri с указанным ID
    private static final int URI_MATCHER_SINGLE_ROW = 2;
//URI pattern	                            Code	Contant name
//content://com.android.contacts/contacts	1000	CONTACTS
//content://com.android.contacts/contacts/#	1001	CONTACTS_ID
//content://com.android.contacts/lookup/*	1002	CONTACTS_LOOKUP
//content://com.android.contacts/lookup/*/#	1003	CONTACTS_LOOKUP_ID
//...
//content://com.android.contacts/data   	3000	DATA
//content://com.android.contacts/data/# 	3001	DATA_ID
//...

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
        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Log.d(LOG_TAG, "Start query");

        openDatabase();

        Log.d(LOG_TAG, "query uriMatcher.match(uri) = " + uriMatcher.match(uri));

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_ALL_ROWS:

                break;
            case URI_MATCHER_SINGLE_ROW:
                String rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.Language.COLUMN_ID + " = " + rowId;
                } else {
                    selection = selection + " AND " + COLUMN_ID + " = " + rowId;
                }
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        Cursor cursor = database.query(TABLE_NAME, projection, selection,
                    selectionArgs, null, null, sortOrder);

        try {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        } finally {
//            dbHelper.close();
//            database.close();
        }

        Log.d(LOG_TAG, "cursor = " + cursor.toString());

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
            default:
                throwIllegalArgumentException(uri);
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//        Log.d(LOG_TAG, "Start insert");
//        Log.d(LOG_TAG, " insert uriMatcher.match(uri) = " + uriMatcher.match(uri));

//        if (uriMatcher.match(uri) != URI_MATCHER_SINGLE_ROW) { //проверка, если вставка больше чем 1 элемент -> ошибка
//            throwIllegalArgumentException(uri);
//        }

        openDatabase();
        long rowId = database.insert(TABLE_NAME, null, values);
        Uri resultUri = ContentUris.withAppendedId(uri, rowId);
        try {
            getContext().getContentResolver().notifyChange(resultUri, null);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        } finally {
//            dbHelper.close();
//            database.close();
        }

        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(LOG_TAG, "Start delete");

        Log.d(LOG_TAG, "delete uriMatcher.match(uri) = " + uriMatcher.match(uri));

        openDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_ALL_ROWS:
                database.delete(TABLE_NAME, null, null);
                break;
            case URI_MATCHER_SINGLE_ROW:
                String rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_ID + " = " + rowId;
                } else {
                    selection = selection + " AND " + COLUMN_ID + " = " + rowId;
                }
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        int countRowsDelete = database.delete(TABLE_NAME, selection, selectionArgs);
        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        } finally {
//            dbHelper.close();
//            database.close();
        }

        return countRowsDelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(LOG_TAG, "Start delete");
        Log.d(LOG_TAG, "query, " + uri.toString());

        openDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_ALL_ROWS:

                break;
            case URI_MATCHER_SINGLE_ROW:
                String rowId = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = COLUMN_ID + " = " + rowId;
                } else {
                    selection = selection + " AND " + COLUMN_ID + " = " + rowId;
                }
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        int countRowsUpdate = database.update(TABLE_NAME, values, selection, selectionArgs);
        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        } finally {
//            dbHelper.close();
//            database.close();
        }

        return countRowsUpdate;
    }

    private void throwIllegalArgumentException (Uri uri){
        throw new IllegalArgumentException("Wrong URI: " + uri);
    }
}
