package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.Ingrediente;
import net.resultadofinal.micomercial.model.Producto;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.service.CaracteristicaS;
import net.resultadofinal.micomercial.service.PresentacionS;
import net.resultadofinal.micomercial.service.ProductoS;
import net.resultadofinal.micomercial.service.TipoProductoS;
import net.resultadofinal.micomercial.util.DataResponse;
import net.resultadofinal.micomercial.util.GeneradorReportes;
import net.resultadofinal.micomercial.util.MyConstant;
import net.resultadofinal.micomercial.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/producto/*")
public class ProductoC {
	
	@Autowired
	private ProductoS productoS;
	@Autowired
	private TipoProductoS tipoproductoS;
	@Autowired
	private PresentacionS presentacionS;
	@Autowired
	private CaracteristicaS caracteristicaS;
	@Autowired
	private DataSource datasource;
	private static final Logger logger = LoggerFactory.getLogger(ProductoC.class);
	private static final String ENTITY = "producto";

	@RequestMapping("gestion")
	public String gestion(Model model){
		model.addAttribute("tipos",tipoproductoS.listAll(MyConstant.BEBIDA));
		model.addAttribute("presentaciones", presentacionS.listarPorTipo((short) -1));
		model.addAttribute("medidas", caracteristicaS.listAll(MyConstant.Caracteristica.MEDIDA));
		return "producto/gestion";
	}
	@RequestMapping("gestionInsumos")
	public String gestionInsumos(Model model){
		model.addAttribute("tipos",tipoproductoS.listAll(MyConstant.INSUMO));
		model.addAttribute("presentaciones", presentacionS.listarPorTipo((short) -1));
		return "producto/gestion-insumos";
	}
	@RequestMapping("gestionPlatos")
	public String gestionPlatos(Model model){
		model.addAttribute("tipos",tipoproductoS.listAll(MyConstant.PLATO));
		return "producto/gestion-platos";
	}
	@RequestMapping("gestionBebidasPreparadas")
	public String gestionBebidasPreparadas(Model model){
		model.addAttribute("tipos",tipoproductoS.listAll(MyConstant.BEBIDA_PREPARADA));
		return "producto/gestion-bebidas-preparadas";
	}
	@RequestMapping("gestionInventario")
	public String gestionInventario(){
		return "producto/gestion-inventario";
	}

	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Producto> listar(HttpServletRequest request, boolean estado, Integer grupo) {
		try {
			return productoS.listado(request, estado, grupo);
		} catch (Exception ex) {
			logger.error("error lista productos: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody DataResponse guardar(Producto p, MultipartFile imgProducto){
		try {
			String nombreArchivo;
			p.setId(productoS.generarCodigo());
			if (Utils.existeArchivo(imgProducto)) {
				nombreArchivo = MyConstant.FORMAT_IMG_PRODUCTO + p.getId() + Utils.getExtensionFile(imgProducto);
				if (!Utils.SubirArchivo(imgProducto, MyConstant.Archivo.RUTA_PRODUCTO, nombreArchivo)) {
					nombreArchivo = MyConstant.PRODUCTO_DEFAULT;
				}
			} else {
				nombreArchivo = MyConstant.PRODUCTO_DEFAULT;
			}
			p.setFoto(nombreArchivo);
			return productoS.adicionar(p);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(Producto p, MultipartFile imgProducto){
		try {
			String nombreArchivo = "";
			if (Utils.existeArchivo(imgProducto)) {
				Producto productoDb = productoS.obtener(p.getId());
				if(!productoDb.getFoto().equals(MyConstant.PRODUCTO_DEFAULT)) {
					Utils.eliminarArchivo(MyConstant.Archivo.RUTA_PRODUCTO, productoDb.getFoto());
				}
				nombreArchivo = MyConstant.FORMAT_IMG_PRODUCTO + p.getId() + Utils.getExtensionFile(imgProducto);
				Utils.eliminarArchivo(MyConstant.Archivo.RUTA_PRODUCTO, nombreArchivo);
				if (!Utils.SubirArchivo(imgProducto, MyConstant.Archivo.RUTA_PRODUCTO, nombreArchivo)) {
					nombreArchivo = MyConstant.PRODUCTO_DEFAULT;
				}
			} else {
				nombreArchivo = MyConstant.PRODUCTO_DEFAULT;
			}
			p.setFoto(nombreArchivo);
			return productoS.modificar(p);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminar")
	public @ResponseBody
    DataResponse eliminar(Long id){
		try {
			return productoS.darEstado(id, false);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("activar")
	public @ResponseBody
    DataResponse activar(Long id){
		try {
			return productoS.darEstado(id, true);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("obtener")
	public @ResponseBody
    DataResponse obtener(Long id){
		try {
			return new DataResponse(true,productoS.obtener(id), Utils.successGet(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("verCatalogo")
	public void verInventario(HttpServletResponse response){
		try {
			String nombre="inventario",tipo="pdf",estado="inline";
			String reportUrl="/Reportes/inventario.jasper";
			Map<String, Object> parametros=new HashMap<String, Object>();
			GeneradorReportes generador=new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo,parametros,datasource.getConnection() ,nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error al inventario="+e.toString());
		}
	}
	@RequestMapping("obtenerIngredientes")
	public @ResponseBody
	DataResponse obtenerIngredientes(Long productoId){
		try {
			return new DataResponse(true, productoS.obtenerIngredientesPorProducto(productoId), Utils.successGet(ENTITY));
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("adicionarIngredientes")
	public @ResponseBody
	DataResponse adicionarIngredientes(Long productoId, Long productos[], Integer cantidades[]){
		try {
			return productoS.adicionarIngredientes(productoId, productos, cantidades);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("modificarIngredientes")
	public @ResponseBody
	DataResponse modificarIngredientes(Long productoId, Long ingredientes[], Integer cantidades[]){
		try {
			return productoS.modificarIngredientes(productoId, ingredientes, cantidades);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("eliminarIngrediente")
	public @ResponseBody
	DataResponse eliminarIngrediente(Long productoId, Short id){
		try {
			return productoS.eliminarIngrediente(productoId, id);
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
}