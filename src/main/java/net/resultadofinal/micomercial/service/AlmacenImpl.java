package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Service
public class AlmacenImpl extends DbConeccion implements AlmacenS {

	private JdbcTemplate db;
	@Autowired
	public AlmacenImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(AlmacenImpl.class);
	private static final String ENTITY = "almacen";
	private String sqlString;

	@Override
	@Transactional
	public boolean registrarAlmacen(Long productoId, Integer sucursalId, Integer cantidad, Long userId, Short tipo, String obs) {
		try {
			sqlString ="select almacen_adicionar(?,?,?,?,?,?);";
			return db.queryForObject(sqlString, Boolean.class, productoId, sucursalId, cantidad, userId, tipo, obs);
		} catch (Exception e) {
			logger.error(Utils.errorAdd(ENTITY, e.toString()));
			throw new RuntimeException("Error al registrar apertura inicial: "+e.getMessage());
		}
	}

}