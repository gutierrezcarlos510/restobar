package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.CartillaSucursal;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CartillaSucursalS {
    DataTableResults<CartillaSucursal> listado(HttpServletRequest request, boolean estado, Integer sucursalId);

    CartillaSucursal obtener(Integer id);

    DataResponse adicionar(CartillaSucursal obj);

    DataResponse modificar(CartillaSucursal obj);

    DataResponse darEstado(Integer id, Boolean estado);

    List<CartillaSucursal> listarPorSucursal(Integer sucursalId);
}
