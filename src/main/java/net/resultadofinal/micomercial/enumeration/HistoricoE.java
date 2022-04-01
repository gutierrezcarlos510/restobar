package net.resultadofinal.micomercial.enumeration;

public enum HistoricoE {
    CREACION_PRODUCTO((short)0),
    COMPRA_PRODUCTO((short)1),
    REVERSION_COMPRA((short)2),
    VENTA_PRODUCTO((short)3),
    REVERSION_VENTA((short)4),
    INGRESO_CARTILLA_DIARIA((short)5),
    MODIFICACION_CARTILLA_DIARIA((short)6),
    REVERSION_CARTILLA_DIARIA((short)7),
    MODIFICACION_SUPERUSUARIO((short)8),
    MODIFICACION_CIERRE_CARTILLA((short)8);
    private final Short tipo;
    private HistoricoE(Short tipo) {
        this.tipo = tipo;
    }
    public Short getTipo() {
        return tipo;
    }
}
