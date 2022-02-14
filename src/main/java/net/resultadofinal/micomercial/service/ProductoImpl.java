package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Ingrediente;
import net.resultadofinal.micomercial.model.Producto;
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
	public DataTableResults<Producto> listado(HttpServletRequest request, boolean estado, Integer clase) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("producto p");
			sqlBuilder.setSelect("p.*,tp.nombre as xtipo,p1.nombre as xpresentacion_unidad, p2.nombre as xpresentacion_caja,case p.tipo_grupo when 1 then 'Bebida' when 2 then 'Insumo' end xgrupo,");
			sqlBuilder.setSelectConcat("case p.tipo_compra when 1 then 'En unidades' else 'En cajas' end xtipo_compra,c.nombre as xmedida,(select count(*)>0 from ingrediente where producto_id=p.id) has_ingredients");
			sqlBuilder.addJoin("tipo_producto tp on tp.id = p.tipo_id");
			sqlBuilder.addJoin("caracteristica c on c.id = p.medida_id");
			sqlBuilder.addLeftJoin("presentacion p1 on p1.id = p.presentacion_unidad_id");
			sqlBuilder.addLeftJoin("presentacion p2 on p2.id = p.presentacion_caja_id");
			sqlBuilder.setWhere("p.estado=:xestado " + (clase > 0 ? " and p.tipo_grupo = "+clase : ""));
			sqlBuilder.addParameter("xestado",estado);
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
	public Long generarCodigo() {
		return db.queryForObject("select coalesce(max(id),0)+1 from producto", Long.class);
	}
	public DataResponse adicionar(Producto p){
		try {
			Long id = generarCodigo();
			sqlString = "INSERT INTO producto(id, nombre, foto, tipo_id, tipo_grupo, pc_unit, pv_unit, pv_caja, pc_caja, pv_unit_descuento, pv_caja_descuento, inventario_minimo_unidad, inventario_minimo_caja, " +
					"unidad_por_caja, tipo_compra, presentacion_unidad_id, presentacion_caja_id, estado,medida_id) " +
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true,?);";
			boolean save = db.update(sqlString, id, p.getNombre(), p.getFoto(), p.getTipoId(), p.getTipoGrupo(), p.getPcUnit(),
					p.getPvUnit(),p.getPvCaja(), p.getPcCaja(), p.getPvUnitDescuento(), p.getPvCajaDescuento(), p.getInventarioMinimoUnidad(), p.getInventarioMinimoCaja(),
					p.getUnidadPorCaja(), p.getTipoCompra(), p.getPresentacionUnidadId(), p.getPresentacionCajaId(), p.getMedidaId()) > 0;
			return new DataResponse(save, save ? id : null, Utils.getSuccessFailedAdd(ENTITY, save));
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	public DataResponse modificar(Producto p){
		try {
			sqlString = "update producto set nombre=?, foto=?, tipo_id=?, tipo_grupo=?, pc_unit=?, pv_unit=?, pv_caja=?, pc_caja=?, pv_unit_descuento=?, pv_caja_descuento=?, inventario_minimo_unidad=?, inventario_minimo_caja=?, " +
					"unidad_por_caja=?, tipo_compra=?, presentacion_unidad_id=?, presentacion_caja_id=?,medida_id=? where id=? " ;
			boolean update = db.update(sqlString, p.getNombre(), p.getFoto(), p.getTipoId(), p.getTipoGrupo(), p.getPcUnit(),
					p.getPvUnit(),p.getPvCaja(), p.getPcCaja(), p.getPvUnitDescuento(), p.getPvCajaDescuento(), p.getInventarioMinimoUnidad(), p.getInventarioMinimoCaja(),
					p.getUnidadPorCaja(), p.getTipoCompra(), p.getPresentacionUnidadId(), p.getPresentacionCajaId(), p.getMedidaId(), p.getId()) > 0;
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
		db.update("insert into ingrediente(id,productoId,cantidad,ingrediente_id) values(?,?,?,?);", id, productoId, cantidad, ingrediente);
	}
	@Transactional
	public DataResponse adicionarIngredientes(Long producto, Long ingredientes[], Integer cantidades[]) {
		try {
			if(ingredientes != null && ingredientes.length > 0) {
				for (short i = 1; i <= ingredientes.length; i++) {
					adicionarIngrediente(i, producto, cantidades[i-1],ingredientes[i-1]);
				}
			}
			return new DataResponse(true, Utils.successGet("Ingredientes"));
		} catch (Exception ex) {
			throw new RuntimeException(Utils.errorAdd("Ingrediente", ex.getMessage()));
		}
	}
	public DataResponse modificarIngredientes(Long producto, Long ingredientes[], Integer cantidades[]) {
		try {
			db.update("delete from ingrediente where producto_id = ?", producto);
			return adicionarIngredientes(producto, ingredientes, cantidades);
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
					"inner join producto p on p.id = i.producto_id inner join caracteristica c on c.id= p.medida_id " +
					"inner join tipo_producto tp on tp.id = p.tipo_id  where i.producto_id = ? ";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Ingrediente.class), productoId);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
}
