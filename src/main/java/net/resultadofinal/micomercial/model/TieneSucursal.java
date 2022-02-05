package net.resultadofinal.micomercial.model;

import java.util.List;

public class TieneSucursal {
	private Long cod_per;
	private Integer cod_suc;
	private Sucursal sucursal;
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	private List<General> generalList;
	public Long getCod_per() {
		return cod_per;
	}
	public void setCod_per(Long cod_per) {
		this.cod_per = cod_per;
	}
	public Integer getCod_suc() {
		return cod_suc;
	}
	public void setCod_suc(Integer cod_suc) {
		this.cod_suc = cod_suc;
	}
	public List<General> getGeneralList() {
		return generalList;
	}
	public void setGeneralList(List<General> generalList) {
		this.generalList = generalList;
	}
	@Override
	public String toString() {
		return "TieneSucursal [cod_per=" + cod_per + ", cod_suc=" + cod_suc + ", generalList=" + generalList + "]";
	}
}
