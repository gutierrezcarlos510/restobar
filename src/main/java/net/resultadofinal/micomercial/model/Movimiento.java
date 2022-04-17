package net.resultadofinal.micomercial.model;

import java.sql.Timestamp;
import java.util.List;

public class Movimiento {
    private Long id, createdBy,usuarioRevision, updatedBy;
    private Timestamp createdAt, fechaRevision, updatedAt;
    private Integer sucursalOrigen, sucursalDestino;
    private Short tipo, estadoMovimiento;
    private Boolean estado;
    private String obs, xcreatedBy,xusuarioRevision,xsucursalOrigen, xsucursalDestino, xestadoMovimiento, xtipo;
    private List<DetalleMovimiento> detalles;

    public String getXtipo() {
        return xtipo;
    }

    public void setXtipo(String xtipo) {
        this.xtipo = xtipo;
    }

    public String getXestadoMovimiento() {
        return xestadoMovimiento;
    }

    public void setXestadoMovimiento(String xestadoMovimiento) {
        this.xestadoMovimiento = xestadoMovimiento;
    }

    public String getXsucursalDestino() {
        return xsucursalDestino;
    }

    public void setXsucursalDestino(String xsucursalDestino) {
        this.xsucursalDestino = xsucursalDestino;
    }

    public String getXsucursalOrigen() {
        return xsucursalOrigen;
    }

    public void setXsucursalOrigen(String xsucursalOrigen) {
        this.xsucursalOrigen = xsucursalOrigen;
    }

    public List<DetalleMovimiento> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleMovimiento> detalles) {
        this.detalles = detalles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUsuarioRevision() {
        return usuarioRevision;
    }

    public void setUsuarioRevision(Long usuarioRevision) {
        this.usuarioRevision = usuarioRevision;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Timestamp fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getSucursalOrigen() {
        return sucursalOrigen;
    }

    public void setSucursalOrigen(Integer sucursalOrigen) {
        this.sucursalOrigen = sucursalOrigen;
    }

    public Integer getSucursalDestino() {
        return sucursalDestino;
    }

    public void setSucursalDestino(Integer sucursalDestino) {
        this.sucursalDestino = sucursalDestino;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }

    public Short getEstadoMovimiento() {
        return estadoMovimiento;
    }

    public void setEstadoMovimiento(Short estadoMovimiento) {
        this.estadoMovimiento = estadoMovimiento;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getXcreatedBy() {
        return xcreatedBy;
    }

    public void setXcreatedBy(String xcreatedBy) {
        this.xcreatedBy = xcreatedBy;
    }

    public String getXusuarioRevision() {
        return xusuarioRevision;
    }

    public void setXusuarioRevision(String xusuarioRevision) {
        this.xusuarioRevision = xusuarioRevision;
    }
}
