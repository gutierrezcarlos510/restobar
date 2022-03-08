package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.CartillaDiaria;
import net.resultadofinal.micomercial.model.DetalleCartillaDiaria;
import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.CartillaDiariaS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/cartillaDiaria/*")
public class CartillaDiariaC {
	
	@Autowired
	private CartillaDiariaS cartillaDiariaS;
	@RequestMapping("gestion")
	public String gestion(){
		return "cartillaDiaria/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<CartillaDiaria> listar(HttpServletRequest request, boolean estado) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return cartillaDiariaS.listado(request, estado, gestion.getCod_suc());
		} catch (Exception ex) {
			System.out.println("error lista cartillaDiaria: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("adicionar")
	public String adicionar(){
		return "cartillaDiaria/adicionar";
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(HttpServletRequest request, CartillaDiaria obj){
		try {
			if(obj.getId() == null) {
				General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
				obj.setCodSuc(gestion.getCod_suc());
				return cartillaDiariaS.adicionar(obj);
			} else {
				return cartillaDiariaS.modificar(obj);
			}
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Integer id){
		try {
			return cartillaDiariaS.darEstado(id,false);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(Integer id){
		try {
			return cartillaDiariaS.darEstado(id,true);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Integer id){
		try {
			CartillaDiaria obj = cartillaDiariaS.obtener(id);
			boolean exist = obj != null;
			return new DataResponse(exist, obj, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtenerDetalles")
	public @ResponseBody
	DataResponse obtenerDetalles(Integer id){
		try {
			List<DetalleCartillaDiaria> obj = cartillaDiariaS.obtenerDetalles(id);
			boolean exist = obj != null;
			return new DataResponse(exist, obj, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}