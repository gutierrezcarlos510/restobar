package net.resultadofinal.micomercial.model;

public class Almacen {
    private Long productoId;
    private Integer sucursalId, cantidad;
    private String xproducto;
    private Integer unidadPorCaja;

    public String getXproducto() {
        return xproducto;
    }

    public void setXproducto(String xproducto) {
        this.xproducto = xproducto;
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

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
    public int getCantidadCaja(){
        if(this.cantidad != null) {
            if(this.unidadPorCaja != null) {
                return cantidad / unidadPorCaja;
            } else {
                return this.cantidad;
            }
        } else{
            return 0;
        }
    }

    public Integer getUnidadPorCaja() {
        return unidadPorCaja;
    }

    public void setUnidadPorCaja(Integer unidadPorCaja) {
        this.unidadPorCaja = unidadPorCaja;
    }
}
