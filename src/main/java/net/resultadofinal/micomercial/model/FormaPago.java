package net.resultadofinal.micomercial.model;

public class FormaPago {
    private Short id;
    private String nombre,alias;
    private Boolean esEfectivoCaja, estado;
    private String xsucursal;
    private Integer sucursalId;

    public String getXsucursal() {
        return xsucursal;
    }

    public void setXsucursal(String xsucursal) {
        this.xsucursal = xsucursal;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getEsEfectivoCaja() {
        return esEfectivoCaja;
    }

    public void setEsEfectivoCaja(Boolean esEfectivoCaja) {
        this.esEfectivoCaja = esEfectivoCaja;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
