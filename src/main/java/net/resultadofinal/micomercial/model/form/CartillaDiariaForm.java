package net.resultadofinal.micomercial.model.form;

import java.sql.Timestamp;
import java.util.List;

public class CartillaDiariaForm {
    private Long id, usuarioId;
    private Integer codSuc;
    private String xusuario;
    private Timestamp finicio, ffin;
    private boolean estado, estadoCartilla;
    private List<CartillaSucursalForm> cartillaSucursalFormList;

    public List<CartillaSucursalForm> getCartillaSucursalFormList() {
        return cartillaSucursalFormList;
    }

    public void setCartillaSucursalFormList(List<CartillaSucursalForm> cartillaSucursalFormList) {
        this.cartillaSucursalFormList = cartillaSucursalFormList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getCodSuc() {
        return codSuc;
    }

    public void setCodSuc(Integer codSuc) {
        this.codSuc = codSuc;
    }

    public String getXusuario() {
        return xusuario;
    }

    public void setXusuario(String xusuario) {
        this.xusuario = xusuario;
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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean isEstadoCartilla() {
        return estadoCartilla;
    }

    public void setEstadoCartilla(boolean estadoCartilla) {
        this.estadoCartilla = estadoCartilla;
    }
}
