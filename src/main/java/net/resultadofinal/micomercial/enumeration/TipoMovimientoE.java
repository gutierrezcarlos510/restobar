package net.resultadofinal.micomercial.enumeration;

public enum TipoMovimientoE {
	TRASPASO_SUCURAL((short)1),
	REGISTRO_MOVIMIENTO((short)2),
	MODIFICACION_ALMACEN((short)3),
	MODIFICACION_CIERRE_CARTILLA((short)4);
	private final Short tipo;
	private TipoMovimientoE(Short tipo) {
		this.tipo = tipo;
	}
	public Short getTipo() {
		return tipo;
	}
}
