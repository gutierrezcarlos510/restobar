package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Cliente;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ClienteS {
	Cliente obtener(Long cod_per);
	Long adicionar(Cliente cli);
	Boolean modificar(Cliente cli);
	DataResponse darEstado(Long cod, Boolean est);
	DataTableResults<Cliente> listado(HttpServletRequest request, boolean estado);
	List<Cliente> listAll();
}
