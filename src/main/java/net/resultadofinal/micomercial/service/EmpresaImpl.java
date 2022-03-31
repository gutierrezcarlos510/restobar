package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Empresa;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.pagination.SqlBuilder;
import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.UtilDataTableS;
import net.resultadofinal.micomercial.util.Utils;
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
public class EmpresaImpl extends DbConeccion implements EmpresaS {
	
	private JdbcTemplate db;
	@Autowired
	public EmpresaImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaImpl.class);
	private static final String ENTITY = "empresa";
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<Empresa> listado(HttpServletRequest request, boolean estado){
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("empresa");
			sqlBuilder.setSelect("cod_emp, nom_emp, dir_emp, tel_emp, ema_emp, est_emp");
			sqlBuilder.setWhere("est_emp=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, Empresa.class, sqlBuilder);
		} catch (Exception e) {

			return null;
		}
	}
	public List<Empresa> listAll() {
		try {
			sqlString = "select * from empresa where est_emp = true;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Empresa.class));
		} catch(Exception ex) {
			LOGGER.error(Utils.errorGet(ENTITY, ex.toString()));
			return null;
		}
	}
	
	public Map<String, Object> obtener(Integer cod_emp){
		try {
			return db.queryForMap("select * from empresa_obtener(?)",cod_emp);
		} catch (Exception e) {
			LOGGER.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	public Boolean adicionar(Empresa em){
		try {
			sqlString = "select empresa_adicionar(?,?,?,?);";
			return db.queryForObject(sqlString,Boolean.class,em.getNom_emp(),em.getDir_emp(),em.getTel_emp(),em.getEma_emp());
		} catch (Exception e) {
			LOGGER.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, ""));
		}
	}
	public Boolean modificar(Empresa em){
		try {
			sqlString = "select empresa_modificar(?,?,?,?,?)";
			return db.queryForObject(sqlString,Boolean.class,em.getNom_emp(),em.getDir_emp(),em.getTel_emp(),em.getEma_emp(),em.getCod_emp());
		} catch (Exception e) {
			LOGGER.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	public Boolean darEstado(Integer cod,Boolean est){
		try {
			return db.queryForObject("select empresa_darestado(?,?);",Boolean.class,cod,est);
		} catch (Exception e) {
			LOGGER.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
	public Boolean validarNom(String nom){
		return db.queryForObject("select empresa_validar(?)", Boolean.class,nom);
	}
}
