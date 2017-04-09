package com.example.alex.yandextranslator;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import com.example.alex.yandextranslator.PostModel;

/**
 * Created by alex on 08.04.17.
 */

public interface YandexTranslatorApi {

    @POST
    Call<List<PostModel>> getData(@Query("name") String resourceName, @Query("num") int count);
}
