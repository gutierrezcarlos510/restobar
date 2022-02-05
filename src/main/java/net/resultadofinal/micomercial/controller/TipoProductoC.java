package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Sucursal;
import net.resultadofinal.micomercial.model.TipoProducto;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.TipoProductoS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/tipoproducto/*")
public class TipoProductoC {
	
	@Autowired
	private TipoProductoS tipoS;
	private static final String ENTITY = "tipo de producto";
	@RequestMapping("gestion")
	public String gestion(){
		return "tipoproducto/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<TipoProducto> listar(HttpServletRequest request, boolean estado) {
		try {
			return tipoS.listado(request, estado);
		} catch (Exception ex) {
			System.out.println("error lista sucursales: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(TipoProducto obj){
		try {
			return tipoS.adicionar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(TipoProducto obj){
		try {
			return tipoS.modificar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Integer cod_tippro){
		try {
			return tipoS.darEstado(cod_tippro,false);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(HttpServletRequest request, Model model, Integer cod_tippro){
		try {
			return tipoS.darEstado(cod_tippro,true);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Integer cod_tippro){
		try {
			TipoProducto obj = tipoS.obtener(cod_tippro);
			boolean exist = obj != null;
			return new DataResponse(exist, obj, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}