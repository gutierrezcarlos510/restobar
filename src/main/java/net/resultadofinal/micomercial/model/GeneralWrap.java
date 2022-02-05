package net.resultadofinal.micomercial.model;

import java.util.List;

public class GeneralWrap {
	private Sucursal sucursal;
	private List<General> generalList;
	public GeneralWrap() {}
	public GeneralWrap(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public Sucursal getSucursal() {
		return sucursal;
	}
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}
	public List<General> getGeneralList() {
		return generalList;
	}
	public void setGeneralList(List<General> generalList) {
		this.generalList = generalList;
	}
}
