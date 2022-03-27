package net.resultadofinal.micomercial.model.wrap;

import java.math.BigDecimal;

public class DetalleResumenArqueoWrap {
    private Long id;
    private String nombre;
    private Boolean esDebe, esEfectivoCaja;
    private BigDecimal total;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEsDebe() {
        return esDebe;
    }

    public void setEsDebe(Boolean esDebe) {
        this.esDebe = esDebe;
    }

    public Boolean getEsEfectivoCaja() {
        return esEfectivoCaja;
    }

    public void setEsEfectivoCaja(Boolean esEfectivoCaja) {
        this.esEfectivoCaja = esEfectivoCaja;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
