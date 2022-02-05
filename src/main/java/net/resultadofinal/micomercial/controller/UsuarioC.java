package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Dato;
import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.model.Sucursal;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.*;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/usuario/*")
public class UsuarioC {
	
	@Autowired
	private UsuarioS usuarioS;
	@Autowired
	private DatoS datoS;
	@Autowired
	private SucursalS sucursalS;
	@Autowired
	private AvatarS avatarS;
	@Autowired
	private RolS rolS;
	private static final String ENTITY = "usuario";
	
	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		model.addAttribute("avatars", avatarS.listar());
		model.addAttribute("roles", rolS.listar());
		model.addAttribute("sucursales", sucursalS.listAll());
		return "usuario/gestion";
	}

	@RequestMapping("lista")
	public @ResponseBody
	DataTableResults<Persona> lista(HttpServletRequest request) throws IOException, SQLException {
		try {
			return usuarioS.listar(request);
		} catch (Exception ex) {
			System.out.println("error lista usuarios: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(Persona user,Integer sucursales[], Integer rolesField[])throws IOException{
		try {
			if(datoS.existeLogin(user.getLogDat())) { //Si no es valido
				return new DataResponse(false, "El nombre de usuario ya existe, ingresar uno nuevo.");
			}
			return usuarioS.adicionar(user,rolesField,sucursales);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(HttpServletRequest request, Model model, Persona obj)throws IOException{
		try {
			Persona user=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			if(usuarioS.modificar(obj)){
				if(user.getCod_per()==obj.getCod_per()) {
					return new DataResponse(true, usuarioS.obtener(obj.getCod_per()), Utils.successMod(ENTITY));
				}else {
					return new DataResponse(true, Utils.successMod(ENTITY));
				} 
			}else{
				return new DataResponse(false, Utils.failedMod(ENTITY));
			}
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request, Model model, Long cod_per)throws IOException{
		try {
			Persona us=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			if(us.getCod_per()==cod_per) {
				return new DataResponse(false, "No se puede eliminar al usuario sesionado");
			}else {
				boolean status = usuarioS.eliminar(cod_per);
				return new DataResponse(status, Utils.getSuccessFailedEli(ENTITY, status));
			}
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(HttpServletRequest request, Model model, Long cod_per)throws IOException{
		try {
			boolean status = usuarioS.activar(cod_per);
			return new DataResponse(status, Utils.getSuccessFailedAct(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("validar")
	public @ResponseBody
    DataResponse validarCi(HttpServletRequest request, String ci){
		try {
			return new DataResponse(usuarioS.validarCi(ci), "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody Map<String, Object> obtener(HttpServletRequest request,Long cod_per){
		Map<String, Object> Data = new HashMap<String, Object>();
		try {
			Persona us=usuarioS.obtener(cod_per);
			Map<String, Object> dat=datoS.obtenerDato(cod_per);
			List<Map<String, Object>> lista= usuarioS.obtenerRoles(cod_per);
			List<Sucursal> sucursalList = sucursalS.obtenerPorUsuario(cod_per);
			Data.put("sucursales", sucursalList);
			Data.put("data", us);
			Data.put("roles",lista);
			Data.put("datos", dat);
			Data.put("status", true);
		} catch (Exception e) {
			Data.put("status", false);
			Data.put("msg", "No se logro obtener los datos del usuario");
		}
		return Data;
	}
	@RequestMapping("guardarAsignacion")
	public @ResponseBody
    DataResponse guardarAsignacion(Integer roles[], Integer sucursales[], Dato d){
		try {
			sucursalS.asignarSucursal(d.getCod_per(), sucursales);
			boolean status = datoS.adicionarDatos(d,roles);
			return new DataResponse(status, Utils.getSuccessFailedAdd("asignacion de "+ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("guardarSucursales")
	public @ResponseBody
    DataResponse guardarSucursales(HttpServletRequest request, Integer sucursales[], Long cod_per){
		try {
			sucursalS.asignarSucursal(cod_per, sucursales);
			return new DataResponse(true, "Se realizo con exito la asignacion de sucursales para el usuario");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("cambiarClave")
	public @ResponseBody
    DataResponse cambiarClave(HttpServletRequest request, Dato d, String cla1){
		try {
			boolean status = datoS.cambiarDatos(d.getCod_per(), d.getLog_dat(), cla1);
			return new DataResponse(status, Utils.getSuccessFailedAdd("cambio de clave del "+ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("buscarci")
	public @ResponseBody
    DataResponse buscarci(HttpServletRequest request, String ci){
		try {
			return new DataResponse(true, usuarioS.buscarCi(ci), "Se realizo con exito la busqueda de C.I.");
		} catch (Exception e) {
			return new DataResponse(false, "No se logro realizar la busqueda de C.I");
		}
	}
	@RequestMapping("buscar_nombres")
	public @ResponseBody
    DataResponse buscar_nombres(HttpServletRequest request, String cad){
		try {
			return new DataResponse(true,usuarioS.buscar_nombres(cad),"Se realizo con exito la busqueda por nombre");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("buscarPorCelular")
	public @ResponseBody
    DataResponse buscarPorCelular(HttpServletRequest request, String celular){
		try {
			return new DataResponse(true, usuarioS.buscarPorTelefono(celular), "Se realizo con exito la busqueda de celular");
		} catch (Exception e) {
			return new DataResponse(false, "No se logro realizar la busqueda de C.I");
		}
	}
	
}