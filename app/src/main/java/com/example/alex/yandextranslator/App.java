package com.example.alex.yandextranslator;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.alex.yandextranslator.adapter.CursorAdapter;
import com.example.alex.yandextranslator.adapter.LanguageDictionareAdapter;
import com.example.alex.yandextranslator.data.Contract;
import com.example.alex.yandextranslator.model.HistoryFavorites;
import com.example.alex.yandextranslator.model.language.Language;
import com.example.alex.yandextranslator.model.response.LanguageDetection;
import com.example.alex.yandextranslator.model.response.LanguageDictionare;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Def;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Ex;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Mean;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Syn;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Tr;
import com.example.alex.yandextranslator.model.response.dictionaryentry.Tr_;
import com.example.alex.yandextranslator.rest.ApiClient;
import com.example.alex.yandextranslator.rest.ApiDictionare;
import com.example.alex.yandextranslator.rest.ApiDictionaryEntry;
import com.example.alex.yandextranslator.rest.ApiLanguageDetection;
import com.example.alex.yandextranslator.rest.ApiTranslator;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by alex on 20.04.17.
 */

public class App extends Application {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private ApiTranslator apiTranslator;
    private ApiLanguageDetection apiLanguageDetection;
    private ApiDictionare apiDictionare;
    private ApiDictionaryEntry apiDictionaryEntry;

    private ArrayList<Language> listLanguage;

    private String[] codeLangToRequest;

    private Map<String, String> mapJson;

    private Gson gson;

    private CursorAdapter cursorAdapter;

    private int rowIDHistoryFavorites = -1;

    private final String KEY_TRANSLATE = "trnsl.1.1.20170407T081255Z.343fc6903b3656af.58d14da04ebc826dbc32072d91d8e3034d99563f";

    private final String KEY_DICTIONARY_ENTRY = "dict.1.1.20170423T182533Z.177a26d2026f3eb9.392449125c6824447549afa55b9c5f97ebbf9899";


    public void setCodeLangToRequest(String[] codeLangToRequest) {
        this.codeLangToRequest = codeLangToRequest;
    }

    public ApiTranslator getApiTranslator() {
        return apiTranslator;
    }

    public ApiDictionare getApiDictionare() {
        return apiDictionare;
    }

    public ApiDictionaryEntry getApiDictionaryEntry() {
        return apiDictionaryEntry;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initCursorAdapter();

        initApiLanguageDictionare();

        initApiLanguageDetection();

        initApiTranslator();

        initApiDictionaryEntry();
    }

    public void initApiDictionaryEntry(){
        apiDictionaryEntry = ApiClient.getClientDictionaryEntry().create(ApiDictionaryEntry.class);
    }

    private void initCursorAdapter(){
        if (cursorAdapter == null) {
            cursorAdapter = new CursorAdapter();
        }
    }

    public Map<String, String> createMapJson(String textToYandex, String key) {
        Log.d(LOG_TAG, "Start createMapJson");
        if (mapJson == null) {
            mapJson = new HashMap<>();
        } else {
            mapJson.clear();
        }

        switch (key) {
            case "dictionare":
//                Log.d(LOG_TAG, "Start createMapJson, case \"dictionare\"");
                mapJson.put("key", KEY_TRANSLATE);
                mapJson.put("ui", "ru");
//                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
            case "languageDetection":
//                Log.d(LOG_TAG, "Start createMapJson, case \"languageDetection\"");
                mapJson.put("key", KEY_TRANSLATE);
                mapJson.put("text", textToYandex);
//                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
            case "translator":
//                Log.d(LOG_TAG, "Start createMapJson, case \"translator\"");

                String lang = setLang(codeLangToRequest);
                Log.d(LOG_TAG, "lang = " + lang);

                mapJson.put("key", KEY_TRANSLATE);
                mapJson.put("text", textToYandex);
                mapJson.put("lang", lang);
//                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
            case "dictionaryEntry":
//                Log.d(LOG_TAG, "Start createMapJson, case \"translator\"");

                String langDictionaryEntry = setLang(codeLangToRequest);
                Log.d(LOG_TAG, "langDictionaryEntry = " + langDictionaryEntry);

                mapJson.put("key", KEY_DICTIONARY_ENTRY);
                mapJson.put("text", textToYandex);
                mapJson.put("lang", langDictionaryEntry);
//                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
        }
        return mapJson;
    }

