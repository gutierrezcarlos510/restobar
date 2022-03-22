package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.GeneralWrap;
import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.util.DataResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface UsuarioS {
	List<Persona> listarUsuariosSistema();
	List<Persona> listarUsuariosPorRol(Integer rolId);
	List<GeneralWrap> obtenerSucursales(Long cod_per);
	void eliminarSucursales(Long cod_per);
	void adicionarSucursales(Long cod_per, List<Integer> sucursales);
	Persona iniciarSesion(String login, String password);
	Boolean guardarFoto(String foto,Long codper);
	DataTableResults<Persona> listar(HttpServletRequest request);
	List<Map<String, Object>> obtenerRoles(Long cod_per);
	DataResponse adicionar(Persona us, Integer roles[], Integer sucursales[]);
	Persona obtener(Long cod_per);
	Boolean modificar(Persona us);
	Boolean eliminar(Long cod);
	Boolean activar(Long cod);
	Boolean validarCi(String ci);
	Map<String, Object> buscarCi(String ci);
	Map<String, Object> buscar_nombres(String cad);
	Persona buscarPorTelefono(String celular);
}
