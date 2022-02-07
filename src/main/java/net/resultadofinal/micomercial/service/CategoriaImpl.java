package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Categoria;
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
public class CategoriaImpl extends DbConeccion implements CategoriaS {

	private JdbcTemplate db;
	@Autowired
	public CategoriaImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(CategoriaImpl.class);
	private static final String ENTITY = "categoria";
	private String sqlString;

	@Autowired
	private UtilDataTableS utilDataTableS;
	@Override
	public DataTableResults<Categoria> listado(HttpServletRequest request, boolean estado) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("categoria c");
			sqlBuilder.setSelect("c.*,case c.tipo when 1 then 'Bebida' when 2 then 'Insumo' when 3 then 'Plato' when 4 then 'Bebida en preparacion' end xtipo");
			sqlBuilder.setWhere("c.estado=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, Categoria.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}
	@Override
	public List<Categoria> listAll() {
		try {
			sqlString = "select c.*,case c.tipo when 1 then 'Bebida' when 2 then 'Insumo' when 3 then 'Plato' when 4 then 'Bebida en preparacion' end xtipo from categoria c where c.estado = true;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Categoria.class));
		} catch(Exception ex) {
			return null;
		}
	}
	@Override
	public Categoria obtener(Integer id){
		try {
			List<Categoria> lista = db.query("select c.*,case c.tipo when 1 then 'Bebida' when 2 then 'Insumo' when 3 then 'Plato' when 4 then 'Bebida en preparacion' end xtipo from categoria c where c,id=?", BeanPropertyRowMapper.newInstance(Categoria.class), id);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Override
	@Transactional
	public DataResponse adicionar(Categoria obj){
		try {
			Integer id = db.queryForObject("select coalesce(max(id),0)+1 from categoria", Integer.class);
			sqlString = "insert into categoria(id,nombre,estado,tipo) values(?,?,true,?)";
			boolean isSave = db.update(sqlString, id, obj.getNombre(),obj.getTipo()) > 0;
			return new DataResponse(isSave, isSave ? id : null, Utils.getSuccessFailedAdd(ENTITY, isSave));
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	@Override
	@Transactional
	public DataResponse modificar(Categoria t){
		try {
			sqlString = "update categoria set nombre=?,tipo=? where id=?";
			boolean isUpdate = db.update(sqlString, t.getNombre(), t.getTipo(), t.getId()) > 0;
			return new DataResponse(isUpdate, Utils.getSuccessFailedMod(ENTITY, isUpdate));
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, e.getMessage()));
		}
	}
	@Override
	@Transactional
	public DataResponse darEstado(Integer id, boolean est){
		try {
			boolean isUpdate = db.update("update categoria set estado = ? where id = ?;",est, id) > 0;
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