    private String setLang(String[] codeLanguageToRequest){
        String languageText = getCodeLanguage(codeLanguageToRequest[0]);
        String languageTranslator = getCodeLanguage(codeLanguageToRequest[1]);
        String lang = languageText + "-" + languageTranslator;

        return lang;
    }

    private String getCodeLanguage(String language){
        for (Language item : listLanguage){
            String l = item.getLanguage();
            if (l.equals(language)){
                return item.getCodeLanguage();
            }
        }
        return "";//TODO написать обработку ошибки
    }

    public void initApiLanguageDictionare(){
        initGson();
        apiDictionare = ApiClient.getClient(gson).create(ApiDictionare.class);
    }

    private void initGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LanguageDictionare.class, new LanguageDictionareAdapter());
        gson = gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    private void initApiLanguageDetection(){
        apiLanguageDetection = ApiClient.getClient().create(ApiLanguageDetection.class);
    }

    private void initApiTranslator(){
        apiTranslator = ApiClient.getClient().create(ApiTranslator.class);
    }

    public void responseLanguageDetection(){
        Call<LanguageDetection> call = apiLanguageDetection.languageDetection(mapJson);

        call.enqueue(new Callback<LanguageDetection>() {
            @Override
            public void onResponse(Call<LanguageDetection> call, Response<LanguageDetection> response) {
                try {
                    if (response.isSuccessful()){
                        String lang = response.body().getLang();
                    } else {
                        //TODO написать обработку ошибок
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    //TODO написать обработку ошибок
                }
            }

            @Override
            public void onFailure(Call<LanguageDetection> call, Throwable t) {
                //TODO написать обработку ошибок
            }
        });
    }

    public void initDataBase (LanguageDictionare languageDictionare){
        Log.d(LOG_TAG, "Start initDataBase");

        Cursor cursor = getContentResolver().query(Contract.Language.CONTENT_URI,
                null, null, null, null);

        ArrayList<Language> listLanguageFromCursor = cursorAdapter.getListToCursor(cursor);
        ArrayList<Language> listLanguageFromResponse = languageDictionare.getListLanguage();

        if (!listLanguageFromResponse.equals(listLanguageFromCursor)){
            getContentResolver().delete(Contract.Language.CONTENT_URI, null, null);
            createNewLanguageTable(listLanguageFromResponse);
        }

        initListLanguage(languageDictionare);
    }

    private void initListLanguage(LanguageDictionare languageDictionare){
        if (listLanguage == null){
            listLanguage = languageDictionare.getListLanguage();
        } else {
            listLanguage.clear();
            listLanguage = languageDictionare.getListLanguage();
        }
    }

    private void createNewLanguageTable(ArrayList<Language> listLanguageFromResponse){
        Log.d(LOG_TAG, "Start createNewLanguageTable");

        for (Language language : listLanguageFromResponse){
            ContentValues cv = new ContentValues();
            cv.put(Contract.Language.COLUMN_CODE_LANGUAGE, language.getCodeLanguage());
            cv.put(Contract.Language.COLUMN_LANGUAGE, language.getLanguage());
            getContentResolver().insert(Contract.Language.CONTENT_URI, cv);
        }
    }

    public ArrayList<Language> getListLanguage(String codeLanguage){
        String selection = Contract.Language.COLUMN_CODE_LANGUAGE + " = ?";
        String[] selectionArgs = {codeLanguage};

        Cursor cursor = getContentResolver().query(Contract.Language.CONTENT_URI,
                null, selection, selectionArgs, null);

        return cursorAdapter.getListToCursor(cursor);
    }

    public String[] convertArrayList() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (Language item : listLanguage){
            String newItem = item.getLanguage();
            stringArrayList.add(newItem);
        }
        return stringArrayList.toArray(new String[stringArrayList.size()]);
    }

    public void addToHistoryFavoritesTable(String textTranslator, String responseTranslator,
                                            String translationDirection, int favorites){
        Log.d(LOG_TAG, "Start getcodeLanguageToRequest");

        String prepareResponseTranslator = responseTranslator.substring(1,
                responseTranslator.length()-1);
        ContentValues cv = new ContentValues();
        cv.put(Contract.HistoryFavorites.COLUMN_TRANSLATABLE_TEXT, textTranslator);
        cv.put(Contract.HistoryFavorites.COLUMN_TRANSLATED_TEXT, prepareResponseTranslator);
        cv.put(Contract.HistoryFavorites.COLUMN_TRANSLATION_DIRECTION, translationDirection);
        cv.put(Contract.HistoryFavorites.COLUMN_FAVORITE, 0);

        if (checkDuplicationInHistory(prepareResponseTranslator)){
            Uri uri = getContentResolver().insert(Contract.HistoryFavorites.CONTENT_URI, cv);
            try {
                rowIDHistoryFavorites = Integer.parseInt(uri.getLastPathSegment());
            } catch (NullPointerException e) {
                Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
            }
        }
    }

    private boolean checkDuplicationInHistory(String prepareResponseTranslator){
        Log.d(LOG_TAG, "Start checkDuplicationInHistory");
        String selection = Contract.HistoryFavorites.COLUMN_TRANSLATED_TEXT + "=?";
        String[] selectionArgs = {prepareResponseTranslator};

        Cursor cursor = getContentResolver().query(Contract.HistoryFavorites.CONTENT_URI,
                null, selection, selectionArgs, null);

        ArrayList<HistoryFavorites> historyFavorites = cursorAdapter.
                getArrayListHistoryFavoritesToCursor(cursor);

        if (historyFavorites.isEmpty()){
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<HistoryFavorites> getHistoryFavoritesArrayList(){
        Cursor cursor = getContentResolver().query(Contract.HistoryFavorites.CONTENT_URI,
                null, null, null, null);

        return cursorAdapter.getArrayListHistoryFavoritesToCursor(cursor);
    }

    public void addToDictionaryEntriesAllTable(List<Def> defList){
        Log.d(LOG_TAG, "Start addToDictionaryEntriesAllTable");

        int rowIDDE = -1;
        int rowIDTranslate = -1;
        int rowIDExample = -1;

        ContentValues cv = new ContentValues();

        if (defList.size() != 0) {

            for (Def item : defList
                    ) {
                Log.d(LOG_TAG, "Text = " + item.getText());
                Log.d(LOG_TAG, "Pos = " + item.getPos());

                cv.clear();
                cv.put(Contract.DictionaryEntries.COLUMN_DE_TEXT, item.getText());
                cv.put(Contract.DictionaryEntries.COLUMN_DE_PART_OF_SPEECH, item.getPos());
                cv.put(Contract.DictionaryEntries.COLUMN_DE_TRANSCRIPTION, item.getTs());
                cv.put(Contract.DictionaryEntries.COLUMN_HISTORY_FAVORITES_ID, rowIDHistoryFavorites);

                Uri uriDE = getContentResolver().insert(Contract.DictionaryEntries.CONTENT_URI_DE, cv);

                try {
                    rowIDDE = Integer.parseInt(uriDE.getLastPathSegment());
                } catch (NullPointerException e) {
                    Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
                }


                List<Tr> trList = item.getTr();
                Log.d(LOG_TAG, "trList.size = " + trList.size());



                for (Tr itemTr : trList
                        ) {
                    Log.d(LOG_TAG, "Tr Text = " + itemTr.getText());
                    Log.d(LOG_TAG, "gen Text = " + itemTr.getGen());

                    cv.clear();
                    cv.put(Contract.DictionaryEntries.COLUMN_TRANSLATE_TEXT, itemTr.getText());
                    cv.put(Contract.DictionaryEntries.COLUMN_TRANSLATE_GENDER, itemTr.getGen());
                    cv.put(Contract.DictionaryEntries.COLUMN_TRANSLATE_DE_ID, rowIDDE);
                    Uri uriTranslate = getContentResolver().insert(Contract.DictionaryEntries.CONTENT_URI_TRANSLATE,
                            cv);

                    try {
                        rowIDTranslate = Integer.parseInt(uriTranslate.getLastPathSegment());
                    } catch (NullPointerException e) {
                        Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
                    }

                    List<Syn> synList = itemTr.getSyn();

                    if (synList != null) {
                        Log.d(LOG_TAG, "synList.size = " + synList.size());

                        for (Syn itemSyn : synList
                                ) {

                            cv.clear();
                            cv.put(Contract.DictionaryEntries.COLUMN_SYNONYM_TEXT, itemSyn.getText());
                            cv.put(Contract.DictionaryEntries.COLUMN_SYNONYM_GENDER, itemSyn.getGen());
                            cv.put(Contract.DictionaryEntries.COLUMN_SYNONYM_TRANSLATE_ID, rowIDTranslate);
                            getContentResolver().insert(Contract.DictionaryEntries.CONTENT_URI_SYNONYM,
                                    cv);
                        }
                    }

                    List<Mean> meanList = itemTr.getMean();

                    if (meanList != null) {
                        Log.d(LOG_TAG, "meanList.size = " + meanList.size());

                        for (Mean itemMean : meanList
                                ) {
                            cv.clear();
                            cv.put(Contract.DictionaryEntries.COLUMN_MEANING_TEXT, itemMean.getText());
                            cv.put(Contract.DictionaryEntries.COLUMN_MEANING_TRANSLATE_ID, rowIDTranslate);
                            getContentResolver().insert(Contract.DictionaryEntries.CONTENT_URI_MEANING,
                                    cv);
                        }

                    } else {
                        Log.d(LOG_TAG, "meanList is null " + (meanList == null));
                    }

                    List<Ex> exList = itemTr.getEx();
                    if (exList != null) {
                        Log.d(LOG_TAG, "exList.size = " + exList.size());

                        for (Ex itemEx : exList
                                ) {
                            cv.clear();
                            cv.put(Contract.DictionaryEntries.COLUMN_EXAMPLE_TEXT, itemEx.getText());
                            cv.put(Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATE_ID, rowIDTranslate);
                            Uri uriExample = getContentResolver().insert(Contract.DictionaryEntries.CONTENT_URI_EXAMPLE,
                                    cv);

                            try {
                                rowIDExample = Integer.parseInt(uriExample.getLastPathSegment());
                            } catch (NullPointerException e) {
                                Log.e(LOG_TAG, "NullPointerException: " + e.getLocalizedMessage());
                            }

                            Log.d(LOG_TAG, "Ex Text = " + itemEx.getText());

                            List<Tr_> tr_List = itemEx.getTr();
                            if (tr_List != null) {
                                Log.d(LOG_TAG, "tr_List.size = " + tr_List.size());

                                for (Tr_ itemTr_ : tr_List
                                        ) {
                                    cv.clear();
                                    cv.put(Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATION_TEXT,
                                            itemTr_.getText());
                                    cv.put(Contract.DictionaryEntries.COLUMN_EXAMPLE_TRANSLATE_EXAMPLE_ID, rowIDExample);
                                    getContentResolver().insert(Contract.DictionaryEntries.CONTENT_URI_EXAMPLE_TRANSLATION,
                                            cv);
                                    Log.d(LOG_TAG, "tr_ Text = " + itemTr_.getText());
                                }

                            } else {
                                Log.d(LOG_TAG, "tr_List is null " + (tr_List == null));
                            }
                        }

                    } else {
                        Log.d(LOG_TAG, "exList it null " + (exList == null));
                    }
                }
            }
        }





//        if (checkDuplicationInHistory(prepareResponseTranslator)){
//            getContentResolver().insert(Contract.HistoryFavorites.CONTENT_URI, cv);
//        }
    }
}
