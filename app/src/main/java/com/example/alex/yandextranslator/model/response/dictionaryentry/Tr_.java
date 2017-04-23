package com.example.alex.yandextranslator.model.response.dictionaryentry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by alex on 23.04.17.
 */

public class Tr_ {

    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(text).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tr_) == false) {
            return false;
        }
        Tr_ rhs = ((Tr_) other);
        return new EqualsBuilder().append(text, rhs.text).isEquals();
    }
}
