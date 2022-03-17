package net.resultadofinal.micomercial.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class ArqueoCaja {
	private Long id,delegadoId,custodioInicialId,custodioFinalId,asientoId;
	private Integer gestion,sucursalId;
	private String descripcion,xdelegado,xcustodioInicial,xcustodioFinal;
	private Timestamp finicio, ffin;
	private BigDecimal montoFinal, montoInicial, montoReal;
	private Boolean estado;
	private List<DetalleArqueo> detalles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDelegadoId() {
		return delegadoId;
	}

	public void setDelegadoId(Long delegadoId) {
		this.delegadoId = delegadoId;
	}

	public Long getCustodioInicialId() {
		return custodioInicialId;
	}

	public void setCustodioInicialId(Long custodioInicialId) {
		this.custodioInicialId = custodioInicialId;
	}

	public Long getCustodioFinalId() {
		return custodioFinalId;
	}

	public void setCustodioFinalId(Long custodioFinalId) {
		this.custodioFinalId = custodioFinalId;
	}

	public Long getAsientoId() {
		return asientoId;
	}

	public void setAsientoId(Long asientoId) {
		this.asientoId = asientoId;
	}

	public Integer getGestion() {
		return gestion;
	}

	public void setGestion(Integer gestion) {
		this.gestion = gestion;
	}

	public Integer getSucursalId() {
		return sucursalId;
	}

	public void setSucursalId(Integer sucursalId) {
		this.sucursalId = sucursalId;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getXdelegado() {
		return xdelegado;
	}

	public void setXdelegado(String xdelegado) {
		this.xdelegado = xdelegado;
	}

	public String getXcustodioInicial() {
		return xcustodioInicial;
	}

	public void setXcustodioInicial(String xcustodioInicial) {
		this.xcustodioInicial = xcustodioInicial;
	}

	public String getXcustodioFinal() {
		return xcustodioFinal;
	}

	public void setXcustodioFinal(String xcustodioFinal) {
		this.xcustodioFinal = xcustodioFinal;
	}

	public Timestamp getFinicio() {
		return finicio;
	}

	public void setFinicio(Timestamp finicio) {
		this.finicio = finicio;
	}

	public Timestamp getFfin() {
		return ffin;
	}

	public void setFfin(Timestamp ffin) {
		this.ffin = ffin;
	}

	public BigDecimal getMontoFinal() {
		return montoFinal;
	}

	public void setMontoFinal(BigDecimal montoFinal) {
		this.montoFinal = montoFinal;
	}

	public BigDecimal getMontoInicial() {
		return montoInicial;
	}

	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}

	public BigDecimal getMontoReal() {
		return montoReal;
	}

	public void setMontoReal(BigDecimal montoReal) {
		this.montoReal = montoReal;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public List<DetalleArqueo> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleArqueo> detalles) {
		this.detalles = detalles;
	}
}
