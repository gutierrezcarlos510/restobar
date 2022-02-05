package net.resultadofinal.micomercial.model;

public class Rol {
	private Integer cod_rol;
	private String nom_rol,des_rol;
	private Boolean est_rol;
	private Integer menus;

	public Integer getMenus() {
		return menus;
	}

	public void setMenus(Integer menus) {
		this.menus = menus;
	}

	private Menu menu;
	public Integer getCod_rol() {
		return cod_rol;
	}
	public void setCod_rol(Integer cod_rol) {
		this.cod_rol = cod_rol;
	}
	public String getNom_rol() {
		return nom_rol;
	}
	public void setNom_rol(String nom_rol) {
		this.nom_rol = nom_rol;
	}
	public String getDes_rol() {
		return des_rol;
	}
	public void setDes_rol(String des_rol) {
		this.des_rol = des_rol;
	}
	public Boolean getEst_rol() {
		return est_rol;
	}
	public void setEst_rol(Boolean est_rol) {
		this.est_rol = est_rol;
	}
	public Menu getMenu() {
		return menu;
	}
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
}
