package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Dato;
import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.util.DashBoard;
import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.UtilClass;
import net.resultadofinal.micomercial.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class DatoImpl extends DbConeccion implements DatoS {
	
	private JdbcTemplate db;
	@Autowired
	public DatoImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(DatoImpl.class);
	private static final String ENTITY = "dato";
	private String sqlString;
	private static final String CLAVE = "tarijabolivia";
	public Map<String, Object> obtenerDato(Long cod){
		try {
			Map<String, Object> dato=db.queryForMap("select * from dato_obtener(?)",cod);
			return dato;
		} catch (Exception e) {
			logger.error(Utils.errorGet("map "+ENTITY, e.toString()));
			return null;
		}
	}
	public Dato obtener(Long cod){
		try {
			sqlString="select dato.* from dato where cod_per=? and fecha_baja is null";
			return db.queryForObject(sqlString, new BeanPropertyRowMapper<Dato>(Dato.class),cod);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Transactional
	public boolean adicionarDatos(Dato d, Integer obtenidos[]){
		try {
			if(d.getCla_dat() == null) {
				d.setCla_dat(CLAVE);
			}
			if(existeLogin(d.getLog_dat())) {
				throw new RuntimeException("Nombre de usuario ya existente, por favor ingrese otro.");
			}
			if(db.queryForObject("select dato_adicionar(?,?,?)",Boolean.class,d.getCod_per(),d.getLog_dat(),d.getCla_dat())){
				List<Map<String, Object>> roles=db.queryForList("select * from usurol where cod_per=? and fecha_baja is null",d.getCod_per());
				List<String> result=new DashBoard().asignarDesignarRoles(roles, obtenidos);
				for (String cad : result) {
					String sub[]=cad.split("-");
					if(Boolean.parseBoolean(sub[1]))
						db.queryForObject("select usurol_adicionar(?,?)",Boolean.class,d.getCod_per(),Integer.parseInt(sub[0]));
					else
						db.queryForObject("select usurol_modificar(?,?)",Boolean.class,d.getCod_per(),Integer.parseInt(sub[0]));
				}
				
			}else {
				logger.error(Utils.failedAdd(ENTITY));
				throw new RuntimeException(Utils.failedAdd(ENTITY));
			}
			return true;
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	public boolean guardarBiometrico(Persona p){
		try {
			return db.queryForObject("select dato_adicionar_biometrico(?,?)", Boolean.class,p.getCod_per(),p.getCodbio_per());
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY+" biometrico", e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY+" biometrico", ""));
		}
	}
	public boolean eliminar(Long cod){
		try {
			return db.queryForObject("select dato_eliminar(?)",Boolean.class,cod);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
	public boolean adicionar(Long cod,String log,String cla){
		try {
			if(cla == null) {
				cla = CLAVE;
			}
			return db.queryForObject("select dato_adicionar(?,?,?)",Boolean.class,cod,log,cla);
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY+" 2", e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY+" 2", e.getMessage()));
		}
	}
	@Override
	public boolean cambiarDatos(Long cod, String log, String cla) {
		try {
			String  sql = "select * from dato where log_dat =? and cod_per <> ? and fecha_baja is null";
			List<Dato> datos = db.query(sql, new BeanPropertyRowMapper<Dato>(Dato.class),log,cod);
			if(UtilClass.isNotNullEmpty(datos)) {
				throw new RuntimeException("El usuario ingresado ya existe, favor de utilizar otro nombre");
			}else {
				db.update("update dato set log_dat=?, cla_dat=? where cod_per = ? and fecha_baja is null;", log, cla, cod);
				return true;
			}
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY+" 2", e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY+" 2", ""));
		}
	}
	public boolean validarBiometrico(String biometrico){
		return db.queryForObject("select dato_validarbiometrico(?)", Boolean.class,biometrico);
	}
	public boolean existeLogin(String login){
		return db.queryForObject("select dato_validarlogin(?)", Boolean.class,login);
	}
}