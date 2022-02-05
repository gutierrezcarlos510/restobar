package net.resultadofinal.micomercial.model;

public class NotificacionSucursalWrap {
    private Integer cod_suc;
    private String xsucursal,tituloNotificacion,mensajeNotificacion;
    private Boolean estadoNotificacion;

    public Integer getCod_suc() {
        return cod_suc;
    }

    public void setCod_suc(Integer cod_suc) {
        this.cod_suc = cod_suc;
    }

    public String getXsucursal() {
        return xsucursal;
    }

    public void setXsucursal(String xsucursal) {
        this.xsucursal = xsucursal;
    }

    public String getTituloNotificacion() {
        return tituloNotificacion;
    }

    public void setTituloNotificacion(String tituloNotificacion) {
        this.tituloNotificacion = tituloNotificacion;
    }

    public String getMensajeNotificacion() {
        return mensajeNotificacion;
    }

    public void setMensajeNotificacion(String mensajeNotificacion) {
        this.mensajeNotificacion = mensajeNotificacion;
    }

    public Boolean getEstadoNotificacion() {
        return estadoNotificacion;
    }

    public void setEstadoNotificacion(Boolean estadoNotificacion) {
        this.estadoNotificacion = estadoNotificacion;
    }
}
