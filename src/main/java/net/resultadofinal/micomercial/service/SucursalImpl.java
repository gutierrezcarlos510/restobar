package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.*;
import net.resultadofinal.micomercial.model.wrap.Organigrama;
import net.resultadofinal.micomercial.model.wrap.RolOrganigrama;
import net.resultadofinal.micomercial.model.wrap.UsuarioOrganigrama;
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
import java.util.Map;

@Service
public class SucursalImpl extends DbConeccion implements SucursalS {
	
	private JdbcTemplate db;
	@Autowired
	public SucursalImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(SucursalImpl.class);
	private static final String ENTITY = "sucursal";
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<Sucursal> listado(HttpServletRequest request, boolean estado) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("sucursal s");
			sqlBuilder.setSelect("s.*");
			sqlBuilder.setWhere("s.estado=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, Sucursal.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}
	public List<Sucursal> listAll() {
		try {
			sqlString = "select * from sucursal where estado = true;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Sucursal.class));
		} catch(Exception ex) {
			return null;
		}
	}
	public Sucursal obtener(Integer cod_suc){
		try {
			return db.queryForObject("select * from sucursal where cod_suc =?", new BeanPropertyRowMapper<Sucursal>(Sucursal.class),cod_suc);
		} catch (Exception e) {
			return null;
		}
	}
	@Transactional
	public DataResponse adicionar(Sucursal s){
		try {
			sqlString = "insert into sucursal(cod_suc,nombre,descripcion,telefono,direccion,estado,alias) values((select coalesce(max(cod_suc),0)+1 from sucursal),?,?,?,?,true,?);";
			boolean save = db.update(sqlString,s.getNombre(),s.getDescripcion(),s.getTelefono(), s.getDireccion(), s.getAlias()) > 0;
			return Utils.getResponseDataAdd(ENTITY, save);
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, ""));
		}
	}
	@Transactional
	public DataResponse modificar(Sucursal s){
		try {
			boolean update = db.update("update sucursal set nombre=?,descripcion=?,telefono=?,direccion=?,alias=? where cod_suc=?;",s.getNombre(),s.getDescripcion(),s.getTelefono(),s.getDireccion(),s.getAlias(),s.getCod_suc()) > 0;
			return Utils.getResponseDataMod(ENTITY, update);
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	@Transactional
	public Boolean darEstado(Integer cod_suc,Boolean estado){
		try {
			
			return db.queryForObject("select sucursal_darestado(?,?)",Boolean.class, cod_suc,estado);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
	public Sucursal getSucursalNow(List<GeneralWrap> generalWrapList, General general) {
		if(general!=null && generalWrapList != null && !generalWrapList.isEmpty()) {
			for(GeneralWrap item : generalWrapList){
				if(item.getSucursal().getCod_suc() == general.getCod_suc())
					return item.getSucursal();
			};
		}
		return null;
	}
	public List<Sucursal> obtenerPorUsuario(Long codUsu){
		try {
			String sql = "select * from persona_obtener_sucursales(?)"+asObjectAdd(asSucursal, "tipo character varying(15)");
			return db.query(sql, new BeanPropertyRowMapper<Sucursal>(Sucursal.class),codUsu);
		} catch (Exception e) {
			logger.error(Utils.errorGet("obtener por usuario la "+ENTITY, e.toString()));
		}
		return null;
	}
	@Transactional
	public void asignarSucursal(Long codUsu,Integer sucursales[]) {
		try {
			eliminarPorUsuario(codUsu);
			if(sucursales != null && sucursales.length>0)
				for (int i = 0; i < sucursales.length; i++) {
					db.update("insert into tiene_sucursal(cod_per,cod_suc) values(?,?);",codUsu,sucursales[i]);
				}
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd("asignar "+ENTITY, ""));
		}
		
	}
	@Transactional
	public void eliminarPorUsuario(Long codUsu) {
		try {
			db.update("delete from tiene_sucursal where cod_per = ?",codUsu);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
	public void adicionarNotificaciones(String titulo, String mensaje, Integer[] sucursales) {
		try {
			if(sucursales != null && sucursales.length > 0) {
				for (int i = 0; i < sucursales.length; i++) {
					adicionarNotificacion(titulo, mensaje, sucursales[i]);
				}
			} else {
				throw new RuntimeException("No se encontro sucursales");
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error al adicionar notificacion a sucursal");
		}
	}
	public void adicionarNotificacion(String titulo, String mensaje, Integer sucursal) {
		try {
			db.update("update sucursal set estado_notificacion=true, titulo_notificacion=?, mensaje_notificacion=? where cod_suc=?", titulo, mensaje, sucursal);
		} catch (Exception ex) {
			throw new RuntimeException("Error al adicionar notificacion a sucursal");
		}
	}
	public void eliminarNotificaciones(Integer sucursales[]) {
		try {
			if(sucursales != null && sucursales.length > 0) {
				for (int i = 0; i < sucursales.length; i++) {
					eliminarNotificacion(sucursales[i]);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error al eliminar notificacion de sucursal");
		}
	}
	public void eliminarNotificacion(Integer sucursal) {
		try {
			db.update("update sucursal set estado_notificacion=false,titulo_notificacion='', mensaje_notificacion='' where cod_suc=?", sucursal);
		} catch (Exception ex) {
			throw new RuntimeException("Error al eliminar notificacion de sucursal");
		}
	}
	public List<NotificacionSucursalWrap> listarNotificaciones(){
		try {
			sqlString = "select cod_suc, nombre as xsucursal, titulo_notificacion, mensaje_notificacion,estado_notificacion from sucursal where estado_notificacion=true and estado=true";
			return db.query(sqlString, new BeanPropertyRowMapper<>(NotificacionSucursalWrap.class));
		} catch (Exception ex) {
			throw new RuntimeException("Error al listar notificaciones");
		}
	}
	public NotificacionSucursalWrap obtenerNotificacionPorSucursal(Integer sucursal) {
		try {
			sqlString = "select cod_suc, nombre as xsucursal, titulo_notificacion, mensaje_notificacion,estado_notificacion from sucursal where cod_suc =?";
			List<NotificacionSucursalWrap> lista = db.query(sqlString, new BeanPropertyRowMapper<>(NotificacionSucursalWrap.class), sucursal);
			if(lista != null && !lista.isEmpty()) {
				return lista.get(0);
			} else {
				return null;
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error al obtener notificacion por sucursal.");
		}
	}
	public Organigrama listarOrganigramaUsuarios(Integer sucursalId) {
		try {
			sqlString = "select cod_rol,nom_rol as xrol from rol where est_rol = true and cod_rol != 1;";
			List<RolOrganigrama> listaRol =db.query(sqlString, BeanPropertyRowMapper.newInstance(RolOrganigrama.class));
			if(UtilClass.isNotNullEmpty(listaRol)) {
				sqlString = "select distinct INITCAP(concat(p.nom_per,' ',p.priape_per)) as xusuario from persona p inner join usurol ur on ur.cod_per = p.cod_per and ur.cod_rol=? inner join tiene_sucursal ts on ts.cod_suc = ? and ts.cod_per = p.cod_per where p.est_per = true";
				for (RolOrganigrama r: listaRol) {
					r.setListaUsuarios(db.query(sqlString, BeanPropertyRowMapper.newInstance(UsuarioOrganigrama.class),r.getCodRol(),sucursalId));
				}
			}
			Organigrama o = new Organigrama();
			o.setNombreSucursal(db.queryForObject("select nombre from sucursal s where s.cod_suc = ?",String.class, sucursalId));
			o.setListaRoles(listaRol);
			return o;
		} catch(Exception ex) {
			return null;
		}
	}
}
