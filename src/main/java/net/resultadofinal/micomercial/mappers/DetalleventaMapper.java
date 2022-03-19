package net.resultadofinal.micomercial.mappers;

import net.resultadofinal.micomercial.model.DetalleVenta;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DetalleventaMapper implements RowMapper<DetalleVenta> {
	@Override
	public DetalleVenta mapRow(ResultSet rs, int rowNum) throws SQLException {
		DetalleVenta detalleventa = new DetalleVenta();
		detalleventa.setVentaId(rs.getLong("venta_id"));
		detalleventa.setId(rs.getShort("id"));
		detalleventa.setProductoId(rs.getLong("producto_id"));
		detalleventa.setPrecio(rs.getBigDecimal("precio"));
		detalleventa.setCantidad(rs.getInt("cantidad"));
		detalleventa.setDescuento(rs.getBigDecimal("descuento"));
		detalleventa.setSubtotal(rs.getBigDecimal("subtotal"));
		detalleventa.setTotal(rs.getBigDecimal("total"));
		return detalleventa;
	}
}