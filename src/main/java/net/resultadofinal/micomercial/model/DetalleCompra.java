package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;

public class DetalleCompra {
	private Long cod_com,cod_pro;
	private Integer cod_detcom,can_detcom, cantidadUnitaria;
	private Short tipoCompra;
	private BigDecimal pre_detcom,des_detcom,subtotDetcom,totDetcom;
	private String producto;

	public Integer getCantidadUnitaria() {
		return cantidadUnitaria;
	}

	public void setCantidadUnitaria(Integer cantidadUnitaria) {
		this.cantidadUnitaria = cantidadUnitaria;
	}

	public Short getTipoCompra() {
		return tipoCompra;
	}

	public void setTipoCompra(Short tipoCompra) {
		this.tipoCompra = tipoCompra;
	}

	public Long getCod_com() {
		return cod_com;
	}

	public void setCod_com(Long cod_com) {
		this.cod_com = cod_com;
	}

	public Long getCod_pro() {
		return cod_pro;
	}

	public void setCod_pro(Long cod_pro) {
		this.cod_pro = cod_pro;
	}

	public Integer getCod_detcom() {
		return cod_detcom;
	}

	public void setCod_detcom(Integer cod_detcom) {
		this.cod_detcom = cod_detcom;
	}

	public Integer getCan_detcom() {
		return can_detcom;
	}

	public void setCan_detcom(Integer can_detcom) {
		this.can_detcom = can_detcom;
	}

	public BigDecimal getPre_detcom() {
		return pre_detcom;
	}

	public void setPre_detcom(BigDecimal pre_detcom) {
		this.pre_detcom = pre_detcom;
	}

	public BigDecimal getDes_detcom() {
		return des_detcom;
	}

	public void setDes_detcom(BigDecimal des_detcom) {
		this.des_detcom = des_detcom;
	}

	public BigDecimal getSubtotDetcom() {
		return subtotDetcom;
	}

	public void setSubtotDetcom(BigDecimal subtotDetcom) {
		this.subtotDetcom = subtotDetcom;
	}

	public BigDecimal getTotDetcom() {
		return totDetcom;
	}

	public void setTotDetcom(BigDecimal totDetcom) {
		this.totDetcom = totDetcom;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}
}
