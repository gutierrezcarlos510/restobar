package net.resultadofinal.micomercial.model.wrap;

import java.util.List;

public class RolOrganigrama {
    private Integer codRol;
    private String xrol;
    private List<UsuarioOrganigrama> listaUsuarios;

    public Integer getCodRol() {
        return codRol;
    }

    public void setCodRol(Integer codRol) {
        this.codRol = codRol;
    }

    public String getXrol() {
        return xrol;
    }

    public void setXrol(String xrol) {
        this.xrol = xrol;
    }

    public List<UsuarioOrganigrama> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<UsuarioOrganigrama> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }
}
