package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.wrap.ArqueoWrap;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArqueoWrapMapper implements RowMapper<ArqueoWrap> {

	@Override
	public ArqueoWrap mapRow(ResultSet rs, int ind) throws SQLException {
		ArqueoWrap obj = new ArqueoWrap();
		obj.setCodArqcaj(rs.getLong("cod_arqcaj"));
		obj.setTingresos(rs.getFloat("tingresos"));
		obj.setTegresos(rs.getFloat("tegresos"));
		obj.setTbanco(rs.getFloat("tbanco"));
		obj.setMontoReal(rs.getFloat("monto_real"));
		obj.setEsActivo(rs.getBoolean("es_activo"));
		obj.setXusuario(rs.getString("xusuario"));
		obj.setFinicio(rs.getString("finicio"));
		obj.setFfinal(rs.getString("ffinal"));
		return obj;
	}

}
