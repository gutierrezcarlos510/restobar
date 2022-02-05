package net.resultadofinal.micomercial.model.wrap;

import net.resultadofinal.micomercial.util.TableRow;

public class AyudantePrestacionWrap extends TableRow {
	private Long codAyu;
	private String prestaciones, ayudante;
	private Long total;
	public Long getCodAyu() {
		return codAyu;
	}
	public void setCodAyu(Long codAyu) {
		this.codAyu = codAyu;
	}
	public String getPrestaciones() {
		return prestaciones;
	}
	public void setPrestaciones(String prestaciones) {
		this.prestaciones = prestaciones;
	}
	public String getAyudante() {
		return ayudante;
	}
	public void setAyudante(String ayudante) {
		this.ayudante = ayudante;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
}
