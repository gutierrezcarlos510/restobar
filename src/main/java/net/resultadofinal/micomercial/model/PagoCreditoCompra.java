package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PagoCreditoCompra {
    private Long compraId, createdBy, updatedBy;
    private Short id, formaPagoId;
    private BigDecimal monto;
    private Timestamp createdAt, updatedAt;
    private String xcreatedBy,xformaPago;

    public String getXcreatedBy() {
        return xcreatedBy;
    }

    public String getXformaPago() {
        return xformaPago;
    }

    public void setXformaPago(String xformaPago) {
        this.xformaPago = xformaPago;
    }

    public void setXcreatedBy(String xcreatedBy) {
        this.xcreatedBy = xcreatedBy;
    }

    private Boolean estado;

    public Long getCompraId() {
        return compraId;
    }

    public void setCompraId(Long compraId) {
        this.compraId = compraId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Short getFormaPagoId() {
        return formaPagoId;
    }

    public void setFormaPagoId(Short formaPagoId) {
        this.formaPagoId = formaPagoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
