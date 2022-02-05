package net.resultadofinal.micomercial.service;

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
	public DataTableResults<Producto> listado(HttpServletRequest request, boolean estado) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("producto p");
			sqlBuilder.setSelect("p.*");
			sqlBuilder.setWhere("p.estado=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, Producto.class, sqlBuilder);
		} catch (Exception e) {
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
	public DataResponse adicionar(Producto p){
		try {
			Long id = db.queryForObject("select coalesce(max(id),0)+1 from producto", Long.class);
			sqlString = "INSERT INTO producto(id, nombre, foto, tipo_id, tipo_grupo, pc_unit, pv_unit, pv_caja, pc_caja, pv_unit_descuento, pv_caja_descuento, inventario_minimo_unidad, inventario_minimo_caja, " +
					"unidad_por_caja, tipo_compra, presentacion_unidad_id, presentacion_caja_id, estado) " +
					"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, true);";
			boolean save = db.update(sqlString, id, p.getNombre(), p.getFoto(), p.getTipoId(), p.getTipoGrupo(), p.getPcUnit(),
					p.getPvUnit(),p.getPvCaja(), p.getPvUnitDescuento(), p.getPvCajaDescuento(), p.getInventarioMinimoUnidad(), p.getInventarioMinimoCaja(),
					p.getUnidadPorCaja(), p.getTipoCompra(), p.getPresentacionUnidadId(), p.getPresentacionCajaId()) > 0;
			return new DataResponse(save, save ? id : null, Utils.getSuccessFailedAdd(ENTITY, save));
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	public DataResponse modificar(Producto p){
		try {
			sqlString = "update producto set nombre=?, foto=?, tipo_id=?, tipo_grupo=?, pc_unit=?, pv_unit=?, pv_caja=?, pc_caja=?, pv_unit_descuento=?, pv_caja_descuento=?, inventario_minimo_unidad=?, inventario_minimo_caja=?, " +
					"unidad_por_caja=?, tipo_compra=?, presentacion_unidad_id=?, presentacion_caja_id=? where id=? " ;
			boolean update = db.update(sqlString, p.getNombre(), p.getFoto(), p.getTipoId(), p.getTipoGrupo(), p.getPcUnit(),
					p.getPvUnit(),p.getPvCaja(), p.getPvUnitDescuento(), p.getPvCajaDescuento(), p.getInventarioMinimoUnidad(), p.getInventarioMinimoCaja(),
					p.getUnidadPorCaja(), p.getTipoCompra(), p.getPresentacionUnidadId(), p.getPresentacionCajaId(), p.getId()) > 0;
			return new DataResponse(update, Utils.getSuccessFailedMod(ENTITY, update));
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	public DataResponse darEstado(Long id,boolean est){
		try {
			sqlString = "update producto set estado = ? where id = ?";
			boolean update = db.update(sqlString, est, id) > 0;
			return new DataResponse(update, est ? Utils.getSuccessFailedAct(ENTITY, update) : Utils.getSuccessFailedEli(ENTITY, update));
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.toString()));
		}
	}

}
