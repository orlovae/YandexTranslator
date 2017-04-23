package com.example.alex.yandextranslator.rest;

import com.example.alex.yandextranslator.model.response.dictionaryentry.DictionaryEntry;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by alex on 23.04.17.
 */

public interface ApiDictionaryEntry {

    @FormUrlEncoded
    @POST("api/v1/dicservice.json/lookup")
    Call<DictionaryEntry> dictionaryEntry (@FieldMap Map<String, String> map);
}
