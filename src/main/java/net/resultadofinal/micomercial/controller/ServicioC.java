package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Servicio;
import net.resultadofinal.micomercial.service.ServicioS;
import net.resultadofinal.micomercial.service.TipoServicioS;
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
@RequestMapping("/servicio/*")
public class ServicioC {
	
	@Autowired
	private ServicioS servicioS;
	@Autowired
	private TipoServicioS tiposervicioS;
	private static final String ENTITY = "servicio";
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		model.addAttribute("tipos",tiposervicioS.listar(-1, true, "", 0));
		return "servicio/gestion";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=servicioS.listar(start, estado, search,length);
		try {
			total=((Map<String, Object>) lista.get(0)).get("Tot").toString();			
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
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(HttpServletRequest request, Servicio s)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			int cod_ser=servicioS.adicionar(s);
			Map<String, Object> tipo=tiposervicioS.obtener(s.getCod_tipser());
			Data.put("cod_ser", cod_ser);
			Data.put("nom_ser", tipo.get("nom_tipser").toString()+" "+s.getNom_ser());
			Data.put("pre_ser", s.getPre_ser());
			Data.put("status", cod_ser>0);
			Data.put("msg", Utils.getSuccessFailedAdd(ENTITY, cod_ser>0));
		} catch (Exception e) {
			Data.put("status", false);
			Data.put("msg", e.getMessage());
		}
		return Data;		
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(HttpServletRequest request, Model model, Servicio m)throws IOException{
		try {
			boolean status =  servicioS.modificar(m);
			return new DataResponse(status, Utils.getSuccessFailedMod(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request, Model model, Integer cod_ser)throws IOException{
		try {
			boolean status =  servicioS.darEstado(cod_ser,false);
			return new DataResponse(status, Utils.getSuccessFailedEli(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(HttpServletRequest request, Model model, Integer cod_ser)throws IOException{
		try {
			boolean status =  servicioS.darEstado(cod_ser,true);
			return new DataResponse(status, Utils.getSuccessFailedEli(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validar(HttpServletRequest request,String nom_ser){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", servicioS.validarNom(nom_ser));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(HttpServletRequest request, Integer cod_ser){
		try {
			return new DataResponse(true, servicioS.obtener(cod_ser), Utils.successGet(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}