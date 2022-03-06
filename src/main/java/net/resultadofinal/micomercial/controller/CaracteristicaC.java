package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Caracteristica;
import net.resultadofinal.micomercial.service.CaracteristicaS;
import net.resultadofinal.micomercial.util.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/caracteristica/*")
public class CaracteristicaC {
	
	@Autowired
	private CaracteristicaS caracteristicaS;
	private static short MEDIDA = 1;

	@RequestMapping("listarMedidas")
	public @ResponseBody
    DataResponse listarMedidas(){
		try {
			return new DataResponse(true, caracteristicaS.listAll(MEDIDA), "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}