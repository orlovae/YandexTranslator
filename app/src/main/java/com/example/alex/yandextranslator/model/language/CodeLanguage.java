package com.example.alex.yandextranslator.model.language;

/**
 * Created by alex on 12.04.17.
 */

public class CodeLanguage {

    private String codeLanguage;

    public String getCodeLanguage() {
        return codeLanguage;
    }

    public void setCodeLanguage(String codeLanguage) {
        this.codeLanguage = codeLanguage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodeLanguage)) return false;

        CodeLanguage that = (CodeLanguage) o;

        return codeLanguage != null ? codeLanguage.equals(that.codeLanguage) : that.codeLanguage == null;

    }

    @Override
    public int hashCode() {
        return codeLanguage != null ? codeLanguage.hashCode() : 0;
    }
}
