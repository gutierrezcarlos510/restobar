package net.resultadofinal.micomercial.model.wrap;

public class ArqueoWrap {
	private Long codArqcaj;
	private Float tingresos, tegresos, montoReal,tbanco;
	private String xusuario,finicio,ffinal;
	private Boolean esActivo;
	public Float getTbanco() {
		return tbanco;
	}
	public void setTbanco(Float tbanco) {
		this.tbanco = tbanco;
	}
	public Long getCodArqcaj() {
		return codArqcaj;
	}
	public void setCodArqcaj(Long codArqcaj) {
		this.codArqcaj = codArqcaj;
	}
	public Float getTingresos() {
		return tingresos;
	}
	public void setTingresos(Float tingresos) {
		this.tingresos = tingresos;
	}
	public Float getTegresos() {
		return tegresos;
	}
	public void setTegresos(Float tegresos) {
		this.tegresos = tegresos;
	}
	public Float getMontoReal() {
		return montoReal;
	}
	public void setMontoReal(Float montoReal) {
		this.montoReal = montoReal;
	}
	public String getXusuario() {
		return xusuario;
	}
	public void setXusuario(String xusuario) {
		this.xusuario = xusuario;
	}
	public String getFinicio() {
		return finicio;
	}
	public void setFinicio(String finicio) {
		this.finicio = finicio;
	}
	public String getFfinal() {
		return ffinal;
	}
	public void setFfinal(String ffinal) {
		this.ffinal = ffinal;
	}
	public Boolean getEsActivo() {
		return esActivo;
	}
	public void setEsActivo(Boolean esActivo) {
		this.esActivo = esActivo;
	}
	public Float getTotalSistema() {
		return this.tingresos-this.tegresos;
	}
	public Float getDiferencia() {
		if(this.montoReal != null) {
			return getTotalSistema()-montoReal;
		}else {
			return -1f;
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
				if(getDiferencia() == 0) {
					return "Arqueo sin observaciones";
				}else {
					if(getDiferencia()> 0f) {
						return getDiferencia()+" (Por demas de efectivo)";
					}else {
						return (-1f*getDiferencia())+" (Faltante)";
					}
				}
			}
			
		}else {
			return "-";
		}
	}
}
