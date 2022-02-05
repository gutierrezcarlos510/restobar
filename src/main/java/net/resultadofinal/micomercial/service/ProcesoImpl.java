package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Menu;
import net.resultadofinal.micomercial.model.Proceso;
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
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Service
public class ProcesoImpl extends DbConeccion implements ProcesoS {
	
	private JdbcTemplate db;
	@Autowired
	public ProcesoImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(PrestacionImpl.class);
	private String sqlString;
	private static final String ENTITY = "proceso";
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<Proceso> listado(HttpServletRequest request, boolean estado) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("proceso");
			sqlBuilder.setSelect("cod_pro,nom_pro,des_pro,ico_pro,est_pro,url_pro");
			sqlBuilder.setWhere("est_pro=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, Proceso.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}
	public List<Proceso> listAll() {
		try {
			sqlString = "select * from menu where est_men = true;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Proceso.class));
		} catch(Exception ex) {
			return null;
		}
	}
	public List<Proceso> obtenerProcesos(Integer cod_men){
		try {
			sqlString = "select proceso.cod_pro,nom_pro,des_pro,ico_pro,est_pro,url_pro," +
					"case when cod_men is null then cast('' as varchar(8)) else cast('selected' as varchar(8)) end as tipo " +
					"from proceso left join mepro on mepro.cod_pro=proceso.cod_pro and mepro.cod_men = ? where est_pro = true";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Proceso.class), cod_men);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	public List<Proceso> obtenerPorMenu(Integer cod_men){
		try {
			sqlString = "select p.* from proceso p inner join mepro me on me.cod_pro=p.cod_pro and me.cod_men = ? where p.est_pro=true;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Proceso.class), cod_men);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY+" por codigo de menu", e.toString()));
			return null;
		}
	}
	public Map<String, Object> obtener(Integer codpro){
		try {
			return db.queryForMap("select * from proceso_obtener(?)",codpro);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Transactional
	public Boolean adicionar(Proceso p){
		try {
			String cad[]=p.getIco_pro().split("-");
			return db.queryForObject("select proceso_adicionar(?,?,?,?)",Boolean.class,p.getNom_pro(),p.getDes_pro(),cad[0]+" "+p.getIco_pro(),p.getUrl_pro());
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, ""));
		}
	}
	@Transactional
	public Boolean modificar(Proceso p){
		try {
			String cad[]=p.getIco_pro().split("-");
			return db.queryForObject("select proceso_modificar(?,?,?,?,?);",Boolean.class,p.getNom_pro(),p.getDes_pro(),cad[0]+" "+p.getIco_pro(),p.getUrl_pro(),p.getCod_pro());
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	@Transactional
	public Boolean darEstado(Integer codpro,Boolean estado){
		try {
			return db.queryForObject("select proceso_darestado(?,?)",Boolean.class,codpro,estado);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
	public Boolean validarNom(String nom){
		return db.queryForObject("select proceso_validar(?)", Boolean.class,nom);
	}
}