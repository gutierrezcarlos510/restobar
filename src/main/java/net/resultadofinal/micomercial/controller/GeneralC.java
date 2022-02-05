package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.*;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.GeneralS;
import net.resultadofinal.micomercial.service.MenuS;
import net.resultadofinal.micomercial.service.SucursalS;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.UtilClass;
import net.resultadofinal.micomercial.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/general/*")
public class GeneralC {
	private static final Logger logger = LoggerFactory.getLogger(GeneralC.class);
	@Autowired
	private GeneralS generalS;
	@Autowired
	private SucursalS sucursalS;
	@Autowired
	private MenuS menuS;
	private static final String ENTITY = "los datos generales del sistema";
	private static final String IMG_DEFAULT = "empresa.png";

	@RequestMapping("cambiar")
	public String cambiar(HttpServletRequest request,Model model,Integer gestion, Integer codSuc){
		try {
			HttpSession sesion = request.getSession();
			Persona usuario=(Persona)sesion.getAttribute(MyConstant.Session.USER);
			General general = generalS.obtener(gestion, codSuc);
			Sucursal sucursal = null;
			if(general!=null) {
				sucursal = sucursalS.obtener(general.getCod_suc());
			}
			if(usuario==null || sucursal ==null || general == null ){
				model.addAttribute("msg","Usuario incorrecto, por favor verifique login y clave.");
				return "principal/login"+ MyConstant.SYSTEM;
			}else{
				List<Rol> rolList = (List<Rol>) sesion.getAttribute(MyConstant.Session.ROLES);
				Rol rol = (Rol) sesion.getAttribute(MyConstant.Session.ROL);
				List<GeneralWrap> misSucursales = (List<GeneralWrap>) sesion.getAttribute(MyConstant.Session.SUCURSALES);
				List<Menu> menuList = null;
				if(UtilClass.isNotNullEmpty(rolList)) {
					rol = rolList.get(0);
					menuList = menuS.listarPorRol(rol.getCod_rol());
				}
				model.addAttribute("user",usuario);
				model.addAttribute("rol",rol);
				model.addAttribute("roles", rolList);
				model.addAttribute("menus", menuList);
				model.addAttribute("gestion",general);
				model.addAttribute("sucursal",sucursal);
				model.addAttribute("sucursales",misSucursales);
				model.addAttribute("menus", menuList);
				model.addAttribute("msg","Bienvenido, "+usuario.getNom_per()+" "+usuario.getPriape_per());
				sesion.setAttribute(MyConstant.Session.SUCURSAL, sucursal);
				sesion.setAttribute(MyConstant.Session.GESTION, general);
				return "principal/principal"+MyConstant.SYSTEM;
			}
		} catch (Exception e) {
			logger.error("error:"+e.toString());
		}
		return "principal/login"+ MyConstant.SYSTEM;
	}
	@RequestMapping("gestion")
	public String gestion(Model model){
		model.addAttribute("sucursales", sucursalS.listAll());
		return "general/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<General> listar(HttpServletRequest request, boolean estado) {
		try {
			return generalS.listado(request, estado);
		} catch (Exception ex) {
			System.out.println("error lista general: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(General g)throws IOException{
		try {
//			g.setLogtex_gen(IMG_DEFAULT);
//			g.setLogsintex_gen(IMG_DEFAULT);
			boolean status = generalS.adicionar(g);
			return new DataResponse(status, status ? Utils.successAdd(ENTITY): Utils.failedAdd(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(General g)throws IOException{
		try {
			g.setLogtex_gen(IMG_DEFAULT);
			g.setLogsintex_gen(IMG_DEFAULT);
			boolean status = generalS.modificar(g);
			return new DataResponse(status, status? Utils.successMod(ENTITY): Utils.failedMod(ENTITY));
		} catch (Exception e) {
			logger.error("error al actualizar="+e.toString());
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Integer ges_gen, Integer cod_suc)throws IOException{
		try {
			boolean status = generalS.darEstado(ges_gen, cod_suc, false);
			return new DataResponse(status, status? Utils.successEli(ENTITY): Utils.failedEli(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(Integer ges_gen, Integer cod_suc)throws IOException{
		try {
			boolean status = generalS.darEstado(ges_gen, cod_suc, true);
			return new DataResponse(status, status? Utils.successAct(ENTITY): Utils.failedAct(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("validar")
	public @ResponseBody Map<String, Object> validarCi(Integer ges_gen){
		Map<String, Object> Data = new HashMap<String, Object>();
		Data.put("status", generalS.validarGestion(ges_gen));
		return Data;
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Integer ges_gen, Integer cod_suc){
		try {
			return new DataResponse(true, generalS.obtener(ges_gen,cod_suc), Utils.successGet(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}