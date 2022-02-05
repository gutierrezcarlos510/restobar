package net.resultadofinal.micomercial.model;

import net.resultadofinal.micomercial.util.TableRow;

import java.util.List;

public class Venta extends TableRow {
	private Long codVen,codPer,codCli,codArqcaj;
	private Integer gesGen,codDetarq,tipVen,codSuc;
	private String fecVen,obsVen;
	private Float totVen,desVen,subtotVen;
	private Boolean estVen;
	private String usuario,cliente,fecha;
	private List<DetalleVenta> detalles;
	private Integer codSubcuenta;
	public Long getCodVen() {
		return codVen;
	}
	public void setCodVen(Long codVen) {
		this.codVen = codVen;
	}
	public Long getCodPer() {
		return codPer;
	}
	public void setCodPer(Long codPer) {
		this.codPer = codPer;
	}
	public Long getCodCli() {
		return codCli;
	}
	public void setCodCli(Long codCli) {
		this.codCli = codCli;
	}
	public Long getCodArqcaj() {
		return codArqcaj;
	}
	public void setCodArqcaj(Long codArqcaj) {
		this.codArqcaj = codArqcaj;
	}
	public Integer getGesGen() {
		return gesGen;
	}
	public void setGesGen(Integer gesGen) {
		this.gesGen = gesGen;
	}
	public Integer getCodDetarq() {
		return codDetarq;
	}
	public void setCodDetarq(Integer codDetarq) {
		this.codDetarq = codDetarq;
	}
	public Integer getTipVen() {
		return tipVen;
	}
	public void setTipVen(Integer tipVen) {
		this.tipVen = tipVen;
	}
	public Integer getCodSuc() {
		return codSuc;
	}
	public void setCodSuc(Integer codSuc) {
		this.codSuc = codSuc;
	}
	public String getFecVen() {
		return fecVen;
	}
	public void setFecVen(String fecVen) {
		this.fecVen = fecVen;
	}
	public String getObsVen() {
		return obsVen;
	}
	public void setObsVen(String obsVen) {
		this.obsVen = obsVen;
	}
	public Float getTotVen() {
		return totVen;
	}
	public void setTotVen(Float totVen) {
		this.totVen = totVen;
	}
	public Float getDesVen() {
		return desVen;
	}
	public void setDesVen(Float desVen) {
		this.desVen = desVen;
	}
	public Float getSubtotVen() {
		return subtotVen;
	}
	public void setSubtotVen(Float subtotVen) {
		this.subtotVen = subtotVen;
	}
	public Boolean getEstVen() {
		return estVen;
	}
	public void setEstVen(Boolean estVen) {
		this.estVen = estVen;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public List<DetalleVenta> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<DetalleVenta> detalles) {
		this.detalles = detalles;
	}
	public Integer getCodSubcuenta() {
		return codSubcuenta;
	}
	public void setCodSubcuenta(Integer codSubcuenta) {
		this.codSubcuenta = codSubcuenta;
	}
}
