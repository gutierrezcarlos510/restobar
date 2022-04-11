package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Almacen {
    private Long productoId;
    private Integer sucursalId;
    private BigDecimal cantidad;
    private String xproducto;
    private Integer unidadPorCaja;

    public String getXproducto() {
        return xproducto;
    }

    public void setXproducto(String xproducto) {
        this.xproducto = xproducto;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
    public BigDecimal getCantidadCaja(){
        if(this.cantidad != null) {
            if(this.unidadPorCaja != null) {
                return cantidad.divide(new BigDecimal(unidadPorCaja), 2, RoundingMode.HALF_DOWN);
            } else {
                return this.cantidad;
            }
        } else{
            return new BigDecimal(0);
        }
    }

    public Integer getUnidadPorCaja() {
        return unidadPorCaja;
    }

    public void setUnidadPorCaja(Integer unidadPorCaja) {
        this.unidadPorCaja = unidadPorCaja;
    }
}
