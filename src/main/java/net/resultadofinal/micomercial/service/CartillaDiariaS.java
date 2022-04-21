package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.CartillaDiaria;
import net.resultadofinal.micomercial.model.DetalleCartillaDiaria;
import net.resultadofinal.micomercial.model.DetalleMovimiento;
import net.resultadofinal.micomercial.model.form.CartillaDiariaForm;
import net.resultadofinal.micomercial.model.wrap.CierreWrap;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CartillaDiariaS {
    DataTableResults<CartillaDiaria> listado(HttpServletRequest request, boolean estado, Integer sucursalId);

    CartillaDiaria obtener(Long id);

    List<DetalleCartillaDiaria> obtenerDetalles(Long id);

    DataResponse adicionar(CartillaDiariaForm obj);

    void adicionarDetalles(CartillaDiariaForm obj, List<DetalleMovimiento> detalleMovimientoList);

    DataResponse modificar(CartillaDiariaForm obj);

    DataResponse darEstado(Integer id, Boolean estado);

    Boolean eliminar(Long cartillaDiariaId,Long user, Integer sucursalId);

    CartillaDiariaForm obtenerCartillaDiariaForm (Long cartillaDiariaId);

    CartillaDiariaForm obtenerCartillaActivaSucursal(Integer sucursalId);

    DataResponse cerrarCartilla(Integer sucursalId, Long userId, CierreWrap obj);
    DataResponse obtenerResumenDetalleCierre(Long cartillaDiariaId, Integer sucursalId);
    DataResponse obtenerUltimaCartillaDiaria(Integer sucursalId);
    DataResponse duplicar(CartillaDiaria obj);
}
