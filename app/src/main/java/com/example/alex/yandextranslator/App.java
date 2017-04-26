package com.example.alex.yandextranslator;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.alex.yandextranslator.adapter.CursorAdapter;
import com.example.alex.yandextranslator.adapter.LanguageDictionareAdapter;
import com.example.alex.yandextranslator.data.Contract;
import com.example.alex.yandextranslator.model.HistoryFavorites;
import com.example.alex.yandextranslator.model.Language;
import com.example.alex.yandextranslator.model.response.LanguageDetection;
import com.example.alex.yandextranslator.model.response.LanguageDictionare;
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
    private ApiTranslator apiTranslator;
    private ApiLanguageDetection apiLanguageDetection;
    private ApiDictionare apiDictionare;

    private ArrayList<Language> listLanguage;

    private String[] codeLangToRequest;

    private Map<String, String> mapJson;

    private Gson gson;

    private CursorAdapter cursorAdapter;

    private final String KEY_API_YANDEX_TRANSLATOR = "trnsl.1.1.20170407T081255Z.343fc6903b3656af.58d14da04ebc826dbc32072d91d8e3034d99563f";
    public static final String KEY_MAP_JSON_DICTIONARE = "dictionare";
    public static final String KEY_MAP_JSON_TRANSLATOR = "translator";
    public static final String KEY_MAP_JSON_LANGUAGE_DETECTION = "languageDetection";
    public static final String KEY_MAP_JSON_KEY = "key";
    public static final String KEY_MAP_JSON_UI = "ui";
    public static final String KEY_MAP_JSON_TEXT = "text";
    public static final String KEY_MAP_JSON_LANG = "lang";

    public void setCodeLangToRequest(String[] codeLangToRequest) {
        this.codeLangToRequest = codeLangToRequest;
    }

    public ApiTranslator getApiTranslator() {
        return apiTranslator;
    }

    public ApiDictionare getApiDictionare() {
        return apiDictionare;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initCursorAdapter();

        initApiLanguageDictionare();

        initApiLanguageDetection();

        initApiTranslator();
    }

    private void initCursorAdapter(){
        if (cursorAdapter == null) {
            cursorAdapter = new CursorAdapter();
        }
    }

    public Map<String, String> createMapJson(String textToYandex, String key) {
        if (mapJson == null) {
            mapJson = new HashMap<>();
        } else {
            mapJson.clear();
        }

        switch (key) {
            case KEY_MAP_JSON_DICTIONARE:
                mapJson.put(KEY_MAP_JSON_KEY, KEY_API_YANDEX_TRANSLATOR);
                mapJson.put(KEY_MAP_JSON_UI, "ru");
                break;
            case KEY_MAP_JSON_LANGUAGE_DETECTION:
                mapJson.put(KEY_MAP_JSON_KEY, KEY_API_YANDEX_TRANSLATOR);
                mapJson.put(KEY_MAP_JSON_TEXT, textToYandex);
                break;
            case KEY_MAP_JSON_TRANSLATOR:
                String lang = setLang(codeLangToRequest);

                mapJson.put(KEY_MAP_JSON_KEY, KEY_API_YANDEX_TRANSLATOR);
                mapJson.put(KEY_MAP_JSON_TEXT, textToYandex);
                mapJson.put(KEY_MAP_JSON_LANG, lang);
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
        /*Пока данный метод не используется*/
        Call<LanguageDetection> call = apiLanguageDetection.languageDetection(mapJson);

        call.enqueue(new Callback<LanguageDetection>() {
            @Override
            public void onResponse(Call<LanguageDetection> call, Response<LanguageDetection> response) {
                try {
                    if (response.isSuccessful()){
                        String lang = response.body().getLang();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<LanguageDetection> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void initDataBase (LanguageDictionare languageDictionare){
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
        String prepareResponseTranslator = responseTranslator.substring(1,
                responseTranslator.length()-1);
        ContentValues cv = new ContentValues();
        cv.put(Contract.HistoryFavorites.COLUMN_TRANSLATABLE_TEXT, textTranslator);
        cv.put(Contract.HistoryFavorites.COLUMN_TRANSLATED_TEXT, prepareResponseTranslator);
        cv.put(Contract.HistoryFavorites.COLUMN_TRANSLATION_DIRECTION, translationDirection);
        cv.put(Contract.HistoryFavorites.COLUMN_FAVORITE, 0);

        if (checkDuplicationInHistory(prepareResponseTranslator)){
            getContentResolver().insert(Contract.HistoryFavorites.CONTENT_URI, cv);
        }
    }

    private boolean checkDuplicationInHistory(String prepareResponseTranslator){
        String selection = Contract.HistoryFavorites.COLUMN_TRANSLATED_TEXT + " = ?";
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
}
