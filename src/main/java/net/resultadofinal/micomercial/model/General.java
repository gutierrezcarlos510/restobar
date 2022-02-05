package net.resultadofinal.micomercial.model;

public class General {
	private Integer ges_gen,cod_suc;
	private String des_gen,nom_gen,logtex_gen,logsintex_gen,tel_gen,dir_gen,lug_gen,nit_gen, xsucursal;
	private Boolean est_gen;

	public String getXsucursal() {
		return xsucursal;
	}

	public void setXsucursal(String xsucursal) {
		this.xsucursal = xsucursal;
	}

	public Integer getCod_suc() {
		return cod_suc;
	}
	public void setCod_suc(Integer cod_suc) {
		this.cod_suc = cod_suc;
	}
	public Integer getGes_gen() {
		return ges_gen;
	}
	public void setGes_gen(Integer ges_gen) {
		this.ges_gen = ges_gen;
	}
	public String getDes_gen() {
		return des_gen;
	}
	public void setDes_gen(String des_gen) {
		this.des_gen = des_gen;
	}
	public String getNom_gen() {
		return nom_gen;
	}
	public void setNom_gen(String nom_gen) {
		this.nom_gen = nom_gen;
	}
	public String getLogtex_gen() {
		return logtex_gen;
	}
	public void setLogtex_gen(String logtex_gen) {
		this.logtex_gen = logtex_gen;
	}
	public String getLogsintex_gen() {
		return logsintex_gen;
	}
	public void setLogsintex_gen(String logsintex_gen) {
		this.logsintex_gen = logsintex_gen;
	}
	public String getTel_gen() {
		return tel_gen;
	}
	public void setTel_gen(String tel_gen) {
		this.tel_gen = tel_gen;
	}
	public String getDir_gen() {
		return dir_gen;
	}
	public void setDir_gen(String dir_gen) {
		this.dir_gen = dir_gen;
	}
	public String getLug_gen() {
		return lug_gen;
	}
	public void setLug_gen(String lug_gen) {
		this.lug_gen = lug_gen;
	}
	public String getNit_gen() {
		return nit_gen;
	}
	public void setNit_gen(String nit_gen) {
		this.nit_gen = nit_gen;
	}
	public Boolean getEst_gen() {
		return est_gen;
	}
	public void setEst_gen(Boolean est_gen) {
		this.est_gen = est_gen;
	}
}
