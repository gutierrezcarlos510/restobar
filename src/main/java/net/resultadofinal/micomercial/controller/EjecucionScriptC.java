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
					arrayCode= new int[]{1,2,3,4,5,6,7,8,9};
					break;
				case 61:
					arrayCode= new int[]{50};
					break;
				case 62:
					arrayCode= new int[]{1,2,3,4,5,6,7,8,9,51,52};
					break;
				case 63:
					arrayCode= new int[]{53};
					break;
				case 64:
					arrayCode= new int[]{1,2,3,4,5,6,7,8,0,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43};
					break;
				case 65:
					return ejecucionScriptS.script2();
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
