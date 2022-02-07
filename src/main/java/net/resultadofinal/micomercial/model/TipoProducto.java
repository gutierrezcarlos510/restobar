package net.resultadofinal.micomercial.model;

import java.util.List;

public class TipoProducto {
	private Integer id;
	private String nombre,descripcion;
	private Boolean estado;
	private Integer categoriaId;
	private String xcategoria;

	public String getXcategoria() {
		return xcategoria;
	}

	public void setXcategoria(String xcategoria) {
		this.xcategoria = xcategoria;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getCategoriaId() {
		return categoriaId;
	}

	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}
}
