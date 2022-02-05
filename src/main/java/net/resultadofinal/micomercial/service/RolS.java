package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Rol;
import net.resultadofinal.micomercial.pagination.DataTableResults;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface RolS {
	List<Rol> listarPorUsuario(Long codPer);
	List<Rol> listAll();
	DataTableResults<Rol> listado(HttpServletRequest request, boolean estado);
	List<Rol> listar();
	Rol obtener(Integer codRol);
	Boolean adicionar(Rol r);
	Boolean modificar(Rol r);
	Boolean darEstado(Integer codRol,Boolean est);
	Boolean adicionarRolMenu(Integer codRol,Integer obtenidos[]);
	Boolean validarNom(String nom);
}
