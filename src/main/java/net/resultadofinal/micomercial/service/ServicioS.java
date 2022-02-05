package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Servicio;

import java.util.List;
import java.util.Map;

public interface ServicioS {
	List<Map<String, Object>> listar(int start,boolean estado,String search,int length);
	Map<String, Object> obtener(Integer cod);
	Integer adicionar(Servicio s);
	Boolean modificar(Servicio s);
	Boolean darEstado(Integer cod,Boolean est);
	Boolean validarNom(String nom);
}
