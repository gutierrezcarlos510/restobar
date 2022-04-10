package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;

public class DetalleVenta {
	private Long ventaId;
	private Short id;
	private Long productoId;
	private BigDecimal cantidad, cantidadUnitaria,precio,descuento,subtotal,total;
	private Long cartillaDiariaId;
	private Short detalleCartillaDiariaId, tipoVenta;
	private Boolean esCompuesto;
	private String xproducto;

	public BigDecimal getCantidadUnitaria() {
		return cantidadUnitaria;
	}

	public void setCantidadUnitaria(BigDecimal cantidadUnitaria) {
		this.cantidadUnitaria = cantidadUnitaria;
	}

	public Long getCartillaDiariaId() {
		return cartillaDiariaId;
	}

	public void setCartillaDiariaId(Long cartillaDiariaId) {
		this.cartillaDiariaId = cartillaDiariaId;
	}

	public Short getDetalleCartillaDiariaId() {
		return detalleCartillaDiariaId;
	}

	public void setDetalleCartillaDiariaId(Short detalleCartillaDiariaId) {
		this.detalleCartillaDiariaId = detalleCartillaDiariaId;
	}

	public Short getTipoVenta() {
		return tipoVenta;
	}

	public void setTipoVenta(Short tipoVenta) {
		this.tipoVenta = tipoVenta;
	}

	public Boolean getEsCompuesto() {
		return esCompuesto;
	}

	public void setEsCompuesto(Boolean esCompuesto) {
		this.esCompuesto = esCompuesto;
	}

	public Long getVentaId() {
		return ventaId;
	}

	public void setVentaId(Long ventaId) {
		this.ventaId = ventaId;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
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

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public BigDecimal getDescuento() {
		return descuento;
	}

	public void setDescuento(BigDecimal descuento) {
		this.descuento = descuento;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getXproducto() {
		return xproducto;
	}

	public void setXproducto(String xproducto) {
		this.xproducto = xproducto;
	}
}