package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Menu;
import net.resultadofinal.micomercial.pagination.DataTableResults;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface MenuS {
	List<Menu> listarPorRol(Integer cod_rol);
	Menu obtener(Integer cod_men);
	Boolean adicionar(Menu m);
	Boolean modificar(Menu m);
	Boolean darEstado(Integer cod_men,Boolean est);
	Boolean adicionarMenuProceso(Integer codm,Integer obtenidos[]);
	Boolean validarNom(String nom);
	List<Menu> obtenerMenusPorCodrol(Integer cod_rol);
	DataTableResults<Menu> listado(HttpServletRequest request, boolean estado);
	List<Menu> listAll();
}
