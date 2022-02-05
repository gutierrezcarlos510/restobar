package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Producto;

import java.util.List;
import java.util.Map;

public interface ProductoS {
	List<Map<String, Object>> listar(int start,boolean estado,String search,int length);
	Map<String, Object> obtener(Integer cod_pro);
	Integer adicionar(Producto p);
	Boolean modificar(Producto p);
	Boolean darEstado(Integer cod_pro,Boolean est);
	Boolean validarNom(String nom);
	Map<String, Object> obtenerxcodigo(String barcode);
	Boolean asignar(Integer cod_pro,String codigos[]);
	void reducirAlmacen(Integer codpro, int cantidad);
	void registrarProductoPerdido(Integer productos[], Integer cantidades[], Long codAsiento);
}
