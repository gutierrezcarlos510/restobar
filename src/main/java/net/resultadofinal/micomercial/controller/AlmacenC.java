package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Almacen;
import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.wrap.AlmacenVenta;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.AlmacenS;
import net.resultadofinal.micomercial.util.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/almacen/*")
public class AlmacenC {
	
	@Autowired
	private AlmacenS almacenS;
	@RequestMapping("gestion")
	public String gestion(){
		return "almacen/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Almacen> listar(HttpServletRequest request) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return almacenS.listado(request, gestion.getCod_suc());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	@RequestMapping("listarProducto")
	public @ResponseBody
	DataTableResults<AlmacenVenta> listarProducto(HttpServletRequest request) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return almacenS.listaProducto(request, gestion.getCod_suc());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}