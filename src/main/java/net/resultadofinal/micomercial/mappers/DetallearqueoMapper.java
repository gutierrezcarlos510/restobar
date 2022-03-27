// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.DetalleArqueo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DetallearqueoMapper implements RowMapper<DetalleArqueo> {
	@Override
	public DetalleArqueo mapRow(ResultSet rs, int rowNum) throws SQLException {
		//cod_arqcaj bigint ,cod_detarq integer ,tip_detarq integer ,des_detarq character varying(500) ,mon_detarq real ,fec_detarq timestamp
		DetalleArqueo detallearqueo = new DetalleArqueo();
		detallearqueo.setArqueoId(rs.getLong("arqueo_id"));
		detallearqueo.setId(rs.getInt("id"));
		detallearqueo.setTipo(rs.getShort("tipo"));
		detallearqueo.setDescripcion(rs.getString("descripcion"));
		detallearqueo.setMonto(rs.getBigDecimal("monto"));
		detallearqueo.setFecha(rs.getTimestamp("fecha"));
		detallearqueo.setEstado(rs.getBoolean("estado"));
		detallearqueo.setFormaPagoId(rs.getShort("forma_pago_id"));
		return detallearqueo;
	}
}