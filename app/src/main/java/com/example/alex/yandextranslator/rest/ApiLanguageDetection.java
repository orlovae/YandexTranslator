package com.example.alex.yandextranslator.rest;

import com.example.alex.yandextranslator.model.Response.LanguageDetection;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by alex on 10.04.17.
 */

public interface ApiLanguageDetection {
    @FormUrlEncoded
    @POST("api/v1.5/tr.json/detect")
    Call<LanguageDetection> languageDetection(@FieldMap Map<String, String> map);
}
