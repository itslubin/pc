package pract5.p1;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private String contenido;

    public Mensaje(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }
}