package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Producto;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;

public interface ProductoS {
	DataTableResults<Producto> listado(HttpServletRequest request, boolean estado, Integer clase);
	Producto obtener(Long id);
	Long generarCodigo();
	DataResponse adicionar(Producto p);
	DataResponse modificar(Producto p);
	DataResponse darEstado(Long id,boolean est);
}
