package net.resultadofinal.micomercial.model;

public class Sucursal {
	private Integer cod_suc;
	private String nombre,descripcion,tipo, telefono, direccion, alias, tituloNotificacion, mensajeNotificacion;
	private Boolean estado, estadoNotificacion;

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTituloNotificacion() {
		return tituloNotificacion;
	}

	public void setTituloNotificacion(String tituloNotificacion) {
		this.tituloNotificacion = tituloNotificacion;
	}

	public String getMensajeNotificacion() {
		return mensajeNotificacion;
	}

	public void setMensajeNotificacion(String mensajeNotificacion) {
		this.mensajeNotificacion = mensajeNotificacion;
	}

	public Boolean getEstadoNotificacion() {
		return estadoNotificacion;
	}

	public void setEstadoNotificacion(Boolean estadoNotificacion) {
		this.estadoNotificacion = estadoNotificacion;
	}

	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Integer getCod_suc() {
		return cod_suc;
	}
	public void setCod_suc(Integer cod_suc) {
		this.cod_suc = cod_suc;
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
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	@Override
	public String toString() {
		return "Sucursal [cod_suc=" + cod_suc + ", nombre=" + nombre + ", descripcion=" + descripcion + ", estado="
				+ estado + "]";
	}
}
