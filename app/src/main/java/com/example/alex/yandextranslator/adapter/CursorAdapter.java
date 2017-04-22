package com.example.alex.yandextranslator.adapter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.alex.yandextranslator.data.Contract;
import com.example.alex.yandextranslator.model.HistoryFavorites;
import com.example.alex.yandextranslator.model.language.Language;

import java.util.ArrayList;

/**
 * Created by alex on 15.04.17.
 */

public class CursorAdapter {
    private final String LOG_TAG = this.getClass().getSimpleName();
    private Context context;
    private ArrayList<Language> languageArrayList;
    private ArrayList<HistoryFavorites> historyFavoritesArrayList;

    public ArrayList<Language> getListToCursor(Cursor cursor){
        Log.d(LOG_TAG, "Start getListToCursor");
        if (languageArrayList == null) {
            languageArrayList = new ArrayList<>();
        } else {
            languageArrayList.clear();
        }

        try {
            if (cursor != null && cursor.moveToFirst()){
                int codeLanguageColIndex = cursor.getColumnIndex(Contract.Language.
                        COLUMN_CODE_LANGUAGE);
                int languageColIndex = cursor.getColumnIndex(Contract.Language.COLUMN_LANGUAGE);
                do {
                    String codeLanguageFromCursor = cursor.getString(codeLanguageColIndex);
                    String languageFromCursor = cursor.getString(languageColIndex);

                    Language language = new Language(codeLanguageFromCursor, languageFromCursor);

                    languageArrayList.add(language);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return languageArrayList;
    }

    public ArrayList<HistoryFavorites> getArrayListHistoryFavoritesToCursor(Cursor cursor){
        Log.d(LOG_TAG, "Start getArrayListHistoryFavoritesToCursor");
        if (historyFavoritesArrayList == null) {
            historyFavoritesArrayList = new ArrayList<>();
        } else {
            historyFavoritesArrayList.clear();
        }

        try {
            if (cursor != null && cursor.moveToFirst()){
                int idColIndex = cursor.getColumnIndex(Contract.HistoryFavorites.COLUMN_ID);
                int translatableTextColIndex = cursor.getColumnIndex(Contract.HistoryFavorites.
                        COLUMN_TRANSLATABLE_TEXT);
                int translatedTextColIndex = cursor.getColumnIndex(Contract.HistoryFavorites.
                        COLUMN_TRANSLATED_TEXT);
                int translationDirectionColIndex = cursor.getColumnIndex(Contract.HistoryFavorites.
                        COLUMN_TRANSLATION_DIRECTION);
                int favoriteColIndex = cursor.getColumnIndex(Contract.HistoryFavorites.
                        COLUMN_FAVORITE);

                do {
                    int id = cursor.getInt(idColIndex);
                    String translatableFromCursor = cursor.getString(translatableTextColIndex);
                    String translatedFromCursor = cursor.getString(translatedTextColIndex);
                    String translationDirectionFromCursor = cursor.
                            getString(translationDirectionColIndex);
                    int favorite = cursor.getInt(favoriteColIndex);
                    boolean favoriteFromCursor = castIntToBoolean(favorite);

                    HistoryFavorites historyFavorites = new HistoryFavorites(
                            id, translatableFromCursor, translatedFromCursor,
                            translationDirectionFromCursor, favoriteFromCursor);

                    historyFavoritesArrayList.add(historyFavorites);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
        }
        return historyFavoritesArrayList;
    }

    private boolean castIntToBoolean(int favoriteFromCursor){
        if (favoriteFromCursor == 0){
            return false;
        } else {
            return true;
        }
    }
}
