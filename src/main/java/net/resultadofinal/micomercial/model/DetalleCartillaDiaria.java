package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;

public class DetalleCartillaDiaria {
    private Short id, detalleCartillaSucursalId;
    private Long productoId, cartillaDiariaId;
    private BigDecimal precioIndividual,precioCompuesto,cantidad;
    private Integer cartillaSucursalId, cantidadEditadaFinal, cantidadFinalAlmacen;

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Short getDetalleCartillaSucursalId() {
        return detalleCartillaSucursalId;
    }

    public void setDetalleCartillaSucursalId(Short detalleCartillaSucursalId) {
        this.detalleCartillaSucursalId = detalleCartillaSucursalId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Long getCartillaDiariaId() {
        return cartillaDiariaId;
    }

    public void setCartillaDiariaId(Long cartillaDiariaId) {
        this.cartillaDiariaId = cartillaDiariaId;
    }

    public BigDecimal getPrecioIndividual() {
        return precioIndividual;
    }

    public void setPrecioIndividual(BigDecimal precioIndividual) {
        this.precioIndividual = precioIndividual;
    }

    public BigDecimal getPrecioCompuesto() {
        return precioCompuesto;
    }

    public void setPrecioCompuesto(BigDecimal precioCompuesto) {
        this.precioCompuesto = precioCompuesto;
    }

    public Integer getCartillaSucursalId() {
        return cartillaSucursalId;
    }

    public void setCartillaSucursalId(Integer cartillaSucursalId) {
        this.cartillaSucursalId = cartillaSucursalId;
    }

    public Integer getCantidadEditadaFinal() {
        return cantidadEditadaFinal;
    }

    public void setCantidadEditadaFinal(Integer cantidadEditadaFinal) {
        this.cantidadEditadaFinal = cantidadEditadaFinal;
    }

    public Integer getCantidadFinalAlmacen() {
        return cantidadFinalAlmacen;
    }

    public void setCantidadFinalAlmacen(Integer cantidadFinalAlmacen) {
        this.cantidadFinalAlmacen = cantidadFinalAlmacen;
    }
}
