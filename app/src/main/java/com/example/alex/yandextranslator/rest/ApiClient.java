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
    public static final String BASE_URL = "https://translate.yandex.net";
    private static Retrofit retrofit = null;

    /*Этот метод используется для логгирования запросов, ответов с/на севрера. Для его работы
    пришлось подключить Java 8 Gradle*/
    static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor
            (message -> Log.d("Retrofit", message)).setLevel(HttpLoggingInterceptor.Level.BODY);
    static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor ).build();


    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClient(Gson gson){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
