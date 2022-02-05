package net.resultadofinal.micomercial.model.contable;

import java.sql.Date;
import java.sql.Timestamp;

public class AsientoContable {
	private Long codAsiento;
	private Integer numero, gesGen, codSuc;
	private Date fecha;
	private Timestamp createdAt;
	private String createdBy,concepto,xfecha;
	private boolean estado;
	public String getXfecha() {
		return xfecha;
	}
	public void setXfecha(String xfecha) {
		this.xfecha = xfecha;
	}
	public Long getCodAsiento() {
		return codAsiento;
	}
	public void setCodAsiento(Long codAsiento) {
		this.codAsiento = codAsiento;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	public Integer getGesGen() {
		return gesGen;
	}
	public void setGesGen(Integer gesGen) {
		this.gesGen = gesGen;
	}
	public Integer getCodSuc() {
		return codSuc;
	}
	public void setCodSuc(Integer codSuc) {
		this.codSuc = codSuc;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
}
