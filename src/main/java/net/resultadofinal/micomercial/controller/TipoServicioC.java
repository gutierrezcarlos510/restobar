package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.TipoServicio;
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
@RequestMapping("/tiposervicio/*")
public class TipoServicioC {
	
	@Autowired
	private TipoServicioS tipoS;
	private static final String ENTITY = "tipo de servicio";
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request){
		return "tiposervicio/gestion";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=tipoS.listar(start, estado, search,length);
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
	public @ResponseBody
    DataResponse guardar(HttpServletRequest request, Model model, TipoServicio t)throws IOException{
		try {
			boolean status = tipoS.adicionar(t);
			return new DataResponse(status, Utils.getSuccessFailedAdd(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(HttpServletRequest request, Model model, TipoServicio t)throws IOException{
		try {
			boolean status = tipoS.modificar(t);
			return new DataResponse(status, Utils.getSuccessFailedMod(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request, Model model, Integer cod_tipser)throws IOException{
		try {
			boolean status = tipoS.darEstado(cod_tipser,false);
			return new DataResponse(status, Utils.getSuccessFailedEli(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(HttpServletRequest request, Model model, Integer cod_tipser)throws IOException{
		try {
			boolean status =  tipoS.darEstado(cod_tipser,true);
			return new DataResponse(status, Utils.getSuccessFailedAct(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("validar")
	public @ResponseBody
    DataResponse validar(HttpServletRequest request, String nom_tipmat){
		try {
			boolean status =  tipoS.validarNom(nom_tipmat);
			return new DataResponse(status, Utils.successGet("validacion de "+ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(HttpServletRequest request, Integer cod_tipser){
		try {
			return new DataResponse(true, tipoS.obtener(cod_tipser), Utils.successGet(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}