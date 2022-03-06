package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;

public class DetalleCartillaSucursal {
    private Integer cartillaSucursalId;
    private Short id;
    private Integer tipoProductoId;
    private BigDecimal precio;
    private String xtipoProducto;

    public Integer getCartillaSucursalId() {
        return cartillaSucursalId;
    }

    public void setCartillaSucursalId(Integer cartillaSucursalId) {
        this.cartillaSucursalId = cartillaSucursalId;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Integer getTipoProductoId() {
        return tipoProductoId;
    }

    public void setTipoProductoId(Integer tipoProductoId) {
        this.tipoProductoId = tipoProductoId;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getXtipoProducto() {
        return xtipoProducto;
    }

    public void setXtipoProducto(String xtipoProducto) {
        this.xtipoProducto = xtipoProducto;
    }
}
