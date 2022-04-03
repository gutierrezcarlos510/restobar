package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Caracteristica;
import net.resultadofinal.micomercial.model.Categoria;
import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Mesa;
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
public class MesaImpl extends DbConeccion implements MesaS {

	private JdbcTemplate db;
	@Autowired
	public MesaImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(MesaImpl.class);
	private static final String ENTITY = "caracteristica";

	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;

	@Override
	public DataTableResults<Mesa> listado(HttpServletRequest request, boolean estado) {
		try {
			General gestion = (General) request.getSession().getAttribute(MyConstant.Session.GESTION);
			SqlBuilder sqlBuilder = new SqlBuilder("mesa m");
			sqlBuilder.setSelect("m.*");
			sqlBuilder.setWhere("m.estado=:xestado and m.sucursal_id = :xsuc");
			sqlBuilder.addParameter("xestado",estado);
			sqlBuilder.addParameter("xsuc", gestion.getCod_suc());
			return utilDataTableS.list(request, Mesa.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Mesa> listPorSucursal(Integer sucursalId) {
		try {
			sqlString = "select m.* from mesa m where m.sucursal_id = ? order by m.orden";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Mesa.class), sucursalId);
		} catch(Exception ex) {
			return null;
		}
	}

	@Override
	public Mesa obtener(Short id){
		try {
			List<Mesa> lista = db.query("select m.* from mesa m where m.id = ?", BeanPropertyRowMapper.newInstance(Mesa.class), id);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}

	@Override
	@Transactional
	public DataResponse adicionar(Mesa obj){
		try {
			Short id = db.queryForObject("select coalesce(max(id),0)+1 from mesa", Short.class);
			Short orden = db.queryForObject("select coalesce(max(orden),0)+1 from mesa where estado=true and sucursal_id =?", Short.class, obj.getSucursalId());
			sqlString = "insert into mesa(id,nombre,alias,cantidad,sucursal_id,orden,estado) values(?,?,?,?,?,?,true)";
			boolean isSave = db.update(sqlString, id, obj.getNombre(), obj.getAlias(),obj.getCantidad(),obj.getSucursalId(),orden) > 0;
			return new DataResponse(isSave, isSave ? id : null, Utils.getSuccessFailedAdd(ENTITY, isSave));
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}

	@Override
	@Transactional
	public DataResponse modificar(Mesa obj){
		try {
			sqlString = "update mesa set nombre=?,alias=?,cantidad=? where id=?";
			boolean isUpdate = db.update(sqlString, obj.getNombre(), obj.getAlias(), obj.getCantidad(), obj.getId()) > 0;
			return new DataResponse(isUpdate, Utils.getSuccessFailedMod(ENTITY, isUpdate));
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, e.getMessage()));
		}
	}
	@Override
	public DataResponse darEstado(Short id, boolean est,Integer sucursalId){
		try {
			if(est) {
				Short orden = db.queryForObject("select coalesce(max(orden),0)+1 from mesa where estado = true and sucursal_id =?", Short.class, sucursalId);
				boolean isUpdate = db.update("update mesa set estado = true, orden=? where id = ?;", orden, id) > 0;
				return new DataResponse(isUpdate, Utils.getSuccessFailedAct(ENTITY, isUpdate));
			} else {
				boolean isUpdate = db.update("update mesa set estado = false where id = ?;", id) > 0;
				return new DataResponse(isUpdate, Utils.getSuccessFailedEli(ENTITY, isUpdate));
			}
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}
	@Transactional
	public DataResponse ordenar(List<Short> codigos){
		try {
			sqlString = "update mesa set orden=? where id=?";
			if(codigos!=null && !codigos.isEmpty()) {
				for (int i = 0; i < codigos.size(); i++) {
					db.update(sqlString, (i+1), codigos.get(i));
				}
			}
			return new DataResponse(true, Utils.getSuccessFailedMod("Actualizacion de orden de las mesas", true));
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, e.getMessage()));
		}
	}
	public List<Mesa> listarMesasLibresPorSucursal(Integer sucursalId) {
		try {
			sqlString = "select * from mesa m where m.estado = true and m.sucursal_id = ? and m.id not in (select v.mesa_id from venta v where v.estado = true and v.tipo =1 and sucursal_id =?) order by m.orden";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Mesa.class), sucursalId, sucursalId);
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public List<Mesa> listarMesasEspeciales() {
		try {
			sqlString = "select * from mesa m where m.id < 0;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Mesa.class));
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
