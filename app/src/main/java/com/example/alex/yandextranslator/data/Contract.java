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

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.alex.yandextranslator";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);

    public static final class Language implements BaseColumns {
        public static final String TABLE_NAME = "Language";
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_CODE_LANGUAGE = "codeLanguage";
        public static final String COLUMN_LANGUAGE = "language";

        public static final String PATH_LANGUAGE = TABLE_NAME;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LANGUAGE);

        public static final String TYPE_LANGUAGE_ALL_ROW =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME;

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

        public static final String TYPE_HISTORY_FAVORITES_SINGLE_ROW =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME;
    }
}
