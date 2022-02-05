package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Rol;
import net.resultadofinal.micomercial.pagination.DataTableResults;
import net.resultadofinal.micomercial.pagination.SqlBuilder;
import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.UtilDataTableS;
import net.resultadofinal.micomercial.util.Utils;
import net.resultadofinal.micomercial.util.Vectores;
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
import java.util.Map;

@Service
public class RolImpl extends DbConeccion implements RolS {
	
	private JdbcTemplate db;
	@Autowired
	public RolImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(RolImpl.class);
	private static final String ENTITY = "rol";
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<Rol> listado(HttpServletRequest request, boolean estado) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("rol");
			sqlBuilder.setSelect("cod_rol,nom_rol,des_rol,est_rol,(select cast(count(*) as int) from rolmen where rolmen.cod_rol=rol.cod_rol) menus");
			sqlBuilder.setWhere("est_rol=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, Rol.class, sqlBuilder);
		} catch (Exception e) {

			return null;
		}
	}
	public List<Rol> listAll() {
		try {
			sqlString = "select * from rol where est_rol = true;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Rol.class));
		} catch(Exception ex) {
			return null;
		}
	}
	public List<Rol> listarPorUsuario(Long cod_per){
		return db.query("select * from rol_obtenerxcodper(?)", new BeanPropertyRowMapper<Rol>(Rol.class),cod_per);
	}
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		if(search==null)search="";
		sqlString = "select * from rol_lista(?,?,?,?)"+asObjectAdd(asRol, "RN BIGINT,Tot INT,menus INT");
		return db.queryForList(sqlString,start,length,search,estado);
	}
	public List<Rol> listar() {
		return db.query("select * from rol where est_rol=true", new BeanPropertyRowMapper<Rol>(Rol.class));
	}
	public Rol obtener(Integer cod_rol){
		try {
			List<Rol> rolList = db.query("select * from rol where cod_rol = ?", BeanPropertyRowMapper.newInstance(Rol.class),cod_rol);
			if(rolList != null && !rolList.isEmpty()) {
				return rolList.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(Utils.successGet(ENTITY));
			return null;
		}
	}
	@Transactional
	public Boolean adicionar(Rol r){
		try {
			return db.queryForObject("select rol_adicionar(?,?);",Boolean.class,r.getNom_rol(),r.getDes_rol());
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, ""));
		}
	}
	@Transactional
	public Boolean modificar(Rol r){
		try {
			return db.queryForObject("select rol_modificar(?,?,?);",Boolean.class,r.getNom_rol(),r.getDes_rol(),r.getCod_rol());
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	@Transactional
	public Boolean darEstado(Integer cod_rol,Boolean est){
		try {
			return db.queryForObject("select rol_darestado(?,?);",Boolean.class,cod_rol,est);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
	@Transactional
	public Boolean adicionarRolMenu(Integer codr,Integer obtenidos[]){
		try {
				return db.queryForObject("select rolmen_adicionar(?,\'"+new Vectores().convertir_Int_a_String(obtenidos)+"\')",Boolean.class,codr);
		} catch (Exception e) {
			logger.error(Utils.errorAdd("menu con relacion a "+ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd("menu con relacion a "+ENTITY, ""));
		}
	}
	public Boolean validarNom(String nom){
		return db.queryForObject("select rol_validar(?)", Boolean.class,nom);
	}
}
