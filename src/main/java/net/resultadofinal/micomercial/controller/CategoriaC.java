package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Categoria;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.CategoriaS;
import net.resultadofinal.micomercial.util.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/categoria/*")
public class CategoriaC {
	
	@Autowired
	private CategoriaS categoriaS;
	@RequestMapping("gestion")
	public String gestion(){
		return "categoria/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Categoria> listar(HttpServletRequest request, boolean estado) {
		try {
			return categoriaS.listado(request, estado);
		} catch (Exception ex) {
			System.out.println("error lista sucursales: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(Categoria obj){
		try {
			return categoriaS.adicionar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(Categoria obj){
		try {
			return categoriaS.modificar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Integer id){
		try {
			return categoriaS.darEstado(id,false);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(HttpServletRequest request, Model model, Integer id){
		try {
			return categoriaS.darEstado(id,true);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Integer id){
		try {
			Categoria obj = categoriaS.obtener(id);
			boolean exist = obj != null;
			return new DataResponse(exist, obj, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}