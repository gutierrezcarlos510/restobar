package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Mesa;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface MesaS {
    DataTableResults<Mesa> listado(HttpServletRequest request, boolean estado);

    List<Mesa> listPorSucursal(Integer sucursalId);

    Mesa obtener(Short id);

    @Transactional
    DataResponse adicionar(Mesa obj);

    @Transactional
    DataResponse modificar(Mesa obj);

    DataResponse darEstado(Short id, boolean est, Integer sucursalId);
    DataResponse ordenar(List<Short> codigos);
    List<Mesa> listarMesasLibresPorSucursal(Integer sucursalId);
}
