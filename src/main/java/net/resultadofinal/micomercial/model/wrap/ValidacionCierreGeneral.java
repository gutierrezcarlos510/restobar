package net.resultadofinal.micomercial.model.wrap;

import java.util.List;

public class ValidacionCierreGeneral {
    private List<ValidacionCierreSucursal> validacionVenta;
    private List<ValidacionCierreSucursal> validacionArqueo;

    public List<ValidacionCierreSucursal> getValidacionVenta() {
        return validacionVenta;
    }

    public void setValidacionVenta(List<ValidacionCierreSucursal> validacionVenta) {
        this.validacionVenta = validacionVenta;
    }

    public List<ValidacionCierreSucursal> getValidacionArqueo() {
        return validacionArqueo;
    }

    public void setValidacionArqueo(List<ValidacionCierreSucursal> validacionArqueo) {
        this.validacionArqueo = validacionArqueo;
    }
    public boolean getExisteVentasAbiertas(){
        if(validacionVenta != null && !validacionVenta.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
    public boolean getExisteArqueosAbiertos(){
        if(validacionArqueo != null && !validacionArqueo.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
