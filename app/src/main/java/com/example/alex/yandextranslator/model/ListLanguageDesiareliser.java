package com.example.alex.yandextranslator.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 12.04.17.
 */

public class ListLanguageDesiareliser /*implements JsonDeserializer<ListLanguage>*/ {
//    @Override
//    public ListLanguage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//        JsonObject jsonObject = json.getAsJsonObject();
//        List<Language> languages = new ArrayList<Language>();
//        for (Map.Entry<String, JsonElement> entry:jsonObject.entrySet()
//             ) {
//            Language language = context.deserialize(entry.getValue(), Language.class);
//            languages.add(language);
//        }
//
//        return new ListLanguage(languages);
//    }
}
