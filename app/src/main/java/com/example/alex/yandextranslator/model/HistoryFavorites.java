package com.example.alex.yandextranslator.model;

/**
 * Created by alex on 22.04.17.
 */

public class HistoryFavorites {
    private int id;
    private String translatableText;
    private String translatedText;
    private String translationDirection;
    private boolean favorite;

    public HistoryFavorites(int id, String translatableText, String translatedText,
                            String translationDirection, boolean favorite) {
        this.id = id;
        this.translatableText = translatableText;
        this.translatedText = translatedText;
        this.translationDirection = translationDirection;
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTranslatableText() {
        return translatableText;
    }

    public void setTranslatableText(String translatableText) {
        this.translatableText = translatableText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getTranslationDirection() {
        return translationDirection;
    }

    public void setTranslationDirection(String translationDirection) {
        this.translationDirection = translationDirection;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
