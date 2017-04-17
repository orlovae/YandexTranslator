package com.example.alex.yandextranslator.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.alex.yandextranslator.model.language.CodeLanguage;
import com.example.alex.yandextranslator.model.language.Language;
import com.example.alex.yandextranslator.model.response.LanguageDictionare;

import java.util.HashMap;

import static com.example.alex.yandextranslator.data.Contract.Language.COLUMN_CODE_LANGUAGE;
import static com.example.alex.yandextranslator.data.Contract.Language.COLUMN_ID;
import static com.example.alex.yandextranslator.data.tables.LanguageTable.COLUMN_LANGUAGE;

/**
 * Created by alex on 15.04.17.
 */

public class CursorToMapLanguageAdapter {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Context context;
    private Cursor cursor;
    private HashMap<CodeLanguage, Language> hashMapLanguageDictionare;
    private String languageFromCursor;

    public CursorToMapLanguageAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public HashMap<CodeLanguage, Language> getHashMapToCursor(){
        Log.d(LOG_TAG, "Start getHashMapToCursor");
        if (hashMapLanguageDictionare == null) {
            hashMapLanguageDictionare = new HashMap<CodeLanguage, Language>();
        } else {
            hashMapLanguageDictionare.clear();
        }

        try {
            if (cursor != null && cursor.moveToFirst()){
                int codeLanguageColIndex = cursor.getColumnIndex(COLUMN_CODE_LANGUAGE);
                int languageColIndex = cursor.getColumnIndex(COLUMN_LANGUAGE);
                do {
                    String codeLanguageFromCursor = cursor.getString(codeLanguageColIndex);
                    String languageFromCursor = cursor.getString(languageColIndex);

                    CodeLanguage codeLanguage = new CodeLanguage();
                    codeLanguage.setCodeLanguage(codeLanguageFromCursor);

                    Language language = new Language();
                    language.setLanguage(languageFromCursor);
                    hashMapLanguageDictionare.put(codeLanguage, language);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return hashMapLanguageDictionare;
    }

    public String getStringLanguageToCursor(){
        Log.d(LOG_TAG, "Start getStringLanguageToCursor");
        if (languageFromCursor != null) {
            languageFromCursor = null;
        }

        try {
            if (cursor != null){
                cursor.moveToLast();
                int languageColIndex = cursor.getColumnIndex(COLUMN_LANGUAGE);
                languageFromCursor = cursor.getString(languageColIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return languageFromCursor;
    }
}
