package net.resultadofinal.micomercial.model.form;

import java.math.BigDecimal;

public class ProductoCartillaForm {
    private Integer cartillaSucursalId;
    private Short detalleCartillaSucursalId;
    private Long productoId;
    private String xproducto;
    private BigDecimal precioIndividual, precioCompuesto;
    private Integer cantidad;

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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
