package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Proveedor;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.GeneralS;
import net.resultadofinal.micomercial.service.ProveedorS;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.GeneradorReportes;
import net.resultadofinal.micomercial.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/proveedor/*")
public class ProveedorC {

	@Autowired
	private ProveedorS proveedorS;
	@Autowired
	private GeneralS generalS;
	@Autowired
	private DataSource datasource;
	private static final Logger logger = LoggerFactory.getLogger(ProveedorC.class);
	private static final String ENTITY = "proveedor";
	@RequestMapping("gestion")
	public String gestion(Model model){
		return "proveedor/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Proveedor> listar(HttpServletRequest request, boolean estado) throws IOException, SQLException {
		try {
			return proveedorS.listado(request, estado);
		} catch (Exception ex) {
			System.out.println("error lista usuarios: "+ex.toString());
			return null;
		}
	}

	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(Proveedor pro)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			pro.setCi_per("0");
			Long cod_pro=proveedorS.adicionar(pro);
			Data.put("cod_pro", cod_pro);
			Data.put("proveedor", pro.toString());
			Data.put("status", cod_pro>0);
			Data.put("msg", Utils.getSuccessFailedAdd(ENTITY, cod_pro>0));
		} catch (Exception e) {
			Data.put("status", false);
			Data.put("msg", e.getMessage());
		}
		
		return Data;
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(Proveedor pro)throws IOException{
		try {
			boolean status = proveedorS.modificar(pro);
			return new DataResponse(status, Utils.getSuccessFailedMod(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Long cod_pro)throws IOException{
		try {
			boolean status =  proveedorS.darEstado(cod_pro, false);
			return new DataResponse(status, Utils.getSuccessFailedEli(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(Long cod_pro)throws IOException{
		try {
			boolean status = proveedorS.darEstado(cod_pro, true);
			return new DataResponse(status, Utils.getSuccessFailedAct(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Long cod_pro){
		try {
			return new DataResponse(true,  proveedorS.obtener(cod_pro), Utils.successGet(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.toString());
		}
	}
	@RequestMapping("ver_credencial")
	public void ver_credencial(HttpServletRequest request,HttpServletResponse response,Long cod_per){
		try {
			General gestion= (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre="credencial_ayudante_"+cod_per,tipo="pdf",estado="inline";
			String reportUrl="/Reportes/ayudante_credencial.jasper";
			Map<String, Object> parametros=new HashMap<String, Object>();
			parametros.put("cod_per", cod_per);
			Utils.loadDataReport(parametros, gestion);
			parametros.put("tipo", "Credencial de Proveedor");
			parametros.put("path",request.getSession().getServletContext().getRealPath("/fotos"));
			parametros.put("path2",request.getSession().getServletContext().getRealPath("/general"));
			GeneradorReportes generador=new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo,parametros,datasource.getConnection() ,nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error al obtener reporte de proveedor: "+e.toString());
		}
	}
}