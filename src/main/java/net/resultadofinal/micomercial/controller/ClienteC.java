package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Cliente;
import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.ClienteS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.GeneradorReportes;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cliente/*")
public class ClienteC {

	@Autowired
	private ClienteS clienteS;
	@Autowired
	private DataSource datasource;
	private static final Logger logger = LoggerFactory.getLogger(ClienteC.class);
	private static final String ENTITY = "cliente";
	@RequestMapping("gestion")
	public String gestion(){
		return "cliente/gestion";
	}

	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Cliente> listar(HttpServletRequest request, boolean estado) {
		try {
			return clienteS.listado(request, estado);
		} catch (Exception ex) {
			logger.error("error lista clientes: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody Map<String, Object> guardar(Cliente cli)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Long cod_pro=clienteS.adicionar(cli);
			boolean status = cod_pro>0;
			Data.put("cod_cli", cod_pro);
			Data.put("cliente", cli.toString());
			Data.put("xcliente", clienteS.obtener(cod_pro));
			Data.put("status", status);
			Data.put("msg", Utils.getSuccessFailedAdd(ENTITY, status));
		} catch (Exception e) {
			Data.put("status", false);
			Data.put("msg", e.getMessage());
		}
		return Data;		
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(Cliente cli)throws IOException{
		try {
			boolean status = clienteS.modificar(cli);
			return new DataResponse(status, Utils.getSuccessFailedMod(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.toString());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Long cod_cli)throws IOException{
		try {
			return clienteS.darEstado(cod_cli, false);
		} catch (Exception e) {
			return new DataResponse(false, e.toString());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(Long cod_cli)throws IOException{
		try {
			return clienteS.darEstado(cod_cli, true);
		} catch (Exception e) {
			return new DataResponse(false, e.toString());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Long cod_cli){
		try {
			return new DataResponse(true, clienteS.obtener(cod_cli), Utils.successGet(ENTITY));
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
			parametros.put("tipo", "Credencial de Cliente");
			parametros.put("path",request.getSession().getServletContext().getRealPath("/fotos"));
			parametros.put("path2",request.getSession().getServletContext().getRealPath("/general"));
			Utils.loadDataReport(parametros, gestion);
			GeneradorReportes generador=new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo,parametros,datasource.getConnection() ,nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error en reporte cliente= "+e.toString());
		}
	}
}