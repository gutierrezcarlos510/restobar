package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.mappers.PersonaMapper;
import net.resultadofinal.micomercial.model.General;
import net.resultadofinal.micomercial.model.GeneralWrap;
import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.model.Sucursal;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioImpl extends DbConeccion implements UsuarioS {
	
	private JdbcTemplate db;
	@Autowired
	public UsuarioImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	@Autowired
	private RolS rolS;
	@Autowired
	private DatoS datoS;
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	private static final Logger logger = LoggerFactory.getLogger(UsuarioImpl.class);
	private static final String ENTITY = "usuario";
	public List<Persona> listarUsuariosSistema(){
		try {
			sqlString = "select * from persona p join usurol ur on ur.cod_per = p.cod_per and ur.cod_rol<5 where p.est_per";
			return db.query(sqlString, new BeanPropertyRowMapper<Persona>(Persona.class));
		} catch (Exception e) {
			logger.error("Error al listar usuarios del sistema:"+e.toString());
			return null;
		}
	}
	
	public List<GeneralWrap> obtenerSucursales(Long cod_per){
		try {
			String sql ="select s.* from sucursal s join tiene_sucursal ts on ts.cod_suc= s.cod_suc and ts.cod_per = ? where s.estado";
			List<Sucursal> sucursalList = db.query(sql, BeanPropertyRowMapper.newInstance(Sucursal.class), cod_per);
			List<GeneralWrap> generalWrapList = new ArrayList<>();
			if(sucursalList != null && !sucursalList.isEmpty()) {
				sucursalList.forEach(item -> {
					GeneralWrap general = new GeneralWrap(item);
					List<General> generalList = db.query("select * from general where cod_suc=? and est_gen=true", BeanPropertyRowMapper.newInstance(General.class),item.getCod_suc());
					general.setGeneralList(generalList);
					generalWrapList.add(general);
				});
			}
			return generalWrapList;
		} catch (Exception e) {
			logger.error("error al obtener sucursales por usuario:"+e.toString());
			return null;
		}
	}
	@Transactional
	public void eliminarSucursales(Long cod_per) {
		try {
			db.update("delete from tiene_sucursal where cod_per = ?", cod_per);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
	@Transactional
	public void adicionarSucursales(Long cod_per, List<Integer> sucursales) {
		try {
			if(sucursales!= null && !sucursales.isEmpty()) {
				sucursales.forEach(item->db.update("insert into tiene_sucursal(cod_per,cod_suc) values (?,?)",cod_per,item));
			}
		} catch (Exception e) {
			logger.error("Error al asignar sucusales a usuario");
			throw new RuntimeException("Error al asignar sucusales a usuario");
		}
	}
	public Persona iniciarSesion(String login, String password){
		try {
			Persona persona = db.queryForObject("select * from persona_iniciar_sesion(?,?)",new BeanPropertyRowMapper<Persona>(Persona.class),login,password);
			persona.setRoles(rolS.listarPorUsuario(persona.getCod_per()));
			persona.setDato(datoS.obtener(persona.getCod_per()));
			return persona;
		} catch (Exception e) {
			logger.error("Error al iniciar sesion:"+e.toString());
			return null;
		}
	}
	public Boolean guardarFoto(String foto,Long codper){
		try {
			db.queryForObject("select persona_guardarfoto(?,?) as resp",Integer.class,foto,codper);
			return true;
		} catch (Exception e) {
			logger.error(Utils.errorAdd("foto "+ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, ""));
		}
	}

	public DataTableResults<Persona> listar(HttpServletRequest request){
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("persona p");
			sqlBuilder.setSelect("ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per, p.cod_per,codbio_per,codesc_per,log_dat, (d.fecha_baja is null) es_activo");
			sqlBuilder.addJoin("dato d on d.cod_per=p.cod_per and (d.fecha_baja is null or (d.fecha_baja is not null and (select count(*) from dato d3 where d3.cod_per=p.cod_per and d3.fecha_baja is null)=0 and d.fecha_baja = (select max(d2.fecha_baja) from dato d2 where d2.cod_per= d.cod_per)))");
			sqlBuilder.setWhere("est_per=true");
			return utilDataTableS.list(request, Persona.class, sqlBuilder);
		} catch (Exception e) {
			System.out.println("error listar Usuario: "+e.toString());
			return null;
		}
	}
	public Persona obtener(Long cod_per){
		try {
			List<Persona> personList = db.query("select * from persona_obtener(?)", new BeanPropertyRowMapper<Persona>(Persona.class),cod_per);
			return UtilClass.getFirst(personList);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	public List<Map<String, Object>> obtenerRoles(Long cod_per){
		try {
			return db.queryForList("select * from rol_obtenerroles(?)"+asObjectAdd(asRol, "tipo VARCHAR(15)"),cod_per);
		} catch (Exception e) {
			logger.error("error obtenerRoles="+e.toString());
			return null;
		}
	}
	@Transactional
	public DataResponse adicionar(Persona us, Integer roles[], Integer sucursales[]){
		try {
			Long codPer = db.queryForObject("select persona_adicionar(?,?,?,?,?,?,?,?,?);",Long.class,us.getCi_per(),us.getNom_per(),us.getPriape_per(),us.getSegape_per(),us.getTel_per(),us.getDir_per(),us.getEma_per(),us.getFot_per(),us.getSex_per());
			if(codPer > 0) {
				if(db.queryForObject("select dato_adicionar(?,?,?)",Boolean.class,codPer,us.getLogDat(),MyConstant.CLAVE)){
					if(sucursales!= null && sucursales.length > 0) {
						for (int i = 0; i < sucursales.length; i++) {
							db.update("insert into tiene_sucursal(cod_per,cod_suc) values (?,?)",codPer,sucursales[i]);
						}
					} else {
						throw new RuntimeException(Utils.errorAdd(ENTITY, "Sin sucursales"));
					}
					if(roles!= null && roles.length > 0) {
						for (int i = 0; i < roles.length; i++) {
							db.queryForObject("select usurol_adicionar(?,?)",Boolean.class,codPer,roles[i]);
						}
					} else {
						throw new RuntimeException(Utils.errorAdd(ENTITY, "Sin roles"));
					}
					return new DataResponse(true, "Se realizo con exito el registro de usuario");
				} else {
					throw new RuntimeException(Utils.errorAdd(ENTITY, "Sin datos de usuario"));
				}
			} else {
				return new DataResponse(false,"Error al adicionar Usuario");
			}
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, ""));
		}
	}
	public Boolean modificar(Persona us){
		try {
			return db.queryForObject("select persona_modificar(?,?,?,?,?,?,?,?,?,?)",Boolean.class,us.getCi_per(),us.getNom_per(),us.getPriape_per(),us.getSegape_per(),us.getDir_per(),us.getTel_per(),us.getEma_per(),us.getFot_per(),us.getSex_per(),us.getCod_per());
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	public Boolean eliminar(Long cod){
		try {
			return db.queryForObject("select persona_eliminar(?)",Boolean.class,cod);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
	public Boolean activar(Long cod){
		try {
			return db.queryForObject("select persona_activar(?)",Boolean.class,cod);
		} catch (Exception e) {
			logger.error(Utils.errorAct(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAct(ENTITY, ""));
		}
	}
	public Boolean validarCi(String ci){
		return db.queryForObject("select persona_validarci(?)", Boolean.class,ci);
	}
	public Map<String, Object> buscarCi(String ci){
		return db.queryForMap("select * from persona_buscar(?)",ci);
	}
	public Map<String, Object> buscar_nombres(String cad){
		return db.queryForMap("select * from persona_buscar_nombres(?)",cad);
	}
	@Override
	public Persona buscarPorTelefono(String celular) {
		try {
			List<Persona> lista = db.query("select * from persona_buscar_por_telefono(?) ", new PersonaMapper(),celular);
			return UtilClass.isNotNullEmpty(lista)?lista.get(0):null;
		} catch (Exception e) {
			logger.error("Error al buscar telefono de persona"+e.toString());
			return null;
		}
	}
}