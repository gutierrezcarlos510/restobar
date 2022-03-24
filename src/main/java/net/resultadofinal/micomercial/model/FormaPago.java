package net.resultadofinal.micomercial.model;

public class FormaPago {
    private Short id;
    private String nombre;
    private Boolean esEfectivoCaja, estado;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEsEfectivoCaja() {
        return esEfectivoCaja;
    }

    public void setEsEfectivoCaja(Boolean esEfectivoCaja) {
        this.esEfectivoCaja = esEfectivoCaja;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
