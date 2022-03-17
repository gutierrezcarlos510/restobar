package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Almacen;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

public interface AlmacenS {
    public DataTableResults<Almacen> listado(HttpServletRequest request, int sucursal);
    @Transactional
    boolean registrarAlmacen(Long productoId, Integer sucursalId, Integer cantidad, Long userId, Short tipo, String obs);
}
