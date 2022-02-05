package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Cliente;
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

@Service
public class ClienteImpl extends DbConeccion implements ClienteS {
	
	private JdbcTemplate db;
	@Autowired
	public ClienteImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	@Autowired
	private UsuarioS usuarioS;
	private static final Logger logger = LoggerFactory.getLogger(ClienteImpl.class);
	private static final String ENTITY = "cliente";
	private String sqlString;
	@Autowired
	private UtilDataTableS utilDataTableS;
	public DataTableResults<Cliente> listado(HttpServletRequest request, boolean estado) {
		try {
			SqlBuilder sqlBuilder = new SqlBuilder("cliente");
			sqlBuilder.setSelect("cliente.cod_cli,est_cli,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per");
			sqlBuilder.addJoin("persona on persona.cod_per=cliente.cod_cli");
			sqlBuilder.setWhere("est_cli=:xestado");
			sqlBuilder.addParameter("xestado",estado);
			return utilDataTableS.list(request, Cliente.class, sqlBuilder);
		} catch (Exception e) {
			return null;
		}
	}
	public List<Cliente> listAll() {
		try {
			sqlString = "select cliente.cod_cli,est_cli,ci_per, nom_per, priape_per, segape_per, tel_per, dir_per, ema_per, fot_per, est_per, sex_per from cliente " +
			"inner join persona on persona.cod_per = cliente.cod_cli where est_cli = true";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Cliente.class));
		} catch(Exception ex) {
			logger.error(Utils.errorGet(ENTITY, ex.getMessage()));
			return null;
		}
	}
	public Cliente obtener(Long cod_per){
		try {
			List<Cliente> lista = db.query("select c.*,p.* from cliente c inner join persona p on c.cod_cli = p.cod_per where c.cod_cli =?", BeanPropertyRowMapper.newInstance(Cliente.class),cod_per);
			return UtilClass.getFirst(lista);
		} catch (Exception e) {
			logger.error(Utils.errorGet(ENTITY, e.toString()));
			return null;
		}
	}
	@Transactional
	public Long adicionar(Cliente cli){
		try {
			Long cod_per=cli.getCod_per();
			if(cli.getCi_per()==null || (cli.getCi_per()!=null && cli.getCi_per().isEmpty())) {
				cli.setCi_per("0");
			}
			if(cli.getTel_per()!=null) {
				cli.setTel_per(cli.getTel_per().trim());
			}
			if(cli.getCod_per()==0)
				cod_per=db.queryForObject("select persona_adicionar(?,?,?,?,?,?,?,?,?);",Long.class,cli.getCi_per(),cli.getNom_per(),cli.getPriape_per(),cli.getSegape_per(),cli.getTel_per(),cli.getDir_per(),cli.getEma_per(),cli.getFot_per(),cli.getSex_per());
			else
				db.queryForObject("select persona_modificar(?,?,?,?,?,?,?,?,?,?)",Boolean.class,cli.getCi_per(),cli.getNom_per(),cli.getPriape_per(),cli.getSegape_per(),cli.getDir_per(),cli.getTel_per(),cli.getEma_per(),cli.getFot_per(),cli.getSex_per(),cli.getCod_per());
			return db.queryForObject("select cliente_adicionar(?);",Long.class,cod_per);
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorAdd(ENTITY, ""));
		}
	}
	@Transactional
	public Boolean modificar(Cliente cli){
		try {
			return db.queryForObject("select persona_modificar(?,?,?,?,?,?,?,?,?,?)",Boolean.class,cli.getCi_per(),cli.getNom_per(),cli.getPriape_per(),cli.getSegape_per(),cli.getDir_per(),cli.getTel_per(),cli.getEma_per(),cli.getFot_per(),cli.getSex_per(),cli.getCod_per());
		} catch (Exception e) {
			logger.error(Utils.errorMod(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorMod(ENTITY, ""));
		}
	}
	@Transactional
	public DataResponse darEstado(Long cod, Boolean est){
		try {
			if(cod == 0) {
				return new DataResponse(false, "No se puede eliminar a cliente creado por sistema.");
			} else {
				boolean success = db.queryForObject("select cliente_darestado(?,?);",Boolean.class,cod,est);
				return new DataResponse(success, est ? Utils.getSuccessFailedAct(ENTITY, success) : Utils.getSuccessFailedEli(ENTITY, success));
			}
		} catch (Exception e) {
			logger.error(Utils.errorEli(ENTITY, e.toString()));
			throw new RuntimeException(Utils.errorEli(ENTITY, ""));
		}
	}
}