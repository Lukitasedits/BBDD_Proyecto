package org.lukitasedits.animecalificaciones.model.structure;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Tablas {
    ANIMES("animes", true ,addColumna(
            new Columna("nombre", "String"),
            new Columna("calificacion_promedio", "double")), 3),
    CATEGORIAS("categorias", true,addColumna(
            new Columna("nombre", "String")), 2),
    USUARIOS("usuarios", true, addColumna(
            new Columna("nombre", "String"),
            new Columna("apellido", "String"), new Columna("password", "String"),
            new Columna("mail", "String")), 5),
    ANIMES_CATEGORIAS("animes_calif.`animes-categorias`", false ,addColumna(
            new Columna("id_anime", "int"),
            new Columna("id_categoria", "int")),
            2),
    USUARIOS_ANIMES("animes_calif.`usuarios-animes`", false ,addColumna(
            new Columna("id_usuario", "int"),
            new Columna("id_anime", "int"), new Columna("calificacion", "int")),
            3);

    private final String nombreTabla;
    private final boolean id;
    private final List<Columna> columnas;
    private final int nColumnas;

    Tablas(String nombreTabla, boolean id, List<Columna> columnas, int nColumnas) {
        this.nombreTabla = nombreTabla;
        this.id = id;
        this.columnas = columnas;
        this.nColumnas = nColumnas;
    }

    public boolean getId() {
        return id;
    }

    private static List<Columna> addColumna(Columna... varargs){
        return Arrays.asList(varargs);
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public List<Columna> getColumnas() {
        return columnas;
    }

    public int getnColumnas() {
        return nColumnas;
    }

    public String nombreColumnas(){
        return columnas.stream().map(Columna::getNombre).collect(Collectors.joining(", "));
    }

    public Columna getColumna(String nombreColum){
       return columnas.stream().filter(c -> c.getNombre().equals(nombreColum)).findFirst().get();
    }

    @Override
    public String toString() {
        return nombreTabla;
    }
}
