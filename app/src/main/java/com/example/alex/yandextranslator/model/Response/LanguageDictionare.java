package com.example.alex.yandextranslator.model.Response;

import com.example.alex.yandextranslator.model.Language.CodeLanguage;
import com.example.alex.yandextranslator.model.Language.Language;

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
