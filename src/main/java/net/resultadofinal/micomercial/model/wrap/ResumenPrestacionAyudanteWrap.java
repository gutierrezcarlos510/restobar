package net.resultadofinal.micomercial.model.wrap;

import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.TableRow;

public class ResumenPrestacionAyudanteWrap extends TableRow {
	private Long codAyu,tpendientes,tcumplidos,tabandonados,tincumplidos,
	tpagadoPorCumplido,tpagadoPorAbandono,tmultadoPorIncumplido,tperdonadoPorIncumplido;
	private String avatar,xayudante;
	public String getXayudante() {
		return xayudante;
	}

	public void setXayudante(String xayudante) {
		this.xayudante = xayudante;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String urlAvatar) {
		this.avatar = urlAvatar;
	}

	public Long getCodAyu() {
		return codAyu;
	}

	public void setCodAyu(Long codAyu) {
		this.codAyu = codAyu;
	}

	public Long getTpendientes() {
		return tpendientes;
	}

	public void setTpendientes(Long tpendientes) {
		this.tpendientes = tpendientes;
	}

	public Long getTcumplidos() {
		return tcumplidos;
	}

	public void setTcumplidos(Long tcumplidos) {
		this.tcumplidos = tcumplidos;
	}

	public Long getTabandonados() {
		return tabandonados;
	}

	public void setTabandonados(Long tabandonados) {
		this.tabandonados = tabandonados;
	}

	public Long getTincumplidos() {
		return tincumplidos;
	}

	public void setTincumplidos(Long tincumplidos) {
		this.tincumplidos = tincumplidos;
	}

	public Long getTpagadoPorCumplido() {
		return tpagadoPorCumplido;
	}

	public void setTpagadoPorCumplido(Long tpagadoPorCumplido) {
		this.tpagadoPorCumplido = tpagadoPorCumplido;
	}

	public Long getTpagadoPorAbandono() {
		return tpagadoPorAbandono;
	}

	public void setTpagadoPorAbandono(Long tpagadoPorAbandono) {
		this.tpagadoPorAbandono = tpagadoPorAbandono;
	}

	public Long getTmultadoPorIncumplido() {
		return tmultadoPorIncumplido;
	}

	public void setTmultadoPorIncumplido(Long tmultadoPorIncumplido) {
		this.tmultadoPorIncumplido = tmultadoPorIncumplido;
	}

	public Long getTperdonadoPorIncumplido() {
		return tperdonadoPorIncumplido;
	}

	public void setTperdonadoPorIncumplido(Long tperdonadoPorIncumplido) {
		this.tperdonadoPorIncumplido = tperdonadoPorIncumplido;
	}
	public Long getTotalCumplidos() {
		if(this.tcumplidos!=null && this.tpagadoPorCumplido!=null)
			return this.tcumplidos+this.tpagadoPorCumplido;
		else
			return -1L;
	}
	public Long getTotalIncumplidos() {
		if(this.tincumplidos!=null && this.tmultadoPorIncumplido!=null)
			return this.tincumplidos+this.tmultadoPorIncumplido+this.tperdonadoPorIncumplido;
		else
			return -1L;
	}
	public Long getTotalAbandonados() {
		if(this.tabandonados!=null && this.tpagadoPorAbandono!=null)
			return this.tabandonados + this.tpagadoPorAbandono;
		else
			return -1L;
	}
	public Long getTotalPorCobrar() {
		if(this.tcumplidos!=null && this.tabandonados!=null)
			return this.tcumplidos + tabandonados;
		else
			return -1L;
	}
	public String getUrlAvatar() {
		return MyConstant.URL_PATH_AVATAR + this.avatar;
	}
	public String getNombres() {
		if(this.xayudante != null) {
			String data[] = this.xayudante.trim().split(" ");
			if(data!=null && data.length>0) {
				int res = data.length /2;
				String cad = "";
				for (int i = 0; i < res; i++) {
					cad += data[i]+" "; 
				}
				return cad.trim();
			}
		}
		return "";
	}
	public String getApellidos() {
		if(this.xayudante != null) {
			String data[] = this.xayudante.trim().split(" ");
			if(data!=null && data.length>0) {
				int res = data.length /2;
				String cad = "";
				for (int i = res; i < data.length; i++) {
					cad += data[i]+" "; 
				}
				return cad.trim();
			}
		}
		return "";
	}
}
