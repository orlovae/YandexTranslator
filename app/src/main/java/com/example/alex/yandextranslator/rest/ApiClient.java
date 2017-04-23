package com.example.alex.yandextranslator.rest;

import android.util.Log;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alex on 10.04.17.
 */

public class ApiClient {
    private static final String LOG_TAG = "ApiClient";
    public static final String TRANSLATE_URL = "https://translate.yandex.net";
    public static final String DICTIONARY_ENTRY_URL = "https://dictionary.yandex.net";
    private static Retrofit retrofit = null;

    static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor
            (message -> Log.d("Retrofit", message)).setLevel(HttpLoggingInterceptor.Level.BODY);
    static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor ).build();


    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().client(client)
                    .baseUrl(TRANSLATE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient(Gson gson){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().client(client)
                    .baseUrl(TRANSLATE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientDictionaryEntry(){
        Log.d(LOG_TAG, "Start retrofit getClientDictionaryEntry");
        retrofit = new Retrofit.Builder().client(client)
                .baseUrl("https://dictionary.yandex.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
