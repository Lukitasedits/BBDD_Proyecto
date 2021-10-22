package org.lukitasedits.animecalificaciones.model.elements.sinId;

import org.lukitasedits.animecalificaciones.model.elements.Elementos;

abstract public class ElementosRelacionales extends Elementos {
    int id1;
    int id2;

    public ElementosRelacionales(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
    }
}
