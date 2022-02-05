package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Persona;
import net.resultadofinal.micomercial.model.Proveedor;
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
public class ProveedorImpl extends DbConeccion implements ProveedorS {
	
	private JdbcTemplate db;
	@Autowired
	public ProveedorImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	@Autowired
	private UsuarioS usuarioS;
	private String sqlString;
	private static final Logger logger = LoggerFactory.getLogger(ProveedorImpl.class);
	private static final String ENTITY = "proveedor";
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<Proveedor> listado(HttpServletRequest request, boolean estado){
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("proveedor pro");
			sqlBuilder.setSelect("pro.cod_pro, pro.cod_emp,est_pro,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,nom_emp");
			sqlBuilder.addJoin("persona p on p.cod_per=pro.cod_pro");
			sqlBuilder.addJoin("empresa emp on emp.cod_emp=pro.cod_emp");
			sqlBuilder.setWhere("est_pro=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, Proveedor.class, sqlBuilder);
		} catch (Exception e) {
			logger.error("error listar Proveedor: "+e.toString());
			return null;
		}
	}
	public List<Proveedor> listAll() {
		try {
			sqlString = "SELECT cod_pro, proveedor.cod_emp,est_pro,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per,nom_emp " +
					"FROM proveedor inner JOIN persona on persona.cod_per=proveedor.cod_pro " +
					"inner JOIN empresa on empresa.cod_emp=proveedor.cod_emp " +
					"where est_pro=true";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Proveedor.class));
		} catch (Exception ex) {
			logger.error("error listar Proveedor: "+ex.toString());
			return null;
		}
	}
	public Proveedor obtener(Long cod_per){
		try {
			List<Proveedor> lista = db.query("select pro.*,p.* from proveedor pro inner join persona p on pro.cod_pro = p.cod_per where cod_pro=?;", BeanPropertyRowMapper.newInstance(Proveedor.class), cod_per);
			if(lista != null && !lista.isEmpty()) {
				return lista.get(0);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Transactional
	public Long adicionar(Proveedor pro){
		try {
			Long cod_per=pro.getCod_per();
			if(pro.getCod_per()==0) {
				cod_per=db.queryForObject("select persona_adicionar(?,?,?,?,?,?,?,?,?);",Long.class,pro.getCi_per(),pro.getNom_per(),pro.getPriape_per(),pro.getSegape_per(),pro.getTel_per(),pro.getDir_per(),pro.getEma_per(),pro.getFot_per(),pro.getSex_per());
			}else {
				db.queryForObject("select persona_modificar(?,?)",Boolean.class,pro.getCi_per(),pro.getNom_per(),pro.getPriape_per(),pro.getSegape_per(),pro.getDir_per(),pro.getTel_per(),pro.getEma_per(),pro.getFot_per(),pro.getSex_per(),pro.getCod_per());
			}
			return db.queryForObject("select proveedor_adicionar(?,?);",Long.class,cod_per,pro.getCod_emp());
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, e.getMessage()));
		}
	}
	@Transactional
	public Boolean modificar(Proveedor pro){
		try {
			db.queryForObject("select proveedor_modificar(?,?)", Boolean.class,pro.getCod_emp(),pro.getCod_pro());
			return db.queryForObject("select persona_modificar(?,?,?,?,?,?,?,?,?,?)",Boolean.class,pro.getCi_per(),pro.getNom_per(),pro.getPriape_per(),pro.getSegape_per(),pro.getDir_per(),pro.getTel_per(),pro.getEma_per(),pro.getFot_per(),pro.getSex_per(),pro.getCod_pro());
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, e.getMessage()));
		}
	}
	@Transactional
	public Boolean darEstado(Long cod,Boolean est){
		try {
			return db.queryForObject("select proveedor_darestado(?,?);",Boolean.class,cod,est);
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, e.getMessage()));
		}
	}
}