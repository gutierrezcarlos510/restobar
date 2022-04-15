package net.resultadofinal.micomercial.model.wrap;

public class ValidacionCierreSucursal {
    private Integer sucursalId;
    private String xsucursal,codigos;
    private Integer total;//Cantidad de datos invalidos

    public Integer getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }

    public String getXsucursal() {
        return xsucursal;
    }

    public void setXsucursal(String xsucursal) {
        this.xsucursal = xsucursal;
    }

    public String getCodigos() {
        return codigos;
    }

    public void setCodigos(String codigos) {
        this.codigos = codigos;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
