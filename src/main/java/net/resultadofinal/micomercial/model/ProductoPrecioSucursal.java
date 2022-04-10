package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;

public class ProductoPrecioSucursal {
    private Long productoId;
    private Integer sucursalId;
    private Short id;
    private String nombre;
    private BigDecimal precio;
    private Boolean esPrincipal;

    public ProductoPrecioSucursal() {
    }

    public ProductoPrecioSucursal(Long productoId, Integer sucursalId, Short id, BigDecimal precio) {
        this.productoId = productoId;
        this.sucursalId = sucursalId;
        this.id = id;
        this.precio = precio;
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

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Boolean getEsPrincipal() {
        return esPrincipal;
    }

    public void setEsPrincipal(Boolean esPrincipal) {
        this.esPrincipal = esPrincipal;
    }
}
