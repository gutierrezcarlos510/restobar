package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Ingrediente;
import net.resultadofinal.micomercial.model.Producto;
import net.resultadofinal.micomercial.model.ProductoPrecioSucursal;
import net.resultadofinal.micomercial.model.Sucursal;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.pagination.SqlBuilder;
import net.resultadofinal.micomercial.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.List;

@Service
@Transactional
public class ProductoImpl extends DbConeccion implements ProductoS {
	
	private JdbcTemplate db;
	@Autowired
	public ProductoImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private String sqlString;
	private static final Logger logger = LoggerFactory.getLogger(ProductoImpl.class);
	private static final String ENTITY = "producto";
	private static final int INSUMO = 1;
	private static final int BEBIDA = 2;
	@Autowired
	private UtilDataTableS utilDataTableS;
	@Autowired
	private SucursalS sucursalS;
	public DataTableResults<Producto> listado(HttpServletRequest request, boolean estado, Integer clase, Integer sucursal) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("producto p");
			sqlBuilder.setSelect("p.*,pps1.controlar_producto,pps1.precio as  pv_unit,pps2.precio as  pv_caja,pps3.precio as  pv_unit_descuento,pps4.precio as  pv_caja_descuento,pps5.inventario_minimo,tp.nombre as xtipo,p1.nombre as xpresentacion_unidad, p2.nombre as xpresentacion_caja,case p.tipo_grupo when 1 then 'Bebida' when 2 then 'Insumo' end xgrupo,");
			sqlBuilder.setSelectConcat("case p.tipo_compra when 1 then 'En unidades' else 'En cajas' end xtipo_compra,c.nombre as xmedida,(select count(*)>0 from ingrediente where producto_id=p.id) has_ingredients,pps5.inventario_minimo");
			sqlBuilder.addJoin("tipo_producto tp on tp.id = p.tipo_id");
			sqlBuilder.addJoin("caracteristica c on c.id = p.medida_id");
			sqlBuilder.addLeftJoin("presentacion p1 on p1.id = p.presentacion_unidad_id");
			sqlBuilder.addLeftJoin("presentacion p2 on p2.id = p.presentacion_caja_id");
//			sqlBuilder.addJoin("producto_precio_sucursal pps1 on pps1.sucursal_id = :xsuc and pps1.producto_id = p.id and pps1.id = 1 and pps1.controlar_producto = true");
			sqlBuilder.addJoin("producto_precio_sucursal pps1 on pps1.sucursal_id = :xsuc and pps1.producto_id = p.id and pps1.id = 1");//Se quito la condicion para controlar productos
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps2 on pps2.sucursal_id = :xsuc and pps2.producto_id = p.id and pps2.id = 2");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps3 on pps3.sucursal_id = :xsuc and pps3.producto_id = p.id and pps3.id = 3");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps4 on pps4.sucursal_id = :xsuc and pps4.producto_id = p.id and pps4.id = 4");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps5 on pps5.sucursal_id = :xsuc and pps5.producto_id = p.id and pps5.id = 1");
			sqlBuilder.setWhere("p.estado=:xestado " + (clase > 0 ? " and p.tipo_grupo = "+clase : ""));
			sqlBuilder.addParameter("xestado",estado);
			sqlBuilder.addParameter("xsuc", sucursal);
			return utilDataTableS.list(request, Producto.class, sqlBuilder);
		} catch (Exception e) {
			logger.error("Error al listar productos: "+e.toString());
			return null;
		}
	}
	public DataTableResults<Producto> listaPorTipo(HttpServletRequest request, boolean estado, Integer tipo, Integer sucursal) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("producto p");
			sqlBuilder.setSelect("p.*,pps1.controlar_producto,pps1.precio as  pv_unit,pps2.precio as  pv_caja,pps3.precio as  pv_unit_descuento,pps4.precio as  pv_caja_descuento,pps5.inventario_minimo,tp.nombre as xtipo");
			sqlBuilder.addJoin("tipo_producto tp on tp.id = p.tipo_id and tp.id=:xtipo");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps1 on pps1.sucursal_id = :xsuc and pps1.producto_id = p.id and pps1.id = 1");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps2 on pps2.sucursal_id = :xsuc and pps2.producto_id = p.id and pps2.id = 2");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps3 on pps3.sucursal_id = :xsuc and pps3.producto_id = p.id and pps3.id = 3");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps4 on pps4.sucursal_id = :xsuc and pps4.producto_id = p.id and pps4.id = 4");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps5 on pps5.sucursal_id = :xsuc and pps5.producto_id = p.id and pps5.id = 1");
			sqlBuilder.setWhere("p.estado=:xestado ");
			sqlBuilder.addParameter("xestado",estado);
			sqlBuilder.addParameter("xtipo",tipo);
			sqlBuilder.addParameter("xsuc", sucursal);
			return utilDataTableS.list(request, Producto.class, sqlBuilder);
		} catch (Exception e) {
			logger.error("Error al listar productos: "+e.toString());
			return null;
		}
	}
	public DataTableResults<Producto> listaPorTipoYControlInventario(HttpServletRequest request, boolean estado, Integer tipo, Integer sucursal) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("producto p");
			sqlBuilder.setSelect("p.*,pps1.controlar_producto,pps1.precio as  pv_unit,pps2.precio as  pv_caja,pps3.precio as  pv_unit_descuento,pps4.precio as  pv_caja_descuento,pps5.inventario_minimo,tp.nombre as xtipo");
			sqlBuilder.addJoin("tipo_producto tp on tp.id = p.tipo_id and tp.id=:xtipo");
