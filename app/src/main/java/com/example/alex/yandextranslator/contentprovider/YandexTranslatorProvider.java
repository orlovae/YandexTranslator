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
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.PATH_DICTIONARY_ENTERIES_DE;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.PATH_DICTIONARY_ENTERIES_EXAMPLE;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.PATH_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.PATH_DICTIONARY_ENTERIES_MEANING;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.PATH_DICTIONARY_ENTERIES_SYNONYM;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.PATH_DICTIONARY_ENTERIES_TRANSLATE;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_DE_ALL_ROW;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_DE_SINGLE_ROW;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_EXAMPLE_ALL_ROW;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_EXAMPLE_SINGLE_ROW;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_ALL_ROW;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_SINGLE_ROW;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_MEANING_ALL_ROW;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_MEANING_SINGLE_ROW;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_SYNONYM_ALL_ROW;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_SYNONYM_SINGLE_ROW;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_TRANSLATE_ALL_ROW;
import static com.example.alex.yandextranslator.data.Contract.DictionaryEntries.TYPE_DICTIONARY_ENTERIES_TRANSLATE_SINGLE_ROW;
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

    /*Table Language*/
    private static final int URI_MATCHER_LANGUAGE_ALL_ROWS = 1000;
    private static final int URI_MATCHER_LANGUAGE_SINGLE_ROW = 1001;

    /*Table History_Favorites*/
    private static final int URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS = 2000;
    private static final int URI_MATCHER_HISTORY_FAVORITES_SINGLE_ROW = 2001;

    /*Table DictionaryEntries.DE*/
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_DE_ALL_ROWS = 3000;
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_DE_SINGLE_ROW = 3001;

    /*Table DictionaryEntries.Translate*/
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_TRANSLATE_ALL_ROWS = 4000;
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_TRANSLATE_SINGLE_ROW = 4001;

    /*Table DictionaryEntries.Synonym*/
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_SYNONYM_ALL_ROWS = 5000;
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_SYNONYM_SINGLE_ROW = 5001;

    /*Table DictionaryEntries.Meaning*/
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_MEANING_ALL_ROWS = 6000;
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_MEANING_SINGLE_ROW = 6001;

    /*Table DictionaryEntries.Example*/
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_ALL_ROWS = 7000;
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_SINGLE_ROW = 7001;

    /*Table DictionaryEntries.ExampleTranslation*/
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_ALL_ROWS = 8000;
    private static final int URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_SINGLE_ROW = 8001;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*Table Language*/
        uriMatcher.addURI(AUTHORITY, PATH_LANGUAGE, URI_MATCHER_LANGUAGE_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_LANGUAGE + "/#", URI_MATCHER_LANGUAGE_SINGLE_ROW);

        /*Table History_Favorites*/
        uriMatcher.addURI(AUTHORITY, PATH_HISTORY_FAVORITES, URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_HISTORY_FAVORITES + "/#",
                URI_MATCHER_HISTORY_FAVORITES_SINGLE_ROW);

        /*Table DictionaryEntries.DE*/
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_DE,
                URI_MATCHER_DICTIONARY_ENTERIES_DE_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_DE + "/#",
                URI_MATCHER_DICTIONARY_ENTERIES_DE_SINGLE_ROW);

        /*Table DictionaryEntries.Translate*/
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_TRANSLATE,
                URI_MATCHER_DICTIONARY_ENTERIES_TRANSLATE_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_TRANSLATE + "/#",
                URI_MATCHER_DICTIONARY_ENTERIES_TRANSLATE_SINGLE_ROW);

        /*Table DictionaryEntries.Synonym*/
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_SYNONYM,
                URI_MATCHER_DICTIONARY_ENTERIES_SYNONYM_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_SYNONYM + "/#",
                URI_MATCHER_DICTIONARY_ENTERIES_SYNONYM_SINGLE_ROW);

        /*Table DictionaryEntries.Meaning*/
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_MEANING,
                URI_MATCHER_DICTIONARY_ENTERIES_MEANING_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_MEANING + "/#",
                URI_MATCHER_DICTIONARY_ENTERIES_MEANING_SINGLE_ROW);

        /*Table DictionaryEntries.Example*/
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_EXAMPLE,
                URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_EXAMPLE + "/#",
                URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_SINGLE_ROW);

        /*Table DictionaryEntries.ExampleTranslation*/
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION,
                URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_ALL_ROWS);
        uriMatcher.addURI(AUTHORITY, PATH_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION + "/#",
                URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_SINGLE_ROW);
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

            /*Table Language*/
            case URI_MATCHER_LANGUAGE_ALL_ROWS:
                return TYPE_LANGUAGE_ALL_ROW;
            case URI_MATCHER_LANGUAGE_SINGLE_ROW:
                return TYPE_LANGUAGE_SINGLE_ROW;

            /*Table HistoryFavorites*/
            case URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS:
                return TYPE_HISTORY_FAVORITES_ALL_ROW;
            case URI_MATCHER_HISTORY_FAVORITES_SINGLE_ROW:
                return TYPE_HISTORY_FAVORITES_SINGLE_ROW;

            /*Table DictionaryEntries.DE*/
            case URI_MATCHER_DICTIONARY_ENTERIES_DE_ALL_ROWS:
                return TYPE_DICTIONARY_ENTERIES_DE_ALL_ROW;
            case URI_MATCHER_DICTIONARY_ENTERIES_DE_SINGLE_ROW:
                return TYPE_DICTIONARY_ENTERIES_DE_SINGLE_ROW;

            /*Table DictionaryEntries.Translate*/
            case URI_MATCHER_DICTIONARY_ENTERIES_TRANSLATE_ALL_ROWS:
                return TYPE_DICTIONARY_ENTERIES_TRANSLATE_ALL_ROW;
            case URI_MATCHER_DICTIONARY_ENTERIES_TRANSLATE_SINGLE_ROW:
                return TYPE_DICTIONARY_ENTERIES_TRANSLATE_SINGLE_ROW;

            /*Table DictionaryEntries.Synonym*/
            case URI_MATCHER_DICTIONARY_ENTERIES_SYNONYM_ALL_ROWS:
                return TYPE_DICTIONARY_ENTERIES_SYNONYM_ALL_ROW;
            case URI_MATCHER_DICTIONARY_ENTERIES_SYNONYM_SINGLE_ROW:
                return TYPE_DICTIONARY_ENTERIES_SYNONYM_SINGLE_ROW;

            /*Table DictionaryEntries.Meaning*/
            case URI_MATCHER_DICTIONARY_ENTERIES_MEANING_ALL_ROWS:
                return TYPE_DICTIONARY_ENTERIES_MEANING_ALL_ROW;
            case URI_MATCHER_DICTIONARY_ENTERIES_MEANING_SINGLE_ROW:
                return TYPE_DICTIONARY_ENTERIES_MEANING_SINGLE_ROW;

            /*Table DictionaryEntries.Example*/
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_ALL_ROWS:
                return TYPE_DICTIONARY_ENTERIES_EXAMPLE_ALL_ROW;
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_SINGLE_ROW:
                return TYPE_DICTIONARY_ENTERIES_EXAMPLE_SINGLE_ROW;

            /*Table DictionaryEntries.ExampleTranslation*/
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_ALL_ROWS:
                return TYPE_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_ALL_ROW;
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_SINGLE_ROW:
                return TYPE_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_SINGLE_ROW;
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
        long rowIDLanguage, rowIDHistoryFavorites, rowIDDictionaryEntries, rowIDTranslate,
                rowIDSynonym, rowIDMeaning, rowIDExample, rowIDExampleTranslation;

        String table_name = "";

        Uri resultUri = null;

        openDatabase();

        switch (uriMatcher.match(uri)) {

            /*Table Language*/
            case URI_MATCHER_LANGUAGE_ALL_ROWS:
                table_name = Contract.Language.TABLE_NAME;
                rowIDLanguage = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDLanguage);
                break;

            /*Table HistoryFavorites*/
            case URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS:
                table_name = Contract.HistoryFavorites.TABLE_NAME;
                rowIDHistoryFavorites = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDHistoryFavorites);
                break;

            /*Table DictionaryEntries*/
            case URI_MATCHER_DICTIONARY_ENTERIES_DE_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_DE;
                rowIDDictionaryEntries = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDDictionaryEntries);
                break;

            /*Table Translate*/
            case URI_MATCHER_DICTIONARY_ENTERIES_TRANSLATE_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_TRANSLATE;
                rowIDTranslate = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDTranslate);
                break;

            /*Table Synonym*/
            case URI_MATCHER_DICTIONARY_ENTERIES_SYNONYM_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_SYNONYM;
                rowIDSynonym = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDSynonym);
                break;

            /*Table Meaning*/
            case URI_MATCHER_DICTIONARY_ENTERIES_MEANING_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_MEANING;
                rowIDMeaning = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDMeaning);
                break;

            /*Table Example*/
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_EXAMPLE;
                rowIDExample = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDExample);
                break;

            /*Table ExampleTranslation*/
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_EXAMPLE_TRANSLATION;
                rowIDExampleTranslation = database.insert(table_name, null, values);
                resultUri = ContentUris.withAppendedId(uri, rowIDExampleTranslation);
                break;
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

        String rowIDLanguage, rowIDHistoryFavorites, rowIDDictionaryEntries, rowIDTranslate,
                rowIDSynonym, rowIDMeaning, rowIDExample, rowIDExampleTranslation;

        int countRowsDelete = -1;

        String table_name = "";

        openDatabase();

        switch (uriMatcher.match(uri)) {

            /*Table Language*/
            case URI_MATCHER_LANGUAGE_ALL_ROWS:
                table_name = Contract.Language.TABLE_NAME;
                countRowsDelete = database.delete(table_name, null, null);
                break;
            case URI_MATCHER_LANGUAGE_SINGLE_ROW:
                table_name = Contract.Language.TABLE_NAME;
                rowIDLanguage = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.Language.COLUMN_ID + " = "
                            + rowIDLanguage;
                } else {
                    selection = selection + " AND "
                            + Contract.Language.COLUMN_ID + " = "
                            + rowIDLanguage;
                }
                countRowsDelete = database.delete(table_name, selection, selectionArgs);
                break;

            /*Table HistoryFavorites*/
            case URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS:
                table_name = Contract.HistoryFavorites.TABLE_NAME;
                countRowsDelete = database.delete(table_name, null, null);
                break;
            case URI_MATCHER_HISTORY_FAVORITES_SINGLE_ROW:
                table_name = Contract.Language.TABLE_NAME;
                rowIDHistoryFavorites = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.HistoryFavorites.COLUMN_ID + " = "
                            + rowIDHistoryFavorites;
                } else {
                    selection = selection + " AND "
                            + Contract.HistoryFavorites.COLUMN_ID + " = "
                            + rowIDHistoryFavorites;
                }
                countRowsDelete = database.delete(table_name, selection, selectionArgs);
                break;

            /*Table DictionaryEntries*/
            case URI_MATCHER_DICTIONARY_ENTERIES_DE_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_DE;
                countRowsDelete = database.delete(table_name, null, null);
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_DE_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_DE;
                rowIDDictionaryEntries = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_DE_ID + " = "
                            + rowIDDictionaryEntries;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_DE_ID + " = "
                            + rowIDDictionaryEntries;
                }
                countRowsDelete = database.delete(table_name, selection, selectionArgs);
                break;

            /*Table Translate*/
            case URI_MATCHER_DICTIONARY_ENTERIES_TRANSLATE_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_TRANSLATE;
                countRowsDelete = database.delete(table_name, null, null);
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_TRANSLATE_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_TRANSLATE;
                rowIDTranslate = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_TRANSLATE_ID + " = "
                            + rowIDTranslate;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_TRANSLATE_ID + " = "
                            + rowIDTranslate;
                }
                countRowsDelete = database.delete(table_name, selection, selectionArgs);
                break;

            /*Table Synonym*/
            case URI_MATCHER_DICTIONARY_ENTERIES_SYNONYM_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_SYNONYM;
                countRowsDelete = database.delete(table_name, null, null);
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_SYNONYM_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_SYNONYM;
                rowIDSynonym = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_SYNONYM_ID + " = "
                            + rowIDSynonym;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_SYNONYM_ID + " = "
                            + rowIDSynonym;
                }
                countRowsDelete = database.delete(table_name, selection, selectionArgs);
                break;

            /*Table Meaning*/
            case URI_MATCHER_DICTIONARY_ENTERIES_MEANING_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_MEANING;
                countRowsDelete = database.delete(table_name, null, null);
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_MEANING_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_MEANING;
                rowIDMeaning = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_MEANING_ID + " = "
                            + rowIDMeaning;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_MEANING_ID + " = "
                            + rowIDMeaning;
                }
                countRowsDelete = database.delete(table_name, selection, selectionArgs);
                break;

            /*Table Example*/
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_EXAMPLE;
                countRowsDelete = database.delete(table_name, null, null);
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_EXAMPLE;
                rowIDExample = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_EXAMPLE_ID + " = "
                            + rowIDExample;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_EXAMPLE_ID + " = "
                            + rowIDExample;
                }
                countRowsDelete = database.delete(table_name, selection, selectionArgs);
                break;

            /*Table ExampleTranslation*/
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_EXAMPLE_TRANSLATION;
                countRowsDelete = database.delete(table_name, null, null);
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_EXAMPLE_TRANSLATION;
                rowIDExampleTranslation = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATION_ID + " = "
                            + rowIDExampleTranslation;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATION_ID + " = "
                            + rowIDExampleTranslation;
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
        String rowIDLanguage, rowIDHistoryFavorites, rowIDDictionaryEntries, rowIDTranslate,
                rowIDSynonym, rowIDMeaning, rowIDExample, rowIDExampleTranslation;

        String table_name = "";

        openDatabase();

        switch (uriMatcher.match(uri)) {

            /*Table Language*/
            case URI_MATCHER_LANGUAGE_ALL_ROWS:
                table_name = Contract.Language.TABLE_NAME;
                break;
            case URI_MATCHER_LANGUAGE_SINGLE_ROW:
                table_name = Contract.Language.TABLE_NAME;
                rowIDLanguage = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.Language.COLUMN_ID + " = "
                            + rowIDLanguage;
                } else {
                    selection = selection + " AND "
                            + Contract.Language.COLUMN_ID + " = "
                            + rowIDLanguage;
                }
                break;

            /*Table HistoryFavorites*/
            case URI_MATCHER_HISTORY_FAVORITES_ALL_ROWS:
                table_name = Contract.HistoryFavorites.TABLE_NAME;
                break;
            case URI_MATCHER_HISTORY_FAVORITES_SINGLE_ROW:
                table_name = Contract.HistoryFavorites.TABLE_NAME;
                rowIDHistoryFavorites = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.HistoryFavorites.COLUMN_ID + " = "
                            + rowIDHistoryFavorites;
                } else {
                    selection = selection + " AND "
                            + Contract.HistoryFavorites.COLUMN_ID + " = "
                            + rowIDHistoryFavorites;
                }
                break;

            /*Table DictionaryEntries*/
            case URI_MATCHER_DICTIONARY_ENTERIES_DE_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_DE;
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_DE_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_DE;
                rowIDDictionaryEntries = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_DE_ID + " = "
                            + rowIDDictionaryEntries;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_DE_ID + " = "
                            + rowIDDictionaryEntries;
                }
                break;

            /*Table Translate*/
            case URI_MATCHER_DICTIONARY_ENTERIES_TRANSLATE_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_TRANSLATE;
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_TRANSLATE_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_TRANSLATE;
                rowIDTranslate = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_TRANSLATE_ID + " = "
                            + rowIDTranslate;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_TRANSLATE_ID
                            + " = " + rowIDTranslate;
                }
                break;

            /*Table Synonym*/
            case URI_MATCHER_DICTIONARY_ENTERIES_SYNONYM_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_SYNONYM;
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_SYNONYM_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_SYNONYM;
                rowIDSynonym = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_SYNONYM_ID + " = "
                            + rowIDSynonym;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_SYNONYM_ID
                            + " = " + rowIDSynonym;
                }
                break;

            /*Table Meaning*/
            case URI_MATCHER_DICTIONARY_ENTERIES_MEANING_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_MEANING;
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_MEANING_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_MEANING;
                rowIDMeaning = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_MEANING_ID + " = "
                            + rowIDMeaning;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_MEANING_ID
                            + " = " + rowIDMeaning;
                }
                break;

            /*Table Example*/
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_EXAMPLE;
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_EXAMPLE;
                rowIDExample = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_EXAMPLE_ID + " = "
                            + rowIDExample;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_EXAMPLE_ID
                            + " = " + rowIDExample;
                }
                break;

            /*Table ExampleTranslation*/
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_ALL_ROWS:
                table_name = Contract.DictionaryEntries.TABLE_NAME_EXAMPLE_TRANSLATION;
                break;
            case URI_MATCHER_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_SINGLE_ROW:
                table_name = Contract.DictionaryEntries.TABLE_NAME_EXAMPLE_TRANSLATION;
                rowIDExampleTranslation = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATION_ID + " = "
                            + rowIDExampleTranslation;
                } else {
                    selection = selection + " AND "
                            + Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATION_ID
                            + " = " + rowIDExampleTranslation;
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
