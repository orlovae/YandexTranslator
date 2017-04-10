package com.example.alex.yandextranslator.rest;

import com.example.alex.yandextranslator.model.TranslatorResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by alex on 08.04.17.
 */

public interface ApiTranslator {
    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Call<TranslatorResponse> translate(@FieldMap Map<String, String> map);
}
