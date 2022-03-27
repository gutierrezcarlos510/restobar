package net.resultadofinal.micomercial.enumeration;

public enum TipoArqueoE {
	INGRESO_VENTA((short)1),
	EGRESO_REVERSION_VENTA((short)2),
	INGRESO_GENERAL((short)3);
	private final Short tipo;
	private TipoArqueoE(Short tipo) {
		this.tipo = tipo;
	}
	public Short getTipo() {
		return tipo;
	}
}
