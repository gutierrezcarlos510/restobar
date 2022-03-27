package net.resultadofinal.micomercial.model.wrap;

import java.util.List;

public class SubDetalleVentaWrap {
    private String xtipoProducto;
    private List<ProductoDetalleVenta> productos;

    public String getXtipoProducto() {
        return xtipoProducto;
    }

    public void setXtipoProducto(String xtipoProducto) {
        this.xtipoProducto = xtipoProducto;
    }

    public List<ProductoDetalleVenta> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoDetalleVenta> productos) {
        this.productos = productos;
    }
}
