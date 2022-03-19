package net.resultadofinal.micomercial.pagination;

public class SqlParameter {
	public SqlParameter() {};
	public SqlParameter(String etiqueta, String value) {
		this.etiqueta = etiqueta;
		this.value = "'"+value+"'";
	}
	public SqlParameter(String etiqueta, Double value) {
		this.etiqueta = etiqueta;
		this.value = value.toString();
	}
	public SqlParameter(String etiqueta, Float value) {
		this.etiqueta = etiqueta;
		this.value = value.toString();
	}
	public SqlParameter(String etiqueta, Boolean value) {
		this.etiqueta = etiqueta;
		this.value = value.toString();
	}
	public SqlParameter(String etiqueta, Integer value) {
		this.etiqueta = etiqueta;
		this.value = value.toString();
	}
	public SqlParameter(String etiqueta, Long value) {
		this.etiqueta = etiqueta;
		this.value = value.toString();
	}
	public SqlParameter(String etiqueta, Short value) {
		this.etiqueta = etiqueta;
		this.value = value.toString();
	}
	private String etiqueta;
	private String value;
	public String getEtiqueta() {
		return etiqueta;
	}
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
