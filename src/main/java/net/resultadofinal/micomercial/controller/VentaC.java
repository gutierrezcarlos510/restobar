package net.resultadofinal.micomercial.controller;

import com.google.gson.GsonBuilder;
import net.resultadofinal.micomercial.model.*;
import net.resultadofinal.micomercial.model.form.VentaForm;
import net.resultadofinal.micomercial.model.wrap.VentaInfoWrap;
import net.resultadofinal.micomercial.model.wrap.VentaPedidoWrap;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.*;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.GeneradorReportes;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/venta/*")
public class VentaC {
	
	private static final Logger logger = LoggerFactory.getLogger(VentaC.class);
	@Autowired
	private ArqueoS arqueocajaS;
	@Autowired
	private VentaS ventaS;
	@Autowired
	private CartillaDiariaS cartillaDiariaS;
	@Autowired
	private AlmacenS almacenS;
	@Autowired
	private MesaS mesaS;
	@Autowired
	private UsuarioS usuarioS;
	@Autowired
	private ClienteS clienteS;
	@Autowired
	private FormaPagoS formaPagoS;
	@Autowired
	private DataSource datasource;
	private static final int ROL_MESERO = 5;
	private static final long CLIENTE_INCOGNITO = 0;
	@RequestMapping("initParam")
	public @ResponseBody DataResponse initParam(HttpServletRequest request, Long ventaId){
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if(gestion != null) {
			Map<String, Object> map = new HashMap<>();

			map.put("productos", almacenS.listarProductos(gestion.getCod_suc()));
			map.put("cartillaDiariaList", cartillaDiariaS.obtenerCartillaActivaSucursal(gestion.getCod_suc()));
			map.put("mesasEspeciales", mesaS.listarMesasEspeciales());
			if(ventaId > 0) {
				VentaForm v = ventaS.obtenerVentaForm(ventaId);
				List<Mesa> mesas = mesaS.listarMesasLibresPorSucursal(gestion.getCod_suc());
				if(v.getMesaId() > 0) { // Si es menor a cero es pedido o no tiene mesa, caso contrario se recupera la mesa
					Mesa mesaVenta = mesaS.obtener(v.getMesaId());
					if(mesas == null) {
						mesas = new ArrayList<>();
					}
					mesas.add(mesaVenta);
				}
				map.put("mesas",mesas);
				map.put("venta", v);
				map.put("cliente", clienteS.obtener(v.getClienteId()));
			} else {
				map.put("mesas", mesaS.listarMesasLibresPorSucursal(gestion.getCod_suc()));
				map.put("cliente", clienteS.obtener(CLIENTE_INCOGNITO));
			}
			return new DataResponse(true, map, "Se realizo con exito.");
		} else {
			return new DataResponse(false, "Error al recuperar informacion");
		}
	}
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request){
		try {
			Persona user=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if(user != null && gestion != null) {
				Arqueo arqueo=arqueocajaS.arqueocajaVerificarSesionActual(user.getCod_per(),gestion.getCod_suc());
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
	public @ResponseBody
	DataTableResults<Venta> lista(HttpServletRequest request, boolean estado, boolean usuario, Short tipo) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			Long cod_user = -1L;
			if (usuario) {
				cod_user = user.getCod_per();
			}
			return ventaS.listado(request, estado,cod_user, gestion.getCod_suc(), tipo);
		} catch (Exception ex) {
			System.out.println("error lista arqueo de caja: "+ex.toString());
			ex.printStackTrace();
			return null;
		}
	}
	@RequestMapping("adicionar")
	public String adicionar(HttpServletRequest request,Model model,Boolean isMobil, Long ventaId, Boolean esMesero,Boolean ingresarPago){
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
		if(gestion != null && user != null) {
			model.addAttribute("meseros", usuarioS.listarUsuariosPorRol(ROL_MESERO, gestion.getCod_suc()));
			model.addAttribute("meseroId", esMesero ? user.getCod_per() : 0);
			model.addAttribute("formas", formaPagoS.listAll(gestion.getCod_suc()));
			model.addAttribute("ventaId", ventaId != null ? ventaId : 0);
			model.addAttribute("ingresarPago", ingresarPago != null ? ingresarPago : false);
			return "venta/adicionar-comanda";
		} else {
			return "principal/login"+ MyConstant.SYSTEM;
		}
	}
	@RequestMapping("guardarComanda")
	public @ResponseBody
	DataResponse guardarComanda(HttpServletRequest request, @RequestBody VentaForm obj){
		try {
			Persona usuario=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if(usuario != null && gestion != null) {
				obj.setCreatedBy(usuario.getCod_per());
				obj.setSucursalId(gestion.getCod_suc());
				obj.setGestion(gestion.getGes_gen());
				return ventaS.guardarComanda(obj);
			} else {
				logger.error("Sesion expirada al ingresar a ventas");
				return new DataResponse(false, "Sesion expirada al adicionar ventas");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizarComanda")
	public @ResponseBody
	DataResponse actualizarComanda(HttpServletRequest request, @RequestBody VentaForm obj){
		try {
			Persona usuario=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if(usuario != null && gestion != null) {
				obj.setCreatedBy(usuario.getCod_per());
				obj.setSucursalId(gestion.getCod_suc());
				return ventaS.actualizarComanda(obj);
			} else {
				logger.error("Sesion expirada al ingresar a ventas");
				return new DataResponse(false, "Sesion expirada al adicionar ventas");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request, Long id){
		try {
			Persona usuario=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			boolean status = ventaS.eliminar(id, usuario.getCod_per());
			return new DataResponse(status, status ? "Se realizo con exito la eliminacion de la venta":"No se logro realizar la eliminacion de la venta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Long id){
		try {
			return new DataResponse(true, ventaS.obtener(id), "Se realizo la consulta exitosamente");
		} catch (Exception e) {
			logger.error("error al obtener="+e.toString());
			return new DataResponse(false, "error al obtener venta: "+e.getMessage());
		}
	}
	@RequestMapping("obtenerInfo")
	public @ResponseBody
	DataResponse obtenerInfo(Long ventaId){
		try {
			return new DataResponse(true, ventaS.obtenerVentaInfo(ventaId), "Se realizo la consulta exitosamente");
		} catch (Exception e) {
			logger.error("error al obtener="+e.toString());
			return new DataResponse(false, "error al obtener venta: "+e.getMessage());
		}
	}
	@RequestMapping("ver")
	public void ver(HttpServletRequest request,HttpServletResponse response,Long id){
		try {

			General general = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre="venta_"+id+"_"+general.getGes_gen(),tipo="pdf",estado="inline";
			Persona us=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			String reportUrl="/Reportes/venta_ver_comprobante.jasper";
			VentaInfoWrap venta = ventaS.obtenerVentaInfo(id);
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.serializeNulls();
			String jsonDetalles = gsonBuilder.create().toJson(venta.getDetalleVentaGlobal());

			Map<String, Object> parametros=new HashMap<String, Object>();
			parametros.put("usuario", us.toString());
			parametros.put("ventaId", id);
			parametros.put("subtotal", venta.getSubtotal());
			parametros.put("descuento", venta.getDescuento());
			parametros.put("costoAdicional", venta.getCostoAdicional());
			parametros.put("total", venta.getTotal());
			parametros.put("totalPagado", venta.getTotalPagado());
			parametros.put("totalCambio", venta.getTotalCambio());
			parametros.put("xcliente", venta.getXcliente());
			parametros.put("xusuario", venta.getXusuario());
			parametros.put("xfecha", new SimpleDateFormat("DD/MM/YYYY HH:mm:ss").format(venta.getFecha()));
			parametros.put("xformaPago", venta.getXformaPago());
			Utils.loadDataReport(parametros, general);
			GeneradorReportes generador=new GeneradorReportes();
			generador.generarReporteJson(response, getClass().getResource(reportUrl), tipo,parametros,jsonDetalles ,nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error reporte de ventas="+e.toString());
		}
	}
	@RequestMapping("gestionPedidoPendientes")
	public String gestionPedidoPendientes(){
		return "venta/pedidosPendientes";
	}
	@RequestMapping("listaPedidoCocina")
	public @ResponseBody
	DataTableResults<VentaPedidoWrap> listaPedidoCocina(HttpServletRequest request, Short area) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return ventaS.listadoPedidoCocina(request, gestion.getCod_suc(), area);
		} catch (Exception ex) {
			System.out.println("error lista arqueo de caja: "+ex.toString());
			ex.printStackTrace();
			return null;
		}
	}
	@RequestMapping("confirmarPedido")
	public @ResponseBody
	DataResponse confirmarPedido(Long ventaId, Short historicoVentaId, Short areaId){
		try {
			return ventaS.registrarPedidoRealizado(ventaId, historicoVentaId, areaId);
		} catch (Exception e) {
			logger.error("error al obtener="+e.toString());
			return new DataResponse(false, "error al obtener venta: "+e.getMessage());
		}
	}
	@RequestMapping("verPedidoPendiente")
	public void verPedidoPendiente(HttpServletRequest request, HttpServletResponse response, Long ventaId, Short historicoVentaId, Short areaId) {
		try {
			String nombre = "pedido_comanda_" + ventaId, tipo = "pdf", estado = "inline";
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			String reportUrl = "/Reportes/venta_pedido_espera.jasper";
			Map<String, Object> parametros = new HashMap<String, Object>();
			HistoricoVenta obj = ventaS.obtenerHistoricoVenta(ventaId, historicoVentaId);
			parametros.put("usuario", us.toString());
			parametros.put("ventaId", ventaId);
			parametros.put("xcliente", obj.getXcliente());
			parametros.put("xusuario", obj.getXusuario());
			parametros.put("xfecha", obj.getFecha() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a").format(obj.getFecha()): "");
			parametros.put("xmesa", obj.getXmesa());
			parametros.put("areaId", areaId);
			GeneradorReportes generador = new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo, parametros,
					datasource.getConnection(), nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error reporte ver=" + e.toString());
		}
	}
}