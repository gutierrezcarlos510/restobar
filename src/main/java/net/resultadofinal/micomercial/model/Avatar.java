package net.resultadofinal.micomercial.model;

import net.resultadofinal.micomercial.util.MyConstant;

public class Avatar {
	private int code;
	private String nombre;
	private String urlImg;
	public Avatar() {}
	public Avatar(int code,String nombre,String urlImg) {
		this.code = code;
		this.nombre = nombre;
		this.urlImg = urlImg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUrlImg() {
		return urlImg;
	}
	public void setUrlImg(String urlImg) {
		this.urlImg = urlImg;
	}
	public String getUrlCompleta() {
		return MyConstant.URL_PATH_AVATAR + urlImg;
	}
}
