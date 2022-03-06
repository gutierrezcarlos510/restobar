package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.TipoProducto;
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
public class TipoProductoImpl extends DbConeccion implements TipoProductoS {
	
	private JdbcTemplate db;
	@Autowired
	public TipoProductoImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(TipoProductoImpl.class);
	private static final String ENTITY = "tipo de producto";

	private String sqlString;

	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<TipoProducto> listado(HttpServletRequest request, boolean estado) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("tipo_producto tp");
			sqlBuilder.setSelect("tp.*,c.nombre as xcategoria");
			sqlBuilder.addJoin("categoria c on c.id = tp.categoria_id");
			sqlBuilder.setWhere("tp.estado=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, TipoProducto.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}
	public List<TipoProducto> listAll(Integer tipo) {
		try {
			sqlString = "select tp.* from tipo_producto tp inner join categoria c on c.id = tp.categoria_id and (c.tipo = ? or ?=0) where tp.estado = true;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(TipoProducto.class), tipo, tipo);
		} catch(Exception ex) {
			return null;
		}
	}
	public TipoProducto obtener(Integer id){
		try {
			List<TipoProducto> lista = db.query("select tp.*,c.nombre as xcategoria from tipo_producto tp inner join categoria c on c.id = tp.categoria_id where tp.id=?", BeanPropertyRowMapper.newInstance(TipoProducto.class), id);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Transactional
	public DataResponse adicionar(TipoProducto obj){
		try {
			Integer id = db.queryForObject("select coalesce(max(id),0)+1 from tipo_producto", Integer.class);
			sqlString = "insert into tipo_producto(id,nombre,descripcion,estado,categoria_id,es_preparado,es_comerciable,area_destino) values(?,?,?,true,?,?,?,?)";
			boolean isSave = db.update(sqlString, id, obj.getNombre(), obj.getDescripcion(), obj.getCategoriaId(), obj.getEsPreparado(), obj.getEsComerciable(), obj.getAreaDestino()) > 0;
			return new DataResponse(isSave, isSave ? id : null, Utils.getSuccessFailedAdd(ENTITY, isSave));
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	@Transactional
	public DataResponse modificar(TipoProducto t){
		try {
			sqlString = "update tipo_producto set nombre=?, descripcion=?,categoria_id=?, es_preparado = ?, es_comerciable = ?, area_destino=? where id=?";
			boolean isUpdate = db.update(sqlString, t.getNombre(), t.getDescripcion(), t.getCategoriaId(), t.getEsPreparado(), t.getEsComerciable(), t.getAreaDestino(), t.getId()) > 0;
			return new DataResponse(isUpdate, Utils.getSuccessFailedMod(ENTITY, isUpdate));
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, e.getMessage()));
		}
	}
	@Transactional
	public DataResponse darEstado(Integer id,boolean est){
		try {
			boolean isUpdate = db.update("update tipo_producto set estado = ? where id = ?;",est, id) > 0;
			if(est) {
				return new DataResponse(isUpdate, Utils.getSuccessFailedAct(ENTITY, isUpdate));
			} else {
				return new DataResponse(isUpdate, Utils.getSuccessFailedEli(ENTITY, isUpdate));
			}
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}
}
