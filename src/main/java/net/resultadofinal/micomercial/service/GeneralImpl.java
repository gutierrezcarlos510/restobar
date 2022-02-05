package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.Menu;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.pagination.SqlBuilder;
import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.UtilDataTableS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class GeneralImpl extends DbConeccion implements GeneralS {
	
	private JdbcTemplate db;
	@Autowired
	public GeneralImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(GeneralImpl.class);
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<General> listado(HttpServletRequest request, boolean estado) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("general");
			sqlBuilder.setSelect("ges_gen, des_gen, est_gen, nom_gen, logtex_gen, logsintex_gen, tel_gen, dir_gen, lug_gen, nit_gen,general.cod_suc,sucursal.nombre as xsucursal");
			sqlBuilder.addJoin("sucursal on sucursal.cod_suc = general.cod_suc");
			sqlBuilder.setWhere("est_gen=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, General.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}
	public List<General> listAll() {
		try {
			sqlString = "select * from general where est_gen = true;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(General.class));
		} catch(Exception ex) {
			return null;
		}
	}
	public General obtener(Integer gestion, Integer cod_suc){
		try {
			return db.queryForObject("select * from general_obtener(?,?)", new BeanPropertyRowMapper<General>(General.class),gestion,cod_suc);
		} catch (Exception e) {
			return null;
		}
	}
	public Boolean adicionar(General g){
		try {
			sqlString = "select general_adicionar(?,?,?,?,?,?,?,?,?,?)";
			return db.queryForObject(sqlString,Boolean.class,g.getGes_gen(),g.getDes_gen(),g.getNom_gen(),g.getLogtex_gen(),g.getLogsintex_gen(),g.getTel_gen(),g.getDir_gen(),g.getLug_gen(),g.getNit_gen(),g.getCod_suc());
		} catch (Exception e) {
			logger.error("error al adicionar general="+e.toString());
			throw new RuntimeException("Error al adicionar datos generales del sistema:"+e.toString());
		}
	}
	public Boolean modificar(General g){
		try {
			sqlString = "select general_modificar(?,?,?,?,?,?,?,?,?);";
			return db.queryForObject(sqlString,Boolean.class,g.getDes_gen(),g.getNom_gen(),g.getLogtex_gen(),g.getLogsintex_gen(),g.getTel_gen(),g.getDir_gen(),g.getLug_gen(),g.getGes_gen(),g.getCod_suc());
		} catch (Exception e) {
			logger.error("error al modificar general="+e.toString());
			throw new RuntimeException("Error al modificar datos generales del sistema:"+e.toString());
		}
	}
	public Boolean darEstado(Integer gestion,Integer cod_suc,Boolean estado){
		try {
			sqlString = "select general_darestado(?,?,?)";
			return db.queryForObject(sqlString,Boolean.class, gestion,cod_suc,estado);
		} catch (Exception e) {
			logger.error("error al eliminar general="+e.toString());
			throw new RuntimeException("Error al cambiar estado de los datos generales del sistema: "+e.toString());
		}
	}
	public Boolean validarGestion(Integer gestion){
		return db.queryForObject("select general_validar(?);", Boolean.class,gestion);
	}
	public List<General> listarPorSucursal(Integer cod_suc){
		try {
			sqlString = "select * from general where cod_suc = ? and est_gen=true";
			return db.query(sqlString, new BeanPropertyRowMapper<General>(General.class),cod_suc);
		} catch (Exception e) {
			logger.error("error al listar por sucursal:"+e.toString());
			return null;
		}
	}
	@Override
	public General existeGestionAnterior(int gestion, int sucursal) {
		try {
			gestion--;
			sqlString = "select * from \"general\" g where g.ges_gen = ? and g.cod_suc =? and est_gen =true";
			return db.queryForObject(sqlString, new BeanPropertyRowMapper<General>(General.class),gestion,sucursal);
		} catch (Exception e) {
			logger.info("No se encontro la gestion anterior:"+e.toString());
			return null;
		}
	}
}
