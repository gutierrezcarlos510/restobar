package net.resultadofinal.micomercial.model.contable;

import java.math.BigDecimal;

public class CuentaContableGrupo {
	private Integer tipoCuenta;
	private Integer codSubcuenta;
	public Integer getCodSubcuenta() {
		return codSubcuenta;
	}
	public void setCodSubcuenta(Integer codSubcuenta) {
		this.codSubcuenta = codSubcuenta;
	}
	private String xcuenta,codigo,xsubcuenta;
	private BigDecimal tdebe,thaber;
	public Integer getTipoCuenta() {
		return tipoCuenta;
	}
	public void setTipoCuenta(Integer tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
	public String getXcuenta() {
		return xcuenta;
	}
	public void setXcuenta(String xcuenta) {
		this.xcuenta = xcuenta;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getXsubcuenta() {
		return xsubcuenta;
	}
	public void setXsubcuenta(String xsubcuenta) {
		this.xsubcuenta = xsubcuenta;
	}
	
	public BigDecimal getTdebe() {
		return tdebe;
	}
	public void setTdebe(BigDecimal tdebe) {
		this.tdebe = tdebe;
	}
	public BigDecimal getThaber() {
		return thaber;
	}
	public void setThaber(BigDecimal thaber) {
		this.thaber = thaber;
	}
	public BigDecimal getDeudor() {
		if(tdebe.compareTo(thaber) > 0)
			return tdebe.subtract(thaber);
		else
			return new BigDecimal(0);
	}
	public BigDecimal getAcreedor() {
		if(thaber.compareTo(tdebe) > 0)
			return thaber.subtract(tdebe);
		else
			return new BigDecimal(0);
	}
	public String getXtipo() {
		if(tipoCuenta != null) {
			if(tipoCuenta == 1)
				return "Activo";
			if(tipoCuenta == 2)
				return "Pasivo";
			if(tipoCuenta == 3)
				return "Capital";
			if(tipoCuenta == 4)
				return "Egreso";
			if(tipoCuenta == 5)
				return "Ingreso";
		}
		return "";
	}
	public String getXcolor() {
		if(tipoCuenta != null) {
			if(tipoCuenta == 1)
				return "w3-pale-green";
			if(tipoCuenta == 2)
				return "w3-pale-red";
			if(tipoCuenta == 3)
				return "w3-pale-yellow";
			if(tipoCuenta == 4)
				return "w3-khaki";
			if(tipoCuenta == 5)
				return "w3-pale-blue";
		}
		return "";
	}
}
