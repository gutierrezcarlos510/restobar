package net.resultadofinal.micomercial.model;

import net.resultadofinal.micomercial.util.TableRow;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class DetalleArqueo extends TableRow {
	private Integer id,subcuentaId;
	private Short tipo;
	private Long arqueoId;
	private String descripcion;
	private Timestamp fecha;
	private BigDecimal monto;
	private Boolean estado;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSubcuentaId() {
		return subcuentaId;
	}

	public void setSubcuentaId(Integer subcuentaId) {
		this.subcuentaId = subcuentaId;
	}

	public void setTipo(Short tipo) {
		this.tipo = tipo;
	}
	public Short getTipo() {
		return tipo;
	}
	public Long getArqueoId() {
		return arqueoId;
	}

	public void setArqueoId(Long arqueoId) {
		this.arqueoId = arqueoId;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public BigDecimal getMonto() {
		return monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public String getXtipo() {
		if(tipo!=null) {
			if(tipo == 1)
				return "Egreso de caja";
			if(tipo == 2)
				return "Pago ayudante x inscripcion";
			if(tipo == 3)
				return "Pago ayudante x prestacion";
			if(tipo == 4)
				return "Compra";
			if(tipo == 5)
				return "Ingreso a caja";
			if(tipo == 6)
				return "Ingreso de incripcion";
			if(tipo == 7)
				return "Ingreso de prestacion";
			if(tipo == 8)
				return "Venta";
			if(tipo == 9)
				return "Caja General";
			if(tipo == 10)
				return "Banco General";
			if(tipo == 11)
				return "Muebles de oficina";
			if(tipo == 12)
				return "Inmuebles o propiedades";
			if(tipo == 13)
				return "Otros activos";
			if(tipo == 14)
				return "Prestamos bancarios";
			if(tipo == 15)
				return "Otros pasivos";
			if(tipo == 16)
				return "Ingreso general";
			if(tipo == 17)
				return "Egreso general";
			if(tipo == 18)
				return "Descuento por venta";
			if(tipo == 19)
				return "Descuento por compra";
			if(tipo == 20)
				return "Venta con pago bancario";
			if(tipo == 21)
				return "Devolucion por incumplimiento de prestacion";
			if(tipo == 22)
				return "Pago prestacion de servicio con banco";
			if(tipo == 23)
				return "Ingreso por multas cobradas";
			if(tipo == 24)
				return "Devolucion con deposito bancario";
			return "";
		}else {
			return "";
		}
	}
}
