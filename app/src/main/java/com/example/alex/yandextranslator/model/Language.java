package com.example.alex.yandextranslator.model;

/**
 * Created by alex on 11.04.17.
 */

public class Language {

    private String codeLanguage;

    private String language;

    public Language(String codeLanguage, String language) {
        this.codeLanguage = codeLanguage;
        this.language = language;
    }

    public String getCodeLanguage() {
        return codeLanguage;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Language)) return false;

        Language language1 = (Language) o;

        if (codeLanguage != null ? !codeLanguage.equals(language1.codeLanguage) :
                language1.codeLanguage != null)
            return false;
        return language != null ? language.equals(language1.language) : language1.language == null;

    }

    @Override
    public int hashCode() {
        int result = codeLanguage != null ? codeLanguage.hashCode() : 0;
        result = 31 * result + (language != null ? language.hashCode() : 0);
        return result;
    }
}

