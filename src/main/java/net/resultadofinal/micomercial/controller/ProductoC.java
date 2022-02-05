package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Producto;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.ProductoS;
import net.resultadofinal.micomercial.service.TipoProductoS;
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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/producto/*")
public class ProductoC {
	
	@Autowired
	private ProductoS productoS;
	@Autowired
	private TipoProductoS tipoproductoS;
	@Autowired
	private DataSource datasource;
	private static final Logger logger = LoggerFactory.getLogger(ProductoC.class);
	private static final String ENTITY = "producto";
	@RequestMapping("gestion")
	public String gestion(Model model){
		model.addAttribute("tipos",tipoproductoS.listAll());
		return "producto/gestion";
	}
	@RequestMapping("gestionInventario")
	public String gestionInventario(){
		return "producto/gestion-inventario";
	}

	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Producto> listar(HttpServletRequest request, boolean estado) {
		try {
			return productoS.listado(request, estado);
		} catch (Exception ex) {
			logger.error("error lista productos: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody DataResponse guardar(Producto p)throws IOException{
		try {
			return productoS.adicionar(p);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(Producto p)throws IOException{
		try {
			return productoS.modificar(p);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Long id)throws IOException{
		try {
			return productoS.darEstado(id, false);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(Long id)throws IOException{
		try {
			return productoS.darEstado(id, true);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Long id){
		try {
			return new DataResponse(true,productoS.obtener(id), Utils.successGet(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("verCatalogo")
	public void verInventario(HttpServletResponse response){
		try {
			String nombre="inventario",tipo="pdf",estado="inline";
			String reportUrl="/Reportes/inventario.jasper";
			Map<String, Object> parametros=new HashMap<String, Object>();
			GeneradorReportes generador=new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo,parametros,datasource.getConnection() ,nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error al inventario="+e.toString());
		}
	}
}