package net.resultadofinal.micomercial.model.wrap;

import java.math.BigDecimal;

public class ProductoDetalleVenta {
    private String xproducto;
    private BigDecimal cantidad;

    public String getXproducto() {
        return xproducto;
    }

    public void setXproducto(String xproducto) {
        this.xproducto = xproducto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
}
