package net.resultadofinal.micomercial.model;

public class Proceso {
	private Integer cod_pro;
	private String nom_pro,des_pro,ico_pro,url_pro,tipo;
	private Boolean est_pro;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getCod_pro() {
		return cod_pro;
	}
	public void setCod_pro(Integer cod_pro) {
		this.cod_pro = cod_pro;
	}
	public String getNom_pro() {
		return nom_pro;
	}
	public void setNom_pro(String nom_pro) {
		this.nom_pro = nom_pro;
	}
	public String getDes_pro() {
		return des_pro;
	}
	public void setDes_pro(String des_pro) {
		this.des_pro = des_pro;
	}
	public String getIco_pro() {
		return ico_pro;
	}
	public void setIco_pro(String ico_pro) {
		this.ico_pro = ico_pro;
	}
	public String getUrl_pro() {
		return url_pro;
	}
	public void setUrl_pro(String url_pro) {
		this.url_pro = url_pro;
	}
	public Boolean getEst_pro() {
		return est_pro;
	}
	public void setEst_pro(Boolean est_pro) {
		this.est_pro = est_pro;
	}
	}
