package net.resultadofinal.micomercial.model.wrap;

import java.math.BigDecimal;

public class CartillaDiariaCierreWrap {
    private Short id, detalleCartillaSucursalId;
    private Long productoId, cartillaDiariaId;
    private BigDecimal precioIndividual,precioCompuesto;
    private Integer cartillaSucursalId;
    private BigDecimal cantidad, cantidadAlmacen, cantidadVendida, cantidadFinalAlmacen;

    public BigDecimal getCantidadFinalAlmacen() {
        return cantidadFinalAlmacen;
    }

    public void setCantidadFinalAlmacen(BigDecimal cantidadFinalAlmacen) {
        this.cantidadFinalAlmacen = cantidadFinalAlmacen;
    }

    private String xproducto;

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

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCantidadAlmacen() {
        return cantidadAlmacen;
    }

    public void setCantidadAlmacen(BigDecimal cantidadAlmacen) {
        this.cantidadAlmacen = cantidadAlmacen;
    }

    public BigDecimal getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(BigDecimal cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public String getXproducto() {
        return xproducto;
    }

    public void setXproducto(String xproducto) {
        this.xproducto = xproducto;
    }
}
