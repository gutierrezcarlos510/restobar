package net.resultadofinal.micomercial.model.form;

import java.math.BigDecimal;
import java.util.List;

public class VentaForm {
    private Long id, clienteId, usuarioId, createdBy,numero;
    private Integer mesaId, sucursalId,gestion;
    private String obs;
    private BigDecimal subtotal, descuento, total, totalPagado, totalCambio;
    private List<DetalleVentaForm> detalleVenta, detalleVentaCompuesto;
    private Short formaPagoId, cantidadPersonas, tipo;

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Integer getGestion() {
        return gestion;
    }

    public void setGestion(Integer gestion) {
        this.gestion = gestion;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }

    public Short getFormaPagoId() {
        return formaPagoId;
    }

    public void setFormaPagoId(Short formaPagoId) {
        this.formaPagoId = formaPagoId;
    }

    public Short getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(Short cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getMesaId() {
        return mesaId;
    }

    public void setMesaId(Integer mesaId) {
        this.mesaId = mesaId;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }

    public BigDecimal getTotalCambio() {
        return totalCambio;
    }

    public void setTotalCambio(BigDecimal totalCambio) {
        this.totalCambio = totalCambio;
    }

    public List<DetalleVentaForm> getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(List<DetalleVentaForm> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public List<DetalleVentaForm> getDetalleVentaCompuesto() {
        return detalleVentaCompuesto;
    }

    public void setDetalleVentaCompuesto(List<DetalleVentaForm> detalleVentaCompuesto) {
        this.detalleVentaCompuesto = detalleVentaCompuesto;
    }
}
