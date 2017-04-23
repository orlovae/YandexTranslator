package com.example.alex.yandextranslator.model.response.dictionaryentry;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by alex on 23.04.17.
 */

public class Ex {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("tr")
    @Expose
    private List<Tr_> tr = null;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Tr_> getTr() {
        return tr;
    }

    public void setTr(List<Tr_> tr) {
        this.tr = tr;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(text).append(tr).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Ex) == false) {
            return false;
        }
        Ex rhs = ((Ex) other);
        return new EqualsBuilder().append(text, rhs.text).append(tr, rhs.tr).isEquals();
    }
}
