package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Venta  {
	private Long id,usuarioId,clienteId,arqueoId;
	private Integer gestion,detalleArqueoId,sucursalId;
	private Short tipo;
	private String obs;
	private Date fecha;
	private BigDecimal total,descuento,subtotal;
	private Boolean estado;
	private String xusuario,xcliente;
	private List<DetalleVenta> detalles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(Long usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Long getClienteId() {
		return clienteId;
	}

	public void setClienteId(Long clienteId) {
		this.clienteId = clienteId;
	}

	public Long getArqueoId() {
		return arqueoId;
	}

	public void setArqueoId(Long arqueoId) {
		this.arqueoId = arqueoId;
	}

	public Integer getGestion() {
		return gestion;
	}

	public void setGestion(Integer gestion) {
		this.gestion = gestion;
	}

	public Integer getDetalleArqueoId() {
		return detalleArqueoId;
	}

	public void setDetalleArqueoId(Integer detalleArqueoId) {
		this.detalleArqueoId = detalleArqueoId;
	}

	public Integer getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getXusuario() {
		return xusuario;
	}

	public void setXusuario(String xusuario) {
		this.xusuario = xusuario;
	}

	public String getXcliente() {
		return xcliente;
	}

	public void setXcliente(String xcliente) {
		this.xcliente = xcliente;
	}

	public List<DetalleVenta> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleVenta> detalles) {
		this.detalles = detalles;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
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

	public Short getTipo() {
		return tipo;
	}

	public void setTipo(Short tipo) {
		this.tipo = tipo;
	}
	public String getXtipo(){
		if(tipo != null) {
			switch (tipo) {
				case 1:
					return "Activo";
				case 2:
					return "Finalizado";
				case 3:
					return "Revertido";
				default:
					return "";
			}
		} else {
			return "";
		}
	}
}
