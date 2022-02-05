package net.resultadofinal.micomercial.model;

import java.util.List;

public class TipoMateria {
	private Integer cod_tipmat;
	private String nom_tipmat,des_tipmat;
	private Boolean est_tipmat;
	private List<Materia> materias;
	public TipoMateria() {
	}
	public TipoMateria(String nombre, String descripcion) {
		this.nom_tipmat = nombre;
		this.des_tipmat = descripcion; 
	}
	public Integer getCod_tipmat() {
		return cod_tipmat;
	}
	public void setCod_tipmat(Integer cod_tipmat) {
		this.cod_tipmat = cod_tipmat;
	}
	public String getNom_tipmat() {
		return nom_tipmat;
	}
	public void setNom_tipmat(String nom_tipmat) {
		this.nom_tipmat = nom_tipmat;
	}
	public String getDes_tipmat() {
		return des_tipmat;
	}
	public void setDes_tipmat(String des_tipmat) {
		this.des_tipmat = des_tipmat;
	}
	public Boolean getEst_tipmat() {
		return est_tipmat;
	}
	public void setEst_tipmat(Boolean est_tipmat) {
		this.est_tipmat = est_tipmat;
	}
	public List<Materia> getMaterias() {
		return materias;
	}
	public void setMaterias(List<Materia> materias) {
		this.materias = materias;
	}
}
