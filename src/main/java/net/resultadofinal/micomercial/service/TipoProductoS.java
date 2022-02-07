package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.TipoProducto;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface TipoProductoS {

	DataTableResults<TipoProducto> listado(HttpServletRequest request, boolean estado);
	List<TipoProducto> listAll(Integer tipo);
	TipoProducto obtener(Integer cod_tippro);
	DataResponse adicionar(TipoProducto t);
	DataResponse modificar(TipoProducto t);
	DataResponse darEstado(Integer cod_tippro, boolean est);
}
