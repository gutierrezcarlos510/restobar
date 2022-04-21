package net.resultadofinal.micomercial.controller;

import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Ingrediente;
import net.resultadofinal.micomercial.model.Persona;
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

	@RequestMapping("gestionInventarioActivo")
	public String gestionInventarioActivo(HttpServletRequest request,Model model){
		General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
		model.addAttribute("productos", productoS.obtenerProductosSucursal(gestion.getCod_suc()));
		return "producto/gestionInventario";
	}
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
		model.addAttribute("medidas", caracteristicaS.listAll(MyConstant.Caracteristica.MEDIDA));
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
		model.addAttribute("medidas", caracteristicaS.listAll(MyConstant.Caracteristica.MEDIDA));
		return "producto/gestion-bebidas-preparadas";
	}
	@RequestMapping("gestionMaterialServicio")
	public String gestionMaterialServicio(Model model){
		model.addAttribute("tipos",tipoproductoS.listAll(MyConstant.MATERIAL_SERVICIO));
		model.addAttribute("presentaciones", presentacionS.listarPorTipo((short) -1));
		model.addAttribute("medidas", caracteristicaS.listAll(MyConstant.Caracteristica.MEDIDA));
		return "producto/gestion-material-servicio";
	}
	@RequestMapping("gestionInventario")
	public String gestionInventario(){
		return "producto/gestion-inventario";
	}

	@RequestMapping("listar")
	public @ResponseBody
	DataTableResults<Producto> listar(HttpServletRequest request, boolean estado, Integer grupo) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return productoS.listado(request, estado, grupo,gestion.getCod_suc());
		} catch (Exception ex) {
			logger.error("error lista productos: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("listarPorTipoProducto")
	public @ResponseBody
	DataTableResults<Producto> listarPorTipoProducto(HttpServletRequest request, boolean estado, Integer tipo) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return productoS.listaPorTipo(request, estado, tipo, gestion.getCod_suc());
		} catch (Exception ex) {
			logger.error("error lista productos: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("listaPorTipoYControlInventario")
	public @ResponseBody
	DataTableResults<Producto> listaPorTipoYControlInventario(HttpServletRequest request, boolean estado, Integer tipo) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return productoS.listaPorTipoYControlInventario(request, estado, tipo, gestion.getCod_suc());
		} catch (Exception ex) {
			logger.error("error lista productos: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("listarPorTipoProductoAlmacen")
	public @ResponseBody
	DataTableResults<Producto> listarPorTipoProductoAlmacen(HttpServletRequest request, Integer tipo) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return productoS.listaPorTipoGrupoAlmacen(request, true, tipo, gestion.getCod_suc());
		} catch (Exception ex) {
			logger.error("error lista productos: "+ex.toString());
			return null;
		}
	}
	@RequestMapping("guardar")
	public @ResponseBody DataResponse guardar(HttpServletRequest request,Producto p, MultipartFile imgProducto){
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
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return productoS.adicionar(p, gestion.getCod_suc());
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("actualizar")
	public @ResponseBody
    DataResponse actualizar(HttpServletRequest request,Producto p, MultipartFile imgProducto){
		try {
			Producto productoDb = productoS.obtener(p.getId());
			String nombreArchivo = "";
			if (Utils.existeArchivo(imgProducto)) {
				if(!productoDb.getFoto().equals(MyConstant.PRODUCTO_DEFAULT)) {
					Utils.eliminarArchivo(MyConstant.Archivo.RUTA_PRODUCTO, productoDb.getFoto());
				}
				nombreArchivo = MyConstant.FORMAT_IMG_PRODUCTO + p.getId() + Utils.getExtensionFile(imgProducto);
				Utils.eliminarArchivo(MyConstant.Archivo.RUTA_PRODUCTO, nombreArchivo);
				if (!Utils.SubirArchivo(imgProducto, MyConstant.Archivo.RUTA_PRODUCTO, nombreArchivo)) {
					nombreArchivo = MyConstant.PRODUCTO_DEFAULT;
				}
			} else {
				nombreArchivo = productoDb.getFoto();
			}
			p.setFoto(nombreArchivo);
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return productoS.modificar(p,gestion.getCod_suc());
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
	DataResponse adicionarIngredientes(Long productoId, Long productos[], Integer cantidades[], Integer cantidadPlatos){
		try {
			return productoS.adicionarIngredientes(productoId, productos, cantidades, cantidadPlatos);
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

	@RequestMapping("activarControlProducto")
	public @ResponseBody
	DataResponse activarControlProducto(HttpServletRequest request,Producto obj){
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			return productoS.actualizarControlProducto(obj, gestion.getCod_suc());
		} catch (Exception e) {
			return new DataResponse(false, e.getMessage());
		}
	}
	@RequestMapping("verCatalogo")
	public void ver(HttpServletRequest request, HttpServletResponse response, Short grupo) {
		try {
			String xtipo = "";
			switch (grupo) {
				case 1:
					xtipo = "Productos";
					break;
				case 2:
					xtipo = "Insumos";
					break;
				case 3:
					xtipo = "Platos Preparados";
					break;
				case 4:
					xtipo = "Bebidas Preparadas";
					break;
				case 5:
					xtipo = "Material de Servicios";
					break;
				default:
					break;
			}
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			String nombre = "catalogo_" + xtipo + "_" + gestion, tipo = "xls", estado = "inline";
			String reportUrl = "/Reportes/producto_por_tipo.jasper";
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("tipo", grupo);
			parametros.put("xtipo", xtipo);
			parametros.put("sucursalId", gestion.getCod_suc());
			parametros.put("xsucursal", gestion.getXsucursal());
			Utils.loadDataReport(parametros, gestion);
			GeneradorReportes generador = new GeneradorReportes();
			generador.generarReporte(response, getClass().getResource(reportUrl), tipo, parametros,
					datasource.getConnection(), nombre, estado);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error reporte ver=" + e.toString());
		}
	}
}