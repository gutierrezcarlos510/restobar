package net.resultadofinal.micomercial.model;

public class DetalleMovimiento {
    private Short id;
    private Long movimientoId, productoId;
    private Integer cantidad, cantidadUnitaria;
    private Boolean tipo, esIngreso;
    private String xproducto;
    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Long getMovimientoId() {
        return movimientoId;
    }

    public void setMovimientoId(Long movimientoId) {
        this.movimientoId = movimientoId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getCantidadUnitaria() {
        return cantidadUnitaria;
    }

    public void setCantidadUnitaria(Integer cantidadUnitaria) {
        this.cantidadUnitaria = cantidadUnitaria;
    }

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
    }

    public Boolean getEsIngreso() {
        return esIngreso;
    }

    public void setEsIngreso(Boolean esIngreso) {
        this.esIngreso = esIngreso;
    }

    public String getXproducto() {
        return xproducto;
    }

    public void setXproducto(String xproducto) {
        this.xproducto = xproducto;
    }
}
