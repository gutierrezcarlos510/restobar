package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Rol;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.MenuS;
import net.resultadofinal.micomercial.service.RolS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.Utils;
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
@RequestMapping("/rol/*")
public class RolC {
	
	@Autowired
	private RolS rolS;
	@Autowired
	private MenuS menuS;
	private static final String ENTITY = "rol";
	
	@RequestMapping("gestion")
	public String gestion(){
		return "rol/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Rol> listar(HttpServletRequest request, boolean estado) {
		try {
			return rolS.listado(request, estado);
		} catch (Exception ex) {
			System.out.println("error lista empresas: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(Rol r)throws IOException{
		try {
			boolean status =  rolS.adicionar(r);
			return new DataResponse(status, Utils.getSuccessFailedAdd(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(HttpServletRequest request, Model model, Rol r)throws IOException{
		try {
			boolean status =  rolS.modificar(r);
			return new DataResponse(status, Utils.getSuccessFailedMod(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}	
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Integer cod_rol)throws IOException{
		try {
			boolean status =  rolS.darEstado(cod_rol,false);
			return new DataResponse(status, Utils.getSuccessFailedEli(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(Integer cod_rol)throws IOException{
		try {
			boolean status =  rolS.darEstado(cod_rol,true);
			return new DataResponse(status, Utils.getSuccessFailedAct(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(String nom_rol){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", rolS.validarNom(nom_rol));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(Integer cod_rol){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Data.put("data", rolS.obtener(cod_rol));
			Data.put("menus",menuS.obtenerMenusPorCodrol(cod_rol));
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			Data.put("msg", Utils.errorGet(ENTITY, ""));
		}
		return Data;
	}
	@RequestMapping("guardarAsignacion")
	public @ResponseBody
    DataResponse guardarAsignacion(Integer cod_rol, Integer menus[]){
		try {
			boolean status =  rolS.adicionarRolMenu(cod_rol, menus);
			return new DataResponse(status, Utils.getSuccessFailedAdd("asignacion de "+ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}