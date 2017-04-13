package com.example.alex.yandextranslator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 12.04.17.
 */

public class MapLanguage {

    private HashMap <CodeLanguage, Language> mapLanguages;

    public HashMap<CodeLanguage, Language> getMapLanguages() {
        return mapLanguages;
    }

    public void setMapLanguages(HashMap<CodeLanguage, Language> mapLanguages) {
        this.mapLanguages = mapLanguages;
    }
}
