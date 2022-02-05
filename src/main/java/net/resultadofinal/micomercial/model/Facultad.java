package net.resultadofinal.micomercial.model;

public class Facultad {
	private Integer cod_fac,cod_ins;
	private String nom_fac,sig_fac,fot_fac;
	private Boolean est_fac;
	public Integer getCod_fac() {
		return cod_fac;
	}
	public void setCod_fac(Integer cod_fac) {
		this.cod_fac = cod_fac;
	}
	public Integer getCod_ins() {
		return cod_ins;
	}
	public void setCod_ins(Integer cod_ins) {
		this.cod_ins = cod_ins;
	}
	public String getNom_fac() {
		return nom_fac;
	}
	public void setNom_fac(String nom_fac) {
		this.nom_fac = nom_fac;
	}
	public String getSig_fac() {
		return sig_fac;
	}
	public void setSig_fac(String sig_fac) {
		this.sig_fac = sig_fac;
	}
	public String getFot_fac() {
		return fot_fac;
	}
	public void setFot_fac(String fot_fac) {
		this.fot_fac = fot_fac;
	}
	public Boolean getEst_fac() {
		return est_fac;
	}
	public void setEst_fac(Boolean est_fac) {
		this.est_fac = est_fac;
	}
}
