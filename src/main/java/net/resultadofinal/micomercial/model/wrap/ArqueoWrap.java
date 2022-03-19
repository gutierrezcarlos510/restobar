package net.resultadofinal.micomercial.model.wrap;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ArqueoWrap {
	private Long id;
	private BigDecimal tingresos, tegresos, montoReal,tbanco;
	private String xusuario;
	private Timestamp finicio,ffin;
	private Boolean esActivo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTingresos() {
		return tingresos;
	}

	public void setTingresos(BigDecimal tingresos) {
		this.tingresos = tingresos;
	}

	public BigDecimal getTegresos() {
		return tegresos;
	}

	public void setTegresos(BigDecimal tegresos) {
		this.tegresos = tegresos;
	}

	public BigDecimal getMontoReal() {
		return montoReal;
	}

	public void setMontoReal(BigDecimal montoReal) {
		this.montoReal = montoReal;
	}

	public BigDecimal getTbanco() {
		return tbanco;
	}

	public void setTbanco(BigDecimal tbanco) {
		this.tbanco = tbanco;
	}

	public String getXusuario() {
		return xusuario;
	}

	public void setXusuario(String xusuario) {
		this.xusuario = xusuario;
	}

	public Timestamp getFinicio() {
		return finicio;
	}

	public void setFinicio(Timestamp finicio) {
		this.finicio = finicio;
	}

	public Timestamp getFfin() {
		return ffin;
	}

	public void setFfin(Timestamp ffin) {
		this.ffin = ffin;
	}

	public Boolean getEsActivo() {
		return esActivo;
	}

	public void setEsActivo(Boolean esActivo) {
		this.esActivo = esActivo;
	}

	public BigDecimal getTotalSistema() {
		return this.tingresos.subtract(this.tegresos);
	}
	public BigDecimal getDiferencia() {
		if(this.montoReal != null) {
			return getTotalSistema().subtract(montoReal);
		}else {
			return new BigDecimal(-1);
		}
	}
	public String getXesActivo() {
		return esActivo ? "Pendiente":"Finalizado";
	}
	public String getInterpretacion() {
		if(esActivo != null) {
			if(esActivo) {
				return "Caja activa";
			}else {
				if(getDiferencia().intValue()==0) {
					return "Arqueo sin observaciones";
				}else {
					if(getDiferencia().floatValue() > 0f) {
						return getDiferencia()+" (Por demas de efectivo)";
					}else {
						return (-1f*getDiferencia().floatValue())+" (Faltante)";
					}
				}
			}
		}else {
			return "-";
		}
	}
}
