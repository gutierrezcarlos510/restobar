package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.CartillaDiaria;
import net.resultadofinal.micomercial.model.DetalleCartillaDiaria;
import net.resultadofinal.micomercial.model.form.CartillaDiariaForm;
import net.resultadofinal.micomercial.model.form.CartillaSucursalForm;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface CartillaDiariaS {
    DataTableResults<CartillaDiaria> listado(HttpServletRequest request, boolean estado, Integer sucursalId);

    CartillaDiaria obtener(Integer id);

    List<DetalleCartillaDiaria> obtenerDetalles(Integer id);

    DataResponse adicionar(CartillaDiariaForm obj);

    void adicionarDetalles(CartillaDiariaForm obj);

    DataResponse modificar(CartillaDiariaForm obj);

    DataResponse darEstado(Integer id, Boolean estado);

    Boolean eliminar(Long cod_com,Long user, Integer sucursalId);

    CartillaDiariaForm obtenerCartillaDiariaForm (Long cartillaDiariaId);

    CartillaDiariaForm obtenerCartillaActivaSucursal(Integer sucursalId);
}
