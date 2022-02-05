package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Presentacion;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.PresentacionS;
import net.resultadofinal.micomercial.util.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/presentacion/*")
public class PresentacionC {
	
	@Autowired
	private PresentacionS presentacionS;
	@RequestMapping("gestion")
	public String gestion(){
		return "presentacion/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Presentacion> listar(HttpServletRequest request, boolean estado, Integer grupo) {
		try {
			return presentacionS.listado(request, estado, grupo);
		} catch (Exception ex) {
			System.out.println("error lista presentaciones: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(Presentacion obj){
		try {
			return presentacionS.adicionar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(Presentacion obj){
		try {
			return presentacionS.modificar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Integer id){
		try {
			return presentacionS.darEstado(id,false);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(Integer id){
		try {
			return presentacionS.darEstado(id,true);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Integer id){
		try {
			Presentacion obj = presentacionS.obtener(id);
			boolean exist = obj != null;
			return new DataResponse(exist, obj, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}