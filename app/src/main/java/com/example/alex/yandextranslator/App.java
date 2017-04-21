package com.example.alex.yandextranslator;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.example.alex.yandextranslator.adapter.CursorToMapLanguageAdapter;
import com.example.alex.yandextranslator.adapter.LanguageDictionareAdapter;
import com.example.alex.yandextranslator.data.Contract;
import com.example.alex.yandextranslator.model.language.Language;
import com.example.alex.yandextranslator.model.response.LanguageDetection;
import com.example.alex.yandextranslator.model.response.LanguageDictionare;
import com.example.alex.yandextranslator.model.response.Translator;
import com.example.alex.yandextranslator.rest.ApiClient;
import com.example.alex.yandextranslator.rest.ApiDictionare;
import com.example.alex.yandextranslator.rest.ApiLanguageDetection;
import com.example.alex.yandextranslator.rest.ApiTranslator;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private ArrayList<Language> listLanguage;

    private String[] codeLangToRequest;
    private String responseTranslator;

    private Map<String, String> mapJson;

    private Gson gson;

    private final String KEY = "trnsl.1.1.20170407T081255Z.343fc6903b3656af.58d14da04ebc826dbc32072d91d8e3034d99563f";

    public String[] getCodeLangToRequest() {
        return codeLangToRequest;
    }

    public void setCodeLangToRequest(String[] codeLangToRequest) {
        this.codeLangToRequest = codeLangToRequest;
    }

    public String getResponseTranslator() {
        return responseTranslator;
    }

    public void setResponseTranslator(String responseTranslator) {
        this.responseTranslator = responseTranslator;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initDictionare();

        initApiLanguageDetection();

        initApiTranslator();
    }

    private void initDictionare() {
        createMapJson(null, "dictionare");
        initApiLanguageDictionare();
        responseLanguageDictionare();

    }

    public void createMapJson(String textToYandex, String key) {
        Log.d(LOG_TAG, "Start createMapJson");
        if (mapJson == null) {
            mapJson = new HashMap<>();
        } else {
            mapJson.clear();
        }

        switch (key) {
            case "dictionare":
//                Log.d(LOG_TAG, "Start createMapJson, case \"dictionare\"");
                mapJson.put("key", KEY);
                mapJson.put("ui", "ru");
//                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
            case "languageDetection":
//                Log.d(LOG_TAG, "Start createMapJson, case \"languageDetection\"");
                mapJson.put("key", KEY);
                mapJson.put("text", textToYandex);
//                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
            case "translator":
//                Log.d(LOG_TAG, "Start createMapJson, case \"translator\"");

                String lang = setLang(codeLangToRequest);
                Log.d(LOG_TAG, "lang = " + lang);

                mapJson.put("key", KEY);
                mapJson.put("text", textToYandex);
                mapJson.put("lang", lang);
//                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
        }
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

    private void initApiLanguageDictionare(){
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

    public void responseTranslator() {
        Log.d(LOG_TAG, "Start responseTranslator");
        Call<Translator> call = apiTranslator.translate(mapJson);

        call.enqueue(new Callback<Translator>() {
            @Override
            public void onResponse(Call<Translator> call, Response<Translator> response) {
                try {
                    if (response.isSuccessful()){
                        Log.d(LOG_TAG, "responseTranslator " + response.body().getText().toString());
                        responseTranslator = response.body().getText().toString();
                    } else {
                        responseTranslator = getString(R.string.error_invalid_responce);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "exeption onResponce " + e.toString());
                    //TODO написать обработку ошибок
                }
            }
            @Override
            public void onFailure(Call<Translator> call, Throwable t) {
                t.printStackTrace();
                Log.d(LOG_TAG, "exeption onFailure " + t.toString());
                //TODO написать обработку ошибок
            }
        });
    }

    public void responseLanguageDictionare(){
        Log.d(LOG_TAG, "Start createRequestLanguageDictionare");
        Call<LanguageDictionare> call = apiDictionare.languageDictionare(mapJson);

        call.enqueue(new Callback<LanguageDictionare>() {
            @Override
            public void onResponse(Call<LanguageDictionare> call, Response<LanguageDictionare> response) {
                try {
                    Log.d(LOG_TAG, "Start onResponse");
                    if (response.isSuccessful()){
                        response.body().toString();
                        LanguageDictionare languageDictionare = response.body();
                        initDataBase(languageDictionare);
                        Log.d(LOG_TAG, "listLanguage = " + languageDictionare.getListLanguage().size());
                    } else {
                        //TODO написать обработку ошибок
                    }

                } catch (Exception e) {
                    Log.d(LOG_TAG, "Start Exception");
                    e.printStackTrace();
                    //TODO написать обработку ошибок
                }
            }

            @Override
            public void onFailure(Call<LanguageDictionare> call, Throwable t) {
                Log.d(LOG_TAG, "Start onFailure");
                Log.d(LOG_TAG, "exeption = " + t.toString());
                //TODO написать обработку ошибок
            }
        });
    }

    private void initDataBase (LanguageDictionare languageDictionare){
        Log.d(LOG_TAG, "Start initDataBase");

        Cursor cursor = getContentResolver().query(Contract.Language.CONTENT_URI,
                null, null, null, null);

        CursorToMapLanguageAdapter cursorToMapLanguageAdapter = new CursorToMapLanguageAdapter(cursor);

        ArrayList<Language> listLanguageFromCursor = cursorToMapLanguageAdapter.getListToCursor();
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

        ContentValues cv = new ContentValues();

        for (Language language : listLanguageFromResponse){
            cv.put(Contract.Language.COLUMN_CODE_LANGUAGE, language.getCodeLanguage());
            cv.put(Contract.Language.COLUMN_LANGUAGE, language.getLanguage());
        }
        getContentResolver().insert(Contract.Language.CONTENT_URI, cv);
    }

    public ArrayList<Language> getListLanguage(String codeLanguage){
        String selection = Contract.Language.COLUMN_CODE_LANGUAGE + "=?";
        String[] selectionArgs = {codeLanguage};

        Cursor cursor = getContentResolver().query(Contract.Language.CONTENT_URI,
                null, selection, selectionArgs, null);
        CursorToMapLanguageAdapter cursorToMapLanguageAdapter = new CursorToMapLanguageAdapter(cursor);

        return cursorToMapLanguageAdapter.getListToCursor();
    }

    public String[] convertArrayList() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (Language item : listLanguage){
            String newItem = item.getLanguage();
            stringArrayList.add(newItem);
        }
        return stringArrayList.toArray(new String[stringArrayList.size()]);
    }
}
