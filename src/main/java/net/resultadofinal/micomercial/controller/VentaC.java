package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.*;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.math.BigDecimal;
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
	private static final int ROL_MESERO = 4;
	private static final long CLIENTE_INCOGNITO = 0;
	@RequestMapping("initParam")
	public @ResponseBody DataResponse initParam(HttpServletRequest request){
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		if(gestion != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("mesas", mesaS.listarMesasLibresPorSucursal(gestion.getCod_suc()));
			map.put("productos", almacenS.listarProductos(gestion.getCod_suc()));
			map.put("cartillaDiariaList", cartillaDiariaS.obtenerCartillaActivaSucursal(gestion.getCod_suc()));
			map.put("cliente", clienteS.obtener(CLIENTE_INCOGNITO));
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
			if (!usuario)
				cod_user = user.getCod_per();
			return ventaS.listado(request, estado,cod_user, gestion.getCod_suc(), tipo);
		} catch (Exception ex) {
			System.out.println("error lista arqueo de caja: "+ex.toString());
			ex.printStackTrace();
			return null;
		}
	}
	@RequestMapping("adicionar")
	public String adicionar(Model model,Boolean isMobil){
		model.addAttribute("meseros", usuarioS.listarUsuariosPorRol(ROL_MESERO));
		model.addAttribute("formas", formaPagoS.listAll());
		return "venta/adicionar-comanda";
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(HttpServletRequest request, Venta obj, Long productos[], Integer cantidades[], BigDecimal precios[],
						 BigDecimal descuentos[], BigDecimal subtotales[], BigDecimal totales[]){
		try {
			Persona usuario=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if(usuario != null && gestion != null) {
				obj.setUsuarioId(usuario.getCod_per());
				obj.setGestion(gestion.getGes_gen());
				obj.setSucursalId(gestion.getCod_suc());
				return ventaS.adicionar(obj,productos,cantidades,precios,descuentos,subtotales,totales);
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
    DataResponse eliminar(Long id){
		try {
			boolean status = ventaS.eliminar(id);
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
	@RequestMapping("ver")
	public void ver(HttpServletRequest request,HttpServletResponse response,Long id){
		try {
			List<DetalleVenta> detalles=ventaS.obtenerDetalle(id);
			BigDecimal descuento = new BigDecimal(0);
			for (DetalleVenta map : detalles) {
				descuento.add(map.getDescuento());
			}
			General general = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre="venta_"+id+"_"+general.getGes_gen(),tipo="pdf",estado="inline";
			Persona us=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			String reportUrl="/Reportes/venta_ver.jasper";
			String SubRep=getClass().getResource("/Reportes/venta_ver_subreporte.jasper").toString();
			Map<String, Object> parametros=new HashMap<String, Object>();
			parametros.put("usuario", us.toString());
			parametros.put("cod_ven", id);
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