//			sqlBuilder.addJoin("producto_precio_sucursal pps1 on pps1.sucursal_id = :xsuc and pps1.producto_id = p.id and pps1.id = 1 and pps1.controlar_producto = true");
			sqlBuilder.addJoin("producto_precio_sucursal pps1 on pps1.sucursal_id = :xsuc and pps1.producto_id = p.id and pps1.id = 1");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps2 on pps2.sucursal_id = :xsuc and pps2.producto_id = p.id and pps2.id = 2");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps3 on pps3.sucursal_id = :xsuc and pps3.producto_id = p.id and pps3.id = 3");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps4 on pps4.sucursal_id = :xsuc and pps4.producto_id = p.id and pps4.id = 4");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps5 on pps5.sucursal_id = :xsuc and pps5.producto_id = p.id and pps5.id = 1");
			sqlBuilder.setWhere("p.estado=:xestado ");
			sqlBuilder.addParameter("xestado",estado);
			sqlBuilder.addParameter("xtipo",tipo);
			sqlBuilder.addParameter("xsuc", sucursal);
			return utilDataTableS.list(request, Producto.class, sqlBuilder);
		} catch (Exception e) {
			logger.error("Error al listar productos: "+e.toString());
			return null;
		}
	}
	public DataTableResults<Producto> listaPorTipoGrupoAlmacen(HttpServletRequest request, boolean estado, Integer tipo, Integer sucursal) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("producto p");
			sqlBuilder.setSelect("p.*,pps1.controlar_producto,pps1.precio as  pv_unit,pps2.precio as  pv_caja,pps3.precio as  pv_unit_descuento,pps4.precio as  pv_caja_descuento,pps5.inventario_minimo,p.id as producto_id,p.nombre as xproducto,tp.nombre as xtipo,(select coalesce(max(a.cantidad),0) from almacen a where a.sucursal_id = :xsuc and a.producto_id = p.id) as cantidad");
			sqlBuilder.addJoin("tipo_producto tp on tp.id = p.tipo_id");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps1 on pps1.sucursal_id = :xsuc and pps1.producto_id = p.id and pps1.id = 1");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps2 on pps2.sucursal_id = :xsuc and pps2.producto_id = p.id and pps2.id = 2");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps3 on pps3.sucursal_id = :xsuc and pps3.producto_id = p.id and pps3.id = 3");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps4 on pps4.sucursal_id = :xsuc and pps4.producto_id = p.id and pps4.id = 4");
			sqlBuilder.addLeftJoin("producto_precio_sucursal pps5 on pps5.sucursal_id = :xsuc and pps5.producto_id = p.id and pps5.id = 1");
			sqlBuilder.setWhere("p.estado=:xestado and p.tipo_grupo=:xtipo");
			sqlBuilder.addParameter("xestado",estado);
			sqlBuilder.addParameter("xtipo",tipo);
			sqlBuilder.addParameter("xsuc", sucursal);
			return utilDataTableS.list(request, Producto.class, sqlBuilder);
		} catch (Exception e) {
			logger.error("Error al listar productos: "+e.toString());
			return null;
		}
	}
	public Producto obtener(Long id){
		try {
			List<Producto> lista = db.query("select * from producto where id=?", BeanPropertyRowMapper.newInstance(Producto.class), id);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	public void registrarPrecioProductoSucursal(ProductoPrecioSucursal obj){
		obj.setEsPrincipal(false);
		if(obj.getId() == 1) {
			obj.setEsPrincipal(true);
			obj.setNombre("Unidad");
			obj.setInventarioMinimo(obj.getInventarioMinimo());
			obj.setControlarProducto(obj.getControlarProducto());
		}
		if(obj.getId() == 2) {
			obj.setNombre("Caja");
		}
		if(obj.getId() == 3) {
			obj.setNombre("Unidad con descuento");
		}
		if(obj.getId() == 4) {
			obj.setNombre("Caja con descuento");
		}
		db.update("insert into producto_precio_sucursal(producto_id, sucursal_id, id, nombre, precio, es_principal,inventario_minimo,controlar_producto) values(?,?,?,?,?,?,?,?);",
				obj.getProductoId(),obj.getSucursalId(),obj.getId(),obj.getNombre(), obj.getPrecio(),obj.getEsPrincipal(),obj.getInventarioMinimo(),obj.getControlarProducto()!=null ? obj.getControlarProducto():false);

	}
	public void actualizarPrecioProductoSucursal(ProductoPrecioSucursal obj) {
		sqlString = "select count(*)>0 from producto_precio_sucursal where producto_id = ? and sucursal_id = ? and id = ?";
		boolean existeProducto = db.queryForObject(sqlString, Boolean.class, obj.getProductoId(), obj.getSucursalId(), obj.getId());
		if(existeProducto) {
			db.update("update producto_precio_sucursal set precio = ?,inventario_minimo=?,controlar_producto=? where producto_id = ? and sucursal_id = ? and id = ?",
					obj.getPrecio(), obj.getInventarioMinimo(), obj.getControlarProducto()!=null ? obj.getControlarProducto():false, obj.getProductoId(), obj.getSucursalId(), obj.getId());
		} else {
			registrarPrecioProductoSucursal(obj);
		}
	}
	public Long generarCodigo() {
		return db.queryForObject("select coalesce(max(id),0)+1 from producto", Long.class);
	}
	@Transactional
	public DataResponse adicionar(Producto p,Integer sucursalId){
		try {
			Long id = generarCodigo();
			sqlString = "INSERT INTO producto(id, nombre, foto, tipo_id, tipo_grupo, pc_unit, pc_caja, " +
					"unidad_por_caja, tipo_compra, presentacion_unidad_id, presentacion_caja_id, estado,medida_id,obs) " +
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true,?,?);";
			boolean save = db.update(sqlString, id, p.getNombre(), p.getFoto(), p.getTipoId(), p.getTipoGrupo(), p.getPcUnit(),
					p.getPcCaja(), p.getUnidadPorCaja(), p.getTipoCompra(), p.getPresentacionUnidadId(), p.getPresentacionCajaId(), p.getMedidaId(),p.getObs()) > 0;
			if(save) {
				if(p.getPvUnit() != null || p.getInventarioMinimo() != null ) {
//					registrarPrecioProductoSucursal(new ProductoPrecioSucursal(id,sucursalId,(short)1,p.getPvUnit(),p.getInventarioMinimo(),p.getControlarProducto()));
					List<Sucursal> sucursales = sucursalS.listAll();
					sucursales.stream().forEach(it -> {
						if(it.getCod_suc() == sucursalId) {
							registrarPrecioProductoSucursal(new ProductoPrecioSucursal(id,sucursalId,(short)1,p.getPvUnit(),p.getInventarioMinimo(),p.getControlarProducto()));
						} else {
							registrarPrecioProductoSucursal(new ProductoPrecioSucursal(id,it.getCod_suc(),(short)1,null,null,false));
						}
					});
				}
				if(p.getPvCaja() != null) {
					registrarPrecioProductoSucursal(new ProductoPrecioSucursal(id,sucursalId,(short)2,p.getPvCaja(),null,null));
				}
				if(p.getPvUnitDescuento() != null) {
					registrarPrecioProductoSucursal(new ProductoPrecioSucursal(id,sucursalId,(short)3,p.getPvUnitDescuento(),null,null));
				}
				if(p.getPvCajaDescuento() != null) {
					registrarPrecioProductoSucursal(new ProductoPrecioSucursal(id,sucursalId,(short)4,p.getPvCajaDescuento(),null,null));
				}
			}
			return new DataResponse(save, save ? id : null, Utils.getSuccessFailedAdd(ENTITY, save));
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	@Transactional
	public DataResponse modificar(Producto p, Integer sucursalId){
		try {
			sqlString = "update producto set nombre=?, foto=?, tipo_id=?, tipo_grupo=?, pc_unit=?, pc_caja=?, " +
					"unidad_por_caja=?, tipo_compra=?, presentacion_unidad_id=?, presentacion_caja_id=?,medida_id=?,obs=? where id=? " ;
			boolean update = db.update(sqlString, p.getNombre(), p.getFoto(), p.getTipoId(), p.getTipoGrupo(), p.getPcUnit(),
					p.getPcCaja(), p.getUnidadPorCaja(), p.getTipoCompra(), p.getPresentacionUnidadId(), p.getPresentacionCajaId(), p.getMedidaId(), p.getObs(), p.getId()) > 0;
			if(update) {
				if(p.getPvUnit() != null || p.getInventarioMinimo() != null) {
					actualizarPrecioProductoSucursal(new ProductoPrecioSucursal(p.getId(),sucursalId,(short)1,p.getPvUnit(),p.getInventarioMinimo(),p.getControlarProducto()));
				}
				if(p.getPvCaja() != null) {
					actualizarPrecioProductoSucursal(new ProductoPrecioSucursal(p.getId(),sucursalId,(short)2,p.getPvCaja(),null,null));
				}
				if(p.getPvUnitDescuento() != null) {
					actualizarPrecioProductoSucursal(new ProductoPrecioSucursal(p.getId(),sucursalId,(short)3,p.getPvUnitDescuento(),null,null));
				}
				if(p.getPvCajaDescuento() != null) {
					actualizarPrecioProductoSucursal(new ProductoPrecioSucursal(p.getId(),sucursalId,(short)4,p.getPvCajaDescuento(),null,null));
				}
			}
			return new DataResponse(update, Utils.getSuccessFailedMod(ENTITY, update));
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, e.getMessage()));
		}
	}
	public DataResponse darEstado(Long id,boolean est){
		try {
			sqlString = "update producto set estado = ? where id = ?";
			boolean update = db.update(sqlString, est, id) > 0;
			return new DataResponse(update, est ? Utils.getSuccessFailedAct(ENTITY, update) : Utils.getSuccessFailedEli(ENTITY, update));
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}
	@Transactional
	public void adicionarIngrediente(Short id, Long productoId, Integer cantidad,Long ingrediente) {
		db.update("insert into ingrediente(id,producto_id,cantidad,ingrediente_id) values(?,?,?,?);", id, productoId, cantidad, ingrediente);
	}
	@Transactional
	public DataResponse adicionarIngredientes(Long producto, Long ingredientes[], Integer cantidades[], Integer cantidadPlatos) {
		try {
			db.update("delete from ingrediente where producto_id = ?", producto);
			if(ingredientes != null && ingredientes.length > 0) {
				for (short i = 1; i <= ingredientes.length; i++) {
					adicionarIngrediente(i, producto, cantidades[i-1],ingredientes[i-1]);
				}
				db.update("update producto set cantidad_platos = ? where id = ?", cantidadPlatos, producto);
			}
			return new DataResponse(true, Utils.successGet("Ingredientes"));
		} catch (Exception ex) {
			throw new RuntimeException(Utils.errorAdd("Ingrediente", ex.getMessage()));
		}
	}
	@Transactional
	public DataResponse modificarIngredientes(Long producto, Long ingredientes[], Integer cantidades[], Integer cantidadPlatos) {
		try {
			db.update("delete from ingrediente where producto_id = ?", producto);
			return adicionarIngredientes(producto, ingredientes, cantidades, cantidadPlatos);
		} catch (Exception e) {
			throw new RuntimeException(Utils.errorMod("Ingrediente", e.getMessage()));
		}
	}
	public DataResponse eliminarIngrediente(Long productoId, Short id) {
		try {
			boolean isDelete = db.update("delete from ingrediente where producto_id = ? and id = ?", productoId, id) > 0;
			return Utils.getResponseDataEli("Ingrediente", isDelete);
		} catch (Exception ex) {
			throw new RuntimeException(Utils.errorEli("Ingrediente", ex.getMessage()));
		}
	}
	public List<Ingrediente> obtenerIngredientesPorProducto(Long productoId) {
		try {
			sqlString = "select i.*,p.nombre xproducto,tp.nombre xtipo, c.alias xmedida from ingrediente i " +
					"inner join producto p on p.id = i.ingrediente_id inner join caracteristica c on c.id= p.medida_id " +
					"inner join tipo_producto tp on tp.id = p.tipo_id  where i.producto_id = ? ";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Ingrediente.class), productoId);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	public List<Producto> obtenerProductosSucursal(Integer sucursalId){
		try {
			sqlString ="select p.*,pps.controlar_producto from producto_precio_sucursal pps " +
					"inner join producto p on p.id = pps.producto_id and pps .sucursal_id = ? and pps.es_principal = true " +
					"where p.estado = true order by p.tipo_grupo,p.nombre asc;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Producto.class), sucursalId);
		} catch (Exception e) {
			return null;
		}
	}
	public DataResponse actualizarControlProducto(Producto obj, Integer sucursalId) {
		try {
			boolean isUpdated = db.update("update producto_precio_sucursal set controlar_producto =? where producto_id =? and sucursal_id = ? and es_principal=true", obj.getControlarProducto(), obj.getId(), sucursalId) > 0;
			return Utils.getResponseDataMod("Producto", isUpdated);
		} catch (Exception ex) {
			throw new RuntimeException(Utils.errorEli("Ingrediente", ex.getMessage()));
		}
	}
}
