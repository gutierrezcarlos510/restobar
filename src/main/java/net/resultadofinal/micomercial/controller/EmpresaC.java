package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Empresa;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.EmpresaS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/empresa/*")
public class EmpresaC {

	@Autowired
	private EmpresaS empresaS;

	@RequestMapping("gestion")
	public String gestion() {
		return "empresa/gestion";
	}
	
	private static final String ENTITY = "empresa";
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Empresa> listar(HttpServletRequest request, boolean estado) {
		try {
			return empresaS.listado(request, estado);
		} catch (Exception ex) {
			System.out.println("error lista empresas: "+ex.toString());
			return null;
		}
	}

	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(Empresa em) throws IOException {
		try {
			boolean status = empresaS.adicionar(em);
			return new DataResponse(status, Utils.getSuccessFailedAdd(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(HttpServletRequest request, Model model, Empresa em)
			throws IOException {
		try {
			boolean status = empresaS.modificar(em);
			return new DataResponse(status, Utils.getSuccessFailedMod(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(HttpServletRequest request, Model model, Integer cod_emp)
			throws IOException {
		try {
			boolean status = empresaS.darEstado(cod_emp, false);
			return new DataResponse(status, Utils.getSuccessFailedEli(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(HttpServletRequest request, Model model, Integer cod_emp){
		try {
			boolean status = empresaS.darEstado(cod_emp, true);
			return new DataResponse(status, Utils.getSuccessFailedAct(ENTITY, status));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}

	@RequestMapping("validar")
	public @ResponseBody
    DataResponse validarCi(HttpServletRequest request, String nom_emp) {
		try {
			return new DataResponse(empresaS.validarNom(nom_emp), "Validacion de "+ENTITY);
		} catch (Exception e) {
			return new DataResponse(false, Utils.errorGet("validacion de la "+ENTITY, ""));
		}
	}

	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(HttpServletRequest request, Integer cod_emp) {
		try {
			return new DataResponse(true, empresaS.obtener(cod_emp), Utils.successGet(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.toString());
		}
	}
}