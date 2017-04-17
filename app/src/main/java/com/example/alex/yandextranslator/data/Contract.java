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
        public static final int TABLE_VERSION = 1;
        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_CODE_LANGUAGE = "codeLanguage";
        public static final String COLUMN_LANGUAGE = "language";

        public static final String PATH = TABLE_NAME; //TODO для работы с разными таблицами

        // Общий Uri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH);

        // Типы данных
        // набор строк //TODO пересмотреть урок, уточнить что это за хрень
        public static final String TYPE_ALL_ROW =
                ContentResolver.CURSOR_DIR_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME;

        // одна строка
        public static final String TYPE_SINGLE_ROW =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +
                        "/" + AUTHORITY + "/" + TABLE_NAME;




    }

}
