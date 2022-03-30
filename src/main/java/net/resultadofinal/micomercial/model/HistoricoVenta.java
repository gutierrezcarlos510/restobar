package net.resultadofinal.micomercial.model;

import java.sql.Timestamp;

public class HistoricoVenta {
    private Long ventaId;
    private Timestamp fecha;
    private Short id;
    private String xmesa, xusuario, xcliente;

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

    public Long getVentaId() {
        return ventaId;
    }

    public void setVentaId(Long ventaId) {
        this.ventaId = ventaId;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }
}
