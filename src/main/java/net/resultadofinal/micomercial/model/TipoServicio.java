package net.resultadofinal.micomercial.model;

import java.util.List;

public class TipoServicio {
	private Integer cod_tipser;
	private String nom_tipser,des_tipser;
	private Boolean est_tipser;
	private List<Servicio> servicios;
	public List<Servicio> getServicios() {
		return servicios;
	}
	public void setServicios(List<Servicio> servicios) {
		this.servicios = servicios;
	}
	public Integer getCod_tipser() {
		return cod_tipser;
	}
	public void setCod_tipser(Integer cod_tipser) {
		this.cod_tipser = cod_tipser;
	}
	public String getNom_tipser() {
		return nom_tipser;
	}
	public void setNom_tipser(String nom_tipser) {
		this.nom_tipser = nom_tipser;
	}
	public String getDes_tipser() {
		return des_tipser;
	}
	public void setDes_tipser(String des_tipser) {
		this.des_tipser = des_tipser;
	}
	public Boolean getEst_tipser() {
		return est_tipser;
	}
	public void setEst_tipser(Boolean est_tipser) {
		this.est_tipser = est_tipser;
	}
}
