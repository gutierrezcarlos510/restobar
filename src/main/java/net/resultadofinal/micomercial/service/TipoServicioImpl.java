package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.mappers.ServicioMapper;
import net.resultadofinal.micomercial.mappers.TiposervicioMapper;
import net.resultadofinal.micomercial.model.TipoServicio;
import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.UtilClass;
import net.resultadofinal.micomercial.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class TipoServicioImpl extends DbConeccion implements TipoServicioS {
	
	private JdbcTemplate db;
	@Autowired
	public TipoServicioImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(TipoServicioImpl.class);
	private static final String ENTITY = "tipo de servicio";
	private String sqlString;
	public List<Map<String, Object>> listar(int start,boolean estado,String search,int length){
		if(search==null)search="";
		sqlString = "select * from tiposervicio_lista(?,?,?,?)";
		sqlString += asObjectAdd(asTipoServicio, (start<0?"":"RN BIGINT,Tot INT"));
		return db.queryForList(sqlString,start,length,search,estado);
	}
	public Map<String, Object> obtener(Integer cod_tipmat){
		try {
			return db.queryForMap("select * from tiposervicio_obtener(?)",cod_tipmat);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	public Boolean adicionar(TipoServicio t){
		try {
			return db.queryForObject("select tiposervicio_adicionar(?,?);",Boolean.class,t.getNom_tipser(),t.getDes_tipser());
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, ""));
		}
	}
	public Boolean modificar(TipoServicio t){
		try {
			return db.queryForObject("select tiposervicio_modificar(?,?,?);",Boolean.class,t.getNom_tipser(),t.getDes_tipser(),t.getCod_tipser());
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	public Boolean darEstado(Integer cod_tipser,Boolean est){
		try {
			return db.queryForObject("select tiposervicio_darestado(?,?);",Boolean.class,cod_tipser,est);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
	public Boolean validarNom(String nom){
		return db.queryForObject("select tiposervicio_validar(?)", Boolean.class,nom);
	}
	@Override
	public List<TipoServicio> listarAgrupadoConServicios(){
		try {
			List<TipoServicio> lista = db.query("select * from tiposervicio where est_tipser=true", new TiposervicioMapper());
			if(UtilClass.isNotNullEmpty(lista)) {
				sqlString = "select * from servicio where cod_tipser=? and est_ser=true";
				lista.forEach(item->{
					item.setServicios(db.query(sqlString, new ServicioMapper(),item.getCod_tipser()));
				});
			}
			return lista;
		} catch (Exception e) {
			logger.info(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
}
