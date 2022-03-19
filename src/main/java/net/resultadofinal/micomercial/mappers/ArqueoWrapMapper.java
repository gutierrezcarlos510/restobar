package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.wrap.ArqueoWrap;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArqueoWrapMapper implements RowMapper<ArqueoWrap> {

	@Override
	public ArqueoWrap mapRow(ResultSet rs, int ind) throws SQLException {
		ArqueoWrap obj = new ArqueoWrap();
		obj.setId(rs.getLong("id"));
		obj.setTingresos(rs.getBigDecimal("tingresos"));
		obj.setTegresos(rs.getBigDecimal("tegresos"));
		obj.setTbanco(rs.getBigDecimal("tbanco"));
		obj.setMontoReal(rs.getBigDecimal("monto_real"));
		obj.setEsActivo(rs.getBoolean("es_activo"));
		obj.setXusuario(rs.getString("xusuario"));
		obj.setFinicio(rs.getTimestamp("finicio"));
		obj.setFfin(rs.getTimestamp("ffin"));
		return obj;
	}

}
