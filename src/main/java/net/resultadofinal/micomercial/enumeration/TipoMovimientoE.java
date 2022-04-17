package net.resultadofinal.micomercial.enumeration;

public enum TipoMovimientoE {
	TRASPASO_SUCURAL((short)1),
	REGISTRO_MOVIMIENTO((short)2),
	MODIFICACION_ALMACEN((short)3),
	MODIFICACION_CIERRE_CARTILLA((short)4),
	REGISTRO_CARTILLA_DIARIA((short)5),
	MODIFICACION_CARTILLA_DIARIA((short)6);
	private final Short tipo;
	private TipoMovimientoE(Short tipo) {
		this.tipo = tipo;
	}
	public Short getTipo() {
		return tipo;
	}
}
