package net.resultadofinal.micomercial.model.contable;

import java.math.BigDecimal;

public class DetalleAsientoContable {
	private Long codAsiento;
	private Integer codDetalle;
	private Integer codSubcuenta;
	private BigDecimal debe,haber;
	private String xsubcuenta;
	private Integer numero;//atributo que indica a que asiento contable pertenece
	public String getXsubcuenta() {
		return xsubcuenta;
	}
	public void setXsubcuenta(String xsubcuenta) {
		this.xsubcuenta = xsubcuenta;
	}
	public Long getCodAsiento() {
		return codAsiento;
	}
	public void setCodAsiento(Long codAsiento) {
		this.codAsiento = codAsiento;
	}
	public Integer getCodDetalle() {
		return codDetalle;
	}
	public void setCodDetalle(Integer codDetalle) {
		this.codDetalle = codDetalle;
	}
	public Integer getCodSubcuenta() {
		return codSubcuenta;
	}
	public void setCodSubcuenta(Integer codSubcuenta) {
		this.codSubcuenta = codSubcuenta;
	}
	public BigDecimal getDebe() {
		return debe;
	}
	public void setDebe(BigDecimal debe) {
		this.debe = debe;
	}
	public BigDecimal getHaber() {
		return haber;
	}
	public void setHaber(BigDecimal haber) {
		this.haber = haber;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
}
