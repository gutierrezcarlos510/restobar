package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;

public class DetalleHistoricoVenta {
    private Long ventaId, productoId;
    private BigDecimal cantidad;
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

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
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
