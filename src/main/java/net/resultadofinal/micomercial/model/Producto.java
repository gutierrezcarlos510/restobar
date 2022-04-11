package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Producto {
	private Long id,productoId;
	private String nombre, foto;
	private Integer tipoId, tipoGrupo, medidaId, cantidadPlatos;
	private BigDecimal pcUnit, pvUnit, pvCaja, pcCaja, pvUnitDescuento, pvCajaDescuento, cantidad;
	private Integer inventarioMinimoUnidad, inventarioMinimoCaja, unidadPorCaja, tipoCompra;
	private Integer presentacionUnidadId, presentacionCajaId;
	private String xtipo, xpresentacionUnidad, xpresentacionCaja,xgrupo,xtipoCompra,xproducto;
	private String xmedida,obs;
	private Boolean hasIngredients;

	public String getXproducto() {
		return xproducto;
	}

	public void setXproducto(String xproducto) {
		this.xproducto = xproducto;
	}

	public Long getProductoId() {
		return productoId;
	}

	public void setProductoId(Long productoId) {
		this.productoId = productoId;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Integer getCantidadPlatos() {
		return cantidadPlatos;
	}

	public void setCantidadPlatos(Integer cantidadPlatos) {
		this.cantidadPlatos = cantidadPlatos;
	}

	public Boolean getHasIngredients() {
		return hasIngredients;
	}

	public void setHasIngredients(Boolean hasIngredients) {
		this.hasIngredients = hasIngredients;
	}

	public Integer getMedidaId() {
		return medidaId;
	}

	public void setMedidaId(Integer medidaId) {
		this.medidaId = medidaId;
	}

	public String getXmedida() {
		return xmedida;
	}

	public void setXmedida(String xmedida) {
		this.xmedida = xmedida;
	}

	public String getXtipoCompra() {
		return xtipoCompra;
	}

	public void setXtipoCompra(String xtipoCompra) {
		this.xtipoCompra = xtipoCompra;
	}

	public String getXgrupo() {
		return xgrupo;
	}

	public void setXgrupo(String xgrupo) {
		this.xgrupo = xgrupo;
	}

	private Boolean estado;

	public String getXtipo() {
		return xtipo;
	}

	public void setXtipo(String xtipo) {
		this.xtipo = xtipo;
	}

	public String getXpresentacionUnidad() {
		return xpresentacionUnidad;
	}

	public void setXpresentacionUnidad(String xpresentacionUnidad) {
		this.xpresentacionUnidad = xpresentacionUnidad;
	}

	public String getXpresentacionCaja() {
		return xpresentacionCaja;
	}

	public void setXpresentacionCaja(String xpresentacionCaja) {
		this.xpresentacionCaja = xpresentacionCaja;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Integer getTipoId() {
		return tipoId;
	}

	public void setTipoId(Integer tipoId) {
		this.tipoId = tipoId;
	}

	public Integer getTipoGrupo() {
		return tipoGrupo;
	}

	public void setTipoGrupo(Integer tipoGrupo) {
		this.tipoGrupo = tipoGrupo;
	}

	public BigDecimal getPcUnit() {
		return pcUnit;
	}

	public void setPcUnit(BigDecimal pcUnit) {
		this.pcUnit = pcUnit;
	}

	public BigDecimal getPvUnit() {
		return pvUnit;
	}

	public void setPvUnit(BigDecimal pvUnit) {
		this.pvUnit = pvUnit;
	}

	public BigDecimal getPvCaja() {
		return pvCaja;
	}

	public void setPvCaja(BigDecimal pvCaja) {
		this.pvCaja = pvCaja;
	}

	public BigDecimal getPcCaja() {
		return pcCaja;
	}

	public void setPcCaja(BigDecimal pcCaja) {
		this.pcCaja = pcCaja;
	}

	public BigDecimal getPvUnitDescuento() {
		return pvUnitDescuento;
	}

	public void setPvUnitDescuento(BigDecimal pvUnitDescuento) {
		this.pvUnitDescuento = pvUnitDescuento;
	}

	public BigDecimal getPvCajaDescuento() {
		return pvCajaDescuento;
	}

	public void setPvCajaDescuento(BigDecimal pvCajaDescuento) {
		this.pvCajaDescuento = pvCajaDescuento;
	}

	public Integer getInventarioMinimoUnidad() {
		return inventarioMinimoUnidad;
	}

	public void setInventarioMinimoUnidad(Integer inventarioMinimoUnidad) {
		this.inventarioMinimoUnidad = inventarioMinimoUnidad;
	}

	public Integer getInventarioMinimoCaja() {
		return inventarioMinimoCaja;
	}

	public void setInventarioMinimoCaja(Integer inventarioMinimoCaja) {
		this.inventarioMinimoCaja = inventarioMinimoCaja;
	}

	public Integer getUnidadPorCaja() {
		return unidadPorCaja;
	}

	public void setUnidadPorCaja(Integer unidadPorCaja) {
		this.unidadPorCaja = unidadPorCaja;
	}

	public Integer getTipoCompra() {
		return tipoCompra;
	}

	public void setTipoCompra(Integer tipoCompra) {
		this.tipoCompra = tipoCompra;
	}

	public Integer getPresentacionUnidadId() {
		return presentacionUnidadId;
	}

	public void setPresentacionUnidadId(Integer presentacionUnidadId) {
		this.presentacionUnidadId = presentacionUnidadId;
	}

	public Integer getPresentacionCajaId() {
		return presentacionCajaId;
	}

	public void setPresentacionCajaId(Integer presentacionCajaId) {
		this.presentacionCajaId = presentacionCajaId;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public BigDecimal getCantidadCaja(){
		if(this.cantidad != null) {
			if(this.unidadPorCaja != null) {
				return cantidad.divide(new BigDecimal(unidadPorCaja), 2, RoundingMode.HALF_DOWN);
			} else {
				return this.cantidad;
			}
		} else{
			return new BigDecimal(0);
		}
	}
}
