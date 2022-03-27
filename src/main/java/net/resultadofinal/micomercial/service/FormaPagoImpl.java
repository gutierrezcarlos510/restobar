package net.resultadofinal.micomercial.service;

import net.resultadofinal.micomercial.model.FormaPago;
import net.resultadofinal.micomercial.util.DbConeccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
public class FormaPagoImpl extends DbConeccion implements FormaPagoS {

	private JdbcTemplate db;
	@Autowired
	public FormaPagoImpl(DataSource dataSource) {
		this.db = new JdbcTemplate(dataSource);		
	}

	private String sqlString;

	@Override
	public List<FormaPago> listAll(int sucursalId) {
		try {
			sqlString = "select fp.* from forma_pago fp where fp.estado = true and fp.sucursal_id=?;";
			return db.query(sqlString, BeanPropertyRowMapper.newInstance(FormaPago.class), sucursalId);
		} catch(Exception ex) {
			return null;
		}
	}
}
