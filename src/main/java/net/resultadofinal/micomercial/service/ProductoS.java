package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Ingrediente;
import net.resultadofinal.micomercial.model.Producto;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductoS {
	DataTableResults<Producto> listado(HttpServletRequest request, boolean estado, Integer clase, Integer sucursal);
	DataTableResults<Producto> listaPorTipo(HttpServletRequest request, boolean estado, Integer tipo,Integer sucursal);
	DataTableResults<Producto> listaPorTipoGrupoAlmacen(HttpServletRequest request, boolean estado, Integer tipo, Integer sucursal);
	Producto obtener(Long id);
	Long generarCodigo();
	DataResponse adicionar(Producto p,Integer sucursalId);
	DataResponse modificar(Producto p,Integer sucursalId);
	DataResponse darEstado(Long id,boolean est);
	DataResponse adicionarIngredientes(Long producto, Long ingredientes[], Integer cantidades[],Integer cantidadPlatos);
	DataResponse modificarIngredientes(Long producto, Long ingredientes[], Integer cantidades[],Integer cantidadPlatos);
	DataResponse eliminarIngrediente(Long productoId, Short id);
	List<Ingrediente> obtenerIngredientesPorProducto(Long productoId);
}
