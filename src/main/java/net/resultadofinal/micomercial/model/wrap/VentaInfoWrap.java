package net.resultadofinal.micomercial.model.wrap;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class VentaInfoWrap {
    private Long numero,arqueoId,id;
    private Integer gestion,detalleArqueoId;
    private Short tipo;
    private String obs;
    private Date fecha;
    private BigDecimal total,descuento,subtotal, totalPagado, totalCambio;
    private String xmesa,xusuario,xcliente,xtipo,xformaPago,xcreatedBy;
    private Short cantidadPersonas;
    private Timestamp createdAt;
    private List<DetalleVentaWrap> detalleVenta;
    private List<DetalleVentaWrap> detalleVentaCompuesto;

    public List<DetalleVentaWrap> getDetalleVenta() {
        return detalleVenta;
    }

    public void setDetalleVenta(List<DetalleVentaWrap> detalleVenta) {
        this.detalleVenta = detalleVenta;
    }

    public List<DetalleVentaWrap> getDetalleVentaCompuesto() {
        return detalleVentaCompuesto;
    }

    public void setDetalleVentaCompuesto(List<DetalleVentaWrap> detalleVentaCompuesto) {
        this.detalleVentaCompuesto = detalleVentaCompuesto;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    public Long getArqueoId() {
        return arqueoId;
    }

    public void setArqueoId(Long arqueoId) {
        this.arqueoId = arqueoId;
    }

    public Integer getGestion() {
        return gestion;
    }

    public void setGestion(Integer gestion) {
        this.gestion = gestion;
    }

    public Integer getDetalleArqueoId() {
        return detalleArqueoId;
    }

    public void setDetalleArqueoId(Integer detalleArqueoId) {
        this.detalleArqueoId = detalleArqueoId;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
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

    public String getXmesa() {
        return xmesa;
    }

    public void setXmesa(String xmesa) {
        this.xmesa = xmesa;
    }

    public String getXusuario() {
        return xusuario;
    }

    public void setXusuario(String xusuario) {
        this.xusuario = xusuario;
    }

    public String getXcliente() {
        return xcliente;
    }

    public void setXcliente(String xcliente) {
        this.xcliente = xcliente;
    }

    public String getXtipo() {
        return xtipo;
    }

    public void setXtipo(String xtipo) {
        this.xtipo = xtipo;
    }

    public String getXformaPago() {
        return xformaPago;
    }

    public void setXformaPago(String xformaPago) {
        this.xformaPago = xformaPago;
    }

    public String getXcreatedBy() {
        return xcreatedBy;
    }

    public void setXcreatedBy(String xcreatedBy) {
        this.xcreatedBy = xcreatedBy;
    }

    public Short getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(Short cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
