package net.resultadofinal.micomercial.model.wrap;

import java.math.BigDecimal;

public class AlmacenVenta {
    private Long productoId;
    private Integer sucursalId;
    private String xproducto,foto;
    private Integer unidadPorCaja;
    private BigDecimal cantidad,pvUnitDescuento, pvCajaDescuento, pvUnit, pvCaja;

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
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

    public String getXproducto() {
        return xproducto;
    }

    public void setXproducto(String xproducto) {
        this.xproducto = xproducto;
    }

    public Integer getUnidadPorCaja() {
        return unidadPorCaja;
    }

    public void setUnidadPorCaja(Integer unidadPorCaja) {
        this.unidadPorCaja = unidadPorCaja;
    }

    public BigDecimal getPvUnitDescuento() {
        return pvUnitDescuento;
    }

    public void setPvUnitDescuento(BigDecimal pvUnitDescuento) {
        this.pvUnitDescuento = pvUnitDescuento;
    }

    public BigDecimal getPvCajaDescuento() {
        return pvCajaDescuento;
    }

    public void setPvCajaDescuento(BigDecimal pvCajaDescuento) {
        this.pvCajaDescuento = pvCajaDescuento;
    }

    public BigDecimal getPvUnit() {
        return pvUnit;
    }

    public void setPvUnit(BigDecimal pvUnit) {
        this.pvUnit = pvUnit;
    }

    public BigDecimal getPvCaja() {
        return pvCaja;
    }

    public void setPvCaja(BigDecimal pvCaja) {
        this.pvCaja = pvCaja;
    }
}
