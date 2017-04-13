package com.example.alex.yandextranslator.model.response;

import com.example.alex.yandextranslator.model.language.CodeLanguage;
import com.example.alex.yandextranslator.model.language.Language;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alex on 12.04.17.
 */

public class LanguageDictionare {

    private ArrayList<String> listDirs;
    private HashMap <CodeLanguage, Language> hashMapLanguageDictionare;

    public LanguageDictionare(ArrayList<String> listDirs,
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
