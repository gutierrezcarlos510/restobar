package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.service.AvatarS;
import net.resultadofinal.micomercial.util.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/avatar/*")
public class AvatarC {
	@Autowired
	private AvatarS avatarS;
	
	@GetMapping("listar")
	public @ResponseBody
    DataResponse listar(){
		try {
			return new DataResponse(true, avatarS.listar(),"Se recupero con exito");
		} catch (Exception e) {
			return new DataResponse(false, "No se logro recuperar los avatars");
		}
	}
}
