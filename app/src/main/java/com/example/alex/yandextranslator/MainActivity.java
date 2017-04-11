package com.example.alex.yandextranslator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.yandextranslator.model.LanguageDetectionResponse;
import com.example.alex.yandextranslator.model.LanguageDictionareResponse;
import com.example.alex.yandextranslator.model.TranslatorResponse;
import com.example.alex.yandextranslator.rest.ApiClient;
import com.example.alex.yandextranslator.rest.ApiDictionare;
import com.example.alex.yandextranslator.rest.ApiLanguageDetection;
import com.example.alex.yandextranslator.rest.ApiTranslator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG_TAG = this.getClass().getSimpleName();

    private TextView textView;
    private EditText editText;
    private Button button;

    private ApiTranslator apiTranslator;
    private ApiLanguageDetection apiLanguageDetection;
    private ApiDictionare apiDictionare;

    private Map<String, String> mapJson;

    private final String URL = "https://translate.yandex.net/";
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
        createRequestLanguageDictionare();

    }

    private void buttonBehavior() {
        button.setOnClickListener(this);
    }

    private void initApiLanguageDictionare(){
        apiDictionare = ApiClient.getClient().create(ApiDictionare.class);
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

                createRequestLanguageDetection();

                createMapJson(textToYandex, "translator");

                createRequestTranslator();

            break;
        }
    }

    private void createRequestLanguageDetection(){
        Call<LanguageDetectionResponse> call = apiLanguageDetection.languageDetection(mapJson);

        call.enqueue(new Callback<LanguageDetectionResponse>() {
            @Override
            public void onResponse(Call<LanguageDetectionResponse> call, Response<LanguageDetectionResponse> response) {
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
            public void onFailure(Call<LanguageDetectionResponse> call, Throwable t) {
                error();
            }
        });
    }

    private void createRequestTranslator() {
        Call<TranslatorResponse> call = apiTranslator.translate(mapJson);

        call.enqueue(new Callback<TranslatorResponse>() {
            @Override
            public void onResponse(Call<TranslatorResponse> call, Response<TranslatorResponse> response) {
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
            public void onFailure(Call<TranslatorResponse> call, Throwable t) {
                t.printStackTrace();
                error();
            }
        });
    }

    private void createRequestLanguageDictionare(){
        Call<LanguageDictionareResponse> call = apiDictionare.languageDictionare(mapJson);
        final Gson gson = new GsonBuilder().create();

        call.enqueue(new Callback<LanguageDictionareResponse>() {
            @Override
            public void onResponse(Call<LanguageDictionareResponse> call, Response<LanguageDictionareResponse> response) {
                try {
                    if (response.isSuccessful()){
                        response.body().toString();
//                        Map<String, String> map = gson.fromJson(response.body().toString(), Map.class);
//
//                        for (Map.Entry entry : map.entrySet()){
//                            Log.d(LOG_TAG, entry.getKey() + ":" + entry.getValue());
//                        }
                        Log.d(LOG_TAG, "response = " + response.body().toString());

                    } else {
                        error();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    error();
                }
            }

            @Override
            public void onFailure(Call<LanguageDictionareResponse> call, Throwable t) {
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
                Log.d(LOG_TAG, "mapJson = " + mapJson.toString());
                break;
            case "languageDetection":
                Log.d(LOG_TAG, "Start createMapJson, case \"languageDetection\"");
                mapJson.put("key", KEY);
                mapJson.put("text", textToYandex);
                break;
            case "translator":
                Log.d(LOG_TAG, "Start createMapJson, case \"translator\"");
                mapJson.put("key", KEY);
                mapJson.put("text", textToYandex);
                mapJson.put("lang", "en-ru");
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
