package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.TipoProducto;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.CategoriaS;
import net.resultadofinal.micomercial.service.TipoProductoS;
import net.resultadofinal.micomercial.util.DataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/tipoproducto/*")
public class TipoProductoC {
	
	@Autowired
	private TipoProductoS tipoS;
	@Autowired
	private CategoriaS categoriaS;

	@RequestMapping("gestion")
	public String gestion(Model model){
		model.addAttribute("categorias", categoriaS.listAll());
		return "tipoproducto/gestion";
	}
	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<TipoProducto> listar(HttpServletRequest request, boolean estado) {
		try {
			return tipoS.listado(request, estado);
		} catch (Exception ex) {
			System.out.println("error lista sucursales: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody
    DataResponse guardar(TipoProducto obj){
		try {
			return tipoS.adicionar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(TipoProducto obj){
		try {
			return tipoS.modificar(obj);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Integer id){
		try {
			return tipoS.darEstado(id,false);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(HttpServletRequest request, Model model, Integer id){
		try {
			return tipoS.darEstado(id,true);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Integer id){
		try {
			TipoProducto obj = tipoS.obtener(id);
			boolean exist = obj != null;
			return new DataResponse(exist, obj, "Se realizo con exito la consulta");
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}