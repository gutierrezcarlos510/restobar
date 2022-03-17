package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.*;
import net.resultadofinal.micomercial.model.wrap.ArqueoWrap;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.ArqueoCajaS;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/arqueocaja/*")
public class ArqueoCajaC {
	@Autowired
	private GeneralS generalS;
	@Autowired
	private ArqueoCajaS arqueocajaS;
	@Autowired
	private UsuarioS usuarioS;
	@Autowired
	DataSource datasource;
	private static final int SESION_ACTIVA = 1;

	private static final Logger logger = LoggerFactory.getLogger(ArqueoCajaC.class);
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request, Model model) {
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if (gestion != null) {
			model.addAttribute("usuarios", usuarioS.listarUsuariosSistema());
			return "arqueocaja/gestion";
		}
		return "principal/login"+ MyConstant.SYSTEM;
	}

	@RequestMapping("gestion_detalle")
	public String gestionDetalle(HttpServletRequest request, Model model, Long cod_arqcaj) {
		Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if (user != null && gestion != null) {
			if(cod_arqcaj != null){
				ArqueoCaja arqueo = arqueocajaS.obtenerCaja(cod_arqcaj);
				model.addAttribute("esPropioCajero",(arqueo.getDelegadoId() == user.getCod_per())?1:0);
				model.addAttribute("cod_arqcaj", cod_arqcaj);
				return "arqueocaja/gestion_detalle";
			}
			ArqueoCaja arqueo = arqueocajaS.arqueocaja_verificar_sesion_actual(user.getCod_per(), gestion.getCod_suc());
			if (arqueo == null) {
				model.addAttribute("cod_arqcaj", 0);
				model.addAttribute("fecha", new Fechas().obtenerFechaActual("dd/MM/yyyy"));
			} else {
				model.addAttribute("arqueo", arqueo);
				model.addAttribute("cod_arqcaj", arqueo.getId());
				model.addAttribute("fecha", arqueo.getFinicio());
				model.addAttribute("estado", 1);
			}
			return "arqueocaja/gestion_detalle";
		} else {
			model.addAttribute("msg", "Sesion expirada, por favor vuelva a ingresar.");
			return "principal/login"+ MyConstant.SYSTEM;
		}
	}
	@RequestMapping("gestion_detalle_vendedor")
	public String gestionDetalleVendedor(HttpServletRequest request, Model model) {
		Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if (user != null && gestion != null) {
			model.addAttribute("userCajero",user.getCod_per());
			ArqueoCaja arqueo = arqueocajaS.arqueocaja_verificar_sesion_actual(user.getCod_per(),
					gestion.getCod_suc());
			if (arqueo == null) {
				model.addAttribute("cod_arqcaj", 0);
				model.addAttribute("fecha", new Fechas().obtenerFechaActual("dd/MM/yyyy"));
			} else {
				model.addAttribute("arqueo", arqueo);
				model.addAttribute("cod_arqcaj", arqueo.getId());
				model.addAttribute("fecha", arqueo.getFinicio());
				model.addAttribute("estado", 1);
			}
		} else {
			model.addAttribute("msg", "Sesion expirada, por favor vuelva a ingresar.");
			return "principal/login"+ MyConstant.SYSTEM;
		}
		return "arqueocaja/gestion-detalle-vendedor";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<ArqueoCaja> listar(HttpServletRequest request, boolean estado,boolean usuario) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			Long cod_user = -1L;
			if (!usuario)
				cod_user = user.getCod_per();
			return arqueocajaS.listado(request, estado,cod_user, gestion.getGes_gen());
		} catch (Exception ex) {
			System.out.println("error lista arqueo de caja: "+ex.toString());
			return null;
		}
	}

	@RequestMapping("lista_detalle")
	public @ResponseBody
	DataTableResults<DetalleArqueo> listaDetalle(HttpServletRequest request, boolean estado,Long cod_arqcaj) {
		try {
			return arqueocajaS.listadoDetalles(request, estado, cod_arqcaj);
		} catch (Exception ex) {
			System.out.println("error lista arqueo de caja: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("iniciar")
	public @ResponseBody
    DataResponse iniciar(HttpServletRequest request, Model model, ArqueoCaja arqueo)
			throws IOException {
		try {
			Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			arqueo.setCustodioInicialId(usuario.getCod_per());// custodio sin nombre
			arqueo.setGestion(gestion.getGes_gen());
			arqueo.setSucursalId(gestion.getCod_suc());
			boolean status = arqueocajaS.iniciar(arqueo) > 0;
			return new DataResponse(status, Utils.getSuccessFailedAct("arqueo de caja", status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("iniciar_independiente")
	public @ResponseBody
    DataResponse iniciar_indepente(HttpServletRequest request, Model model,
                                   ArqueoCaja arqueo) throws IOException {
		try {
			Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			arqueo.setDelegadoId(usuario.getCod_per());
			arqueo.setCustodioInicialId(1L);
			arqueo.setGestion(gestion.getGes_gen());
			arqueo.setSucursalId(gestion.getCod_suc());
			Long cod = arqueocajaS.iniciar(arqueo);
			boolean status = cod > 0;
			return new DataResponse(status, arqueocajaS.obtener(cod), Utils.getSuccessFailedAdd("arqueo de caja", status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(HttpServletRequest request, Model model, ArqueoCaja arqueo)
			throws IOException {
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
    DataResponse cerrar(HttpServletRequest request, Model model, ArqueoCaja arqueo)
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
                                      ArqueoCaja arqueo) throws IOException {
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
    DataResponse eliminar(HttpServletRequest request, Model model, Long cod_arqcaj)
			throws IOException {
		try {
			Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			boolean status = arqueocajaS.darEstado(cod_arqcaj, false, usuario.getCod_per());
			return new DataResponse(status, Utils.getSuccessFailedEli("arqueo de caja", status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminarDetalle")
	public @ResponseBody
    DataResponse eliminarDetalle(Long cod_arqcaj, Integer cod_detarq) {
		try {
			boolean status = arqueocajaS.eliminarDetalle(cod_arqcaj,cod_detarq);
			return new DataResponse(status, Utils.getSuccessFailedEli("arqueo de caja", status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(HttpServletRequest request, Long cod_arqcaj) {
		try {
			return new DataResponse(true, arqueocajaS.obtener(cod_arqcaj), Utils.successGet("arqueo de caja"));
		} catch (Exception e) {
			return new DataResponse(false, "No se logro obtener arqueo de caja");
		}
	}
	@RequestMapping("obtenerResumen")
	public @ResponseBody
    DataResponse obtenerResumen(HttpServletRequest request, Long cod_arqcaj) {
		try {
			ArqueoWrap arqueo = arqueocajaS.obtenerResumenArqueo(cod_arqcaj);
			boolean status = arqueo != null;
			return new DataResponse(status, arqueo, status? Utils.successGet("resumen de arqueo de caja"): Utils.failedGet("resumen de arqueo de caja"));
		} catch (Exception e) {
			return new DataResponse(false, "No se logro obtener arqueo de caja");
		}
	}

	@RequestMapping("obtener_detalle")
	public @ResponseBody
    DataResponse obtener_detalle(HttpServletRequest request, Long cod_arqcaj,
                                 Integer cod_detarq) {
		try {
			return new DataResponse(true, arqueocajaS.obtenerDetalle(cod_detarq, cod_arqcaj), Utils.successGet("detalle de arqueo de caja"));
		} catch (Exception e) {
			return new DataResponse(false, "No se logro obtener detalle de arqueo de caja");
		}
	}

	@RequestMapping("ver")
	public void ver(HttpServletRequest request, HttpServletResponse response, Long cod_arqcaj,Boolean esImpresionFacturera) {
		try {
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre = "arqueocaja_" + cod_arqcaj + "_" + gestion, tipo = "pdf", estado = "inline";
			String reportUrl = (esImpresionFacturera!=null && esImpresionFacturera)?"/Reportes/arqueocaja_ver_factura.jasper":"/Reportes/arqueocaja_ver.jasper";
			ArqueoCaja arqueo = arqueocajaS.obtenerCaja(cod_arqcaj);
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
			parametros.put("cod_arqcaj", cod_arqcaj);
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
			General general = generalS.obtener(gestion.getGes_gen(), sucursal.getCod_suc());
			String nombre = "arqueo_usuario_" + codUsuario + "_" + gestion, tipo = "pdf", estado = "inline";

			String reportUrl = (esImpresionFacturera!=null && esImpresionFacturera)?"/Reportes/arqueocaja_ver_factura.jasper":"/Reportes/arqueocaja_ver.jasper";
			ArqueoCaja arqueoUsuario = arqueocajaS.obtenerUltimaCajaPorUsuario(codUsuario, sucursal.getCod_suc());
			if(arqueoUsuario == null){
				logger.error("No se encontro ultima caja del usuario para ver");
				return;
			}
			ArqueoCaja arqueo = arqueocajaS.obtenerCaja(arqueoUsuario.getId());
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

	private void commonVer(HttpServletRequest request, HttpServletResponse response, General gestion, String nombre, String tipo, String estado, String reportUrl, ArqueoCaja arqueo, Map<String, Object> parametros, BigDecimal montoReal, BigDecimal montoFinal, String interpretacion) throws JRException, SQLException, IOException {
		Utils.loadDataReport(parametros, gestion);
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
		parametros.put("logsintex_gen", gestion.getLogsintex_gen());
		parametros.put("path", request.getSession().getServletContext().getRealPath("/general"));
		parametros.put("interpretacion", interpretacion);
		GeneradorReportes generador = new GeneradorReportes();
		generador.generarReporte(response, getClass().getResource(reportUrl), tipo, parametros,
				datasource.getConnection(), nombre, estado);
	}

	@RequestMapping("verCajaInicial")
	public void verCajaIncial(HttpServletRequest request, HttpServletResponse response, Long cod_arqcaj) {
		try {
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre = "arqueocaja_" + cod_arqcaj + "_" + gestion, tipo = "pdf", estado = "inline";

			String reportUrl = "/Reportes/arqueocaja_inicial.jasper";
			Map<String, Object> arqueo = arqueocajaS.obtener(cod_arqcaj);
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("usuario", us.toString());
			parametros.put("cod_arqcaj", cod_arqcaj);
			parametros.put("delegado", arqueo.get("delegado").toString());
			parametros.put("cusini", arqueo.get("custodio_inicial").toString());
			parametros.put("cusfin", arqueo.get("custodio_final").toString());
			parametros.put("estado", Boolean.parseBoolean(arqueo.get("est_arqcaj").toString()) ? "Activo" : "Inactivo");
			parametros.put("fini", arqueo.get("fechai").toString());
			parametros.put("ffin", arqueo.get("fechaf") != null ? arqueo.get("fechaf").toString() : "");
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
	public void ver_informe_venta(HttpServletRequest request, HttpServletResponse response, Long cod_arqcaj) {
		try {
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre = "arqueocaja_ventas_" + cod_arqcaj + "_" + gestion, tipo = "pdf", estado = "inline";
			ArqueoTotal totales = arqueocajaS.obtenerTotal(cod_arqcaj);
			String reportUrl = "/Reportes/arqueocaja_ventas.jasper";
			Map<String, Object> arqueo = arqueocajaS.obtener(cod_arqcaj);
			Map<String, Object> parametros = new HashMap<String, Object>();
			Utils.loadDataReport(parametros, gestion);
			parametros.put("usuario", us.toString());
			parametros.put("cod_arqcaj", cod_arqcaj);
			parametros.put("delegado", arqueo.get("delegado").toString());
			parametros.put("cusini", arqueo.get("custodio_inicial").toString());
			parametros.put("cusfin", arqueo.get("custodio_final").toString());
			parametros.put("estado", Boolean.parseBoolean(arqueo.get("est_arqcaj").toString()) ? "Activo" : "Inactivo");
			parametros.put("fini", arqueo.get("fechai").toString());
			parametros.put("ffin", arqueo.get("fechaf") != null ? arqueo.get("fechaf").toString() : "");
			parametros.put("monrea",
					arqueo.get("monrea_arqcaj") != null ? Float.parseFloat(arqueo.get("monrea_arqcaj").toString())
							: null);
			parametros.put("monini", Float.parseFloat(arqueo.get("monini_arqcaj").toString()));
			parametros.put("monfin",
					arqueo.get("monfin_arqcaj") != null ? Float.parseFloat(arqueo.get("monfin_arqcaj").toString())
							: null);
			parametros.put("gestion", gestion.getGes_gen());
			parametros.put("ingresos", totales.getTingresos());
			Float egresoTotal = totales.getTegresos() + totales.getTcompras();
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
	public void ver_detalle(HttpServletRequest request, HttpServletResponse response, Long cod_arqcaj,
			Integer cod_detarq) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre = "Detalle arqueocaja_" + cod_arqcaj + "_" + cod_detarq + "_" + gestion, tipo = "pdf",
					estado = "inline";
			Persona us = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			String reportUrl = "/Reportes/detallearqueo_ver.jasper";
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("usuario", us.toString());
			parametros.put("cod_arqcaj", cod_arqcaj);
			parametros.put("cod_detarq", cod_detarq);
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
    DataResponse guardarDetalle(HttpServletRequest request, Model model,
                                DetalleArqueo detalle) throws IOException {
		try {
			Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if (user != null && gestion != null) {
				ArqueoCaja arqueo = arqueocajaS.arqueocaja_verificar_sesion_actual(user.getCod_per(), gestion.getCod_suc());
				if (arqueo != null) {
					detalle.setArqueoId(arqueo.getId());
					boolean status = arqueocajaS.adicionarDetalle(detalle) > 0;
					return new DataResponse(status, Utils.getSuccessFailedAdd("detalle de arqueo", status));
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
			ArqueoCaja arqueo = arqueocajaS.arqueocaja_verificar_sesion_actual(user.getCod_per(), gestion.getCod_suc());
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
                                              Long cod_arqcaj, Integer cod_detarq) throws IOException {
		try {
			if (arqueocajaS.eliminarDetalle(cod_arqcaj, cod_detarq)) {
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
			ArqueoCaja arqueo = arqueocajaS.arqueocaja_verificar_sesion_actual(user.getCod_per(), gestion.getCod_suc());
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
	DataResponse rehabilitar(HttpServletRequest request, Model model, Long cod)
			throws IOException {
		try {
			boolean status = arqueocajaS.rehabilitarArqueo(cod);
			return new DataResponse(status, Utils.getSuccessFailedEli("arqueo de caja", status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}