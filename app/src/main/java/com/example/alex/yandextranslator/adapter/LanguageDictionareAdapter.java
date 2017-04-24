package com.example.alex.yandextranslator.adapter;

import com.example.alex.yandextranslator.model.Language;
import com.example.alex.yandextranslator.model.response.LanguageDictionare;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by alex on 12.04.17.
 */

public class LanguageDictionareAdapter implements JsonDeserializer<LanguageDictionare> {

    @Override
    public LanguageDictionare deserialize(JsonElement jsonElement, Type typeOfT,
                                   JsonDeserializationContext context)
            throws JsonParseException {
        ArrayList<String> listDirs = new ArrayList<>();

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray dirs = jsonObject.getAsJsonArray("dirs");
        /*имя dirs приходит в ответе переводчика*/
        for (JsonElement dir: dirs){
            listDirs.add(dir.getAsString());
        }

        ArrayList<Language> listLanguage = new ArrayList<>();

        JsonObject jsonObjectLang = jsonObject.get("langs").getAsJsonObject();
        /*имя langs приходит в ответе переводчика*/

        for (Map.Entry<String, JsonElement> entry:jsonObjectLang.entrySet()
             ) {
            String codeLanguage = entry.getKey();

            String valueTmp = entry.getValue().toString();
            String stringLanguage = valueTmp.substring(1, valueTmp.length()-1);

            Language language = new Language(codeLanguage, stringLanguage);

            listLanguage.add(language);
        }
        return new LanguageDictionare(listDirs, listLanguage);
    }
}
