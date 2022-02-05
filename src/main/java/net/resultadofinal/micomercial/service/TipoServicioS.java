package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.TipoServicio;

import java.util.List;
import java.util.Map;

public interface TipoServicioS {
	List<Map<String, Object>> listar(int start,boolean estado,String search,int length);
	Map<String, Object> obtener(Integer cod_tipmat);
	Boolean adicionar(TipoServicio t);
	Boolean modificar(TipoServicio t);
	Boolean darEstado(Integer cod_tipser,Boolean est);
	Boolean validarNom(String nom);
	List<TipoServicio> listarAgrupadoConServicios();
}
