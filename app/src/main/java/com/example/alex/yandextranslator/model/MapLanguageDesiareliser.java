package com.example.alex.yandextranslator.model;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 12.04.17.
 */

public class MapLanguageDesiareliser implements JsonDeserializer<MapLanguage> {
    private final String LOG_TAG = this.getClass().getSimpleName();

    @Override
    public MapLanguage deserialize(JsonElement jsonElement, Type typeOfT,
                                   JsonDeserializationContext context)
            throws JsonParseException {
        Log.d(LOG_TAG, "Start deserialize");

        ArrayList<String> listDirs = new ArrayList<>();

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray dirs = jsonObject.getAsJsonArray("dirs");
        for (JsonElement dir: dirs){
            listDirs.add(dir.getAsString());
        }

        HashMap<CodeLanguage, Language> hashMapLanguageDictionare = new HashMap<>();

        JsonObject jsonObjectLang = jsonObject.get("langs").getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry:jsonObjectLang.entrySet()
             ) {
            CodeLanguage codeLanguageKey = new CodeLanguage();
            codeLanguageKey.setCodeLanguage(entry.getKey());
            Log.d(LOG_TAG, "key = " + codeLanguageKey.getCodeLanguage());

            Language languageValue = new Language();
            String valueTmp = entry.getValue().toString();
            String value = valueTmp.substring(1, valueTmp.length()-1);
            Log.d(LOG_TAG, "value = " + value);
            languageValue.setLanguage(value);
            hashMapLanguageDictionare.put(codeLanguageKey, languageValue);
        }
        return new MapLanguage(listDirs, hashMapLanguageDictionare);
    }
}
