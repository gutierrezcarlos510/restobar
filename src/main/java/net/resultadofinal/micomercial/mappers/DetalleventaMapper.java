// ENTITY_java.vm
package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.DetalleVenta;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DetalleventaMapper implements RowMapper<DetalleVenta> {
	@Override
	public DetalleVenta mapRow(ResultSet rs, int rowNum) throws SQLException {
		DetalleVenta detalleventa = new DetalleVenta();
		detalleventa.setCodVen(rs.getLong("cod_ven"));
		detalleventa.setCodDetven(rs.getInt("cod_detven"));
		detalleventa.setCodPro(rs.getInt("cod_pro"));
		detalleventa.setPreDetven(rs.getFloat("pre_detven"));
		detalleventa.setCanDetven(rs.getInt("can_detven"));
		detalleventa.setDesDetven(rs.getFloat("des_detven"));
		detalleventa.setSubtotDetven(rs.getFloat("subtot_detven"));
		detalleventa.setTotDetven(rs.getFloat("tot_detven"));
		detalleventa.setProducto(rs.getString("producto"));
		return detalleventa;
	}
}