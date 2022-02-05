package net.resultadofinal.micomercial.model;

import net.resultadofinal.micomercial.util.TableRow;

public class DetalleArqueo extends TableRow {
	private Integer cod_detarq,tip_detarq,codSubcuenta;
	private Long cod_arqcaj;
	private String des_detarq,fec_detarq,fecha;
	private Float mon_detarq;
	private Boolean est_detarq;
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	public Integer getCodSubcuenta() {
		return codSubcuenta;
	}
	public void setCodSubcuenta(Integer codSubcuenta) {
		this.codSubcuenta = codSubcuenta;
	}
	public Long getCod_arqcaj() {
		return cod_arqcaj;
	}
	public void setCod_arqcaj(Long cod_arqcaj) {
		this.cod_arqcaj = cod_arqcaj;
	}
	public Integer getCod_detarq() {
		return cod_detarq;
	}
	public void setCod_detarq(Integer cod_detarq) {
		this.cod_detarq = cod_detarq;
	}
	public Integer getTip_detarq() {
		return tip_detarq;
	}
	public void setTip_detarq(Integer tip_detarq) {
		this.tip_detarq = tip_detarq;
	}
	public String getDes_detarq() {
		return des_detarq;
	}
	public void setDes_detarq(String des_detarq) {
		this.des_detarq = des_detarq;
	}
	public String getFec_detarq() {
		return fec_detarq;
	}
	public void setFec_detarq(String fec_detarq) {
		this.fec_detarq = fec_detarq;
	}
	public Float getMon_detarq() {
		return mon_detarq;
	}
	public void setMon_detarq(Float mon_detarq) {
		this.mon_detarq = mon_detarq;
	}
	public Boolean getEst_detarq() {
		return est_detarq;
	}
	public void setEst_detarq(Boolean est_detarq) {
		this.est_detarq = est_detarq;
	}
	public String getTipo() {
		if(tip_detarq!=null) {
			if(tip_detarq == 1)
				return "Egreso de caja";
			if(tip_detarq == 2)
				return "Pago ayudante x inscripcion";
			if(tip_detarq == 3)
				return "Pago ayudante x prestacion";
			if(tip_detarq == 4)
				return "Compra";
			if(tip_detarq == 5)
				return "Ingreso a caja";
			if(tip_detarq == 6)
				return "Ingreso de incripcion";
			if(tip_detarq == 7)
				return "Ingreso de prestacion";
			if(tip_detarq == 8)
				return "Venta";
			if(tip_detarq == 9)
				return "Caja General";
			if(tip_detarq == 10)
				return "Banco General";
			if(tip_detarq == 11)
				return "Muebles de oficina";
			if(tip_detarq == 12)
				return "Inmuebles o propiedades";
			if(tip_detarq == 13)
				return "Otros activos";
			if(tip_detarq == 14)
				return "Prestamos bancarios";
			if(tip_detarq == 15)
				return "Otros pasivos";
			if(tip_detarq == 16)
				return "Ingreso general";
			if(tip_detarq == 17)
				return "Egreso general";
			if(tip_detarq == 18)
				return "Descuento por venta";
			if(tip_detarq == 19)
				return "Descuento por compra";
			if(tip_detarq == 20)
				return "Venta con pago bancario";
			if(tip_detarq == 21)
				return "Devolucion por incumplimiento de prestacion";
			if(tip_detarq == 22)
				return "Pago prestacion de servicio con banco";
			if(tip_detarq == 23)
				return "Ingreso por multas cobradas";
			if(tip_detarq == 24)
				return "Devolucion con deposito bancario";
			return "";
		}else {
			return "";
		}
	}
}
