package net.resultadofinal.micomercial.model.wrap;

import java.math.BigDecimal;
import java.util.List;

public class DetalleVentaWrap {
    private String xproducto,xtipoVenta;
    private BigDecimal cantidad;
    private BigDecimal precio, total;
    private List<SubDetalleVentaWrap> subdetallesCompuestos;

    public String getXtipoVenta() {
        return xtipoVenta;
    }

    public void setXtipoVenta(String xtipoVenta) {
        this.xtipoVenta = xtipoVenta;
    }

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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<SubDetalleVentaWrap> getSubdetallesCompuestos() {
        return subdetallesCompuestos;
    }

    public void setSubdetallesCompuestos(List<SubDetalleVentaWrap> subdetallesCompuestos) {
        this.subdetallesCompuestos = subdetallesCompuestos;
    }
}
