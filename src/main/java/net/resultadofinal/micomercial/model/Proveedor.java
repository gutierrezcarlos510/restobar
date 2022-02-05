package net.resultadofinal.micomercial.model;

public class Proveedor extends Persona {
	private Long cod_pro;
	private Integer cod_emp;
	private String nom_emp;

	public String getNom_emp() {
		return nom_emp;
	}

	public void setNom_emp(String nom_emp) {
		this.nom_emp = nom_emp;
	}

	public Long getCod_pro() {
		return cod_pro;
	}

	public void setCod_pro(Long cod_pro) {
		this.cod_pro = cod_pro;
	}
	public Integer getCod_emp() {
		return cod_emp;
	}

	public void setCod_emp(Integer cod_emp) {
		this.cod_emp = cod_emp;
	}
}
