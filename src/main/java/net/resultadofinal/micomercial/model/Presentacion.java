package net.resultadofinal.micomercial.model;

public class Presentacion {
    private Integer id;
    private String nombre;
    private Boolean estado;
    private Short tipo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }
}
