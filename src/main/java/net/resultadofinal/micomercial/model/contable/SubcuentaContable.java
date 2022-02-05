package net.resultadofinal.micomercial.model.contable;

import net.resultadofinal.micomercial.util.TableRow;

public class SubcuentaContable extends TableRow {
	private Integer codCuenta, codSubcuenta;
	private String nombre, descripcion, codigo;
	private boolean estado,esExterno;
	public Integer getCodCuenta() {
		return codCuenta;
	}
	public void setCodCuenta(Integer codCuenta) {
		this.codCuenta = codCuenta;
	}
	public Integer getCodSubcuenta() {
		return codSubcuenta;
	}
	public void setCodSubcuenta(Integer codSubcuenta) {
		this.codSubcuenta = codSubcuenta;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public boolean isEsExterno() {
		return esExterno;
	}
	public void setEsExterno(boolean esExterno) {
		this.esExterno = esExterno;
	}
}
