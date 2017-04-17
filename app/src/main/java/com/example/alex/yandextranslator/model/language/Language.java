package com.example.alex.yandextranslator.model.language;

/**
 * Created by alex on 11.04.17.
 */

public class Language {

    private String language;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Language)) return false;

        Language language1 = (Language) o;

        return language != null ? language.equals(language1.language) : language1.language == null;

    }

    @Override
    public int hashCode() {
        return language != null ? language.hashCode() : 0;
    }
}
