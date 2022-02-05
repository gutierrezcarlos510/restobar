package net.resultadofinal.micomercial.model;

public class DetalleCompra {
	private Long cod_com;
	private Integer cod_detcom,cod_pro,can_detcom;
	private Float pre_detcom,des_detcom,subtotDetcom,totDetcom;
	private String producto;
	public String getProducto() {
		return producto;
	}
	public void setProducto(String producto) {
		this.producto = producto;
	}
	public Float getSubtotDetcom() {
		return subtotDetcom;
	}
	public void setSubtotDetcom(Float subtotDetcom) {
		this.subtotDetcom = subtotDetcom;
	}
	public Float getTotDetcom() {
		return totDetcom;
	}
	public void setTotDetcom(Float totDetcom) {
		this.totDetcom = totDetcom;
	}
	public Long getCod_com() {
		return cod_com;
	}
	public void setCod_com(Long cod_com) {
		this.cod_com = cod_com;
	}
	public Integer getCod_detcom() {
		return cod_detcom;
	}
	public void setCod_detcom(Integer cod_detcom) {
		this.cod_detcom = cod_detcom;
	}
	public Integer getCod_pro() {
		return cod_pro;
	}
	public void setCod_pro(Integer cod_pro) {
		this.cod_pro = cod_pro;
	}
	public Integer getCan_detcom() {
		return can_detcom;
	}
	public void setCan_detcom(Integer can_detcom) {
		this.can_detcom = can_detcom;
	}
	public Float getPre_detcom() {
		return pre_detcom;
	}
	public void setPre_detcom(Float pre_detcom) {
		this.pre_detcom = pre_detcom;
	}
	public Float getDes_detcom() {
		return des_detcom;
	}
	public void setDes_detcom(Float des_detcom) {
		this.des_detcom = des_detcom;
	}
}
