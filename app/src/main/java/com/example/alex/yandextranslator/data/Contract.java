package com.example.alex.yandextranslator.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by alex on 15.04.17.
 */

public final class Contract {

    public Contract() {
    }

    // Uri authority
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.alex.yandextranslator";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    // path
    public static final class Language implements BaseColumns {
        public static final String TABLE_NAME = "Language";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_CODE_LANGUAGE = "codeLanguage";
        public static final String COLUMN_LANGUAGE = "language";

        public static final String PATH_LANGUAGE = TABLE_NAME;

        // Общий Uri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LANGUAGE);

        // Типы данных
        // набор строк //
        public static final String TYPE_LANGUAGE_ALL_ROW =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME;

        // одна строка
        public static final String TYPE_LANGUAGE_SINGLE_ROW =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class HistoryFavorites implements BaseColumns {
        public static final String TABLE_NAME = "HistoryFavorites";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_TRANSLATABLE_TEXT = "translatable";
        public static final String COLUMN_TRANSLATED_TEXT = "translated";
        public static final String COLUMN_TRANSLATION_DIRECTION = "translation_direction";
        public static final String COLUMN_FAVORITE = "favorites";

        public static final String PATH_HISTORY_FAVORITES = TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_HISTORY_FAVORITES);

        public static final String TYPE_HISTORY_FAVORITES_ALL_ROW =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME;

        // одна строка
        public static final String TYPE_HISTORY_FAVORITES_SINGLE_ROW =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME;
    }

    public static final class DictionaryEntries implements BaseColumns {

        /*Table DictionaryEntries*/
        public static final String TABLE_NAME_DE = "DictionaryEntries";
        public static final String COLUMN_DE_ID = BaseColumns._ID;
        public static final String COLUMN_DE_TEXT = "text";
        public static final String COLUMN_DE_PART_OF_SPEECH = "part_of_speech";
        public static final String COLUMN_DE_TRANSCRIPTION = "transcription";

        public static final String PATH_DICTIONARY_ENTERIES_DE = TABLE_NAME_DE;

        public static final Uri CONTENT_URI_DE = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_DICTIONARY_ENTERIES_DE);

        public static final String TYPE_DICTIONARY_ENTERIES_DE_ALL_ROW =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_DE;

        public static final String TYPE_DICTIONARY_ENTERIES_DE_SINGLE_ROW =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_DE;

        /*Table Translate*/
        public static final String TABLE_NAME_TRANSLATE = "Translate";
        public static final String COLUMN_TRANSLATE_ID = BaseColumns._ID;
        public static final String COLUMN_TRANSLATE_TEXT = "translate_text";
        public static final String COLUMN_TRANSLATE_PART_OF_SPEECH = "translate_part_of_speech";
        public static final String COLUMN_TRANSLATE_GENDER = "translate_gender";
        public static final String COLUMN_TRANSLATE_DE_ID = "DE_id";

        public static final String PATH_DICTIONARY_ENTERIES_TRANSLATE = TABLE_NAME_TRANSLATE;

        public static final Uri CONTENT_URI_TRANSLATE = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_DICTIONARY_ENTERIES_TRANSLATE);

        public static final String TYPE_DICTIONARY_ENTERIES_TRANSLATE_ALL_ROW =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_TRANSLATE;

        public static final String TYPE_DICTIONARY_ENTERIES_TRANSLATE_SINGLE_ROW =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_TRANSLATE;

        /*Table Synonym*/
        public static final String TABLE_NAME_SYNONYM = "Synonym";
        public static final String COLUMN_SYNONYM_ID = BaseColumns._ID;
        public static final String COLUMN_SYNONYM_TEXT = "synonym_text";
        public static final String COLUMN_SYNONYM_PART_OF_SPEECH = "synonym_part_of_speech";
        public static final String COLUMN_SYNONYM_GENDER = "synonym_gender";
        public static final String COLUMN_SYNONYM_TRANSLATE_ID = "translate_id";

        public static final String PATH_DICTIONARY_ENTERIES_SYNONYM = TABLE_NAME_SYNONYM;

        public static final Uri CONTENT_URI_SYNONYM = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_DICTIONARY_ENTERIES_SYNONYM);

        public static final String TYPE_DICTIONARY_ENTERIES_SYNONYM_ALL_ROW =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_SYNONYM;

        public static final String TYPE_DICTIONARY_ENTERIES_SYNONYM_SINGLE_ROW =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_SYNONYM;

        /*Table Meaning*/
        public static final String TABLE_NAME_MEANING = "Meaning";
        public static final String COLUMN_MEANING_ID = BaseColumns._ID;
        public static final String COLUMN_MEANING_TEXT = "meaning_text";
        public static final String COLUMN_MEANING_TRANSLATE_ID = "translate_id";

        public static final String PATH_DICTIONARY_ENTERIES_MEANING = TABLE_NAME_MEANING;

        public static final Uri CONTENT_URI_MEANING = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_DICTIONARY_ENTERIES_MEANING);

        public static final String TYPE_DICTIONARY_ENTERIES_MEANING_ALL_ROW =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_MEANING;

        public static final String TYPE_DICTIONARY_ENTERIES_MEANING_SINGLE_ROW =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_MEANING;

        /*Table Example*/
        public static final String TABLE_NAME_EXAMPLE = "Example";
        public static final String COLUMN_EXAMPLE_ID = BaseColumns._ID;
        public static final String COLUMN_EXAMPLE_TEXT = "example_text";
        public static final String COLUMN_EXAMPLE_TRANSLATE_ID = "translate_id";

        public static final String PATH_DICTIONARY_ENTERIES_EXAMPLE = TABLE_NAME_EXAMPLE;

        public static final Uri CONTENT_URI_EXAMPLE = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_DICTIONARY_ENTERIES_EXAMPLE);

        public static final String TYPE_DICTIONARY_ENTERIES_EXAMPLE_ALL_ROW =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_EXAMPLE;

        public static final String TYPE_DICTIONARY_ENTERIES_EXAMPLE_SINGLE_ROW =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_EXAMPLE;

        /*Table ExampleTranslation*/
        public static final String TABLE_NAME_EXAMPLE_TRANSLATION = "ExampleTranslation";
        public static final String COLUMN_EXAMPLE_TRANSLATION_ID = BaseColumns._ID;
        public static final String COLUMN_EXAMPLE_TRANSLATION_TEXT = "example_translation_text";
        public static final String COLUMN_EXAMPLE_TRANSLATE_EXAMPLE_ID = "example_id";

        public static final String PATH_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION = TABLE_NAME_EXAMPLE_TRANSLATION;

        public static final Uri CONTENT_URI_EXAMPLE_TRANSLATION = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATH_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION);

        public static final String TYPE_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_ALL_ROW =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_EXAMPLE_TRANSLATION;

        public static final String TYPE_DICTIONARY_ENTERIES_EXAMPLE_TRANSLATION_SINGLE_ROW =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME_EXAMPLE_TRANSLATION;
    }

}
