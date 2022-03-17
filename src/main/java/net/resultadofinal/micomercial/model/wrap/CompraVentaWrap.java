package net.resultadofinal.micomercial.model.wrap;

import java.math.BigDecimal;

public class CompraVentaWrap {
	private String lista;
	private BigDecimal total;
	public String getLista() {
		return lista;
	}
	public void setLista(String lista) {
		this.lista = lista;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
