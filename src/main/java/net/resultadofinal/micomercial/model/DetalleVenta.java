package net.resultadofinal.micomercial.model;

public class DetalleVenta {
	private Long codVen;
	private Integer codDetven;
	private Integer codPro;
	private Float preDetven;
	private Integer canDetven;
	private Float desDetven;
	private Float subtotDetven;
	private Float totDetven;
	private String producto;
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public Long getCodVen() {
		return codVen;
	}
	public void setCodVen(Long codVen) {
		this.codVen = codVen;
	}
	public Integer getCodDetven() {
		return codDetven;
	}
	public void setCodDetven(Integer codDetven) {
		this.codDetven = codDetven;
	}
	public Integer getCodPro() {
		return codPro;
	}
	public void setCodPro(Integer codPro) {
		this.codPro = codPro;
	}
	public Float getPreDetven() {
		return preDetven;
	}
	public void setPreDetven(Float preDetven) {
		this.preDetven = preDetven;
	}
	public Integer getCanDetven() {
		return canDetven;
	}
	public void setCanDetven(Integer canDetven) {
		this.canDetven = canDetven;
	}
	public Float getDesDetven() {
		return desDetven;
	}
	public void setDesDetven(Float desDetven) {
		this.desDetven = desDetven;
	}
	public Float getSubtotDetven() {
		return subtotDetven;
	}
	public void setSubtotDetven(Float subtotDetven) {
		this.subtotDetven = subtotDetven;
	}
	public Float getTotDetven() {
		return totDetven;
	}
	public void setTotDetven(Float totDetven) {
		this.totDetven = totDetven;
	}
}