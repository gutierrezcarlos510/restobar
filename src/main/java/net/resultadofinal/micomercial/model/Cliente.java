package net.resultadofinal.micomercial.model;

public class Cliente extends Persona{
	private Long cod_cli;
	private boolean est_cli;
	public Long getCod_cli() {
		return cod_cli;
	}
	public void setCod_cli(Long cod_cli) {
		this.cod_cli = cod_cli;
	}
	public boolean isEst_cli() {
		return est_cli;
	}
	public void setEst_cli(boolean est_cli) {
		this.est_cli = est_cli;
	}
}
