package com.example.alex.yandextranslator.model.response.dictionaryentry;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by alex on 23.04.17.
 */

public class Head {

    @Override
    public int hashCode() {
        return new HashCodeBuilder().toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Head) == false) {
            return false;
        }
        Head rhs = ((Head) other);
        return new EqualsBuilder().isEquals();
    }
}
