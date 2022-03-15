package net.resultadofinal.micomercial.model;

import java.sql.Timestamp;

public class HistoricoAlmacen {
    private Long productoId, usuarioId;
    private Timestamp fecha;
    private Integer cantidadInicial, cantidadEntrante, cantidadFinal;
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

    public Integer getCantidadInicial() {
        return cantidadInicial;
    }

    public void setCantidadInicial(Integer cantidadInicial) {
        this.cantidadInicial = cantidadInicial;
    }

    public Integer getCantidadEntrante() {
        return cantidadEntrante;
    }

    public void setCantidadEntrante(Integer cantidadEntrante) {
        this.cantidadEntrante = cantidadEntrante;
    }

    public Integer getCantidadFinal() {
        return cantidadFinal;
    }

    public void setCantidadFinal(Integer cantidadFinal) {
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
