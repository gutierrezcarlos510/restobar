package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.model.Proveedor;
import net.resultadofinal.micomercial.pagination.DataTableResults;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ProveedorS {
	Proveedor obtener(Long cod_per);
	Long adicionar(Proveedor pro);
	Boolean modificar(Proveedor pro);
	Boolean darEstado(Long cod,Boolean est);
	DataTableResults<Proveedor> listado(HttpServletRequest request, boolean estado);
	List<Proveedor> listAll();
}
