package net.resultadofinal.micomercial.model;

import java.util.List;

public class ArqueoCaja {
	private Long cod_arqcaj,del_arqcaj,cusini_arqcaj,cusfin_arqcaj,codAsiento;
	private Integer ges_gen,cod_suc;
	private String des_arqcaj,fini_arqcaj,ffin_arqcaj,delegado,custodio_inicial,custodio_final,fechai,fechaf;
	private Float monini_arqcaj,monfin_arqcaj,monrea_arqcaj;
	private Boolean est_arqcaj;
	private List<DetalleArqueo> detalles;
	public Integer getCod_suc() {
		return cod_suc;
	}
	public Long getCodAsiento() {
		return codAsiento;
	}
	public void setCodAsiento(Long codAsiento) {
		this.codAsiento = codAsiento;
	}
	public void setCod_suc(Integer cod_suc) {
		this.cod_suc = cod_suc;
	}
	public Long getCod_arqcaj() {
		return cod_arqcaj;
	}
	public void setCod_arqcaj(Long cod_arqcaj) {
		this.cod_arqcaj = cod_arqcaj;
	}
	public Long getDel_arqcaj() {
		return del_arqcaj;
	}
	public void setDel_arqcaj(Long del_arqcaj) {
		this.del_arqcaj = del_arqcaj;
	}
	public Long getCusini_arqcaj() {
		return cusini_arqcaj;
	}
	public void setCusini_arqcaj(Long cusini_arqcaj) {
		this.cusini_arqcaj = cusini_arqcaj;
	}
	public Long getCusfin_arqcaj() {
		return cusfin_arqcaj;
	}
	public void setCusfin_arqcaj(Long cusfin_arqcaj) {
		this.cusfin_arqcaj = cusfin_arqcaj;
	}
	public Integer getGes_gen() {
		return ges_gen;
	}
	public void setGes_gen(Integer ges_gen) {
		this.ges_gen = ges_gen;
	}
	public String getDes_arqcaj() {
		return des_arqcaj;
	}
	public void setDes_arqcaj(String des_arqcaj) {
		this.des_arqcaj = des_arqcaj;
	}
	public String getFini_arqcaj() {
		return fini_arqcaj;
	}
	public void setFini_arqcaj(String fini_arqcaj) {
		this.fini_arqcaj = fini_arqcaj;
	}
	public String getFfin_arqcaj() {
		return ffin_arqcaj;
	}
	public void setFfin_arqcaj(String ffin_arqcaj) {
		this.ffin_arqcaj = ffin_arqcaj;
	}
	public Float getMonini_arqcaj() {
		return monini_arqcaj;
	}
	public void setMonini_arqcaj(Float monini_arqcaj) {
		this.monini_arqcaj = monini_arqcaj;
	}
	public Float getMonfin_arqcaj() {
		return monfin_arqcaj;
	}
	public void setMonfin_arqcaj(Float monfin_arqcaj) {
		this.monfin_arqcaj = monfin_arqcaj;
	}
	public Float getMonrea_arqcaj() {
		return monrea_arqcaj;
	}
	public void setMonrea_arqcaj(Float monrea_arqcaj) {
		this.monrea_arqcaj = monrea_arqcaj;
	}
	public Boolean getEst_arqcaj() {
		return est_arqcaj;
	}
	public void setEst_arqcaj(Boolean est_arqcaj) {
		this.est_arqcaj = est_arqcaj;
	}
	public String getDelegado() {
		return delegado;
	}
	public void setDelegado(String delegado) {
		this.delegado = delegado;
	}
	public String getCustodio_inicial() {
		return custodio_inicial;
	}
	public void setCustodio_inicial(String custodio_inicial) {
		this.custodio_inicial = custodio_inicial;
	}
	public String getCustodio_final() {
		return custodio_final;
	}
	public void setCustodio_final(String custodio_final) {
		this.custodio_final = custodio_final;
	}
	public String getFechai() {
		return fechai;
	}
	public void setFechai(String fechai) {
		this.fechai = fechai;
	}
	public String getFechaf() {
		return fechaf;
	}
	public void setFechaf(String fechaf) {
		this.fechaf = fechaf;
	}
	public List<DetalleArqueo> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<DetalleArqueo> detalles) {
		this.detalles = detalles;
	}
	@Override
	public String toString() {
		return "ArqueoCaja [cod_arqcaj=" + cod_arqcaj + ", del_arqcaj=" + del_arqcaj + ", cusini_arqcaj="
				+ cusini_arqcaj + ", cusfin_arqcaj=" + cusfin_arqcaj + ", ges_gen=" + ges_gen + ", cod_suc=" + cod_suc
				+ ", des_arqcaj=" + des_arqcaj + ", fini_arqcaj=" + fini_arqcaj + ", ffin_arqcaj=" + ffin_arqcaj
				+ ", delegado=" + delegado + ", custodio_inicial=" + custodio_inicial + ", custodio_final="
				+ custodio_final + ", fechai=" + fechai + ", fechaf=" + fechaf + ", monini_arqcaj=" + monini_arqcaj
				+ ", monfin_arqcaj=" + monfin_arqcaj + ", monrea_arqcaj=" + monrea_arqcaj + ", est_arqcaj=" + est_arqcaj
				+ "]";
	}
}
