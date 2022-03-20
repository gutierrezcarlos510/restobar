package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Compra;
import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.model.wrap.HistorialCompraProducto;
import net.resultadofinal.micomercial.service.*;
import net.resultadofinal.micomercial.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/compra/*")
public class CompraC {

	private static final Logger logger = LoggerFactory.getLogger(CompraC.class);
	@Autowired
	private CompraS compraS;
	@Autowired
	private ProveedorS proveedorS;
	@Autowired
	private TipoProductoS tipoProductoS;
	@Autowired
	private DataSource datasource;

	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request, Model model) {
		Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if (user != null && gestion != null) {
//			ArqueoCaja arqueo = arqueocajaS.arqueocaja_verificar_sesion_actual(user.getCod_per(), gestion.getCod_suc());
//			if (arqueo == null) {
//				return "excepcion/sesion_caja";
//			} else {
//				return "compra/gestion";
//			}
			return "compra/gestion";
		} else {
			return "principal/login"+ MyConstant.SYSTEM;
		}
	}

	@RequestMapping("lista")
	public @ResponseBody Map<?, ?> lista(HttpServletRequest request, Integer draw, Integer start, boolean estado,
			boolean usuario, int length, String fini, String ffin) throws IOException {
		String total;
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
		Long cod_user = user.getCod_per();
		if (!usuario)
			cod_user = -1L;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		List<Compra> lista = compraS.listar(start, estado, search, length, cod_user, fini, ffin, gestion.getGes_gen());
		try {
			total = UtilClass.isNotNullEmpty(lista) ? lista.get(0).getTot().toString() : "0";
		} catch (Exception e) {
			total = "0";
		}
		Data.put("draw", draw);
		Data.put("recordsTotal", total);
		Data.put("data", lista);
		if (!search.equals(""))
			Data.put("recordsFiltered", lista.size());
		else
			Data.put("recordsFiltered", total);
		return Data;

	}

	@RequestMapping("adicionar")
	public String adicionar(Model model,Boolean isMobil) {
		model.addAttribute("proveedores", proveedorS.listAll());
		model.addAttribute("tipos", tipoProductoS.listAll(-1));
		model.addAttribute("actual", new Fechas().obtenerFechaActual("dd/MM/yyyy"));
		if(isMobil!=null && isMobil){
			return "compra/adicionar-mobil";
		}else {
			return "compra/adicionar";
		}
	}

	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(HttpServletRequest request, Compra c, Long productos[],
						 Integer cantidades[], BigDecimal precios[], BigDecimal descuentos[], BigDecimal subtotales[], BigDecimal totales[])
			throws IOException {
		try {
			Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if (usuario != null && gestion != null) {
				c.setCod_per(usuario.getCod_per());
				c.setGes_gen(gestion.getGes_gen());
				c.setCod_suc(gestion.getCod_suc());
				c.setUsuario(usuario.getNom_per()+" "+usuario.getPriape_per());
				boolean status = compraS.adicionar(c, productos, cantidades, precios, descuentos, subtotales, totales);
				return new DataResponse(status,
						status ? "Se realizo con exito el registro de la compra" : "No se logro registrar la compra:");
			} else {
				return new DataResponse(false, "No se logro obtener los datos de la sesion del usuario");
			}
		} catch (Exception e) {
			logger.error("Error al guardar la compra:" + e.toString());
			return new DataResponse(false, "Error al guardar el registro de compra:" + e.getMessage());
		}
	}

	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request, Model model, Long cod_com)
			throws IOException {
		try {
			Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			boolean status = compraS.eliminar(cod_com, usuario.getCod_per());
			return new DataResponse(status, status ? "Se realizo con exito la eliminacion de la compra"
					: "No se logro realizar la eliminacion de la compra");
		} catch (Exception e) {
			logger.error("eliminar la compra: " + e.getMessage());
			return new DataResponse(false, e.getMessage());
		}

	}

	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(HttpServletRequest request, Long cod_com) {
		try {
			return new DataResponse(true, compraS.obtener(cod_com), "Se logro con exito la consulta");
		} catch (Exception e) {
			logger.error("error al obtener compra: " + e.getMessage());
			return new DataResponse(false, "error al obtener la compra: " + e.toString());
		}
	}

	@RequestMapping("ver")
	public void ver(HttpServletRequest request, HttpServletResponse response, Long cod_com) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre = "compra_" + cod_com + "_" + gestion, tipo = "pdf", estado = "inline";
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			String reportUrl = "/Reportes/compra_ver.jasper";
			String SubRep = getClass().getResource("/Reportes/compra_ver_subreporte.jasper").toString();
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("usuario", us.toString());
			parametros.put("cod_com", cod_com);
			Utils.loadDataReport(parametros, gestion);
			parametros.put("subrep", SubRep.substring(0, SubRep.lastIndexOf("/")));
			GeneradorReportes generador = new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo, parametros,
					datasource.getConnection(), nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error reporte ver=" + e.toString());
		}
	}

	@GetMapping("obtenerHistorialCompraProducto/{id}")
	public @ResponseBody
    DataResponse obtenerHistorialCompraProducto(@PathVariable(name = "id") Integer codpro) {
		try {
			List<HistorialCompraProducto> historialProducto = compraS.obtenerHistorialCompraProducto(codpro);
			return new DataResponse(true, historialProducto, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}