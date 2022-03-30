package net.resultadofinal.micomercial.model;

public class DetalleHistoricoVenta {
    private Long ventaId, productoId;
    private Integer cantidad;
    private Short historicoVentaId;
    private Boolean estaImpreso;

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
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

    public Short getHistoricoVentaId() {
        return historicoVentaId;
    }

    public void setHistoricoVentaId(Short historicoVentaId) {
        this.historicoVentaId = historicoVentaId;
    }

    public Boolean getEstaImpreso() {
        return estaImpreso;
    }

    public void setEstaImpreso(Boolean estaImpreso) {
        this.estaImpreso = estaImpreso;
    }
}
