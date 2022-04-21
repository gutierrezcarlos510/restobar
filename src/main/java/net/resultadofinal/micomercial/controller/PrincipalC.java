package net.resultadofinal.micomercial.controller;


import net.resultadofinal.micomercial.model.*;
import net.resultadofinal.micomercial.service.MenuS;
import net.resultadofinal.micomercial.service.RolS;
import net.resultadofinal.micomercial.service.SucursalS;
import net.resultadofinal.micomercial.service.UsuarioS;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.Fechas;
import net.resultadofinal.micomercial.util.UtilClass;
import net.resultadofinal.micomercial.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/principal/*")
public class PrincipalC {
	@Autowired
	private UsuarioS usuarioS;
	@Autowired
	private MenuS menuS;
	@Autowired
	private RolS rolS;
	@Autowired
	private SucursalS sucursalS;
	
	private static final Logger logger = LoggerFactory.getLogger(PrincipalC.class);

	@RequestMapping("login")
	public String login(Model model){
		model.addAttribute("msg","Sistema de Administraci&oacute;n y Control Comercial");
		return "principal/login" + MyConstant.SYSTEM;
	}
	public General getGeneralNow(List<GeneralWrap> generalWrapList) {
		int yearNow = Fechas.getYearNow();
		if(generalWrapList != null && !generalWrapList.isEmpty()) {
			for(GeneralWrap item : generalWrapList){
				if(item.getGeneralList() != null && !item.getGeneralList().isEmpty()) {
					for(General subitem : item.getGeneralList()){
						if(subitem.getGes_gen() == yearNow) {
							return subitem;
						}
					};
				}
			};
		}
		return null;
	}
	@RequestMapping("validar")
	public String validar(HttpServletRequest request,Model model,String login,String password){
		try {
			Persona usuario=usuarioS.iniciarSesion(login, password);
			if(usuario==null){
				model.addAttribute("msg","Usuario incorrecto, por favor verifique login y clave.");
				return "principal/login" + MyConstant.SYSTEM;
			}else{
				List<GeneralWrap> misSucursales = usuarioS.obtenerSucursales(usuario.getCod_per());
				List<Rol> rolList = rolS.listarPorUsuario(usuario.getCod_per());
				List<Menu> menuList = null;
				Rol rol = null;
				if(UtilClass.isNotNullEmpty(rolList)) {
					rol = rolList.get(0);
					menuList = menuS.listarPorRol(rol.getCod_rol());
				}
				General general = getGeneralNow(misSucursales);
				Sucursal sucursal = sucursalS.getSucursalNow(misSucursales, general);
				if(misSucursales!=null && !misSucursales.isEmpty()) {
					HttpSession sesion=request.getSession();
					reloadMain(model, usuario, misSucursales, rolList, menuList, rol, general, sucursal, sesion);
					sesion.setAttribute(MyConstant.Session.ROLES, rolList);
					sesion.setAttribute(MyConstant.Session.USER, usuario);
					sesion.setAttribute(MyConstant.Session.GESTION, general);
					sesion.setAttribute(MyConstant.Session.SUCURSAL, sucursal);
					sesion.setAttribute(MyConstant.Session.SUCURSALES, misSucursales);
//					return "principal/principal" + MyConstant.SYSTEM;
					return "sucursal/seleccion-sucursal";
				}else {
					return "principal/login" + MyConstant.SYSTEM;
				}
			}
		} catch (Exception e) {
			logger.error("error:"+e.toString());
		}
		return "principal/login" + MyConstant.SYSTEM;
	}

	private void reloadMain(Model model, Persona usuario, List<GeneralWrap> misSucursales, List<Rol> rolList, List<Menu> menuList, Rol rol, General general, Sucursal sucursal, HttpSession sesion) {
		model.addAttribute("user",usuario);
		model.addAttribute("rol",rol);
		model.addAttribute("roles", rolList);
		model.addAttribute("gestion",general);
		model.addAttribute("sucursal",sucursal);
		model.addAttribute("sucursales",misSucursales);
		model.addAttribute("menus", menuList);
		model.addAttribute("msg","Bienvenido, "+usuario.getNom_per()+" "+usuario.getPriape_per());
		sesion.setAttribute(MyConstant.Session.ROL, rol);
	}

	@RequestMapping("salir")
	public String salir(HttpServletRequest request,Model model){
		HttpSession sesion=request.getSession();
		sesion.invalidate();
		model.addAttribute("msg","Gracias por utilizar el sistema");
		return "redirect:../principal/login";
	}
	@SuppressWarnings("unchecked")
	@RequestMapping("cambiarRol")
	public String cambiarRol(HttpServletRequest request,Model model,Integer cod_rol){
		try {
			HttpSession sesion = request.getSession();
			Persona usuario=(Persona)sesion.getAttribute(MyConstant.Session.USER);
			Sucursal sucursal = (Sucursal) sesion.getAttribute(MyConstant.Session.SUCURSAL);
			General general = (General) sesion.getAttribute(MyConstant.Session.GESTION);
			if(usuario==null || sucursal ==null || general == null ){
				model.addAttribute("msg","Usuario incorrecto, por favor verifique login y clave.");
				return "principal/login" + MyConstant.SYSTEM;
			}else{
				List<Rol> rolList = (List<Rol>) sesion.getAttribute(MyConstant.Session.ROLES);
				List<GeneralWrap> misSucursales = (List<GeneralWrap>) sesion.getAttribute(MyConstant.Session.SUCURSALES);
				List<Menu> menuList = null;
				Rol rol = null;
				if(UtilClass.isNotNullEmpty(rolList)) {
					for(Rol item : rolList ){
						if(Objects.equals(item.getCod_rol(), cod_rol)) {
							rol = item;
							menuList = menuS.listarPorRol(item.getCod_rol());
						}
					};
				}
				reloadMain(model, usuario, misSucursales, rolList, menuList, rol, general, sucursal, sesion);
				return "principal/principal" + MyConstant.SYSTEM;
			}
		} catch (Exception e) {
			System.out.println("error:"+e.toString());
		}
		return "principal/login" + MyConstant.SYSTEM;
	}
	@RequestMapping("asistencia")
	public String asistencia(){
		return "principal/asistencia";
	}
	@RequestMapping("cambiarSucursal")
	public String cambiarSucursal(HttpServletRequest request, Model model, Integer sucursal) throws IOException {
		HttpSession sesion = request.getSession();
		Persona person = (Persona) sesion.getAttribute(MyConstant.Session.USER);
		Rol rolSesionado = (Rol) sesion.getAttribute(MyConstant.Session.ROL);
		if (person != null) {
			if(sucursal == null) {
				sucursal = 0;
			}
			model.addAttribute(MyConstant.Session.USER, person);
			model.addAttribute(MyConstant.Session.ROL, rolSesionado);
			model.addAttribute(MyConstant.Session.ROLES, person.getRoles());
			List<GeneralWrap> sucursales = (List<GeneralWrap>) sesion.getAttribute(MyConstant.Session.SUCURSALES);
			model.addAttribute(MyConstant.Session.SUCURSALES, sucursales);
			if (sucursales != null && !sucursales.isEmpty()) {
				GeneralWrap sucursalSelected = sucursales.get(sucursal);
				int year = Calendar.getInstance().get(Calendar.YEAR);
				boolean existYearNow = false;
				for (General gestion: sucursalSelected.getGeneralList()) {
					if(year == gestion.getGes_gen()) {
						existYearNow = true;
						model.addAttribute(MyConstant.Session.GESTION, gestion);
						sesion.setAttribute(MyConstant.Session.GESTION, gestion);
					}
				}
				if(!existYearNow) {
					model.addAttribute(MyConstant.Session.GESTION, sucursalSelected.getGeneralList().get(0));
					sesion.setAttribute(MyConstant.Session.GESTION, sucursalSelected.getGeneralList().get(0));
				}
				model.addAttribute(MyConstant.Session.SUCURSAL, sucursalSelected.getSucursal());
				sesion.setAttribute(MyConstant.Session.SUCURSAL, sucursalSelected.getSucursal());
			}

			List<Menu> menuList = null;
			Rol rol = null;
			if(UtilClass.isNotNullEmpty(person.getRoles())) {
				rol = person.getRoles().get(0);
				menuList = menuS.listarPorRol(rol.getCod_rol());
			}
			model.addAttribute("menus", menuList);
			model.addAttribute("msg", "bienvenido, " + person.getNom_per() + " " + person.getPriape_per());

			return "principal/principal" + MyConstant.SYSTEM;
		} else {
			model.addAttribute("msg", "Sesion finalizada");
//			model.addAttribute("categorias",categoriaS.listar_todos());
			model.addAttribute("isCliente", 0);
			return "principal/login";
		}
	}
}
