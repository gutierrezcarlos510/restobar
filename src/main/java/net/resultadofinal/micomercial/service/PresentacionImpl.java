package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Presentacion;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.pagination.SqlBuilder;
import net.resultadofinal.micomercial.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.List;

@Service
public class PresentacionImpl extends DbConeccion implements PresentacionS {

	protected JdbcTemplate db;
	@Autowired
	public PresentacionImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(PresentacionImpl.class);
	private static final String ENTITY = "presentacion";
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<Presentacion> listado(HttpServletRequest request, boolean estado, Integer tipo) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("presentacion p");
			sqlBuilder.setSelect("p.*");
			sqlBuilder.setWhere("p.estado=:xestado and (p.tipo = :xtipo or :xtipo = 0)");
			sqlBuilder.addParameter("xestado", estado);
			sqlBuilder.addParameter("xtipo", tipo);
			return utilDataTableS.list(request, Presentacion.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public Presentacion obtener(Integer id){
		try {
			List<Presentacion> lista = db.query("select * from presentacion where id=?", BeanPropertyRowMapper.newInstance(Presentacion.class), id);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	public DataResponse adicionar(Presentacion obj){
		try {
			Integer id = db.queryForObject("select coalesce(max(id),0)+1 from presentacion", Integer.class);
			sqlString = "insert into presentacion(id,nombre,tipo,estado) values(?,?,?,true)";
			boolean save = db.update(sqlString, id, obj.getNombre(),obj.getTipo()) > 0;
			return new DataResponse(save, Utils.getSuccessFailedAdd(ENTITY, save));
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	@Override
	public DataResponse modificar(Presentacion obj){
		try {
			sqlString = "update presentacion set nombre=?,tipo=? where id=?";
			boolean update = db.update(sqlString, obj.getNombre(), obj.getTipo(), obj.getId()) > 0;
			return new DataResponse(update, Utils.getSuccessFailedMod(ENTITY, update));
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	@Override
	public DataResponse darEstado(Integer id, Boolean estado) {
		try {
			sqlString = "update presentacion set estado = ? where id=?";
			boolean update = db.update(sqlString, estado, id) > 0;
			return estado ? Utils.getResponseDataAct(ENTITY, update) : Utils.getResponseDataEli(ENTITY, update);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}
	public List<Presentacion> listarPorTipo(Short tipo) {
		return db.query("select * from presentacion where tipo = ? or ?=-1", BeanPropertyRowMapper.newInstance(Presentacion.class), tipo, tipo);
	}
}
