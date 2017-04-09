package com.example.alex.yandextranslator;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import com.example.alex.yandextranslator.PostModel;

/**
 * Created by alex on 08.04.17.
 */

public interface YandexTranslatorApi {
    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    Call<Object> translate(@FieldMap Map<String, String> map);
}
