package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Dato;
import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.model.Sucursal;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.DatoS;
import net.resultadofinal.micomercial.service.GeneralS;
import net.resultadofinal.micomercial.service.UsuarioS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.GeneradorReportes;
import net.resultadofinal.micomercial.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/dato/*")
public class DatoC {
	@Autowired
    private GeneralS generalS;
	@Autowired
    private UsuarioS usuarioS;
	@Autowired
    private DatoS datoS;
	@Autowired
	private DataSource datasource;
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "dato/gestion";
	}

	@RequestMapping("lista")
	public @ResponseBody
	DataTableResults<Persona> lista(HttpServletRequest request) throws IOException, SQLException {
		return usuarioS.listar(request);
	}
	@RequestMapping("guardarBiometrico")
	public @ResponseBody DataResponse guardarAsignacion(HttpServletRequest request, Persona p){
		try {
			boolean status = datoS.guardarBiometrico(p);
			return new DataResponse(status, (status ? "Transaccion exitosa" : "Error de transaccion."));
		} catch (Exception ex) {
			return new DataResponse(false, ex.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody DataResponse eliminar(HttpServletRequest request,Model model,Long cod_per)throws IOException{
		try {
			boolean status = datoS.eliminar(cod_per);
			return new DataResponse(status, (status ? "Transaccion exitosa" : "Error de transaccion."));
		} catch (Exception ex) {
			return new DataResponse(false, ex.getMessage());
		}
	}
	@RequestMapping("generar")
	public @ResponseBody DataResponse generar(Dato obj)throws IOException{
		try {
			boolean status = datoS.adicionar(obj.getCod_per(), obj.getLog_dat(), null);
			return new DataResponse(status, (status ? "Transaccion exitosa" : "Error de transaccion."));
		} catch (Exception ex) {
			return new DataResponse(false, ex.getMessage());
		}
	}
	@RequestMapping("validarBiometrico")
	public @ResponseBody Map<String, Object> validarBiometrico(HttpServletRequest request,String codbio_per){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", datoS.validarBiometrico(codbio_per));
		return Data;
	}
	@RequestMapping("validarLogin")
	public @ResponseBody Map<String, Object> validarLogin(HttpServletRequest request,String log_dat){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", datoS.existeLogin(log_dat));
		return Data;
	}
	@RequestMapping("ver")
	public void ver(HttpServletRequest request,HttpServletResponse response,Long cod_per){
		try {
			General general = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre = "dato_" + cod_per + "_" + general.getGes_gen(), tipo = "pdf", estado = "inline";
			Persona persona=usuarioS.obtener(cod_per);
			Persona us = (Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			String reportUrl = "/Reportes/dato_ver.jasper";
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("usuario", us.toString());
			parametros.put("cod_per", cod_per);
			parametros.put("persona", persona.getNom_per()+" "+persona.getPriape_per()+persona.getSegape_per());
			Utils.loadDataReport(parametros, general);
			GeneradorReportes generador=new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo,parametros,datasource.getConnection() ,nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error rep="+e.toString());
		}
	}
}
