/**
 * 
 */
package net.resultadofinal.micomercial.model.contable;

import net.resultadofinal.micomercial.util.TableRow;

/**
 * @author CARLOS
 *
 */
public class CuentaContable extends TableRow {
	private Integer tipoCuenta,tipoGrupo,codCuenta;
	private String nombre,descripcion,codigo;
	private boolean estado;
	public Integer getTipoCuenta() {
		return tipoCuenta;
	}
	public void setTipoCuenta(Integer tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
	public Integer getTipoGrupo() {
		return tipoGrupo;
	}
	public void setTipoGrupo(Integer tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}
	public Integer getCodCuenta() {
		return codCuenta;
	}
	public void setCodCuenta(Integer codCuenta) {
		this.codCuenta = codCuenta;
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
	public String getXtipo() {
		if(tipoCuenta != null) {
			if(tipoCuenta == 1)
				return "Activo";
			if(tipoCuenta == 2)
				return "Pasivo";
			if(tipoCuenta == 3)
				return "Capital";
			if(tipoCuenta == 4)
				return "Egreso";
			if(tipoCuenta == 5)
				return "Ingreso";
		}
		return "";
	}
	public String getXgrupo() {
		if(tipoGrupo != null) {
			if(tipoGrupo == 0)
				return "General";
			if(tipoGrupo == 1)
				return "Caja";
			if(tipoGrupo == 2)
				return "Banco";
			if(tipoGrupo == 3)
				return "Caja Diaria Circulante";
			if(tipoGrupo == 4)
				return "Activo General";
			if(tipoGrupo == 5)
				return "Otros Prestamos";
			if(tipoGrupo == 6)
				return "Otros Pasivos";
			if(tipoGrupo == 7)
				return "Otros egresos";
			if(tipoGrupo == 8)
				return "Otros ingresos";
		}
		return "";
	}
	public boolean getEsAdicional() {
		if(tipoGrupo != null) {
			if(tipoGrupo == 1 ||tipoGrupo == 2 ||tipoGrupo == 4 ||tipoGrupo == 5 ||tipoGrupo == 6 ||tipoGrupo == 7 ||tipoGrupo == 8) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
}
