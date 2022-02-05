package net.resultadofinal.micomercial.enumeration;

public enum TipoArqueoE {
	EGRESO_CAJA(1),
	PAGO_AYUDANTE_INSCRIPCION(2),
	VENCIDO(3),
	PERDIDA(4),
	USO_EN_FARMACIA(5),
	DISMINUCION_ALMACEN_POR_USUARIO(6),
	AUMENTO_ALMACEN_POR_USUARIO(7),
	ENTRADA(8),
	SALIDA(9),
	TRASPASO_ENTRE_FARMACIA_INGRESO(10);
	private final Integer tipo;
	private TipoArqueoE(Integer tipo) {
		this.tipo = tipo;
	}
	public Integer getTipo() {
		return tipo;
	}
}
