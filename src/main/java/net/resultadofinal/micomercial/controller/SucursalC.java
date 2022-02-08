package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.NotificacionSucursalWrap;
import net.resultadofinal.micomercial.model.Sucursal;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.SucursalS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/sucursal/*")
public class SucursalC {
	
	@Autowired
	private SucursalS sucursalS;
	private static final String ENTITY = "sucursal";
	
	@RequestMapping("gestion")
	public String gestion(Model model){
		model.addAttribute("sucursales", sucursalS.listAll());
		return "sucursal/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Sucursal> listar(HttpServletRequest request, boolean estado) {
		try {
			return sucursalS.listado(request, estado);
		} catch (Exception ex) {
			System.out.println("error lista sucursales: "+ex.toString());
			return null;
		}
	}

	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar( Sucursal s)throws IOException{
		try {
			return sucursalS.adicionar(s);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(Sucursal s)throws IOException{
		try {
			return sucursalS.modificar(s);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Integer cod_suc)throws IOException{
		try {
			boolean status =  sucursalS.darEstado(cod_suc, false);
			return new DataResponse(status, Utils.getSuccessFailedEli(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(HttpServletRequest request, Model model, Integer cod_suc)throws IOException{
		try {
			boolean status =  sucursalS.darEstado(cod_suc, true);
			return new DataResponse(status, Utils.getSuccessFailedAct(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Integer cod_suc){
		try {
			return new DataResponse(true, sucursalS.obtener(cod_suc), Utils.successGet(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("adicionarNotificacion")
	public @ResponseBody DataResponse adicionarNotificacion(Sucursal obj){
		try {
			sucursalS.adicionarNotificacion(obj.getTituloNotificacion(),obj.getMensajeNotificacion(),obj.getCod_suc());
			return new DataResponse(true,"Se realizo con exito el registro de la notificacion para la sucursal.");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("adicionarNotificaciones")
	public @ResponseBody DataResponse adicionarNotificaciones(String titulo, String mensaje, Integer sucursales[]){
		try {
			sucursalS.adicionarNotificaciones(titulo, mensaje, sucursales);
			return new DataResponse(true, "Se realizo con exito el registro de notificacion para las sucursales");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminarNotificacion")
	public @ResponseBody DataResponse eliminarNotificacion(Integer cod_suc){
		try {
			sucursalS.eliminarNotificacion(cod_suc);
			return new DataResponse(true, "Se realizo con exito la eliminacion de notificacion");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminarNotificaciones")
	public @ResponseBody DataResponse eliminarNotificaciones(Integer sucursales[]){
		try {
			sucursalS.eliminarNotificaciones(sucursales);
			return new DataResponse(true, "Se realizo con exito la eliminacion de notificaciones");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@GetMapping("obtenerNotificaciones")
	public @ResponseBody DataResponse obtenerNotificaciones(){
		try {
			List<NotificacionSucursalWrap> lista = sucursalS.listarNotificaciones();
			return new DataResponse(true, lista,"Se realizo con exito la eliminacion de notificaciones");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@GetMapping("obtenerNotificacionPorSucursal")
	public @ResponseBody
	NotificacionSucursalWrap obtenerNotificacionPorSucursal(HttpServletRequest request){
		try {
			Sucursal sucursal = (Sucursal) request.getSession().getAttribute(MyConstant.Session.SUCURSAL);
			if(sucursal != null) {
				return sucursalS.obtenerNotificacionPorSucursal(sucursal.getCod_suc());
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
}