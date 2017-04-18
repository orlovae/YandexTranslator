package com.example.alex.yandextranslator.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.alex.yandextranslator.model.language.Language;

import java.util.ArrayList;

import static com.example.alex.yandextranslator.data.Contract.Language.COLUMN_CODE_LANGUAGE;
import static com.example.alex.yandextranslator.data.tables.LanguageTable.COLUMN_LANGUAGE;

/**
 * Created by alex on 15.04.17.
 */

public class CursorToMapLanguageAdapter {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Context context;
    private Cursor cursor;
    private ArrayList<Language> listLanguage;
    private ArrayList<String> listStringLanguage;

    public CursorToMapLanguageAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public ArrayList<Language> getListToCursor(){
        Log.d(LOG_TAG, "Start getHashMapToCursor");
        if (listLanguage == null) {
            listLanguage = new ArrayList<>();
        } else {
            listLanguage.clear();
        }

        try {
            if (cursor != null && cursor.moveToFirst()){
                int codeLanguageColIndex = cursor.getColumnIndex(COLUMN_CODE_LANGUAGE);
                int languageColIndex = cursor.getColumnIndex(COLUMN_LANGUAGE);
                do {
                    String codeLanguageFromCursor = cursor.getString(codeLanguageColIndex);
                    String languageFromCursor = cursor.getString(languageColIndex);

                    Language language = new Language(codeLanguageFromCursor, languageFromCursor);

                    listLanguage.add(language);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return listLanguage;
    }

    public String[] getStringArayLanguageToCursor(){
        Log.d(LOG_TAG, "Start getStringLanguageToCursor");
        String language;

        if (listStringLanguage == null) {
            listStringLanguage = new ArrayList<>();
        } else {
            listStringLanguage.clear();
        }

        try {
            if (cursor != null && cursor.moveToFirst()){
                int languageColIndex = cursor.getColumnIndex(COLUMN_LANGUAGE);
                language = cursor.getString(languageColIndex);
                listStringLanguage.add(language);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return listStringLanguage.toArray(new String[listStringLanguage.size()]);
    }
}
