package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.CartillaSucursal;
import net.resultadofinal.micomercial.model.DetalleCartillaSucursal;
import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.form.CartillaSucursalForm;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.CartillaSucursalS;
import net.resultadofinal.micomercial.service.TipoProductoS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cartillaSucursal/*")
public class CartillaSucursalC {
	
	@Autowired
	private CartillaSucursalS cartillaSucursalS;
	@Autowired
	private TipoProductoS tipoProductoS;
	@RequestMapping("gestion")
	public String gestion(Model model){
		model.addAttribute("tipos", tipoProductoS.listAll(0));
		return "cartillaSucursal/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<CartillaSucursal> listar(HttpServletRequest request, boolean estado) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return cartillaSucursalS.listado(request, estado, gestion.getCod_suc());
		} catch (Exception ex) {
			System.out.println("error lista cartillaSucursales: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(HttpServletRequest request, CartillaSucursal obj, Integer tipos[], BigDecimal precios[]){
		try {
			if(obj.getId() == null) {
				General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
				obj.setCodSuc(gestion.getCod_suc());
				return cartillaSucursalS.adicionar(obj,tipos,precios);
			} else {
				return cartillaSucursalS.modificar(obj, tipos, precios);
			}
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Integer id){
		try {
			return cartillaSucursalS.darEstado(id,false);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(Integer id){
		try {
			return cartillaSucursalS.darEstado(id,true);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Integer id){
		try {
			CartillaSucursal obj = cartillaSucursalS.obtener(id);
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
			List<DetalleCartillaSucursal> obj = cartillaSucursalS.obtenerDetalles(id);
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
			List<CartillaSucursalForm> obj = cartillaSucursalS.listarPorSucursal(gestion.getCod_suc());
			boolean exist = obj != null;
			return new DataResponse(exist, obj, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}