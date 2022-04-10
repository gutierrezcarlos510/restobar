package net.resultadofinal.micomercial.model.form;

import java.math.BigDecimal;

public class ProductoCartillaForm {
    private Short id;
    private Long cartillaDiariaId;
    private Integer cartillaSucursalId;
    private Short detalleCartillaSucursalId;
    private Long productoId;
    private String xproducto, foto;
    private BigDecimal precioIndividual, precioCompuesto;
    private BigDecimal cantidad, cantidadModificar,cantidadAlmacen;
    private Boolean esProductoFabricado;

    public BigDecimal getCantidadAlmacen() {
        return cantidadAlmacen;
    }

    public void setCantidadAlmacen(BigDecimal cantidadAlmacen) {
        this.cantidadAlmacen = cantidadAlmacen;
    }

    public Boolean getEsProductoFabricado() {
        return esProductoFabricado;
    }

    public void setEsProductoFabricado(Boolean esProductoFabricado) {
        this.esProductoFabricado = esProductoFabricado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public BigDecimal getCantidadModificar() {
        return cantidadModificar;
    }

    public void setCantidadModificar(BigDecimal cantidadModificar) {
        this.cantidadModificar = cantidadModificar;
    }

    public Long getCartillaDiariaId() {
        return cartillaDiariaId;
    }

    public void setCartillaDiariaId(Long cartillaDiariaId) {
        this.cartillaDiariaId = cartillaDiariaId;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Integer getCartillaSucursalId() {
        return cartillaSucursalId;
    }

    public void setCartillaSucursalId(Integer cartillaSucursalId) {
        this.cartillaSucursalId = cartillaSucursalId;
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

    public String getXproducto() {
        return xproducto;
    }

    public void setXproducto(String xproducto) {
        this.xproducto = xproducto;
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

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
}
