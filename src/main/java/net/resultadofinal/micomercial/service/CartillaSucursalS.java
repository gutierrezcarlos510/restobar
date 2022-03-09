package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.CartillaSucursal;
import net.resultadofinal.micomercial.model.DetalleCartillaSucursal;
import net.resultadofinal.micomercial.model.form.CartillaSucursalForm;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface CartillaSucursalS {
    DataTableResults<CartillaSucursal> listado(HttpServletRequest request, boolean estado, Integer sucursalId);

    CartillaSucursal obtener(Integer id);

    List<DetalleCartillaSucursal> obtenerDetalles(Integer id);

    DataResponse adicionar(CartillaSucursal obj,Integer tipos[], BigDecimal precios[]);

    DataResponse modificar(CartillaSucursal obj,Integer tipos[], BigDecimal precios[]);

    DataResponse darEstado(Integer id, Boolean estado);

    List<CartillaSucursalForm> listarPorSucursal(Integer sucursalId);
}
