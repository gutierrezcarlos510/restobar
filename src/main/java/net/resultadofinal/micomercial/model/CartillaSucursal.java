package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;

public class CartillaSucursal {
    private Integer id,codSuc;
    private String nombre;
    private BigDecimal total;
    private boolean estado,estaCompuesto;

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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean isEstaCompuesto() {
        return estaCompuesto;
    }

    public void setEstaCompuesto(boolean estaCompuesto) {
        this.estaCompuesto = estaCompuesto;
    }

    public Integer getCodSuc() {
        return codSuc;
    }

    public void setCodSuc(Integer codSuc) {
        this.codSuc = codSuc;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
