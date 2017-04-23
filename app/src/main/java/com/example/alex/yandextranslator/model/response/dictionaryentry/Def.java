package com.example.alex.yandextranslator.model.response.dictionaryentry;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by alex on 23.04.17.
 */

public class Def {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("pos")
    @Expose
    private String pos;
    @SerializedName("tr")
    @Expose
    private List<Tr> tr = null;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public List<Tr> getTr() {
        return tr;
    }

    public void setTr(List<Tr> tr) {
        this.tr = tr;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(text).append(pos).append(tr).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Def) == false) {
            return false;
        }
        Def rhs = ((Def) other);
        return new EqualsBuilder().append(text, rhs.text).append(pos, rhs.pos).append(tr, rhs.tr).isEquals();
    }
}
