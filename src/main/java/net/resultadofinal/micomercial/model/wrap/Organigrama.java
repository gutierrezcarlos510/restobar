package net.resultadofinal.micomercial.model.wrap;

import java.util.List;

public class Organigrama {
    private String nombreSucursal;
    private List<RolOrganigrama> listaRoles;

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public List<RolOrganigrama> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<RolOrganigrama> listaRoles) {
        this.listaRoles = listaRoles;
    }
}
