package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.pagination.DataTableResults;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface GeneralS {
	General obtener(Integer gestion, Integer cod_suc);
	DataTableResults<General> listado(HttpServletRequest request, boolean estado);
	List<General> listAll();
	Boolean adicionar(General g);
	Boolean modificar(General g);
	Boolean darEstado(Integer gestion,Integer cod_suc,Boolean estado);
	Boolean validarGestion(Integer gestion);
	List<General> listarPorSucursal(Integer cod_suc);
	General existeGestionAnterior(int gestion, int sucursal);
}
