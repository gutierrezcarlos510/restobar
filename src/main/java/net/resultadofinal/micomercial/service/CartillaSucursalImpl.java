package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.CartillaSucursal;
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
public class CartillaSucursalImpl extends DbConeccion implements CartillaSucursalS {

	protected JdbcTemplate db;
	@Autowired
	public CartillaSucursalImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(CartillaSucursalImpl.class);
	private static final String ENTITY = "CartillaSucursal";
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	@Override
	public DataTableResults<CartillaSucursal> listado(HttpServletRequest request, boolean estado, Integer sucursalId) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("cartilla_sucursal cs");
			sqlBuilder.setSelect("cs.*");
			sqlBuilder.setWhere("cs.estado=:xestado and cs.cod_suc=:xsuc");
			sqlBuilder.addParameter("xestado", estado);
			sqlBuilder.addParameter("xsuc", sucursalId);
			return utilDataTableS.list(request, CartillaSucursal.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public CartillaSucursal obtener(Integer id){
		try {
			List<CartillaSucursal> lista = db.query("select * from cartilla_sucursal where id=?", BeanPropertyRowMapper.newInstance(CartillaSucursal.class), id);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	
	@Override
	public DataResponse adicionar(CartillaSucursal obj){
		try {
			Integer id = db.queryForObject("select coalesce(max(id),0)+1 from cartilla_sucursal", Integer.class);
			sqlString = "insert into cartilla_sucursal(id,nombre,total,cod_suc,esta_compuesto,estado) values(?,?,?,?,?,true)";
			boolean save = db.update(sqlString, id, obj.getNombre(),obj.getTotal(),obj.getCodSuc(),obj.isEstaCompuesto()) > 0;
			return new DataResponse(save, Utils.getSuccessFailedAdd(ENTITY, save));
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	
	@Override
	public DataResponse modificar(CartillaSucursal obj){
		try {
			sqlString = "update cartilla_sucursal set nombre=?,total=?,esta_compuesto=? where id=?";
			boolean update = db.update(sqlString, obj.getNombre(), obj.getTotal(),obj.isEstaCompuesto(), obj.getId()) > 0;
			return new DataResponse(update, Utils.getSuccessFailedMod(ENTITY, update));
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	
	@Override
	public DataResponse darEstado(Integer id, Boolean estado) {
		try {
			sqlString = "update cartilla_sucursal set estado = ? where id=?";
			boolean update = db.update(sqlString, estado, id) > 0;
			return estado ? Utils.getResponseDataAct(ENTITY, update) : Utils.getResponseDataEli(ENTITY, update);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}
	@Override
	public List<CartillaSucursal> listarPorSucursal(Integer sucursalId) {
		return db.query("select * from cartilla_sucursal where cod_suc = ?", BeanPropertyRowMapper.newInstance(CartillaSucursal.class), sucursalId);
	}
}
