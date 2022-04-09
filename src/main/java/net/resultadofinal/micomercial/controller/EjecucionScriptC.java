package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.service.EjecucionScriptS;
import net.resultadofinal.micomercial.util.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/ejecucion/*")
public class EjecucionScriptC {
	@Autowired
	private EjecucionScriptS ejecucionScriptS;

	@RequestMapping("gestion")
	public String gestion(HttpServletRequest request,Model model){
		return "sql/gestion";
	}
	@RequestMapping("execute")
	public @ResponseBody
    DataResponse execute(int code, String password) {
		try {
			if(password!=null && password.equals("admin7167968")) {
				int arrayCode[]= null;
				switch (code) {
				case 60:
					arrayCode= new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14};
					break;
				default:
					arrayCode= new int[]{code};
					break;
				}
				return ejecucionScriptS.script1(arrayCode);
			}else {
				return new DataResponse(false, "Clave de acceso incorrecta para ejecucion de script.");
			}
		} catch (Exception e) {
			return new DataResponse(false, "Error al ejecutar script: " + e.getMessage());
		}
	}
}
