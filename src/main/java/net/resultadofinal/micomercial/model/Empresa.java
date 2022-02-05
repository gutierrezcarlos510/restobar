package net.resultadofinal.micomercial.model;

public class Empresa {
	private Integer cod_emp;
	private String nom_emp,dir_emp,tel_emp,ema_emp;
	private Boolean est_emp;
	public Integer getCod_emp() {
		return cod_emp;
	}
	public void setCod_emp(Integer cod_emp) {
		this.cod_emp = cod_emp;
	}
	public String getNom_emp() {
		return nom_emp;
	}
	public void setNom_emp(String nom_emp) {
		this.nom_emp = nom_emp;
	}
	public String getDir_emp() {
		return dir_emp;
	}
	public void setDir_emp(String dir_emp) {
		this.dir_emp = dir_emp;
	}
	public String getTel_emp() {
		return tel_emp;
	}
	public void setTel_emp(String tel_emp) {
		this.tel_emp = tel_emp;
	}
	public String getEma_emp() {
		return ema_emp;
	}
	public void setEma_emp(String ema_emp) {
		this.ema_emp = ema_emp;
	}
	public Boolean getEst_emp() {
		return est_emp;
	}
	public void setEst_emp(Boolean est_emp) {
		this.est_emp = est_emp;
	}
}
