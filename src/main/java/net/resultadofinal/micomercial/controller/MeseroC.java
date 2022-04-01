package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.util.MyConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/mesero/*")
public class MeseroC {
	
	private static final Logger logger = LoggerFactory.getLogger(MeseroC.class);
	@RequestMapping("gestionComanda")
	public String gestionComanda(HttpServletRequest request){
		try {
			Persona user=(Persona)request.getSession().getAttribute(MyConstant.Session.USER);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			if(user != null && gestion != null) {
				return "mesero/gestion";
			} else {
				logger.error("Sesion expirada al ingresar a ventas");
				return "principal/login"+ MyConstant.SYSTEM;
			}
		} catch (Exception e) {
			logger.error("Error al ingresar gestion de ventas");
			return "principal/login"+ MyConstant.SYSTEM;
		}
	}
}