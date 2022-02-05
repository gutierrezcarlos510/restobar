package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Producto;
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
import java.util.List;
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
	
	@SuppressWarnings("unchecked")
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<?> lista=productoS.listar(start, estado, search,length);
		try {
			total=((Map<String, Object>) lista.get(0)).get("Tot").toString();			
		} catch (Exception e) {
			total="0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		Data.put("recordsFiltered", total);
		return Data;
	}
	@RequestMapping("guardar")
	public @ResponseBody DataResponse guardar(Producto p)throws IOException{
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			int cod_pro=productoS.adicionar(p);
			Map<String, Object> tipo=tipoproductoS.obtener(p.getCod_tippro());
			Data.put("cod_pro", cod_pro);
			Data.put("nom_pro", tipo.get("nom_tippro").toString()+" "+p.getNom_pro());
			Data.put("status", cod_pro>0);
			Data.put("msg", Utils.getSuccessFailedAdd(ENTITY, cod_pro>0));
		} catch (Exception e) {
			Data.put("msg", e.getMessage());
			Data.put("status", false);
		}
		return Data;		
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(Producto p)throws IOException{
		try {
			boolean status = productoS.modificar(p);
			return new DataResponse(status, Utils.getSuccessFailedMod(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Integer cod_pro)throws IOException{
		try {
			boolean status = productoS.darEstado(cod_pro,false);
			return new DataResponse(status, Utils.getSuccessFailedEli(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(Integer cod_pro)throws IOException{
		try {
			boolean status = productoS.darEstado(cod_pro,true);
			return new DataResponse(status, Utils.getSuccessFailedAct(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(String nom_pro){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", productoS.validarNom(nom_pro));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Integer cod_pro){
		try {
			return new DataResponse(true,productoS.obtener(cod_pro), Utils.successGet(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("asignar")
	public @ResponseBody
    DataResponse asignar(Integer cod_pro, String codigos[])throws IOException{
		try {
			boolean status = productoS.asignar(cod_pro, codigos);
			return new DataResponse(status, Utils.getSuccessFailedAdd("asignacion de "+ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtenerxcodigo")
	public @ResponseBody
    DataResponse obtenerxcodigo(String barcode){
		try {
			Map<String, Object> producto=productoS.obtenerxcodigo(barcode);
//			return new DataResponse(producto!=null, Utils.successGet("codigos del "+ENTITY));
			return new DataResponse(producto!=null, producto, Utils.successGet("codigos del "+ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("imprimir_codigos")
	public void imprimir_codigos(HttpServletResponse response,Integer cod_pro){
		try {
			String nombre="producto_"+cod_pro+"_",tipo="pdf",estado="inline";
			String reportUrl="/Reportes/producto_codigo_barra.jasper";
			Map<String, Object> parametros=new HashMap<String, Object>();
			Map<String, Object> producto=productoS.obtener(cod_pro);
			Map<String, Object> tipo_producto=(Map<String, Object>) producto.get("tipo");
			parametros.put("cod_pro", cod_pro);
			parametros.put("producto", tipo_producto.get("nom_tippro").toString()+" "+producto.get("nom_pro").toString());
			GeneradorReportes generador=new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo,parametros,datasource.getConnection() ,nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error al imprimir codigos="+e.toString());
		}
	}
	@RequestMapping("verInventario")
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