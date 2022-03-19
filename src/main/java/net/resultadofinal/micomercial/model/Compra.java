package net.resultadofinal.micomercial.model;

import net.resultadofinal.micomercial.util.TableRow;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Compra extends TableRow {
	private Long cod_com,cod_per,cod_pro,cod_arqcaj;
	private Integer ges_gen,cod_detarq,cod_suc;
	private String obs_com;
	private Date fec_com;
	private BigDecimal tot_com,des_com,subtotCom;
	private Boolean est_com;
	private String usuario,proveedor,fecha;
	private List<DetalleCompra> detalles;

	public Long getCod_com() {
		return cod_com;
	}

	public void setCod_com(Long cod_com) {
		this.cod_com = cod_com;
	}

	public Long getCod_per() {
		return cod_per;
	}

	public void setCod_per(Long cod_per) {
		this.cod_per = cod_per;
	}

	public Long getCod_pro() {
		return cod_pro;
	}

	public void setCod_pro(Long cod_pro) {
		this.cod_pro = cod_pro;
	}

	public Long getCod_arqcaj() {
		return cod_arqcaj;
	}

	public void setCod_arqcaj(Long cod_arqcaj) {
		this.cod_arqcaj = cod_arqcaj;
	}

	public Integer getGes_gen() {
		return ges_gen;
	}

	public void setGes_gen(Integer ges_gen) {
		this.ges_gen = ges_gen;
	}

	public Integer getCod_detarq() {
		return cod_detarq;
	}

	public void setCod_detarq(Integer cod_detarq) {
		this.cod_detarq = cod_detarq;
	}

	public Integer getCod_suc() {
		return cod_suc;
	}

	public void setCod_suc(Integer cod_suc) {
		this.cod_suc = cod_suc;
	}

	public String getObs_com() {
		return obs_com;
	}

	public void setObs_com(String obs_com) {
		this.obs_com = obs_com;
	}

	public Date getFec_com() {
		return fec_com;
	}

	public void setFec_com(Date fec_com) {
		this.fec_com = fec_com;
	}

	public BigDecimal getTot_com() {
		return tot_com;
	}

	public void setTot_com(BigDecimal tot_com) {
		this.tot_com = tot_com;
	}

	public BigDecimal getDes_com() {
		return des_com;
	}

	public void setDes_com(BigDecimal des_com) {
		this.des_com = des_com;
	}

	public BigDecimal getSubtotCom() {
		return subtotCom;
	}

	public void setSubtotCom(BigDecimal subtotCom) {
		this.subtotCom = subtotCom;
	}

	public Boolean getEst_com() {
		return est_com;
	}

	public void setEst_com(Boolean est_com) {
		this.est_com = est_com;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public List<DetalleCompra> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleCompra> detalles) {
		this.detalles = detalles;
	}
}
