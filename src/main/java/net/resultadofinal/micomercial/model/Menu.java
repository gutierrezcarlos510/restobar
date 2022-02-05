package net.resultadofinal.micomercial.model;

import java.util.List;
import java.util.Map;

public class Menu {
	private Integer cod_men;
	private String nom_men,des_men,ico_men;
	private Boolean est_men;
	private String tipo;
	private Integer totalProcesos;
	private List<Proceso> procesos;
	public Integer getTotalProcesos() {
		return totalProcesos;
	}

	public void setTotalProcesos(Integer totalProcesos) {
		this.totalProcesos = totalProcesos;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Integer getCod_men() {
		return cod_men;
	}
	public void setCod_men(Integer cod_men) {
		this.cod_men = cod_men;
	}
	public String getNom_men() {
		return nom_men;
	}
	public void setNom_men(String nom_men) {
		this.nom_men = nom_men;
	}
	public String getDes_men() {
		return des_men;
	}
	public void setDes_men(String des_men) {
		this.des_men = des_men;
	}
	public String getIco_men() {
		return ico_men;
	}
	public void setIco_men(String ico_men) {
		this.ico_men = ico_men;
	}
	public Boolean getEst_men() {
		return est_men;
	}
	public void setEst_men(Boolean est_men) {
		this.est_men = est_men;
	}

	public List<Proceso> getProcesos() {
		return procesos;
	}

	public void setProcesos(List<Proceso> procesos) {
		this.procesos = procesos;
	}
}
