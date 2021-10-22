package org.lukitasedits.animecalificaciones.model.elements.conId;

import org.lukitasedits.animecalificaciones.annotations.Descriptible;
import org.lukitasedits.animecalificaciones.model.elements.Elementos;

abstract public class ElementosConId extends Elementos {
    @Descriptible(isId = true)
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
