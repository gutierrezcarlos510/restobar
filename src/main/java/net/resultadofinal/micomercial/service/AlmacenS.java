package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Almacen;
import net.resultadofinal.micomercial.model.wrap.AlmacenVenta;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface AlmacenS {
    DataTableResults<Almacen> listado(HttpServletRequest request, int sucursal);

    /**
     * Registro de producto en almacen
     * @param productoId
     * @param sucursalId
     * @param cantidad
     * @param userId
     * @param tipo Significado 0 = creacion de producto en almacen, 1 = Compra de producto, 2= reversion de compra, 3= venta de producto, 4= reversion de venta, 5 = Ingreso por cartilla diaria, 6 = modificacion por cartilla diaria, solo alimentos preparados, 7= Reversion de cartilla diaria. 8 = modificacion manual por superusuario, la cantidad de almacen. 9=modificacion de productos preparados, desde cierre de cartilla
     * @param obs Observacion en el historico
     * @return
     */
    @Transactional
    boolean registrarAlmacen(Long productoId, Integer sucursalId, Integer cantidad, Long userId, Short tipo, String obs);
    List<AlmacenVenta> listarProductos(Integer sucursalId);
    DataTableResults<AlmacenVenta> listaProducto(HttpServletRequest request, int sucursal);
}
