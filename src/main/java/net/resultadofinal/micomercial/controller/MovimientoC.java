package net.resultadofinal.micomercial.controller;

import com.google.gson.GsonBuilder;
import net.resultadofinal.micomercial.model.DetalleMovimiento;
import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Movimiento;
import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.model.wrap.VentaInfoWrap;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.MovimientoS;
import net.resultadofinal.micomercial.service.SucursalS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.GeneradorReportes;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.Utils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/movimiento/*")
public class MovimientoC {
	
	@Autowired
	private MovimientoS movimientoS;
	@Autowired
	private SucursalS sucursalS;
	@Autowired
	private DataSource dataSource;
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request, Model model){
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		model.addAttribute("sucursalSesionada", gestion.getCod_suc());
		return "movimiento/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Movimiento> listar(HttpServletRequest request, boolean estado) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return movimientoS.listado(request, estado, gestion.getCod_suc());
		} catch (Exception ex) {
			System.out.println("error lista movimientoes: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("adicionar")
	public String adicionar(HttpServletRequest request,Model model){
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		model.addAttribute("sucursalOrigen", gestion.getCod_suc());
		model.addAttribute("sucursales", sucursalS.listAll());
		return "movimiento/adicionar";
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(HttpServletRequest request,@RequestBody Movimiento obj){
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			obj.setSucursalOrigen(gestion.getCod_suc());
			obj.setCreatedBy(user.getCod_per());
			obj.setEstadoMovimiento((short)0);//Pendiente para revisar y confirmar
			return movimientoS.adicionar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request,Long movimientoId){
		try {
			Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			Movimiento obj = new Movimiento();
			obj.setId(movimientoId);
			obj.setUpdatedBy(user.getCod_per());
			return movimientoS.eliminar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("revisar")
	public @ResponseBody
	DataResponse revisar(HttpServletRequest request,Movimiento obj){
		try {
			Persona user = (Persona) request.getSession().getAttribute(MyConstant.Session.USER);
			obj.setUsuarioRevision(user.getCod_per());
			return movimientoS.revisar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Long movimientoId){
		try {
			Movimiento obj = movimientoS.obtener(movimientoId);
			boolean exist = obj != null;
			if(exist) {
				obj.setDetalles(movimientoS.obtenerDetalles(movimientoId));
			}
			return new DataResponse(exist, obj, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtenerDetalles")
	public @ResponseBody
	DataResponse obtenerDetalles(Long movimientoId){
		try {
			List<DetalleMovimiento> obj = movimientoS.obtenerDetalles(movimientoId);
			boolean exist = obj != null;
			return new DataResponse(exist, obj, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("ver")
	public void ver(HttpServletRequest request, HttpServletResponse response, Long id){
		try {
			General general = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre="movimiento_"+id+"_"+general.getGes_gen(),tipo="pdf",estado="inline";
			Persona us=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			String reportUrl="/Reportes/movimiento_ver.jasper";
			Movimiento movimiento = movimientoS.obtener(id);
			if(movimiento.getTipo() == 1) {
				reportUrl="/Reportes/movimiento_traspaso_ver.jasper";
			}
			Map<String, Object> parametros=new HashMap<String, Object>();
			parametros.put("usuario", us.toString());
			parametros.put("movimientoId", id);
			parametros.put("xusuarioRevision", movimiento.getXusuarioRevision());
			parametros.put("xcreatedBy", movimiento.getXcreatedBy());
			parametros.put("xcreatedAt", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a").format(movimiento.getCreatedAt()));
			parametros.put("xtipo", movimiento.getXtipo());
			parametros.put("xestadoMovimiento", movimiento.getXestadoMovimiento());
			parametros.put("xfechaRevision", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a").format(movimiento.getFechaRevision()));
			parametros.put("obs", movimiento.getObs());
			parametros.put("sucursalOrigen", movimiento.getXsucursalOrigen());
			parametros.put("xusuario", movimiento.getXsucursalDestino());
			Utils.loadDataReport(parametros, general);
			GeneradorReportes generador=new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo,parametros, dataSource.getConnection(),nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}