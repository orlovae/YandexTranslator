package com.example.alex.yandextranslator;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by alex on 08.04.17.
 */

public interface YandexTranslatorApi {
    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Call<Object> translate(@FieldMap Map<String, String> map);
}
