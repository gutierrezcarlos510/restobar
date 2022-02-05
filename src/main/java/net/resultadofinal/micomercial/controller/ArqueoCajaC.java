package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.*;
import net.resultadofinal.micomercial.model.contable.AsientoContable;
import net.resultadofinal.micomercial.model.contable.UtilAsientoContable;
import net.resultadofinal.micomercial.model.wrap.ArqueoWrap;
import net.resultadofinal.micomercial.model.wrap.CompraVentaWrap;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.*;
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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
	private CuentaContableS cuentaContableS;
	@Autowired
	private AsientoContableS asientoS;
	@Autowired
	DataSource datasource;
	private static final int SESION_ACTIVA = 1;

	private static final Logger logger = LoggerFactory.getLogger(ArqueoCajaC.class);
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request, Model model) {
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if (gestion != null) {
			ArqueoCaja arqueoInicial = arqueocajaS.obtenerArqueoInicial(gestion.getGes_gen(), gestion.getCod_suc());
			if (arqueoInicial != null && arqueoInicial.getCusfin_arqcaj() != null) {
				model.addAttribute("usuarios", usuarioS.listarUsuariosSistema());
				model.addAttribute("cuentas", cuentaContableS.listarCajas(true));
				model.addAttribute("cuentas2", cuentaContableS.listarBancos(true));
				return "arqueocaja/gestion"+ MyConstant.SYSTEM;
			} else {
				if (arqueoInicial != null) {
					model.addAttribute("sesion", SESION_ACTIVA);
					model.addAttribute("arqueo", arqueoInicial);
					model.addAttribute("cod_arqcaj", arqueoInicial.getCod_arqcaj());
					model.addAttribute("fecha", arqueoInicial.getFini_arqcaj().substring(0, 19));
					model.addAttribute("estado", 1);
					return "arqueocaja/caja-inicial-abierta";
				} else {
					return "arqueocaja/caja-inicial-registrar";
				}
			}

		}
		return "principal/login"+ MyConstant.SYSTEM;
	}

	@RequestMapping("adicionarArqueoInicial")
	public String adicionarArqueoInicial(HttpServletRequest request, Model model) {
		// Proceso de iniciar caja inicial
		Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		ArqueoCaja arqueo = new ArqueoCaja();
		arqueo.setDes_arqcaj("Apertura de caja inicial de la gestion " + gestion.getGes_gen());
		arqueo.setMonini_arqcaj(0f);
		arqueo.setDel_arqcaj(usuario.getCod_per());
		arqueo.setCusini_arqcaj(1L);
		arqueo.setGes_gen(gestion.getGes_gen());
		arqueo.setCod_suc(gestion.getCod_suc());
		Long cod = arqueocajaS.iniciar(arqueo);
		model.addAttribute("sesion", SESION_ACTIVA);
		model.addAttribute("arqueo", arqueocajaS.obtener(cod));
		model.addAttribute("cod_arqcaj", cod);
		model.addAttribute("fecha", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		model.addAttribute("estado", 1);
		return "arqueocaja/caja-inicial-abierta";
	}

	@RequestMapping("gestion_detalle")
	public String gestionDetalle(HttpServletRequest request, Model model, Long cod_arqcaj) {
		Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if (user != null && gestion != null) {
			if(cod_arqcaj != null){
				ArqueoCaja arqueo = arqueocajaS.obtenerCaja(cod_arqcaj);
				model.addAttribute("esPropioCajero",(arqueo.getDel_arqcaj() == user.getCod_per())?1:0);
				model.addAttribute("cod_arqcaj", cod_arqcaj);
				return "arqueocaja/gestion_detalle";
			}
			ArqueoCaja arqueoInicial = arqueocajaS.obtenerArqueoInicial(gestion.getGes_gen(), gestion.getCod_suc());
			if (arqueoInicial != null && arqueoInicial.getCusfin_arqcaj() != null) {
				ArqueoCaja arqueo = arqueocajaS.arqueocaja_verificar_sesion_actual(user.getCod_per(),
						gestion.getCod_suc());
				if (arqueo == null) {
					model.addAttribute("cod_arqcaj", 0);
					model.addAttribute("fecha", new Fechas().obtenerFechaActual("dd/MM/yyyy"));
				} else {
					model.addAttribute("arqueo", arqueo);
					model.addAttribute("cod_arqcaj", arqueo.getCod_arqcaj());
					model.addAttribute("fecha", arqueo.getFini_arqcaj().substring(0, 19));
					model.addAttribute("estado", 1);
				}
				return "arqueocaja/gestion_detalle";
			} else {
				if (arqueoInicial != null) {
					model.addAttribute("arqueo", arqueoInicial);
					model.addAttribute("cod_arqcaj", arqueoInicial.getCod_arqcaj());
					model.addAttribute("fecha", arqueoInicial.getFini_arqcaj().substring(0, 19));
					model.addAttribute("estado", 1);
					return "arqueocaja/caja-inicial-abierta";
				} else {
					return "arqueocaja/caja-inicial-registrar";
				}
			}
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
			ArqueoCaja arqueoInicial = arqueocajaS.obtenerArqueoInicial(gestion.getGes_gen(), gestion.getCod_suc());
			if (arqueoInicial != null && arqueoInicial.getCusfin_arqcaj() != null) {
				ArqueoCaja arqueo = arqueocajaS.arqueocaja_verificar_sesion_actual(user.getCod_per(),
						gestion.getCod_suc());
				if (arqueo == null) {
					model.addAttribute("cod_arqcaj", 0);
					model.addAttribute("fecha", new Fechas().obtenerFechaActual("dd/MM/yyyy"));
				} else {
					model.addAttribute("arqueo", arqueo);
					model.addAttribute("cod_arqcaj", arqueo.getCod_arqcaj());
					model.addAttribute("fecha", arqueo.getFini_arqcaj().substring(0, 19));
					model.addAttribute("estado", 1);
				}
			} else {
				if (arqueoInicial != null) {
					model.addAttribute("arqueo", arqueoInicial);
					model.addAttribute("cod_arqcaj", arqueoInicial.getCod_arqcaj());
					model.addAttribute("fecha", arqueoInicial.getFini_arqcaj().substring(0, 19));
					model.addAttribute("estado", 1);
					return "arqueocaja/caja-inicial-abierta";
				} else {
					return "arqueocaja/caja-inicial-registrar";
				}
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
			arqueo.setCusini_arqcaj(usuario.getCod_per());// custodio sin nombre
			arqueo.setGes_gen(gestion.getGes_gen());
			arqueo.setCod_suc(gestion.getCod_suc());
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
			arqueo.setDel_arqcaj(usuario.getCod_per());
			arqueo.setCusini_arqcaj(1L);
			arqueo.setGes_gen(gestion.getGes_gen());
			arqueo.setCod_suc(gestion.getCod_suc());
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
			arqueo.setGes_gen(gestion.getGes_gen());
			arqueo.setCod_suc(gestion.getCod_suc());
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
			arqueo.setCusfin_arqcaj(usuario.getCod_per());
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
			arqueo.setCusfin_arqcaj(MyConstant.ArqueoCaja.USER_ADMIN);
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
			Float montoReal = arqueo.getMonrea_arqcaj() != null ? arqueo.getMonrea_arqcaj() : null;
			Float montoFinal = arqueo.getMonfin_arqcaj() != null ? arqueo.getMonfin_arqcaj() : null;
			String interpretacion = "";
			if (montoReal != null && montoFinal != null) {
				int resp = Float.compare(montoReal, montoFinal);
				if (resp == 0) {
					interpretacion = "Cierre de caja Correcto";
				} else {
					if (resp > 0) {
						interpretacion = "Ganancia extra en caja diaria: " + (montoReal - montoFinal);
					} else {
						interpretacion = "Perdida extra en caja diaria: " + (montoFinal - montoReal);
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
			ArqueoCaja arqueo = arqueocajaS.obtenerCaja(arqueoUsuario.getCod_arqcaj());
			Map<String, Object> parametros = new HashMap<String, Object>();
			Float montoReal = arqueo.getMonrea_arqcaj() != null ? arqueo.getMonrea_arqcaj() : null;
			Float montoFinal = arqueo.getMonfin_arqcaj() != null ? arqueo.getMonfin_arqcaj() : null;
			String interpretacion = "";
			if (montoReal != null && montoFinal != null) {
				int resp = Float.compare(montoReal, montoFinal);
				if (resp == 0) {
					interpretacion = "Cierre de caja Correcto";
				} else {
					if (resp > 0) {
						interpretacion = "Ganancia extra en caja diaria: " + (montoReal - montoFinal);
					} else {
						interpretacion = "Perdida extra en caja diaria: " + (montoFinal - montoReal);
					}
				}
			} else {
				interpretacion = "Cierre de caja pendiente.";
			}
			parametros.put("usuario", us.toString());
			parametros.put("cod_arqcaj", arqueo.getCod_arqcaj());
			commonVer(request, response, gestion, nombre, tipo, estado, reportUrl, arqueo, parametros, montoReal, montoFinal, interpretacion);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error de reporte caja=" + e.toString());
		}
	}

	private void commonVer(HttpServletRequest request, HttpServletResponse response, General gestion, String nombre, String tipo, String estado, String reportUrl, ArqueoCaja arqueo, Map<String, Object> parametros, Float montoReal, Float montoFinal, String interpretacion) throws JRException, SQLException, IOException {
		Utils.loadDataReport(parametros, gestion);
		parametros.put("delegado", arqueo.getDelegado());
		parametros.put("cusini", arqueo.getCustodio_inicial());
		parametros.put("cusfin", arqueo.getCustodio_final());
		parametros.put("estado", arqueo.getEst_arqcaj() ? "Activo" : "Inactivo");
		parametros.put("fini", arqueo.getFechai());
		parametros.put("ffin", arqueo.getFechaf() != null ? arqueo.getFechaf() : "");
		parametros.put("monrea", montoReal);
		parametros.put("monini", arqueo.getMonini_arqcaj());
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
					detalle.setCod_arqcaj(arqueo.getCod_arqcaj());
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

	@RequestMapping("adicionarCaja")
	public String adicionarCaja(Model model) {
		model.addAttribute("cuentas", cuentaContableS.listarCajas(true));
		model.addAttribute("tipo", MyConstant.Caja.CAJA_GENERAL);
		return "arqueocaja/adicionarCaja";
	}

	@RequestMapping("adicionarBanco")
	public String adicionarBanco(Model model) {
		model.addAttribute("cuentas", cuentaContableS.listarBancos(true));
		model.addAttribute("tipo", MyConstant.Caja.BANCO_GENERAL);
		return "arqueocaja/adicionarBanco";
	}

	@RequestMapping("adicionarMueble")
	public String adicionarMueble(Model model) {
		model.addAttribute("tipo", MyConstant.Caja.MUEBLE);
		model.addAttribute("subcuenta", cuentaContableS.obtenerCuentaMueble());
		return "arqueocaja/adicionarMueble";
	}

	@RequestMapping("adicionarInmueble")
	public String adicionarInmueble(Model model) {
		model.addAttribute("tipo", MyConstant.Caja.INMUEBLE);
		model.addAttribute("subcuenta", cuentaContableS.obtenerCuentaInmueble());
		return "arqueocaja/adicionarInmueble";
	}

	@RequestMapping("adicionarActivo")
	public String adicionarActivo(Model model) {
		model.addAttribute("cuentas", cuentaContableS.listarActivosGenerales(true));
		model.addAttribute("tipo", MyConstant.Caja.OTRO_ACTIVO);
		return "arqueocaja/adicionarActivo";
	}

	@RequestMapping("adicionarPrestamo")
	public String adicionarPrestamo(Model model) {
		model.addAttribute("cuentas", cuentaContableS.listarPrestamosBancarios(true));
		model.addAttribute("tipo", MyConstant.Caja.PRESTAMO_BANCARIO);
		return "arqueocaja/adicionarPrestamo";
	}

	@RequestMapping("adicionarPasivo")
	public String adicionarPasivo(Model model) {
		model.addAttribute("cuentas", cuentaContableS.listarPasivosGeneral(true));
		model.addAttribute("tipo", MyConstant.Caja.OTRO_PASIVO);
		return "arqueocaja/adicionarPasivo";
	}

	@RequestMapping("adicionarIngreso")
	public String adicionarIngreso(Model model) {
		model.addAttribute("cuentas", cuentaContableS.listarIngresoGeneral(true));
		model.addAttribute("cuentas2", cuentaContableS.listarCajas(true));
		model.addAttribute("cuentas3", cuentaContableS.listarBancos(true));
		model.addAttribute("tipo", MyConstant.Caja.INGRESO_GENERAL);
		return "arqueocaja/adicionarIngreso";
	}

	@RequestMapping("adicionarEgreso")
	public String adicionarEgreso(Model model) {
		model.addAttribute("cuentas", cuentaContableS.listarEgresoGeneral(true));
		model.addAttribute("tipo", MyConstant.Caja.EGRESO_GENERAL);
		return "arqueocaja/adicionarEgreso";
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
				detalle.setCod_arqcaj(arqueo.getCod_arqcaj());
				if (detalle.getDes_detarq() != null && !detalle.getDes_detarq().isEmpty()) {
					detalle.setDes_detarq("Transaccion de apertura de inicial." + detalle.getDes_detarq());
				} else {
					detalle.setDes_detarq("Transaccion de apertura de inicial.");
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

	@RequestMapping("finalizar")
	public @ResponseBody
    DataResponse finalizarCajainicial(HttpServletRequest request, Model model, ArqueoCaja arqueo)
			throws IOException {
		try {
			Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if (usuario != null && gestion != null) {
				arqueo.setCusfin_arqcaj(usuario.getCod_per());
				AsientoContable asiento = new AsientoContable();
				asiento.setCodSuc(gestion.getCod_suc());
				asiento.setConcepto(
						"Apertura inicial de la empresa " + gestion.getNom_gen() + "-" + gestion.getGes_gen());
				asiento.setCreatedBy(usuario.getNom_per() + " " + usuario.getPriape_per());
				asiento.setCreatedAt(new Timestamp(System.currentTimeMillis()));
				asiento.setFecha(java.sql.Date.valueOf(LocalDate.now()));
				asiento.setGesGen(gestion.getGes_gen());
				List<DetalleArqueo> lista = arqueocajaS.obtenerDetallexArqueoCaja(arqueo.getCod_arqcaj());
				if (lista != null) {
					Integer cuentas[] = new Integer[lista.size() + 1];
					Float debes[] = new Float[lista.size() + 1];
					Float haberes[] = new Float[lista.size() + 1];
					Float tactivos = 0f;
					Float tpasivos = 0f;
					int ind = 0;
					for (DetalleArqueo det : lista) {
						if (det.getCodSubcuenta() != null) {
							if (det.getTip_detarq() == 14 || det.getTip_detarq() == 15) {
								tpasivos += det.getMon_detarq();
								debes[ind] = 0f;
								haberes[ind] = det.getMon_detarq();
							} else {
								tactivos += det.getMon_detarq();
								debes[ind] = det.getMon_detarq();
								haberes[ind] = 0f;
							}
							cuentas[ind] = det.getCodSubcuenta();

						} else {
							if (det.getTip_detarq() == 4) {// Inventario
								cuentas[ind] = cuentaContableS.obtenerCuentaInventario();
								tactivos += det.getMon_detarq();
								debes[ind] = det.getMon_detarq();
								haberes[ind] = 0f;
							}
						}
						ind++;
					}
					cuentas[ind] = cuentaContableS.obtenerCuentaCapital();
					debes[ind] = 0f;
					haberes[ind] = tactivos - tpasivos;
					// Crear una cuenta de capital con la formula Capital = Activo - Pasivo

					Long codAsiento = asientoS.adicionar(asiento, cuentas, debes, haberes);
					// Datos para cerrar caja
					arqueo.setDes_arqcaj("Cierre de caja inicial del sistema. Gestion " + gestion.getGes_gen());
					arqueo.setCusfin_arqcaj(usuario.getCod_per());
					arqueo.setMonfin_arqcaj(0f);
					arqueo.setMonrea_arqcaj(0f);
					arqueo.setCodAsiento(codAsiento);
					if (arqueocajaS.cerrarCajaInicial(arqueo)) {
						return new DataResponse(true, "Se realizo con exito la finalizacion de caja");
					} else {
						return new DataResponse(false, "Transaccion fallida de la finalizacion de caja");
					}
				}
			}
			return new DataResponse(false, "Transaccion fallida, no se logro realizar el cierre de caja.");
		} catch (Exception e) {
			return new DataResponse(false, "Error de excepcion, " + e.getMessage());
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
				detalle.setCod_arqcaj(arqueo.getCod_arqcaj());
				boolean status = arqueocajaS.adicionarDetalle(detalle) > 0;
				return new DataResponse(status, Utils.getSuccessFailedAdd("arqueo de caja normal ", status));
			} else
				return new DataResponse(false, "No se logro obtener el arqueo de caja");
		}else {
			return new DataResponse(false, "Sesion expirada del usuario");
		}
	}

	@RequestMapping("registrarAsientoContable")
	public @ResponseBody
    DataResponse registrarAsientoContable(HttpServletRequest request, Model model,
                                          ArqueoCaja arqueo, Integer codeSubcuenta) throws IOException {
		try {
			Persona usuario = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if (usuario != null && gestion != null) {
				ArqueoCaja current = arqueocajaS.obtenerCaja(arqueo.getCod_arqcaj());
				arqueo.setCusfin_arqcaj(usuario.getCod_per());
				AsientoContable asiento = new AsientoContable();
				asiento.setCodSuc(gestion.getCod_suc());
				asiento.setConcepto("Registro de caja diaria numero: " + current.getCod_arqcaj() + ". Fecha: "
						+ current.getFini_arqcaj().substring(0, 10));
				asiento.setCreatedBy(usuario.getNom_per() + " " + usuario.getPriape_per());
				asiento.setCreatedAt(new Timestamp(System.currentTimeMillis()));
				asiento.setFecha(java.sql.Date.valueOf(LocalDate.now()));
				asiento.setGesGen(gestion.getGes_gen());
				List<DetalleArqueo> lista = arqueocajaS.obtenerDetallexArqueoCajaAgrupado(arqueo.getCod_arqcaj());
				if (lista != null) {
					UtilAsientoContable utilAsiento = new UtilAsientoContable();
					Float totalCajaDiaria = 0f;
					for (DetalleArqueo det : lista) {
						if (det.getCodSubcuenta() != null) {
							if (det.getTip_detarq() == 16) {// Otros ingresos
								totalCajaDiaria += det.getMon_detarq();// ingresa a caja
								utilAsiento.addTransactionSimple(cuentaContableS.obtenerCuentaCajaDiaria(),
										det.getCodSubcuenta(), det.getMon_detarq());
							}
							if (det.getTip_detarq() == 17) {// Otros egreso
								totalCajaDiaria -= det.getMon_detarq();
								utilAsiento.addTransactionSimple(det.getCodSubcuenta(),
										cuentaContableS.obtenerCuentaCajaDiaria(), det.getMon_detarq());
							}

							if (det.getTip_detarq() == 20) {
								totalCajaDiaria += det.getMon_detarq();
								CompraVentaWrap descuentoVenta = arqueocajaS
										.obtenerDescuentoVentaPorBanco(arqueo.getCod_arqcaj(), det.getCodSubcuenta());
								if (descuentoVenta != null) {
									utilAsiento.addDetalle(det.getCodSubcuenta(), det.getMon_detarq(), true);
									if (descuentoVenta.getTotal().floatValue() > 0) {
										asiento.setConcepto(asiento.getConcepto() + " Con descuentos en las ventas: "
												+ descuentoVenta.getLista() + ".");
										utilAsiento.addDetalle(cuentaContableS.obtenerCuentaDescuentoPorVenta(),
												descuentoVenta.getTotal(), true);
									}
									utilAsiento.addDetalle(cuentaContableS.obtenerCuentaVenta(),
											det.getMon_detarq() + descuentoVenta.getTotal(), false);// Es el total de
																									// ingreso con el
																									// total de
																									// descuento
								} else {
									utilAsiento.addTransactionSimple(det.getCodSubcuenta(),
											cuentaContableS.obtenerCuentaVenta(), det.getMon_detarq());
								}
							}
						} else {
							if (det.getTip_detarq() == 4) {// compras
								totalCajaDiaria -= det.getMon_detarq();
								CompraVentaWrap descuentoCompra = arqueocajaS
										.obtenerDescuentoCompra(arqueo.getCod_arqcaj());
								if (descuentoCompra != null) {
									if (descuentoCompra.getTotal().floatValue() > 0) {
										asiento.setConcepto(asiento.getConcepto() + " Con descuentos en las compras: "
												+ descuentoCompra.getLista() + ".");
										utilAsiento.addDetalle(cuentaContableS.obtenerCuentaCompra(),
												det.getMon_detarq() + descuentoCompra.getTotal(), true);
									} else {
										utilAsiento.addDetalle(cuentaContableS.obtenerCuentaCompra(),
												det.getMon_detarq(), true);
									}
									utilAsiento.addDetalle(cuentaContableS.obtenerCuentaCajaDiaria(),
											det.getMon_detarq(), false);
									if (descuentoCompra.getTotal().floatValue() > 0) {
										utilAsiento.addDetalle(cuentaContableS.obtenerCuentaDescuentoPorCompra(),
												descuentoCompra.getTotal(), false);// Agregamos un detalle para el
																					// descuento de la compra
									}
								} else {// Si no se encuentra el descuento de la compra, se registra en base al detalle de arqueo
									utilAsiento.addTransactionSimple(cuentaContableS.obtenerCuentaCompra(),
											cuentaContableS.obtenerCuentaCajaDiaria(), det.getMon_detarq());
								}
							}
							if (det.getTip_detarq() == 8) {// ventas
								totalCajaDiaria += det.getMon_detarq();
								CompraVentaWrap descuentoVenta = arqueocajaS.obtenerDescuentoVenta(arqueo.getCod_arqcaj());
								if (descuentoVenta != null) {
									utilAsiento.addDetalle(cuentaContableS.obtenerCuentaCajaDiaria(),det.getMon_detarq(), true);
									if (descuentoVenta.getTotal().floatValue() > 0) {
										asiento.setConcepto(asiento.getConcepto() + " Con descuentos en las ventas: "+ descuentoVenta.getLista() + ".");
										utilAsiento.addDetalle(cuentaContableS.obtenerCuentaDescuentoPorVenta(),descuentoVenta.getTotal(), true);
									}
									utilAsiento.addDetalle(cuentaContableS.obtenerCuentaVenta(),
											det.getMon_detarq() + descuentoVenta.getTotal(), false);// Es el total de ingreso con el total de descuento
								} else {
									utilAsiento.addTransactionSimple(cuentaContableS.obtenerCuentaCajaDiaria(),
											cuentaContableS.obtenerCuentaVenta(), det.getMon_detarq());
								}

							}
						}
					}
					// Verificar si hay perdidas o ganancias en caja diaria
					if (!current.getMonfin_arqcaj().equals(current.getMonrea_arqcaj().floatValue())) {
						if (Float.compare(current.getMonfin_arqcaj(), current.getMonrea_arqcaj()) > 0) {// Hay perdida
																										// de dinero
							utilAsiento.addTransactionSimple(cuentaContableS.obtenerCuentaPerdidaDineroCajaDiaria(),
									cuentaContableS.obtenerCuentaCajaDiaria(),
									current.getMonfin_arqcaj() - current.getMonrea_arqcaj());
						} else {// Hay ganancia de dinero caja diaria
							utilAsiento.addTransactionSimple(cuentaContableS.obtenerCuentaGananciaDineroCajaDiaria(),
									cuentaContableS.obtenerCuentaCajaDiaria(),
									current.getMonrea_arqcaj() - current.getMonfin_arqcaj());
						}
					}

					Long codAsiento = asientoS.adicionar(asiento, utilAsiento.getVectorCuenta(),
							utilAsiento.getVectorDebe(), utilAsiento.getVectorHaber());
					// Datos para registro de arqueo con asiento contable
					arqueocajaS.registrarArqueoAsiento(arqueo.getCod_arqcaj(), codAsiento);
					// Registrar Asiento de Transferencia de caja diaria a caja seleccionada
					utilAsiento.clear();
					utilAsiento.addTransactionSimple(codeSubcuenta, cuentaContableS.obtenerCuentaCajaDiaria(),
							current.getMonrea_arqcaj());

					asiento.setConcepto("Transferencia del efectivo de la caja diaria #: " + current.getCod_arqcaj()
							+ ". Fecha: " + current.getFini_arqcaj().substring(0, 10));

					asientoS.adicionar(asiento, utilAsiento.getVectorCuenta(), utilAsiento.getVectorDebe(),
							utilAsiento.getVectorHaber());
					return new DataResponse(true, "Se registro con exito");
				}
			}
			return new DataResponse(false, "Transaccion fallida, no se logro realizar el cierre de caja.");
		} catch (Exception e) {
			return new DataResponse(false, "Error de excepcion, " + e.getMessage());
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