package net.resultadofinal.micomercial.model.wrap;

import java.sql.Timestamp;
import java.util.List;

public class ResumenArqueoWrap {
    private Long id;
    private String xusuario;
    private Timestamp finicio, ffin;
    private Boolean esActivo;
    private List<DetalleResumenArqueoWrap> detalleResumen;

    public Boolean getEsActivo() {
        return esActivo;
    }

    public void setEsActivo(Boolean esActivo) {
        this.esActivo = esActivo;
    }

    public List<DetalleResumenArqueoWrap> getDetalleResumen() {
        return detalleResumen;
    }

    public void setDetalleResumen(List<DetalleResumenArqueoWrap> detalleResumen) {
        this.detalleResumen = detalleResumen;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
