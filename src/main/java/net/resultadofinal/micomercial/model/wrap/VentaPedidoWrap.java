package net.resultadofinal.micomercial.model.wrap;

import java.sql.Timestamp;

public class VentaPedidoWrap {
    private Long ventaId;
    private Short tipo;
    private Timestamp fecha;
    private String xmesa;
    private String xusuario;
    private String xcliente;
    private Short areaDestino, historicoVentaId;
    public String getXtipo(){
        if(tipo != null) {
            switch (tipo) {
                case 1:
                    return "Comanda pendiente";
                case 2:
                    return "Comanda pagada";
                case 3:
                    return "Comanda revertida";
                case 4:
                    return "Pedido";
                case 5:
                    return "Reserva";
                default:
                    return "-";
            }
        } else {
            return "sn";
        }
    }

    public String getXmesa() {
        return xmesa;
    }

    public void setXmesa(String xmesa) {
        this.xmesa = xmesa;
    }

    public Short getHistoricoVentaId() {
        return historicoVentaId;
    }

    public void setHistoricoVentaId(Short historicoVentaId) {
        this.historicoVentaId = historicoVentaId;
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

    public Short getAreaDestino() {
        return areaDestino;
    }

    public void setAreaDestino(Short areaDestino) {
        this.areaDestino = areaDestino;
    }
}
