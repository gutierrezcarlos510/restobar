package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Mesa;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.MesaS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/mesa/*")
public class MesaC {
	
	@Autowired
	private MesaS mesaS;
	@RequestMapping("gestion")
	public String gestion(){
		return "mesa/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Mesa> listar(HttpServletRequest request, boolean estado) {
		try {
			return mesaS.listado(request, estado);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(HttpServletRequest request, Mesa obj){
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			obj.setSucursalId(gestion.getCod_suc());
			return mesaS.adicionar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(Mesa obj){
		try {
			return mesaS.modificar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request,Short id){
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return mesaS.darEstado(id,false, gestion.getCod_suc());
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(HttpServletRequest request,Short id){
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return mesaS.darEstado(id,true,gestion.getCod_suc());
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Short id){
		try {
			Mesa obj = mesaS.obtener(id);
			boolean exist = obj != null;
			return new DataResponse(exist, obj, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("listarPorSucursal")
	public @ResponseBody
	DataResponse listarPorSucursal(HttpServletRequest request){
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return new DataResponse(true, mesaS.listPorSucursal(gestion.getCod_suc()), "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("ordenar")
	public @ResponseBody
	DataResponse Ordenar(@RequestBody List<Short> codigos){
		try {
			return mesaS.ordenar(codigos);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("listarMesasLibresPorSucursal")
	public @ResponseBody
	DataResponse listarMesasLibresPorSucursal(HttpServletRequest request){
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return new DataResponse(true, mesaS.listarMesasLibresPorSucursal(gestion.getCod_suc()), "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}