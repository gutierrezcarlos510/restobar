package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.ArqueoCaja;
import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.model.contable.AsientoContable;
import net.resultadofinal.micomercial.model.contable.CuentaContableGrupo;
import net.resultadofinal.micomercial.model.contable.UtilAsientoContable;
import net.resultadofinal.micomercial.model.wrap.LibroMayorSubcuenta;
import net.resultadofinal.micomercial.service.ArqueoCajaS;
import net.resultadofinal.micomercial.service.AsientoContableS;
import net.resultadofinal.micomercial.service.CuentaContableS;
import net.resultadofinal.micomercial.service.ProductoS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.GeneradorReportes;
import net.resultadofinal.micomercial.util.MyConstant;
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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/asiento/*")
public class AsientoContableC {
	
	@Autowired
	private AsientoContableS asientoContableS;
	@Autowired
	private ArqueoCajaS arqueocajaS;
	@Autowired
	private CuentaContableS cuentaContableS;
	@Autowired
	private ProductoS productoS;
	@Autowired
	private DataSource datasource;
	private static final Logger logger = LoggerFactory.getLogger(AsientoContableC.class);
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request, Model model) {
		General gestion= (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if(gestion == null) {
			model.addAttribute("msg","Sesion expirada, vuelva a loguearse.");
			return "principal/login"+ MyConstant.SYSTEM;
		}
		ArqueoCaja arqueoInicial = arqueocajaS.obtenerArqueoInicial(gestion.getGes_gen(), gestion.getCod_suc());
		if(arqueoInicial != null && arqueoInicial.getCusfin_arqcaj() !=null) {
			List<CuentaContableGrupo> cuentas = asientoContableS.listarSumaSaldo(gestion.getCod_suc(), gestion.getGes_gen());
			if(cuentas!= null && !cuentas.isEmpty()) {
				model.addAttribute("tdebe", cuentas.stream().mapToDouble(x->x.getTdebe().doubleValue()).sum());
				model.addAttribute("thaber", cuentas.stream().mapToDouble(x->x.getThaber().doubleValue()).sum());
				model.addAttribute("tdeudor", cuentas.stream().mapToDouble(x->x.getDeudor().doubleValue()).sum());
				model.addAttribute("tacreedor", cuentas.stream().mapToDouble(x->x.getAcreedor().doubleValue()).sum());
			}
			model.addAttribute("cuentas", cuentas);
			model.addAttribute("gestion", gestion);
			return "asiento-contable/gestion";
		}else {
			if(arqueoInicial != null){
				return "excepcion/registrar-caja-inicial-abierta";
			}else {
				return "excepcion/registrar-caja-inicial";
			}
		}
	}
	@RequestMapping("gestionLibroDiario")
	public String gestionLibroDiario(HttpServletRequest request, Model model) {
		General gestion= (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if(gestion == null) {
			model.addAttribute("msg","Sesion expirada, vuelva a loguearse.");
			return "principal/login"+ MyConstant.SYSTEM;
		}
		ArqueoCaja arqueoInicial = arqueocajaS.obtenerArqueoInicial(gestion.getGes_gen(), gestion.getCod_suc());
		if(arqueoInicial != null && arqueoInicial.getCusfin_arqcaj() !=null) {
			model.addAttribute("gestion",gestion.getGes_gen());
			return "asiento-contable/libro-diario";
		}else {
			if(arqueoInicial != null){
				return "excepcion/registrar-caja-inicial-abierta";
			}else {
				return "excepcion/registrar-caja-inicial";
			}
		}
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("listaLibroDiario")
	public @ResponseBody Map<?, ?> listaLibroDiario(HttpServletRequest request, Integer draw, Integer start, boolean estado,int length)throws IOException{
		String total;
		Map<String, Object> Data = new HashMap<String, Object>();
		String search = request.getParameter("search[value]");
		General gestion= (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		List<?> lista= asientoContableS.listarLibroDiario(start, estado, search, length, gestion.getGes_gen(), gestion.getCod_suc());
		try {
			total=((Map<String, Object>) lista.get(0)).get("tot").toString();			
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
	@RequestMapping("obtenerDetalle")
	public @ResponseBody
    DataResponse obtenerDetalle(Long cod) {
		try {
			return asientoContableS.obtenerDetalles(cod);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("adicionarIngresoEfectivo")
	public String adicionarIngresoEfectivo(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		return "asiento-contable/adicionarIngresoEfectivo";
	}
	@RequestMapping("adicionarIngresoGeneral")
	public String adicionarIngresoGeneral(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentas3",cuentaContableS.listarIngresoGeneral(true));
		return "asiento-contable/adicionarIngresoGeneral";
	}
	@RequestMapping("adicionarPagoSalario")
	public String adicionarPagoSalario(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentaDestino",cuentaContableS.obtenerCuentaSueldoSalario());
		return "asiento-contable/adicionarPagoSalario";
	}
	@RequestMapping("adicionarPagoAlquiler")
	public String adicionarPagoAlquiler(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentaDestino",cuentaContableS.obtenerCuentaAlquiler());
		return "asiento-contable/adicionarPagoAlquiler";
	}
	@RequestMapping("adicionarPagoServicioBasico")
	public String adicionarPagoServicioBasico(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentaDestino",cuentaContableS.obtenerCuentaServicioBasico());
		return "asiento-contable/adicionarPagoServicioBasico";
	}
	@RequestMapping("adicionarPagoTransporte")
	public String adicionarPagoTransporte(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentaDestino",cuentaContableS.obtenerCuentaFleteTransporte());
		return "asiento-contable/adicionarPagoTransporte";
	}
	@RequestMapping("adicionarPagoGastoGeneral")
	public String adicionarPagoTrasnporte(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentas3",cuentaContableS.listarEgresoGeneral(true));
		return "asiento-contable/adicionarPagoGastoGeneral";
	}
	@RequestMapping("adicionarPagoPrestamoBancario")
	public String adicionarPagoPrestamoBancario(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentas3",cuentaContableS.listarPrestamosBancarios(true));
		return "asiento-contable/adicionarPagoPrestamoBancario";
	}
	@RequestMapping("adicionarPagoPasivoGeneral")
	public String adicionarPagoPasivoGeneral(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentas3",cuentaContableS.listarPasivosGeneral(true));
		return "asiento-contable/adicionarPagoGastoGeneral";
	}
	@RequestMapping("adicionarPagoMaterialOficina")
	public String adicionarPagoMaterialOficina(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentaDestino",cuentaContableS.obtenerCuentaMaterialOficina());
		return "asiento-contable/adicionarPagoMaterialOficina";
	}
	@RequestMapping("adicionarPagoServicioLimpieza")
	public String adicionarPagoServicioLimpieza(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentaDestino",cuentaContableS.obtenerCuentaServicioLimpieza());
		return "asiento-contable/adicionarPagoServicioLimpieza";
	}
	@RequestMapping("adicionarPagoEquipoComputacional")
	public String adicionarPagoEquipoComputacional(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentaDestino",cuentaContableS.obtenerCuentaEquipoComputacional());
		return "asiento-contable/adicionarPagoEquipoComputacional";
	}
	@RequestMapping("adicionarPagoMueble")
	public String adicionarPagoMueble(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentaDestino",cuentaContableS.obtenerCuentaMueble());
		return "asiento-contable/adicionarPagoMueble";
	}
	@RequestMapping("adicionarPagoInmueble")
	public String adicionarPagoInmueble(Model model){
		model.addAttribute("cuentas",cuentaContableS.listarBancos(true));
		model.addAttribute("cuentas2",cuentaContableS.listarCajas(true));
		model.addAttribute("cuentaDestino",cuentaContableS.obtenerCuentaInmueble());
		return "asiento-contable/adicionarPagoInmueble";
	}
	@RequestMapping("adicionarProductoPerdido")
	public String adicionarProductoPerdido(HttpServletRequest request,Model model)throws IOException{
//		model.addAttribute("productos",productoS.listar(-1, true, "", 0));
		return "asiento-contable/adicionarProductoPerdido";		
	}
	@RequestMapping("guardarProductoPerdido")
	public @ResponseBody
    DataResponse guardarProductoPerdido(HttpServletRequest request, Model model, Integer productos[], Integer cantidades[], Float precios[], Float subtotales[], String concepto, Float subtotal)throws IOException{
				
		try {
			Persona user=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion =(General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if(user != null && gestion!=null) {
				AsientoContable asiento = new AsientoContable();
				asiento.setCodSuc(gestion.getCod_suc());
				asiento.setGesGen(gestion.getGes_gen());
				asiento.setConcepto(concepto);
				asiento.setFecha(java.sql.Date.valueOf(LocalDate.now()));
				asiento.setCreatedBy(user.getNom_per()+" "+user.getPriape_per());
				asiento.setCreatedAt(new Timestamp(System.currentTimeMillis()));
				
				UtilAsientoContable utilAsiento = new UtilAsientoContable();
				Integer destino = cuentaContableS.obtenerCuentaPerdidaProducto();
				Integer origen = cuentaContableS.obtenerCuentaCompra();
				utilAsiento.addTransactionSimple(destino, origen, subtotal);
				Long codAsiento = asientoContableS.adicionarAsientoProductoPerdido(asiento, utilAsiento.getVectorCuenta(), utilAsiento.getVectorDebe(), utilAsiento.getVectorHaber(),productos,cantidades);
				if(codAsiento>0) {
					return new DataResponse(true, "Se realizo con exito el registro de asiento contable.");
				}else {
					return new DataResponse(false, "No se logro registrar el asiento contable");
				}
			}else {
				return new DataResponse(false, "Sesiones no encontradas");
			}	
		} catch (Exception e) {
			return new DataResponse(false, "Excepcion al registrar asiento contable: "+e.getMessage());
		}
	}
	@RequestMapping("guardarAsientoEfectivo")
	public @ResponseBody
    DataResponse guardarAsientoEfectivo(HttpServletRequest request, Model model, int cuentaOrigen, int cuentaDestino, float monto, String concepto)throws IOException{
		try {
			Persona user=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion =(General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			
			if(user != null && gestion!=null) {
				AsientoContable asiento = new AsientoContable();
				asiento.setCodSuc(gestion.getCod_suc());
				asiento.setGesGen(gestion.getGes_gen());
				asiento.setConcepto(concepto);
				asiento.setFecha(java.sql.Date.valueOf(LocalDate.now()));
				asiento.setCreatedBy(user.getNom_per()+" "+user.getPriape_per());
				asiento.setCreatedAt(new Timestamp(System.currentTimeMillis()));
				
				UtilAsientoContable utilAsiento = new UtilAsientoContable();
				utilAsiento.addTransactionSimple(cuentaDestino, cuentaOrigen, monto);
				Long codAsiento = asientoContableS.adicionar(asiento, utilAsiento.getVectorCuenta(), utilAsiento.getVectorDebe(), utilAsiento.getVectorHaber());
				if(codAsiento>0) {
					return new DataResponse(true, "Se realizo con exito el registro de asiento contable.");
				}else {
					return new DataResponse(false, "No se logro registrar el asiento contable");
				}
			}else {
				return new DataResponse(false, "Sesiones no encontradas");
			}	
		} catch (Exception e) {
			return new DataResponse(false, "Excepcion al registrar asiento contable");
		}
			
	}
	@RequestMapping("verLibroMayor")
	public String verLibroMayor(HttpServletRequest request, Model model,Integer codSubcuenta) {
		General gestion= (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if(gestion == null) {
			model.addAttribute("msg","Sesion expirada, vuelva a loguearse.");
			return "principal/login"+ MyConstant.SYSTEM;
		}
		LibroMayorSubcuenta libroMayor = asientoContableS.obtenerLibroMayorSubcuenta(codSubcuenta, gestion.getGes_gen(), gestion.getCod_suc());
		if(libroMayor!=null) {
			Double tdebe = libroMayor.getTotalDebe();
			Double thaber = libroMayor.getTotalHaber();
			Double diffDebe = null;
			Double diffHaber = null;
			if(Double.compare(tdebe, thaber)!=0) {
				if(Double.compare(tdebe, thaber)>0) {
					diffDebe = tdebe-thaber;
				}else {
					diffHaber = thaber-tdebe;
				}
			}
			model.addAttribute("libroMayor",libroMayor);
			model.addAttribute("detalles",libroMayor.getDetalles());
			model.addAttribute("tdebe",tdebe);
			model.addAttribute("thaber",thaber);
			model.addAttribute("diffDebe",diffDebe);
			model.addAttribute("diffHaber", diffHaber);
		}
		
		return "asiento-contable/ver-libro-mayor";
	}
	@RequestMapping("verLibroDiario")
	public void verLibroDario(HttpServletRequest request,HttpServletResponse response,String fini, String ffin){
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre="libro_diario_"+fini+"_"+ffin,tipo="pdf",estado="inline";
			Persona us=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			String reportUrl="/Reportes/libro_diario.jasper";
			String subRep=getClass().getResource("/Reportes/libro_diario_subreport.jasper").toString();
			subRep = subRep.substring(0, subRep.lastIndexOf("/"))+"/libro_diario_subreport.jasper";
			Map<String, Object> parametros=new HashMap<String, Object>();
			parametros.put("usuario", us.toString());
			parametros.put("fini", fini);
			parametros.put("ffin", ffin);
			parametros.put("codsuc", gestion.getCod_suc());
			Utils.loadDataReport(parametros, gestion);
			parametros.put("path", subRep);
			GeneradorReportes generador=new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo,parametros,datasource.getConnection() ,nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error rep="+e.toString());
		}
	}
}
