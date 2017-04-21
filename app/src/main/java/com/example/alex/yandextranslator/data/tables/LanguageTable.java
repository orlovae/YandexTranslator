package com.example.alex.yandextranslator.data.tables;

import android.provider.BaseColumns;

/**
 * Created by alex on 14.04.17.
 */

public  class LanguageTable implements BaseColumns {
    public static final String TABLE_NAME = "Language";
    public static final int TABLE_VERSION = 1;
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_CODE_LANGUAGE = "codeLanguage";
    public static final String COLUMN_LANGUAGE = "language";

}
