package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.*;
import net.resultadofinal.micomercial.model.wrap.ArqueoWrap;
import net.resultadofinal.micomercial.model.wrap.ResumenArqueoWrap;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.ArqueoS;
import net.resultadofinal.micomercial.service.FormaPagoS;
import net.resultadofinal.micomercial.service.GeneralS;
import net.resultadofinal.micomercial.service.UsuarioS;
import net.resultadofinal.micomercial.util.*;
import net.sf.jasperreports.engine.JRException;
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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/arqueocaja/*")
public class ArqueoCajaC {
	@Autowired
	private ArqueoS arqueocajaS;
	@Autowired
	private FormaPagoS formaPagoS;
	@Autowired
	private UsuarioS usuarioS;
	@Autowired
	DataSource datasource;

	private static final Logger logger = LoggerFactory.getLogger(ArqueoCajaC.class);
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request, Model model) {
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if (gestion != null) {
			model.addAttribute("usuarios", usuarioS.listarUsuariosSistema());
			model.addAttribute("formas", formaPagoS.listAll(gestion.getCod_suc()));
			return "arqueocaja/gestion";
		}
		return "principal/login"+ MyConstant.SYSTEM;
	}

	@RequestMapping("gestion_detalle")
	public String gestionDetalle(HttpServletRequest request, Model model, Long arqueoId) {
		Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if (user != null && gestion != null) {
			Arqueo arqueo = null;
			if(arqueoId != null){ // seleccion de caja
				arqueo = arqueocajaS.obtenerCaja(arqueoId);
			} else { // Se recupera la caja
				arqueo = arqueocajaS.arqueocajaVerificarSesionActual(user.getCod_per(), gestion.getCod_suc());
			}
			comunGestionArqueo(model, user, gestion, arqueo);
			return "arqueocaja/gestion_detalle";
		} else {
			model.addAttribute("msg", "Sesion expirada, por favor vuelva a ingresar.");
			return "principal/login"+ MyConstant.SYSTEM;
		}
	}

	private void comunGestionArqueo(Model model, Persona user, General gestion, Arqueo arqueo) {
		model.addAttribute("formas", formaPagoS.listAll(gestion.getCod_suc()));
		if (arqueo == null) {
			model.addAttribute("esPropioCajero",false);
			model.addAttribute("arqueoId", 0);
		} else {
			boolean esPropioCajero = arqueo.getDelegadoId() == user.getCod_per();
			model.addAttribute("esPropioCajero",esPropioCajero);
			model.addAttribute("arqueoId", arqueo.getId());
		}
	}

	@RequestMapping("gestionDetalleVendedor")
	public String gestionDetalleVendedor(HttpServletRequest request, Model model) {
		Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if (user != null && gestion != null) {
			model.addAttribute("userCajero",user.getCod_per());
			Arqueo arqueo = arqueocajaS.arqueocajaVerificarSesionActual(user.getCod_per(), gestion.getCod_suc());
			if(arqueo == null) {
				return "excepcion/sesion_caja";
			}
			comunGestionArqueo(model, user, gestion, arqueo);
			model.addAttribute("formas", formaPagoS.listAll(gestion.getCod_suc()));
		} else {
			model.addAttribute("msg", "Sesion expirada, por favor vuelva a ingresar.");
			return "principal/login"+ MyConstant.SYSTEM;
		}
		return "arqueocaja/gestion_detalle";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Arqueo> listar(HttpServletRequest request, boolean estado, boolean usuario,boolean estaActivoCaja) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			Long cod_user = -1L;
			if (!usuario)
				cod_user = user.getCod_per();
			return arqueocajaS.listado(request, estado,cod_user, gestion.getGes_gen(),estaActivoCaja);
		} catch (Exception ex) {
			System.out.println("error lista arqueo de caja: "+ex.toString());
			ex.printStackTrace();
			return null;
		}
	}

	@RequestMapping("lista_detalle")
	public @ResponseBody
	DataTableResults<DetalleArqueo> listaDetalle(HttpServletRequest request, boolean estado,Long arqueoId) {
		try {
			return arqueocajaS.listadoDetalles(request, estado, arqueoId);
		} catch (Exception ex) {
			System.out.println("error lista arqueo de caja: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("iniciar")
	public @ResponseBody
    DataResponse iniciar(HttpServletRequest request, Model model, Arqueo arqueo)
			throws IOException {
		try {
			Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			arqueo.setCustodioInicialId(usuario.getCod_per());// custodio sin nombre
			arqueo.setGestion(gestion.getGes_gen());
			arqueo.setSucursalId(gestion.getCod_suc());
			return arqueocajaS.iniciar(arqueo);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("iniciar_independiente")
	public @ResponseBody
    DataResponse iniciar_indepente(HttpServletRequest request, Model model,
                                   Arqueo arqueo) throws IOException {
		try {
			Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			arqueo.setDelegadoId(usuario.getCod_per());
			arqueo.setCustodioInicialId(1L);
			arqueo.setGestion(gestion.getGes_gen());
			arqueo.
					setSucursalId(gestion.getCod_suc());
			DataResponse resp = arqueocajaS.iniciar(arqueo);
			if(resp.isStatus()) {
				return new DataResponse(true, arqueocajaS.obtenerCaja((Long)resp.getData()), Utils.getSuccessFailedAdd("arqueo de caja", true));
			} else {
				return resp;
			}
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(HttpServletRequest request, Arqueo arqueo) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			arqueo.setGestion(gestion.getGes_gen());
			arqueo.setSucursalId(gestion.getCod_suc());
			boolean status = arqueocajaS.modificar(arqueo);
			return new DataResponse(status, Utils.getSuccessFailedAdd("arqueo de caja", status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("cerrar")
	public @ResponseBody
    DataResponse cerrar(HttpServletRequest request, Model model, Arqueo arqueo)
			throws IOException {
		try {
			Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			arqueo.setCustodioFinalId(usuario.getCod_per());
			boolean status = arqueocajaS.cerrar(arqueo);
			return new DataResponse(status, Utils.getSuccessFailedAdd("cerrar arqueo de caja", status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("cerrar_independiente")
	public @ResponseBody
    DataResponse cerrar_independiente(HttpServletRequest request, Model model,
                                      Arqueo arqueo) throws IOException {
		try {
			arqueo.setCustodioFinalId(MyConstant.ArqueoCaja.USER_ADMIN);
			boolean status = arqueocajaS.cerrar(arqueo);
			return new DataResponse(status, Utils.getSuccessFailedAdd("cierre independiente de arqueo de caja", status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request, Long arqueoId)
			throws IOException {
		try {
			DataResponse existeVentas = arqueocajaS.existeVentasConArqueo(arqueoId);
			if(existeVentas.isStatus()) {
				existeVentas.setStatus(false);
				return existeVentas;
			}
			Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			boolean status = arqueocajaS.darEstado(arqueoId, false, usuario.getCod_per());
			return new DataResponse(status, Utils.getSuccessFailedEli("arqueo de caja", status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminarDetalle")
	public @ResponseBody
    DataResponse eliminarDetalle(Long arqueoId, Integer detalleArqueoId) {
		try {
			boolean status = arqueocajaS.eliminarDetalle(arqueoId, detalleArqueoId);
			return new DataResponse(status, Utils.getSuccessFailedEli("arqueo de caja", status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(HttpServletRequest request, Long arqueoId) {
		try {
			return new DataResponse(true, arqueocajaS.obtenerCaja(arqueoId), Utils.successGet("arqueo de caja"));
		} catch (Exception e) {
			return new DataResponse(false, "No se logro obtener arqueo de caja");
		}
	}
	@RequestMapping("obtenerResumen")
	public @ResponseBody
    DataResponse obtenerResumen(HttpServletRequest request, Long arqueoId) {
		try {
			ResumenArqueoWrap arqueo = arqueocajaS.obtenerResumenArqueo(arqueoId);
			boolean status = arqueo != null;
			return new DataResponse(status, arqueo, status? Utils.successGet("resumen de arqueo de caja"): Utils.failedGet("resumen de arqueo de caja"));
		} catch (Exception e) {
			return new DataResponse(false, "No se logro obtener arqueo de caja");
		}
	}

	@RequestMapping("obtener_detalle")
	public @ResponseBody
    DataResponse obtener_detalle(HttpServletRequest request, Long arqueoId,
                                 Integer detalleArqueoId) {
		try {
			return new DataResponse(true, arqueocajaS.obtenerDetalle(detalleArqueoId, arqueoId), Utils.successGet("detalle de arqueo de caja"));
		} catch (Exception e) {
			return new DataResponse(false, "No se logro obtener detalle de arqueo de caja");
		}
	}

	@RequestMapping("ver")
	public void ver(HttpServletRequest request, HttpServletResponse response, Long arqueoId,Boolean esImpresionFacturera) {
		try {
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre = "arqueocaja_" + arqueoId + "_" + gestion.getGes_gen(), tipo = "pdf", estado = "inline";
			String reportUrl = (esImpresionFacturera!=null && esImpresionFacturera)?"/Reportes/arqueocaja_ver_factura.jasper":"/Reportes/arqueocaja_ver.jasper";
			Arqueo arqueo = arqueocajaS.obtenerCaja(arqueoId);
			if(arqueo == null){
				logger.error("No se encontro ultima caja del usuario para ver");
				return;
			}
			Map<String, Object> parametros = new HashMap<String, Object>();
			BigDecimal montoReal = arqueo.getMontoReal() != null ? arqueo.getMontoReal() : null;
			BigDecimal montoFinal = arqueo.getMontoFinal() != null ? arqueo.getMontoFinal() : null;
			String interpretacion = "";
			if (montoReal != null && montoFinal != null) {
				int resp = montoReal.compareTo(montoFinal);
				if (resp == 0) {
					interpretacion = "Cierre de caja Correcto";
				} else {
					if (resp > 0) {
						interpretacion = "Ganancia extra en caja diaria: " + (montoReal.subtract(montoFinal));
					} else {
						interpretacion = "Perdida extra en caja diaria: " + (montoFinal.subtract(montoReal));
					}
				}
			} else {
				interpretacion = "Cierre de caja pendiente.";
			}
			parametros.put("usuario", us.toString());
			parametros.put("cod_arqcaj", arqueoId);
			commonVer(request, response, gestion, nombre, tipo, estado, reportUrl, arqueo, parametros, montoReal, montoFinal, interpretacion);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error de reporte caja=" + e.toString());
		}
	}

	@RequestMapping("verPorUsuario")
	public void verPorUsuario(HttpServletRequest request, HttpServletResponse response, Long codUsuario,Boolean esImpresionFacturera) {
		try {
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			Sucursal sucursal = (Sucursal) request.getSession().getAttribute(MyConstant.Session.SUCURSAL);
			String nombre = "arqueo_usuario_" + codUsuario + "_" + gestion, tipo = "pdf", estado = "inline";

			String reportUrl = (esImpresionFacturera!=null && esImpresionFacturera)?"/Reportes/arqueocaja_ver_factura.jasper":"/Reportes/arqueocaja_ver.jasper";
			Arqueo arqueoUsuario = arqueocajaS.obtenerUltimaCajaPorUsuario(codUsuario, sucursal.getCod_suc());
			if(arqueoUsuario == null){
				logger.error("No se encontro ultima caja del usuario para ver");
				return;
			}
			Arqueo arqueo = arqueocajaS.obtenerCaja(arqueoUsuario.getId());
			Map<String, Object> parametros = new HashMap<String, Object>();
			BigDecimal montoReal = arqueo.getMontoReal() != null ? arqueo.getMontoReal() : null;
			BigDecimal montoFinal = arqueo.getMontoFinal() != null ? arqueo.getMontoFinal() : null;
			String interpretacion = "";
			if (montoReal != null && montoFinal != null) {
				int resp = montoReal.compareTo(montoFinal);
				if (resp == 0) {
					interpretacion = "Cierre de caja Correcto";
				} else {
					if (resp > 0) {
						interpretacion = "Ganancia extra en caja diaria: " + (montoReal.subtract(montoFinal));
					} else {
						interpretacion = "Perdida extra en caja diaria: " + (montoFinal.subtract(montoReal));
					}
				}
			} else {
				interpretacion = "Cierre de caja pendiente.";
			}
			parametros.put("usuario", us.toString());
			parametros.put("cod_arqcaj", arqueo.getId());
			commonVer(request, response, gestion, nombre, tipo, estado, reportUrl, arqueo, parametros, montoReal, montoFinal, interpretacion);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error de reporte caja=" + e.toString());
		}
	}

	private void commonVer(HttpServletRequest request, HttpServletResponse response, General gestion, String nombre, String tipo,
						   String estado, String reportUrl, Arqueo arqueo, Map<String, Object> parametros, BigDecimal montoReal,
						   BigDecimal montoFinal, String interpretacion) throws JRException, SQLException, IOException {
		Utils.loadDataReport(parametros, gestion);
		String SubRep=getClass().getResource("/Reportes/arqueocaja_ver.jasper").toString();
		parametros.put("delegado", arqueo.getXdelegado());
		parametros.put("cusini", arqueo.getXcustodioInicial());
		parametros.put("cusfin", arqueo.getXcustodioFinal());
		parametros.put("estado", arqueo.getEstado() ? "Activo" : "Inactivo");
		parametros.put("fini", arqueo.getFinicio().toString());
		parametros.put("ffin", arqueo.getFfin() != null ? arqueo.getFfin().toString() : "");
		parametros.put("monrea", montoReal);
		parametros.put("monini", arqueo.getMontoInicial());
		parametros.put("monfin", montoFinal);
		parametros.put("gestion", gestion.getGes_gen());
		parametros.put("sucursalId", gestion.getCod_suc());
		parametros.put("logsintex_gen", gestion.getLogsintex_gen());
		parametros.put("path", SubRep.substring(0, SubRep.lastIndexOf("/")));
		System.out.println(SubRep.substring(0, SubRep.lastIndexOf("/")));
		parametros.put("interpretacion", interpretacion);
		GeneradorReportes generador = new GeneradorReportes();
		generador.generarReporte(response, getClass().getResource(reportUrl), tipo, parametros,
				datasource.getConnection(), nombre, estado);
	}

	@RequestMapping("verCajaInicial")
	public void verCajaIncial(HttpServletRequest request, HttpServletResponse response, Long arqueoId) {
		try {
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre = "arqueocaja_" + arqueoId + "_" + gestion, tipo = "pdf", estado = "inline";

			String reportUrl = "/Reportes/arqueocaja_inicial.jasper";
			Arqueo arqueo = arqueocajaS.obtenerCaja(arqueoId);
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("usuario", us.toString());
			parametros.put("arqueoId", arqueoId);
			parametros.put("delegado", arqueo.getXdelegado());
			parametros.put("cusini", arqueo.getXcustodioInicial());
			parametros.put("cusfin", arqueo.getXcustodioFinal());
			parametros.put("estado", arqueo.getEstado() ? "Activo" : "Inactivo");
			parametros.put("fini", arqueo.getFinicio());
			parametros.put("ffin", arqueo.getFfin() != null ? arqueo.getFfin() : "");
			Utils.loadDataReport(parametros, gestion);
			parametros.put("logsintex_gen", gestion.getLogsintex_gen());
			parametros.put("path", request.getSession().getServletContext().getRealPath("/general"));
			GeneradorReportes generador = new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo, parametros,
					datasource.getConnection(), nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error de reporte de caja inicial=" + e.toString());
		}
	}

	@RequestMapping("ver_informe_venta")
	public void ver_informe_venta(HttpServletRequest request, HttpServletResponse response, Long arqueoId) {
		try {
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre = "arqueocaja_ventas_" + arqueoId + "_" + gestion, tipo = "pdf", estado = "inline";
			ArqueoTotal totales = arqueocajaS.obtenerTotal(arqueoId);
			String reportUrl = "/Reportes/arqueocaja_ventas.jasper";
			Arqueo arqueo = arqueocajaS.obtenerCaja(arqueoId);
			Map<String, Object> parametros = new HashMap<String, Object>();
			Utils.loadDataReport(parametros, gestion);
			parametros.put("usuario", us.toString());
			parametros.put("cod_arqcaj", arqueoId);
			parametros.put("delegado", arqueo.getXdelegado());
			parametros.put("cusini", arqueo.getXcustodioInicial());
			parametros.put("cusfin", arqueo.getXcustodioFinal());
			parametros.put("estado", arqueo.getEstado() ? "Activo" : "Inactivo");
			parametros.put("fini", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(arqueo.getFinicio()));
			parametros.put("ffin", arqueo.getFfin() != null ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(arqueo.getFfin()) : "");
			parametros.put("monrea", arqueo.getMontoReal() != null ? arqueo.getMontoReal() : null);
			parametros.put("monini", arqueo.getMontoInicial());
			parametros.put("monfin", arqueo.getMontoFinal() != null ? arqueo.getMontoFinal() : null);
			parametros.put("gestion", gestion.getGes_gen());
			parametros.put("ingresos", totales.getTingresos());
			BigDecimal egresoTotal = totales.getTegresos().add(totales.getTcompras());
			parametros.put("egresos", egresoTotal);
			parametros.put("logsintex_gen", gestion.getLogsintex_gen());
			parametros.put("path", request.getSession().getServletContext().getRealPath("/general"));
			GeneradorReportes generador = new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo, parametros,
					datasource.getConnection(), nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error de reporte de ventas=" + e.toString());
		}
	}

	@RequestMapping("ver_detalle")
	public void ver_detalle(HttpServletRequest request, HttpServletResponse response, Long arqueoId,
			Integer detalleArqueoId) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre = "Detalle arqueocaja_" + arqueoId + "_" + detalleArqueoId + "_" + gestion, tipo = "pdf",
					estado = "inline";
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			String reportUrl = "/Reportes/detallearqueo_ver.jasper";
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("usuario", us.toString());
			parametros.put("arqueoId", arqueoId);
			parametros.put("detalleArqueoId", detalleArqueoId);
			Utils.loadDataReport(parametros, gestion);
			GeneradorReportes generador = new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo, parametros,
					datasource.getConnection(), nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error en reporte de detalles de caja=" + e.toString());
		}
	}

	@RequestMapping("guardarDetalle")
	public @ResponseBody
    DataResponse guardarDetalle(HttpServletRequest request,
                                DetalleArqueo detalle) {
		try {
			Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if (user != null && gestion != null) {
				if (detalle != null) {
					short detalleId = arqueocajaS.adicionarDetalle(detalle);
					return new DataResponse(detalleId > 0, detalleId, Utils.getSuccessFailedAdd("detalle de arqueo", detalleId>0));
				} else
					return new DataResponse(false, "No se logro recuperar el arqueo de caja, para registro de detalle");
			}else {
				return new DataResponse(false, "Sesion expirada del usuario");
			}
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("guardarArqueoInicial")
	public @ResponseBody
    DataResponse guardarCajaInicial(HttpServletRequest request, Model model,
                                    DetalleArqueo detalle) throws IOException {
		Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if (user != null && gestion != null) {
			Arqueo arqueo = arqueocajaS.arqueocajaVerificarSesionActual(user.getCod_per(), gestion.getCod_suc());
			if (arqueo != null) {
				detalle.setArqueoId(arqueo.getId());
				if (detalle.getDescripcion() != null && !detalle.getDescripcion().isEmpty()) {
					detalle.setDescripcion("Transaccion de apertura de inicial." + detalle.getDescripcion());
				} else {
					detalle.setDescripcion("Transaccion de apertura de inicial.");
				}
				boolean status = arqueocajaS.adicionarDetalle(detalle) > 0;
				return new DataResponse(status, Utils.getSuccessFailedAdd("arqueo inicial", status));
			} else
				return new DataResponse(false, "No se logro obtener el arqueo de caja");
		}else {
			return new DataResponse(false, "Sesion exiparada del usuario");
		}
	}

	@RequestMapping("eliminarDetalleArqueoInicial")
	public @ResponseBody
    DataResponse eliminarDetalleArqueoInicial(HttpServletRequest request, Model model,
                                              Long arqueoId, Integer detalleArqueoId) throws IOException {
		try {
			if (arqueocajaS.eliminarDetalle(arqueoId, detalleArqueoId)) {
				return new DataResponse(true, "Se realizo con exito la eliminacion del registro");
			} else {
				return new DataResponse(false, "No se logro eliminar el registro");
			}
		} catch (Exception e) {
			// TODO: handle exception
			return new DataResponse(false, "Error al eliminar registro de detalle de arqueo");
		}
	}

	@RequestMapping("guardarArqueoNormal")
	public @ResponseBody
    DataResponse guardarCajaNormal(HttpServletRequest request, Model model,
                                   DetalleArqueo detalle) throws IOException {
		Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if (user != null && gestion != null) {
			Arqueo arqueo = arqueocajaS.arqueocajaVerificarSesionActual(user.getCod_per(), gestion.getCod_suc());
			if (arqueo != null) {
				detalle.setArqueoId(arqueo.getId());
				boolean status = arqueocajaS.adicionarDetalle(detalle) > 0;
				return new DataResponse(status, Utils.getSuccessFailedAdd("arqueo de caja normal ", status));
			} else
				return new DataResponse(false, "No se logro obtener el arqueo de caja");
		}else {
			return new DataResponse(false, "Sesion expirada del usuario");
		}
	}

	@RequestMapping("rehabilitar")
	public @ResponseBody
	DataResponse rehabilitar( Long arqueoId)
			throws IOException {
		try {
			boolean status = arqueocajaS.rehabilitarArqueo(arqueoId);
			return new DataResponse(status, Utils.getSuccessFailedEli("arqueo de caja", status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("imprimirUltimoArqueo")
	public void imprimirUltimoArqueo(HttpServletRequest request, HttpServletResponse response,Boolean esImpresionFacturera) {
		try {
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if(us != null && gestion !=null) {
				Arqueo ultimoArqueoUserSession = arqueocajaS.obtenerUltimoArqueoUsuario(us.getCod_per(), gestion.getCod_suc());
				if(ultimoArqueoUserSession != null) {
					String nombre = "arqueocaja_" + ultimoArqueoUserSession.getId() + "_" + gestion.getGes_gen(), tipo = "pdf", estado = "inline";
					String reportUrl = (esImpresionFacturera!=null && esImpresionFacturera)?"/Reportes/arqueocaja_ver_factura.jasper":"/Reportes/arqueocaja_ver.jasper";
					if(ultimoArqueoUserSession == null){
						logger.error("No se encontro ultima caja del usuario para ver");
						return;
					}
					Map<String, Object> parametros = new HashMap<String, Object>();
					BigDecimal montoReal = ultimoArqueoUserSession.getMontoReal() != null ? ultimoArqueoUserSession.getMontoReal() : null;
					BigDecimal montoFinal = ultimoArqueoUserSession.getMontoFinal() != null ? ultimoArqueoUserSession.getMontoFinal() : null;
					String interpretacion = "";
					if (montoReal != null && montoFinal != null) {
						int resp = montoReal.compareTo(montoFinal);
						if (resp == 0) {
							interpretacion = "Cierre de caja Correcto";
						} else {
							if (resp > 0) {
								interpretacion = "Ganancia extra en caja diaria: " + (montoReal.subtract(montoFinal));
							} else {
								interpretacion = "Perdida extra en caja diaria: " + (montoFinal.subtract(montoReal));
							}
						}
					} else {
						interpretacion = "Cierre de caja pendiente.";
					}
					parametros.put("usuario", us.toString());
					parametros.put("cod_arqcaj", ultimoArqueoUserSession.getId());
					commonVer(request, response, gestion, nombre, tipo, estado, reportUrl, ultimoArqueoUserSession, parametros, montoReal, montoFinal, interpretacion);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error de reporte caja=" + e.toString());
		}
	}
}