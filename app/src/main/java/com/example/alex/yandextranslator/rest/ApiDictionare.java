package com.example.alex.yandextranslator.rest;

import com.example.alex.yandextranslator.model.LanguageDictionareResponse;
import com.example.alex.yandextranslator.model.MapLanguage;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by alex on 11.04.17.
 */

public interface ApiDictionare {

    @FormUrlEncoded
    @POST("api/v1.5/tr.json/getLangs")
    Call<MapLanguage> languageDictionare(@FieldMap Map<String, String> map);
}
