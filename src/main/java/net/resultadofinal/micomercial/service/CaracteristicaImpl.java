package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.Caracteristica;
import net.resultadofinal.micomercial.util.DbConeccion;
import net.resultadofinal.micomercial.util.UtilClass;
import net.resultadofinal.micomercial.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class CaracteristicaImpl extends DbConeccion implements CaracteristicaS {

	private JdbcTemplate db;
	@Autowired
	public CaracteristicaImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}
	private static final Logger logger = LoggerFactory.getLogger(CaracteristicaImpl.class);
	private static final String ENTITY = "caracteristica";

	private String sqlString;

	@Override
	public List<Caracteristica> listAll(short tipo) {
		try {
			sqlString = "select c.* from caracteristica c where c.estado = true and c.tipo = ?;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(Caracteristica.class), new Object[]{tipo});
		} catch(Exception ex) {
			return null;
		}
	}
}
