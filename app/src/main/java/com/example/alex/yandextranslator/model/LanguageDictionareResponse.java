package com.example.alex.yandextranslator.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 11.04.17.
 */

public class LanguageDictionareResponse {

    @SerializedName("dirs")
    @Expose
    private List<String> dirs = null;
    @SerializedName("langs")
    @Expose
    private MapLanguage langs = null;

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(List<String> dirs) {
        this.dirs = dirs;
    }

    public MapLanguage getLangs() {
        return langs;
    }

    public void setLangs(MapLanguage langs) {
        this.langs = langs;
    }
}
