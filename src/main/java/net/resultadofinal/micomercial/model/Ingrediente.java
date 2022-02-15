package net.resultadofinal.micomercial.model;

public class Ingrediente {
    private Short id;
    private Long productoId, ingredienteId;
    private Integer cantidad;
    private String xproducto, xtipo, xmedida;

    public Long getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Long ingredienteId) {
        this.ingredienteId = ingredienteId;
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getXproducto() {
        return xproducto;
    }

    public void setXproducto(String xproducto) {
        this.xproducto = xproducto;
    }

    public String getXtipo() {
        return xtipo;
    }

    public void setXtipo(String xtipo) {
        this.xtipo = xtipo;
    }

    public String getXmedida() {
        return xmedida;
    }

    public void setXmedida(String xmedida) {
        this.xmedida = xmedida;
    }
}
