package com.example.alex.yandextranslator;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.yandextranslator.adapter.CursorToMapLanguageAdapter;
import com.example.alex.yandextranslator.data.Contract;
import com.example.alex.yandextranslator.model.language.CodeLanguage;
import com.example.alex.yandextranslator.model.language.Language;
import com.example.alex.yandextranslator.model.response.LanguageDetection;
import com.example.alex.yandextranslator.model.response.LanguageDictionare;
import com.example.alex.yandextranslator.model.response.Translator;
import com.example.alex.yandextranslator.adapter.LanguageDictionareAdapter;
import com.example.alex.yandextranslator.rest.ApiClient;
import com.example.alex.yandextranslator.rest.ApiDictionare;
import com.example.alex.yandextranslator.rest.ApiLanguageDetection;
import com.example.alex.yandextranslator.rest.ApiTranslator;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>{
    private final String LOG_TAG = this.getClass().getSimpleName();

    private TextView textViewTranslate;
    private TextView textViewLanguageText, textViewRevers, textViewLanguageTranslation;
    private EditText editText;
    private Button button;

    private ApiTranslator apiTranslator;
    private ApiLanguageDetection apiLanguageDetection;
    private ApiDictionare apiDictionare;

    private Map<String, String> mapJson;

    private Gson gson;

    private final String KEY = "trnsl.1.1.20170407T081255Z.343fc6903b3656af.58d14da04ebc826dbc32072d91d8e3034d99563f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDictionare();

        initView();

        initApiLanguageDetection();

        initApiTranslator();

        buttonBehavior();

    }

    private void initDictionare() {
        Log.d(LOG_TAG, "Start initDictionare");

        createMapJson(null, "dictionare");
        initApiLanguageDictionare();
        responseLanguageDictionare();

    }

    private void buttonBehavior() {
        button.setOnClickListener(this);
        textViewRevers.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
//        Log.d(LOG_TAG, "Start onClick");
        switch (v.getId()) {
            case R.id.button:
                String textToYandex = getEditText(editText);
//                Log.d(LOG_TAG, "textToYandex = " + textToYandex);

                createMapJson(textToYandex, "languageDetection");

                responseLanguageDetection();

                createMapJson(textToYandex, "translator");

                responseTranslator();

            break;
            case R.id.text_view_revers:
                reversTextViewLanguageSelect();
                break;
        }
    }

    private void reversTextViewLanguageSelect(){
        String stringTextViewLanguageText = textViewLanguageText.getText().toString();
        String stringTextViewLanguageTranslation = textViewLanguageTranslation.getText().toString();
        textViewLanguageText.setText(stringTextViewLanguageTranslation);
        textViewLanguageTranslation.setText(stringTextViewLanguageText);
    }

    private void responseLanguageDetection(){
        Call<LanguageDetection> call = apiLanguageDetection.languageDetection(mapJson);

        call.enqueue(new Callback<LanguageDetection>() {
            @Override
            public void onResponse(Call<LanguageDetection> call, Response<LanguageDetection> response) {
                try {
                    if (response.isSuccessful()){
                        String lang = response.body().getLang();
                        Toast.makeText(MainActivity.this, "Language is " + lang, Toast.LENGTH_SHORT).show();
                    } else {
                        error();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    error();
                }
            }

            @Override
            public void onFailure(Call<LanguageDetection> call, Throwable t) {
                error();
            }
        });
    }

    private void responseTranslator() {
        Call<Translator> call = apiTranslator.translate(mapJson);

        call.enqueue(new Callback<Translator>() {
            @Override
            public void onResponse(Call<Translator> call, Response<Translator> response) {
                try {
                    if (response.isSuccessful()){
                        textViewTranslate.setText(response.body().getText().toString());
                    } else {
                        textViewTranslate.setText(R.string.error_invalid_responce);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    error();
                }
            }
            @Override
            public void onFailure(Call<Translator> call, Throwable t) {
                t.printStackTrace();
                error();
            }
        });
    }

    private void responseLanguageDictionare(){
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
                        Log.d(LOG_TAG, "mapLanguage = " + languageDictionare);
                    } else {
                        error();
                    }

                } catch (Exception e) {
                    Log.d(LOG_TAG, "Start Exception");
                    e.printStackTrace();
                    error();
                }
            }

            @Override
            public void onFailure(Call<LanguageDictionare> call, Throwable t) {
                Log.d(LOG_TAG, "Start onFailure");
                Log.d(LOG_TAG, "exeption = " + t.toString());
                error();
            }
        });
    }

    private void createMapJson(String textToYandex, String key) {
        Log.d(LOG_TAG, "Start createMapJson");
        if (mapJson == null) {
            mapJson = new HashMap<>();
        } else {
            mapJson.clear();
        }

        switch (key) {
            case "dictionare":
                Log.d(LOG_TAG, "Start createMapJson, case \"dictionare\"");
                mapJson.put("key", KEY);
                mapJson.put("ui", "ru");
                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
            case "languageDetection":
                Log.d(LOG_TAG, "Start createMapJson, case \"languageDetection\"");
                mapJson.put("key", KEY);
                mapJson.put("text", textToYandex);
                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
            case "translator":
                Log.d(LOG_TAG, "Start createMapJson, case \"translator\"");
                mapJson.put("key", KEY);
                mapJson.put("text", textToYandex);
                mapJson.put("lang", "en-ru");
                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
        }
    }

    private void initView() {
        editText = (EditText)findViewById(R.id.edit_text);
        button = (Button)findViewById(R.id.button);
        textViewTranslate = (TextView)findViewById(R.id.text_view_translate);
        textViewLanguageText = (TextView)findViewById(R.id.text_view_language_text);
        textViewRevers = (TextView)findViewById(R.id.text_view_revers);
        textViewRevers.setText(R.string.revers);
        textViewLanguageTranslation = (TextView)findViewById(R.id.text_view_language_translator);
        setIntiTextViewSelectLanguage();
    }

    private void setIntiTextViewSelectLanguage(){
        String stringTextViewLanguageText = getStringLanguage("en");
        String stringTextViewLanguageTranslation = getStringLanguage("ru");
        textViewLanguageText.setText(stringTextViewLanguageText);
        textViewLanguageTranslation.setText(stringTextViewLanguageTranslation);
    }

    private String getStringLanguage(String codeLanguage){

        String selection = Contract.Language.COLUMN_CODE_LANGUAGE + "=?";

        String[] selectionArgs = {codeLanguage};

        Cursor cursor = getContentResolver().query(Contract.Language.CONTENT_URI,
                null, selection, selectionArgs, null);
        CursorToMapLanguageAdapter cursorToMapLanguageAdapter = new CursorToMapLanguageAdapter(cursor);

        return cursorToMapLanguageAdapter.getStringLanguageToCursor();
    }

    private String getEditText(EditText editText){
        String text = editText.getText().toString();
        if (text.length() == 0) text = "";
        return text;
    }

    private void error(){
        Toast.makeText(MainActivity.this, R.string.error_invalid_responce, Toast.LENGTH_LONG).show();
    }

    private void initDataBase (LanguageDictionare languageDictionare){
        Log.d(LOG_TAG, "Start initDataBase");

        Cursor cursor = getContentResolver().query(Contract.Language.CONTENT_URI, null, null, null,
                null);

        CursorToMapLanguageAdapter cursorToMapLanguageAdapter = new CursorToMapLanguageAdapter(cursor);

        HashMap <CodeLanguage, Language> hashMapLanguageDictionareFromCursor =
                cursorToMapLanguageAdapter.getHashMapToCursor();
        HashMap <CodeLanguage, Language> hashMapLanguageDictionareFromResponse =
                languageDictionare.getHashMapLanguageDictionare();
//        Log.d(LOG_TAG, "Сравнение баз данных: " + hashMapLanguageDictionareFromResponse.equals(hashMapLanguageDictionareFromCursor));
//        Log.d(LOG_TAG, "hashMapLanguageDictionareFromResponse hashCode: " + hashMapLanguageDictionareFromResponse.hashCode());
//        Log.d(LOG_TAG, "hashMapLanguageDictionareFromCursor hashCode: " + hashMapLanguageDictionareFromCursor.hashCode());
        if (!hashMapLanguageDictionareFromResponse.equals(hashMapLanguageDictionareFromCursor)){
            getContentResolver().delete(Contract.Language.CONTENT_URI, null, null);
            createNewLanguageTable(hashMapLanguageDictionareFromResponse);
        }
    }

    private void createNewLanguageTable(HashMap <CodeLanguage, Language> hashMapLanguageDictionareFromResponse){
        Log.d(LOG_TAG, "Start createNewLanguageTable");
        for (Map.Entry<CodeLanguage, Language> entry : hashMapLanguageDictionareFromResponse.entrySet()){
            ContentValues cv = new ContentValues();
            cv.put(Contract.Language.COLUMN_CODE_LANGUAGE, entry.getKey().getCodeLanguage());
            cv.put(Contract.Language.COLUMN_LANGUAGE, entry.getValue().getLanguage());

            getContentResolver().insert(Contract.Language.CONTENT_URI, cv);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                Contract.Language._ID,
                Contract.Language.COLUMN_CODE_LANGUAGE,
                Contract.Language.COLUMN_LANGUAGE
        };
        return new CursorLoader(this,
                Contract.Language.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
