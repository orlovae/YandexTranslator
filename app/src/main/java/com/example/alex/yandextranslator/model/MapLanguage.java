package com.example.alex.yandextranslator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 12.04.17.
 */

public class MapLanguage {

    private ArrayList<String> listDirs;
    private HashMap <CodeLanguage, Language> hashMapLanguageDictionare;

    public MapLanguage(ArrayList<String> listDirs,
                       HashMap<CodeLanguage, Language> hashMapLanguageDictionare) {
        this.listDirs = listDirs;
        this.hashMapLanguageDictionare = hashMapLanguageDictionare;
    }

    public ArrayList<String> getListDirs() {
        return listDirs;
    }

    public void setListDirs(ArrayList<String> listDirs) {
        this.listDirs = listDirs;
    }

    public HashMap<CodeLanguage, Language> getHashMapLanguageDictionare() {
        return hashMapLanguageDictionare;
    }

    public void setHashMapLanguageDictionare(HashMap<CodeLanguage, Language> hashMapLanguageDictionare) {
        this.hashMapLanguageDictionare = hashMapLanguageDictionare;
    }
}
