package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Empresa;
import net.resultadofinal.micomercial.pagination.DataTableResults;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface EmpresaS {
	Map<String, Object> obtener(Integer cod_emp);
	Boolean adicionar(Empresa em);
	Boolean modificar(Empresa em);
	Boolean darEstado(Integer cod,Boolean est);
	Boolean validarNom(String nom);
	DataTableResults<Empresa> listado(HttpServletRequest request, boolean estado);
	List<Empresa> listAll();
}
