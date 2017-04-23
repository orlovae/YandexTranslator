package com.example.alex.yandextranslator.model.response.dictionaryentry;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by alex on 23.04.17.
 */

public class DictionaryEntry {

    @SerializedName("head")
    @Expose
    private Head head;
    @SerializedName("def")
    @Expose
    private List<Def> def = null;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public List<Def> getDef() {
        return def;
    }

    public void setDef(List<Def> def) {
        this.def = def;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(head).append(def).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DictionaryEntry) == false) {
            return false;
        }
        DictionaryEntry rhs = ((DictionaryEntry) other);
        return new EqualsBuilder().append(head, rhs.head).append(def, rhs.def).isEquals();
    }
}
