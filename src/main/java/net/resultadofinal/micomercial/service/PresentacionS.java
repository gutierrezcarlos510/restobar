package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Presentacion;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PresentacionS {
    DataTableResults<Presentacion> listado(HttpServletRequest request, boolean estado, Integer tipo);

    Presentacion obtener(Integer id);

    DataResponse adicionar(Presentacion obj);

    DataResponse modificar(Presentacion obj);

    DataResponse darEstado(Integer id, Boolean estado);

    List<Presentacion> listarPorTipo(Short tipo);
}
