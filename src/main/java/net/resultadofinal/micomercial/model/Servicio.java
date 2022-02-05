package net.resultadofinal.micomercial.model;

public class Servicio {
	private Integer cod_ser,cod_tipser;
	private String nom_ser,des_ser;
	private Float pre_ser,porcentaje_multa,porcentaje_ayudante;
	private Boolean est_ser;
	public Float getPorcentaje_ayudante() {
		return porcentaje_ayudante;
	}
	public void setPorcentaje_ayudante(Float porcentaje_ayudante) {
		this.porcentaje_ayudante = porcentaje_ayudante;
	}
	public Float getPorcentaje_multa() {
		return porcentaje_multa;
	}
	public void setPorcentaje_multa(Float porcentaje_multa) {
		this.porcentaje_multa = porcentaje_multa;
	}
	public Integer getCod_ser() {
		return cod_ser;
	}
	public void setCod_ser(Integer cod_ser) {
		this.cod_ser = cod_ser;
	}
	public Integer getCod_tipser() {
		return cod_tipser;
	}
	public void setCod_tipser(Integer cod_tipser) {
		this.cod_tipser = cod_tipser;
	}
	public String getNom_ser() {
		return nom_ser;
	}
	public void setNom_ser(String nom_ser) {
		this.nom_ser = nom_ser;
	}
	public String getDes_ser() {
		return des_ser;
	}
	public void setDes_ser(String des_ser) {
		this.des_ser = des_ser;
	}
	public Float getPre_ser() {
		return pre_ser;
	}
	public void setPre_ser(Float pre_ser) {
		this.pre_ser = pre_ser;
	}
	public Boolean getEst_ser() {
		return est_ser;
	}
	public void setEst_ser(Boolean est_ser) {
		this.est_ser = est_ser;
	}
}
