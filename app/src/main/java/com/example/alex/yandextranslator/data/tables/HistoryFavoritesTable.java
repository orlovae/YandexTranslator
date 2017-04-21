package com.example.alex.yandextranslator.data.tables;

import android.provider.BaseColumns;

/**
 * Created by alex on 21.04.17.
 */

public class HistoryFavoritesTable implements BaseColumns {

    public static final String TABLE_NAME = "HistoryFavorites";
    public static final int TABLE_VERSION = 1;
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_TRANSLATABLE_TEXT = "translatable text";
    public static final String COLUMN_TRANSLATED_TEXT = "translated text";
    public static final String COLUMN_TRANSLATION_DIRECTION = "translation direction";
    public static final boolean CULUMN_FAVORITE = false;
}
