package com.example.alex.yandextranslator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
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

    private final String URL = "https://translate.yandex.ru";
    private final String KEY = "trnsl.1.1.20170407T081255Z.343fc6903b3656af.58d14da04ebc826dbc32072d91d8e3034d99563f";


    private Gson gson = new GsonBuilder().create();

    private Retrofit retrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(URL)
            .build();

    private YandexTranslatorApi yandexTranslatorApi = retrofit.create(YandexTranslatorApi.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        buttonBehavior();
    }

    private void buttonBehavior() {
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(LOG_TAG, "Start onClick");
        switch (v.getId()) {
            case R.id.button:
                String textToYandex = getEditText(editText);
                Log.d(LOG_TAG, "textToYandex = " + textToYandex);

                Map<String, String> mapJson = new HashMap<>();
                mapJson.put("key", KEY);
                mapJson.put("text", textToYandex);
                mapJson.put("lang", "en-ru");

                Call<Object> call = yandexTranslatorApi.translate(mapJson);


                    call.enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {
                            try {
                                Map<String, String> map = gson.fromJson(response.body().toString(), Map.class);

                                for (Map.Entry entry : map.entrySet()) {
                                    if (entry.getKey().equals("text")) {
                                        textView.setText(entry.getValue().toString());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });
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

}
