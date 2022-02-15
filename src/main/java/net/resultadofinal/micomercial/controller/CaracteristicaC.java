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

	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Integer id){
		try {
			Caracteristica obj = caracteristicaS.obtener(id);
			boolean exist = obj != null;
			return new DataResponse(exist, obj, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}