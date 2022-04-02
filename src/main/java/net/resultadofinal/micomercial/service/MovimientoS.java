package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.DetalleMovimiento;
import net.resultadofinal.micomercial.model.Movimiento;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MovimientoS {
    DataTableResults<Movimiento> listado(HttpServletRequest request, boolean estado, Integer sucursalId);

    Movimiento obtener(Long id);

    List<DetalleMovimiento> obtenerDetalles(Long id);

    DataResponse adicionar(Movimiento obj);

    void adicionarDetalles(Movimiento obj);

    DataResponse revisar(Movimiento obj);

    DataResponse eliminar(Movimiento obj);
}
