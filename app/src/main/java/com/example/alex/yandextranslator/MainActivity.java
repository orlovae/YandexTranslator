package com.example.alex.yandextranslator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private TextView textView;
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
        }
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
                        textView.setText(response.body().getText().toString());
                    } else {
                        textView.setText(R.string.error_invalid_responce);
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
                        LanguageDictionare LanguageDictionare = response.body();
                        Log.d(LOG_TAG, "mapLanguage = " + LanguageDictionare);
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
        textView = (TextView)findViewById(R.id.text_view);
    }

    private String getEditText(EditText editText){
        String text = editText.getText().toString();
        if (text.length() == 0) text = "";
        return text;
    }

    private void error(){
        Toast.makeText(MainActivity.this, R.string.error_invalid_responce, Toast.LENGTH_LONG).show();
    }

}
