package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.*;
import net.resultadofinal.micomercial.service.*;
import net.resultadofinal.micomercial.util.*;
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
@RequestMapping("/venta/*")
public class VentaC {
	
	private static final Logger logger = LoggerFactory.getLogger(VentaC.class);
	@Autowired
	private ArqueoCajaS arqueocajaS;
	@Autowired
	private GeneralS generalS;
	@Autowired
	private VentaS ventaS;
	@Autowired
	private ClienteS clienteS;
	@Autowired
	private TipoProductoS tipoProductoS;
	@Autowired
	private DataSource datasource;
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		try {
			Persona user=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if(user != null && gestion != null) {
				ArqueoCaja arqueo=arqueocajaS.arqueocaja_verificar_sesion_actual(user.getCod_per(),gestion.getCod_suc());
				if(arqueo==null) {
					return "excepcion/sesion_caja";
				}else {
					return "venta/gestion";
				}
			} else {
				logger.error("Sesion expirada al ingresar a ventas");
				return "principal/login"+ MyConstant.SYSTEM;
			}
		} catch (Exception e) {
			logger.error("Error al ingresar gestion de ventas");
			return "principal/login"+ MyConstant.SYSTEM;
		}
	}
	
	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,boolean usuario,int length,String fini,String ffin)throws IOException{
		String total;
		General gestion= (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		Persona user=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
		Long cod_user=user.getCod_per();
		if(!usuario)cod_user=-1L;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<Venta> lista=ventaS.listar(start, estado, search,length,cod_user,fini,ffin,gestion.getGes_gen(),gestion.getCod_suc());
		try {
			total= UtilClass.isNotNullEmpty(lista)?lista.get(0).getTot().toString():"0";
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
	@RequestMapping("adicionar")
	public String adicionar(Model model,Boolean isMobil){
		model.addAttribute("clientes",clienteS.listAll());
		model.addAttribute("tipos",tipoProductoS.listAll(-1));
		if(isMobil != null && isMobil){
			return "venta/adicionar-mobil";
		} else {
			if(MyConstant.VENTA_CON_SELECT){
//				model.addAttribute("productos",tipoProductoS.listarAgrupadoConProductos());
				return "venta/adicionar-select";
			}else{
				return "venta/adicionar";
			}
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(HttpServletRequest request, Model model, Venta obj, Integer productos[], Integer cantidades[], Float precios[], Float descuentos[], Float subtotales[], Float totales[])throws IOException{
		try {
			Persona usuario=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if(usuario != null && gestion != null) {
				obj.setCodPer(usuario.getCod_per());
				obj.setGesGen(gestion.getGes_gen());
				obj.setCodSuc(gestion.getCod_suc());
				boolean status = ventaS.adicionar(obj,productos,cantidades,precios,descuentos,subtotales,totales);
				return new DataResponse(status, status ? "Se realizo con exito el registro de la venta":"No se logro realizar la venta");
			} else {
				logger.error("Sesion expirada al ingresar a ventas");
				return new DataResponse(false, "Sesion expirada al adicionar ventas");
			}
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("venta_simple")
	public @ResponseBody
    DataResponse venta_simple(HttpServletRequest request, Model model, int cod_pro, float precio)throws IOException{
		try {
			Persona usuario=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion= (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if(usuario != null && gestion !=null) {
				boolean status = ventaS.adicionarVentaSimple(cod_pro,usuario.getCod_per(),gestion.getGes_gen(),precio,gestion.getCod_suc());
				return new DataResponse(status, Utils.getSuccessFailedAdd("venta simple", status));
			}else {
				logger.error("Sesion expirada al ingresar a ventas");
				return new DataResponse(false, "Sesion expirada al adicionar ventas");
			}
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("venta_simple_manual")
	public @ResponseBody
    DataResponse venta_simple_manual(HttpServletRequest request, Model model, Integer producto, Float precio, Integer cantidad, Float total)throws IOException{
		try {
			Persona usuario=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion= (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if(usuario != null && gestion !=null) {
				boolean status = ventaS.adicionarVentaSimpleManual(producto,usuario.getCod_per(),gestion.getGes_gen(),precio,cantidad,total,gestion.getCod_suc());
				return new DataResponse(status, Utils.getSuccessFailedAdd("venta simple", status));
			}else {
				logger.error("Sesion expirada al ingresar a ventas");
				return new DataResponse(false, "Sesion expirada al adicionar ventas");
			}
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request, Model model, Long codVen)throws IOException{
		try {
			boolean status = ventaS.eliminar(codVen,false);
			return new DataResponse(status, status ? "Se realizo con exito la eliminacion de la venta":"No se logro realizar la eliminacion de la venta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(HttpServletRequest request, Long cod_ven){
		try {
			return new DataResponse(true, ventaS.obtener(cod_ven), "Se realizo la consulta exitosamente");
		} catch (Exception e) {
			logger.error("error al obtener="+e.toString());
			return new DataResponse(false, "error al obtener venta: "+e.getMessage());
		}
	}
	@RequestMapping("ver")
	public void ver(HttpServletRequest request,HttpServletResponse response,Long codVen){
		try {
			List<DetalleVenta> detalles=ventaS.obtenerDetalle(codVen);
			float descuento=0;
			for (DetalleVenta map : detalles) {
				descuento+=map.getDesDetven();
			}
			General general = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre="venta_"+codVen+"_"+general.getGes_gen(),tipo="pdf",estado="inline";
			Persona us=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			String reportUrl="/Reportes/venta_ver.jasper";
			String SubRep=getClass().getResource("/Reportes/venta_ver_subreporte.jasper").toString();
			Map<String, Object> parametros=new HashMap<String, Object>();
			parametros.put("usuario", us.toString());
			parametros.put("cod_ven", codVen);
			parametros.put("subrep",SubRep.substring(0, SubRep.lastIndexOf("/")));
			parametros.put("des_detven", descuento);
			Utils.loadDataReport(parametros, general);
			GeneradorReportes generador=new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo,parametros,datasource.getConnection() ,nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error reporte de ventas="+e.toString());
		}
	}
}