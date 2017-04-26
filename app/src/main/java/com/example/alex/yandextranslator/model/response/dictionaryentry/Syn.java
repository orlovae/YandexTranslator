package com.example.alex.yandextranslator.model.response.dictionaryentry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by alex on 23.04.17.
 */

public class Syn {
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("gen")
    @Expose
    private String gen;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(text).append(gen).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Syn) == false) {
            return false;
        }
        Syn rhs = ((Syn) other);
        return new EqualsBuilder().append(text, rhs.text).append(gen, rhs.gen).isEquals();
    }
}
