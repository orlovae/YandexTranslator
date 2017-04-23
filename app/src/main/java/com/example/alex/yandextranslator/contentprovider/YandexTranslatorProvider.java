package com.example.alex.yandextranslator.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
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
import static com.example.alex.yandextranslator.data.Contract.HistoryFavorites.PATH_HISTORY_FAVORITES;
import static com.example.alex.yandextranslator.data.Contract.HistoryFavorites.TYPE_HISTORY_FAVORITES_ALL_ROW;
import static com.example.alex.yandextranslator.data.Contract.HistoryFavorites.TYPE_HISTORY_FAVORITES_SINGLE_ROW;
import static com.example.alex.yandextranslator.data.Contract.Language.PATH_LANGUAGE;
import static com.example.alex.yandextranslator.data.Contract.Language.TYPE_LANGUAGE_ALL_ROW;
import static com.example.alex.yandextranslator.data.Contract.Language.TYPE_LANGUAGE_SINGLE_ROW;

/**
 * Created by alex on 13.04.17.
 */

public class YandexTranslatorProvider extends ContentProvider {
    private final String LOG_TAG = YandexTranslatorProvider.class.getSimpleName();

    private static final int URI_MATCHER_LANGUAGE_ALL_ROWS = 1000;
    private static final int URI_MATCHER_LANGUAGE_SINGLE_ROW = 1001;

    private static final int URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS = 2000;
    private static final int URI_MATCHER_HISTORY_FAVORITES_SINGLE_ROW = 2001;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_LANGUAGE, URI_MATCHER_LANGUAGE_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_HISTORY_FAVORITES, URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_LANGUAGE + "/#", URI_MATCHER_LANGUAGE_SINGLE_ROW);
        uriMatcher.addURI(AUTHORITY, PATH_HISTORY_FAVORITES + "/#",
                URI_MATCHER_HISTORY_FAVORITES_SINGLE_ROW);
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
//        Log.d(LOG_TAG, "onCreate");
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
//        Log.d(LOG_TAG, "Start query");

        String rowIdLanguage, rowIdHistoryFavorites;
        String table_name = "";

        openDatabase();

//        Log.d(LOG_TAG, "query uriMatcher.match(uri) = " + uriMatcher.match(uri));

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_LANGUAGE_ALL_ROWS:
                table_name = Contract.Language.TABLE_NAME;
                break;
            case URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS:
                table_name = Contract.HistoryFavorites.TABLE_NAME;
                break;
            case URI_MATCHER_LANGUAGE_SINGLE_ROW:
                table_name = Contract.Language.TABLE_NAME;
                rowIdLanguage = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.Language.COLUMN_ID + " = " + rowIdLanguage;
                } else {
                    selection = selection + " AND " + Contract.Language.COLUMN_ID + " = " +
                            rowIdLanguage;
                }
                break;
            case URI_MATCHER_HISTORY_FAVORITES_SINGLE_ROW:
                table_name = Contract.HistoryFavorites.TABLE_NAME;
                rowIdHistoryFavorites = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.HistoryFavorites.COLUMN_ID + " = " + rowIdHistoryFavorites;
                } else {
                    selection = selection + " AND " + Contract.HistoryFavorites.COLUMN_ID + " = " +
                            rowIdHistoryFavorites;
                }
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        Cursor cursor = database.query(table_name, projection, selection,
                    selectionArgs, null, null, sortOrder);

        try {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        } finally {
//            database.close();
//            dbHelper.close();
        }
//        Log.d(LOG_TAG, "cursor = " + cursor.toString());
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
//        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_LANGUAGE_SINGLE_ROW:
                return TYPE_LANGUAGE_SINGLE_ROW;
            case URI_MATCHER_HISTORY_FAVORITES_SINGLE_ROW:
                return TYPE_HISTORY_FAVORITES_SINGLE_ROW;
            case URI_MATCHER_LANGUAGE_ALL_ROWS:
                return TYPE_LANGUAGE_ALL_ROW;
            case URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS:
                return TYPE_HISTORY_FAVORITES_ALL_ROW;
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

