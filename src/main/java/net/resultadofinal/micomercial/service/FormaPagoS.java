package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.FormaPago;
import net.resultadofinal.micomercial.model.Mesa;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface FormaPagoS {
    DataTableResults<FormaPago> listado(HttpServletRequest request, boolean estado);
    List<FormaPago> listAll(int sucursalId);
    DataResponse adicionar(FormaPago obj);
    DataResponse modificar(FormaPago obj);
    DataResponse eliminar(Short id);
}
