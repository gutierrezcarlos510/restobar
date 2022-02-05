package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.contable.CuentaContable;
import net.resultadofinal.micomercial.model.contable.SubcuentaContable;
import net.resultadofinal.micomercial.service.CuentaContableS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.UtilClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cuenta/*")
public class CuentaContableC {
	
	@Autowired
	private CuentaContableS cuentaS;
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "cuenta-contable/gestion";
	}
	@RequestMapping("vistaSubcuentas")
	public String vistaSubcuentas(HttpServletRequest request,Model model,Integer codCuenta){
		CuentaContable cuenta = cuentaS.obtener(codCuenta);
		model.addAttribute("cuenta", cuenta);
		return "cuenta-contable/gestion-subcuenta";
	}
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, int draw, int start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<CuentaContable> lista= cuentaS.listar(start, estado, search,length);
		try {
			total= UtilClass.isNotNullEmpty(lista)?lista.get(0).getTot().toString():"0";
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		if(!search.equals(""))
			Data.put("recordsFiltered", lista.size());
		else
			Data.put("recordsFiltered", total);
		return Data;
	}
	@RequestMapping("guardarSubcuenta")
	public @ResponseBody
    DataResponse guardarSubcuenta(HttpServletRequest request, Model model, SubcuentaContable subcuenta)throws IOException{
		try {
			return cuentaS.adicionarSubcuenta(subcuenta);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizarSubcuenta")
	public @ResponseBody
    DataResponse actualizarSubcuenta(HttpServletRequest request, Model model, SubcuentaContable subcuenta)throws IOException{
		try {
			return cuentaS.modificarSubcuenta(subcuenta);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminarSubcuenta")
	public @ResponseBody
    DataResponse eliminarSubcuenta(HttpServletRequest request, Model model, Integer codSubcuenta)throws IOException{
		try {
			return cuentaS.eliminar(codSubcuenta);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("existeCodigoSubcuenta")
	public @ResponseBody
    DataResponse existeCodigoSubcuenta(HttpServletRequest request, String codigo){
		try {
			boolean res = cuentaS.existeCodigo(codigo);
			return new DataResponse(res, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, "error al consultar"+e.getMessage());
		}
	}
	@RequestMapping("obtenerSubcuenta")
	public @ResponseBody
    DataResponse obtenerSubcuenta(HttpServletRequest request, Integer codSubcuenta){
		try {
			SubcuentaContable subcuenta = cuentaS.obtenerSubcuenta(codSubcuenta);
			return new DataResponse(true, subcuenta, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, "Error al consultar obtener subcuenta:"+e.toString());
		}
	}
	@RequestMapping("listaSubcuentas")
	public @ResponseBody Map<?, ?> listaSubcuentas(HttpServletRequest request, int draw, int start, boolean estado,int length,Integer codCuenta)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<SubcuentaContable> lista= cuentaS.listarSubcuentas(start, estado, search,length,codCuenta);
		try {
			total= UtilClass.isNotNullEmpty(lista)?lista.get(0).getTot().toString():"0";
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		if(!search.equals(""))
			Data.put("recordsFiltered", lista.size());
		else
			Data.put("recordsFiltered", total);
		return Data;
	}
}