//        if (uriMatcher.match(uri) != URI_MATCHER_LANGUAGE_SINGLE_ROW) { //проверка, если вставка больше чем 1 элемент -> ошибка
//            throwIllegalArgumentException(uri);
//        }
        long rowIDLanguage, rowIDHistoryFavorites;

        String table_name = "";

        Uri resultUri = null;

        openDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_LANGUAGE_ALL_ROWS:
                table_name = Contract.Language.TABLE_NAME;
                rowIDLanguage = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDLanguage);
                break;
            case URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS:
                table_name = Contract.HistoryFavorites.TABLE_NAME;
                rowIDHistoryFavorites = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDHistoryFavorites);
        }

        try {
            getContext().getContentResolver().notifyChange(resultUri, null);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        } finally {
//            database.close();
//            dbHelper.close();
        }

        return resultUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
//        Log.d(LOG_TAG, "Start delete");
//        Log.d(LOG_TAG, "delete uriMatcher.match(uri) = " + uriMatcher.match(uri));

        String rowIDLanguage, rowIDHistoryFavorites;

        int countRowsDelete = -1;

        String table_name = "";

        openDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_LANGUAGE_ALL_ROWS:
                table_name = Contract.Language.TABLE_NAME;
                countRowsDelete = database.delete(table_name, null, null);
                break;
            case URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS:
                table_name = Contract.HistoryFavorites.TABLE_NAME;
                countRowsDelete = database.delete(table_name,null, null);
                break;
            case URI_MATCHER_LANGUAGE_SINGLE_ROW:
                table_name = Contract.Language.TABLE_NAME;
                rowIDLanguage = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.Language.COLUMN_ID + " = " + rowIDLanguage;
                } else {
                    selection = selection + " AND " + Contract.Language.COLUMN_ID + " = " +
                            rowIDLanguage;
                }
                countRowsDelete = database.delete(table_name, selection, selectionArgs);
                break;
            case URI_MATCHER_HISTORY_FAVORITES_SINGLE_ROW:
                table_name = Contract.Language.TABLE_NAME;
                rowIDHistoryFavorites = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.HistoryFavorites.COLUMN_ID + " = " + rowIDHistoryFavorites;
                } else {
                    selection = selection + " AND " + Contract.HistoryFavorites.COLUMN_ID + " = " +
                            rowIDHistoryFavorites;
                }
                countRowsDelete = database.delete(table_name, selection, selectionArgs);
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        } finally {
//            database.close();
//            dbHelper.close();
        }

        return countRowsDelete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
//        Log.d(LOG_TAG, "Start delete");
//        Log.d(LOG_TAG, "query, " + uri.toString());
        String rowIDLanguage, rowIDHistoryFavorites;

        String table_name = "";

        openDatabase();

        switch (uriMatcher.match(uri)) {
            case URI_MATCHER_LANGUAGE_ALL_ROWS:
                table_name = Contract.Language.TABLE_NAME;
                break;
            case URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS:
                table_name =Contract.HistoryFavorites.TABLE_NAME;
                break;
            case URI_MATCHER_LANGUAGE_SINGLE_ROW:
                table_name = Contract.Language.TABLE_NAME;
                rowIDLanguage = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.Language.COLUMN_ID + " = " + rowIDLanguage;
                } else {
                    selection = selection + " AND " + Contract.Language.COLUMN_ID + " = " +
                            rowIDLanguage;
                }
                break;
            case URI_MATCHER_HISTORY_FAVORITES_SINGLE_ROW:
                table_name =Contract.HistoryFavorites.TABLE_NAME;
                rowIDHistoryFavorites = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.HistoryFavorites.COLUMN_ID + " = " +
                            rowIDHistoryFavorites;
                } else {
                    selection = selection + " AND " + Contract.HistoryFavorites.COLUMN_ID +
                            " = " + rowIDHistoryFavorites;
                }
                break;
            default:
                throwIllegalArgumentException(uri);
        }

        int countRowsUpdate = database.update(table_name, values, selection, selectionArgs);
        Log.d(LOG_TAG, "countRowsUpdate = " + countRowsUpdate);
        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
        } finally {
//            database.close();
//            dbHelper.close();
        }

        return countRowsUpdate;
    }

    private void throwIllegalArgumentException (Uri uri){
        throw new IllegalArgumentException("Wrong URI: " + uri);
    }
}
