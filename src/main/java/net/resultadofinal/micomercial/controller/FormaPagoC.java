package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.FormaPago;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.FormaPagoS;
import net.resultadofinal.micomercial.service.MesaS;
import net.resultadofinal.micomercial.service.SucursalS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.MyConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/formaPago/*")
public class FormaPagoC {
	
	@Autowired
	private FormaPagoS formaPagoS;
	@Autowired
	private SucursalS sucursalS;
	@RequestMapping("gestion")
	public String gestion(Model model){
		model.addAttribute("sucursales", sucursalS.listAll());
		return "formaPago/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<FormaPago> listar(HttpServletRequest request, boolean estado) {
		try {
			return formaPagoS.listado(request, estado);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(HttpServletRequest request, FormaPago obj){
		try {
			return formaPagoS.adicionar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(FormaPago obj){
		try {
			return formaPagoS.modificar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request,Short id){
		try {
			return formaPagoS.eliminar(id);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}