package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.CartillaDiaria;
import net.resultadofinal.micomercial.model.DetalleCartillaDiaria;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

public interface CartillaDiariaS {
    DataTableResults<CartillaDiaria> listado(HttpServletRequest request, boolean estado, Integer sucursalId);

    CartillaDiaria obtener(Integer id);

    List<DetalleCartillaDiaria> obtenerDetalles(Integer id);

    DataResponse adicionar(CartillaDiaria obj);

    void adicionarDetalles(Long cartillaDiariaId, List<DetalleCartillaDiaria> detalles);

    DataResponse modificar(CartillaDiaria obj);

    DataResponse darEstado(Integer id, Boolean estado);
}
