package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class HistoricoAlmacen {
    private Long productoId, usuarioId;
    private Timestamp fecha;
    private BigDecimal cantidadInicial, cantidadEntrante, cantidadFinal;
    private Short tipo;
    private String observacion;

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getCantidadInicial() {
        return cantidadInicial;
    }

    public void setCantidadInicial(BigDecimal cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }

    public BigDecimal getCantidadEntrante() {
        return cantidadEntrante;
    }

    public void setCantidadEntrante(BigDecimal cantidadEntrante) {
        this.cantidadEntrante = cantidadEntrante;
    }

    public BigDecimal getCantidadFinal() {
        return cantidadFinal;
    }

    public void setCantidadFinal(BigDecimal cantidadFinal) {
        this.cantidadFinal = cantidadFinal;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
