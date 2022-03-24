package net.resultadofinal.micomercial.model.form;

import java.math.BigDecimal;
import java.util.List;

public class CartillaSucursalForm {
    private Integer id;
    private String nombre;
    private BigDecimal total;
    private boolean estaCompuesto;
    private Integer codSuc;

    public Integer getCodSuc() {
        return codSuc;
    }

    public void setCodSuc(Integer codSuc) {
        this.codSuc = codSuc;
    }

    private List<DetalleCartillaForm> detalleCartillaList;

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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public boolean isEstaCompuesto() {
        return estaCompuesto;
    }

    public void setEstaCompuesto(boolean estaCompuesto) {
        this.estaCompuesto = estaCompuesto;
    }

    public List<DetalleCartillaForm> getDetalleCartillaList() {
        return detalleCartillaList;
    }

    public void setDetalleCartillaList(List<DetalleCartillaForm> detalleCartillaList) {
        this.detalleCartillaList = detalleCartillaList;
    }
}
