package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Menu;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.MenuS;
import net.resultadofinal.micomercial.service.ProcesoS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/menu/*")
public class MenuC {
	
	@Autowired
	private MenuS menuS;
	@Autowired
	private ProcesoS procesoS;
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "menu/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Menu> listar(HttpServletRequest request, boolean estado) {
		try {
			return menuS.listado(request, estado);
		} catch (Exception ex) {
			System.out.println("error lista menus: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request, Model model, Menu m)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", menuS.adicionar(m));
		return Data;		
	}
	@RequestMapping("actualizar")
	public @ResponseBody Map<String, Object> actualizar(HttpServletRequest request, Model model, Menu m, String ico_men1)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		m.setIco_men(ico_men1);
		Data.put("status", menuS.modificar(m));
		return Data;		
	}
	@RequestMapping("eliminar")
	public @ResponseBody Map<String, Object> eliminar(HttpServletRequest request,Model model,Integer cod_men)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", menuS.darEstado(cod_men,false));
		return Data;
	}
	@RequestMapping("activar")
	public @ResponseBody Map<String, Object> activar(HttpServletRequest request,Model model,Integer cod_men)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", menuS.darEstado(cod_men,true));
		return Data;
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(HttpServletRequest request,String nom_men){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", menuS.validarNom(nom_men));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,Integer cod_men){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", menuS.obtener(cod_men));
			Data.put("procesos",procesoS.obtenerProcesos(cod_men));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			System.out.println("error al obtener="+e.toString());
		}
		return Data;
	}
	@RequestMapping("guardarAsignacion")
	public @ResponseBody Map<String, Object> guardarAsignacion(HttpServletRequest request,Integer cod_men,Integer procesos[]){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", menuS.adicionarMenuProceso(cod_men, procesos));
		return Data;
	}
}