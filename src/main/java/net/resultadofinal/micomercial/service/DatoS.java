package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Dato;
import net.resultadofinal.micomercial.model.Persona;

import java.util.Map;

public interface DatoS {
	Map<String, Object> obtenerDato(Long cod);
	Dato obtener(Long cod);
	boolean adicionarDatos(Dato d, Integer obtenidos[]);
	boolean guardarBiometrico(Persona p);
	boolean eliminar(Long cod);
	boolean adicionar(Long cod,String log,String cla);
	boolean validarBiometrico(String biometrico);
	boolean existeLogin(String login);
	boolean cambiarDatos(Long cod, String log, String cla);
}
