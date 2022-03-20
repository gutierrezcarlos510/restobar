package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Almacen;
import net.resultadofinal.micomercial.model.wrap.AlmacenVenta;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AlmacenS {
    DataTableResults<Almacen> listado(HttpServletRequest request, int sucursal);
    @Transactional
    boolean registrarAlmacen(Long productoId, Integer sucursalId, Integer cantidad, Long userId, Short tipo, String obs);
    List<AlmacenVenta> listarProductos(Integer sucursalId);
    DataTableResults<AlmacenVenta> listaProducto(HttpServletRequest request, int sucursal);
}
