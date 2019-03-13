package com.jezh.textsaver.entity.extension;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Common "id" part for all entities.
 * Allows to simplify entity's equals() and hashCode() to make indexing and looking up easier.
 * ALL id of any entities will be unique
 * */
// @MappedSuperclass: Designates a class whose mapping information is applied to the entities that inherit from it.
// A mapped superclass has no separate table defined for it.
// (briefly, to create base class for entities without @Entity and, therefore, without creating distinct table for this base class)
@MappedSuperclass
public abstract class AbstractIdentifier {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // FIXME: 23.11.2018 или вообще убрать стратегию, чтобы работал
    // единый id для всех страниц?
    @Getter
    @Setter
    protected Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractIdentifier) ) return false;
        return id.equals(((AbstractIdentifier) o).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "id = " + id + ", ";
    }
}
