package net.resultadofinal.micomercial.model;

import java.sql.Timestamp;
import java.util.List;

public class CartillaDiaria {
    private Long id;
    private Timestamp finicio,ffin,fechaCierre;
    private Long usuarioId,usuarioCierre;
    private boolean estado, estadoCartilla;
    private Integer codSuc;
    private String xusuario,xfinicio;
    private List<DetalleCartillaDiaria> detalles;

    public String getXfinicio() {
        return xfinicio;
    }

    public void setXfinicio(String xfinicio) {
        this.xfinicio = xfinicio;
    }

    public String getXusuario() {
        return xusuario;
    }

    public void setXusuario(String xusuario) {
        this.xusuario = xusuario;
    }

    public boolean isEstadoCartilla() {
        return estadoCartilla;
    }

    public void setEstadoCartilla(boolean estadoCartilla) {
        this.estadoCartilla = estadoCartilla;
    }

    public List<DetalleCartillaDiaria> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCartillaDiaria> detalles) {
        this.detalles = detalles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getFinicio() {
        return finicio;
    }

    public void setFinicio(Timestamp finicio) {
        this.finicio = finicio;
    }

    public Timestamp getFfin() {
        return ffin;
    }

    public void setFfin(Timestamp ffin) {
        this.ffin = ffin;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Integer getCodSuc() {
        return codSuc;
    }

    public void setCodSuc(Integer codSuc) {
        this.codSuc = codSuc;
    }

    public Timestamp getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Timestamp fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Long getUsuarioCierre() {
        return usuarioCierre;
    }

    public void setUsuarioCierre(Long usuarioCierre) {
        this.usuarioCierre = usuarioCierre;
    }
}
