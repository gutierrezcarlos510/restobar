package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Menu;
import net.resultadofinal.micomercial.model.Proceso;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.ProcesoS;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/proceso/*")
public class ProcesoC {
	
	@Autowired
	private ProcesoS procesoS;
	private static final String ENTITY = "proceso";
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "proceso/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Proceso> listar(HttpServletRequest request, boolean estado) {
		try {
			return procesoS.listado(request, estado);
		} catch (Exception ex) {
			System.out.println("error lista menus: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(HttpServletRequest request, Model model, Proceso p)throws IOException{
		try {
			boolean status = procesoS.adicionar(p);
			return new DataResponse(status, Utils.getSuccessFailedAdd(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(HttpServletRequest request, Model model, Proceso p, String ico_pro1)throws IOException{
		try {
			p.setIco_pro(ico_pro1);
			boolean status = procesoS.modificar(p);
			return new DataResponse(status, Utils.getSuccessFailedMod(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request, Model model, Integer cod_pro)throws IOException{
		try {
			boolean status = procesoS.darEstado(cod_pro,false);
			return new DataResponse(status, Utils.getSuccessFailedEli(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(HttpServletRequest request, Model model, Integer cod_pro)throws IOException{
		try {
			boolean status = procesoS.darEstado(cod_pro,true);
			return new DataResponse(status, Utils.getSuccessFailedAct(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("validar")
	public @ResponseBody
    DataResponse validar(HttpServletRequest request, String nom_pro){
		try {
			return new DataResponse(procesoS.validarNom(nom_pro), "Se realizo con exito la obtencion de validacion de proceso");
		} catch (Exception e) {
			return new DataResponse(false, "Error al validar nombre de proceso");
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(HttpServletRequest request, Integer cod_pro){
		try {
			return new DataResponse(true, procesoS.obtener(cod_pro),"Se realizo con exito la obtencion del proceso");
		} catch (Exception e) {
			return new DataResponse(false, "No se logro realizar la consulta");
		}
	}
}