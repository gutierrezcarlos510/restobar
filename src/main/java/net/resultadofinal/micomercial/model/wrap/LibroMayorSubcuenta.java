package net.resultadofinal.micomercial.model.wrap;

import net.resultadofinal.micomercial.model.contable.DetalleAsientoContable;

import java.util.List;

public class LibroMayorSubcuenta {
	private Integer codSubcuenta;
	private String codigo;
	private String xsubcuenta;
	private List<DetalleAsientoContable> detalles;
	public Integer getCodSubcuenta() {
		return codSubcuenta;
	}
	public void setCodSubcuenta(Integer codSubcuenta) {
		this.codSubcuenta = codSubcuenta;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getXsubcuenta() {
		return xsubcuenta;
	}
	public void setXsubcuenta(String xsubcuenta) {
		this.xsubcuenta = xsubcuenta;
	}
	public List<DetalleAsientoContable> getDetalles() {
		return detalles;
	}
	public void setDetalles(List<DetalleAsientoContable> detalles) {
		this.detalles = detalles;
	}
	public Double getTotalDebe() {
		if(detalles!=null && !detalles.isEmpty()) {
			return detalles.stream().mapToDouble(x->x.getDebe().doubleValue()).sum();
		}
		return 0d;
	}
	public Double getTotalHaber() {
		if(detalles!=null && !detalles.isEmpty()) {
			return detalles.stream().mapToDouble(x->x.getHaber().doubleValue()).sum();
		}
		return 0d;
	}
}
