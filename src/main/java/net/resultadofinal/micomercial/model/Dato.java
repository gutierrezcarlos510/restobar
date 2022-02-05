package net.resultadofinal.micomercial.model;

public class Dato {
	private Long cod_per;
	private String log_dat,cla_dat,fecha_alta,fecha_baja;
	public Long getCod_per() {
		return cod_per;
	}
	public void setCod_per(Long cod_per) {
		this.cod_per = cod_per;
	}
	public String getLog_dat() {
		return log_dat;
	}
	public void setLog_dat(String log_dat) {
		this.log_dat = log_dat;
	}
	public String getCla_dat() {
		return cla_dat;
	}
	public void setCla_dat(String cla_dat) {
		this.cla_dat = cla_dat;
	}
	public String getFecha_alta() {
		return fecha_alta;
	}
	public void setFecha_alta(String fecha_alta) {
		this.fecha_alta = fecha_alta;
	}
	public String getFecha_baja() {
		return fecha_baja;
	}
	public void setFecha_baja(String fecha_baja) {
		this.fecha_baja = fecha_baja;
	}
}
