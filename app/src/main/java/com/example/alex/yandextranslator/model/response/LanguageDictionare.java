package com.example.alex.yandextranslator.model.response;

import com.example.alex.yandextranslator.model.language.Language;

import java.util.ArrayList;

/**
 * Created by alex on 12.04.17.
 */

public class LanguageDictionare {

    private ArrayList<String> listDirs;
    private ArrayList<Language> listLanguage;

    public LanguageDictionare(ArrayList<String> listDirs, ArrayList<Language> listLanguage) {
        this.listDirs = listDirs;
        this.listLanguage = listLanguage;
    }

    public ArrayList<String> getListDirs() {
        return listDirs;
    }

    public void setListDirs(ArrayList<String> listDirs) {
        this.listDirs = listDirs;
    }

    public ArrayList<Language> getListLanguage() {
        return listLanguage;
    }

    public void setListLanguage(ArrayList<Language> listLanguage) {
        this.listLanguage = listLanguage;
    }
}
