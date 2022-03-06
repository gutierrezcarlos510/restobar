package net.resultadofinal.micomercial.model;

public class TipoProducto {
	private Integer id;
	private String nombre,descripcion;
	private Boolean estado, esPreparado, esComerciable;
	private Integer categoriaId;
	private String xcategoria;
	private Short areaDestino;

	public Boolean getEsComerciable() {
		return esComerciable;
	}

	public void setEsComerciable(Boolean esComerciable) {
		this.esComerciable = esComerciable;
	}

	public Short getAreaDestino() {
		return areaDestino;
	}

	public void setAreaDestino(Short areaDestino) {
		this.areaDestino = areaDestino;
	}

	public Boolean getEsPreparado() {
		return esPreparado;
	}

	public void setEsPreparado(Boolean esPreparado) {
		this.esPreparado = esPreparado;
	}

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
