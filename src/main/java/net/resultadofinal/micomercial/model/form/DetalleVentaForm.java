package net.resultadofinal.micomercial.model.form;

import java.math.BigDecimal;

public class DetalleVentaForm {
    private Short id;
    private Long ventaId;
    private Long productoId;
    private BigDecimal cantidad, cantidadUnitaria;
    private BigDecimal precio, total,precioCombo;
    private Short tipoVenta;//1=unidad, 2= caja
    private Long cartillaDiariaId;
    private Integer cartillaSucursalId;
    private Short detalleCartillaSucursalId, detalleCartillaDiariaId;
    private Boolean esCompuesto;
    private String xtipoProducto, xcombo, xproducto;

    public BigDecimal getPrecioCombo() {
        return precioCombo;
    }

    public void setPrecioCombo(BigDecimal precioCombo) {
        this.precioCombo = precioCombo;
    }

    public String getXtipoProducto() {
        return xtipoProducto;
    }

    public void setXtipoProducto(String xtipoProducto) {
        this.xtipoProducto = xtipoProducto;
    }

    public String getXcombo() {
        return xcombo;
    }

    public void setXcombo(String xcombo) {
        this.xcombo = xcombo;
    }

    public String getXproducto() {
        return xproducto;
    }

    public void setXproducto(String xproducto) {
        this.xproducto = xproducto;
    }

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getCantidadUnitaria() {
        return cantidadUnitaria;
    }

    public void setCantidadUnitaria(BigDecimal cantidadUnitaria) {
        this.cantidadUnitaria = cantidadUnitaria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Short getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(Short tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public Long getCartillaDiariaId() {
        return cartillaDiariaId;
    }

    public void setCartillaDiariaId(Long cartillaDiariaId) {
        this.cartillaDiariaId = cartillaDiariaId;
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

    public Short getDetalleCartillaDiariaId() {
        return detalleCartillaDiariaId;
    }

    public void setDetalleCartillaDiariaId(Short detalleCartillaDiariaId) {
        this.detalleCartillaDiariaId = detalleCartillaDiariaId;
    }

    public Boolean getEsCompuesto() {
        return esCompuesto;
    }

    public void setEsCompuesto(Boolean esCompuesto) {
        this.esCompuesto = esCompuesto;
    }
}
