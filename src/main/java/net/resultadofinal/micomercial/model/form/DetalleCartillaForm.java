package net.resultadofinal.micomercial.model.form;

import java.math.BigDecimal;
import java.util.List;

public class DetalleCartillaForm {
    private Short id;
    private Integer tipoProductoId,cartillaSucursalId;
    private BigDecimal precio;
    private String xtipoProducto;
    private List<ProductoCartillaForm> productos;

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

    public List<ProductoCartillaForm> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoCartillaForm> productos) {
        this.productos = productos;
    }
}
