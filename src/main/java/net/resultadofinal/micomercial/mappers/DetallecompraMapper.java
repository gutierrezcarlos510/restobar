// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.DetalleCompra;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DetallecompraMapper implements RowMapper<DetalleCompra> {
	@Override
	public DetalleCompra mapRow(ResultSet rs, int rowNum) throws SQLException {
		DetalleCompra detallecompra = new DetalleCompra();
		detallecompra.setCod_com(rs.getLong("cod_com"));
		detallecompra.setCod_detcom(rs.getInt("cod_detcom"));
		detallecompra.setCod_pro(rs.getLong("cod_pro"));
		detallecompra.setPre_detcom(rs.getBigDecimal("pre_detcom"));
		detallecompra.setCan_detcom(rs.getInt("can_detcom"));
		detallecompra.setDes_detcom(rs.getBigDecimal("des_detcom"));
		detallecompra.setSubtotDetcom(rs.getBigDecimal("subtot_detcom"));
		detallecompra.setTotDetcom(rs.getBigDecimal("tot_detcom"));
		return detallecompra;
	}
}