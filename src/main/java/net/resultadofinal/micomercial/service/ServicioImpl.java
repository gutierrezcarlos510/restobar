package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Servicio;
import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class ServicioImpl extends DbConeccion implements ServicioS {
	
	private JdbcTemplate db;
	@Autowired
	public ServicioImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(ServicioImpl.class);
	private static final String ENTITY = "servicio";
	private String sqlString;
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		if(search==null)search="";
		sqlString = "select * from servicio_lista(?,?,?,?)";
		sqlString += asObjectAdd(asServicio, "nom_tipser character varying(150)"+(start<0?"":",RN BIGINT,Tot INT"));
		return db.queryForList(sqlString,start,length,search,estado);
	}
	public Map<String, Object> obtener(Integer cod){
		try {
			return db.queryForMap("select * from servicio_obtener(?)",cod);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Transactional
	public Integer adicionar(Servicio s){
		try {
			int cod= db.queryForObject("select servicio_adicionar(?,?,?,?,?,?);",Integer.class,s.getNom_ser(),s.getDes_ser(),s.getCod_tipser(),s.getPre_ser(),s.getPorcentaje_multa(),s.getPorcentaje_ayudante());
			return cod;
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, ""));
		}
	}
	@Transactional
	public Boolean modificar(Servicio s){
		try {
			return db.queryForObject("select servicio_modificar(?,?,?,?,?,?,?);",Boolean.class,s.getNom_ser(),s.getDes_ser(),s.getCod_ser(),s.getCod_tipser(),s.getPre_ser(),s.getPorcentaje_multa(),s.getPorcentaje_ayudante());
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	@Transactional
	public Boolean darEstado(Integer cod,Boolean est){
		try {
			return db.queryForObject("select servicio_darestado(?,?);",Boolean.class,cod,est);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
	public Boolean validarNom(String nom){
		return db.queryForObject("select servicio_validar(?)", Boolean.class,nom);
	}
}
